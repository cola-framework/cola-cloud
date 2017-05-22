/*
 * Copyright 2002-${Year} the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.cola.libs.cache.management;

import com.cola.libs.cache.hibernate.IntensiveCache;

import org.apache.ignite.Ignite;
import org.apache.ignite.IgniteCheckedException;
import org.apache.ignite.IgniteException;
import org.apache.ignite.internal.IgniteKernal;
import org.apache.ignite.internal.processors.cache.IgniteInternalCache;
import org.apache.ignite.internal.util.GridLeanSet;
import org.apache.ignite.internal.util.typedef.internal.U;
import org.apache.ignite.lang.IgniteCallable;
import org.apache.ignite.resources.IgniteInstanceResource;
import org.apache.ignite.transactions.Transaction;
import org.apache.ignite.transactions.TransactionConcurrency;
import org.apache.ignite.transactions.TransactionIsolation;
import org.hibernate.cache.CacheException;
import org.hibernate.cache.spi.access.SoftLock;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.Cache;
import org.springframework.cache.support.SimpleValueWrapper;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.Set;
import java.util.concurrent.Callable;

/**
 * cola
 * Created by jiachen.shi on 8/24/2016.
 */
public class CustomizedIgniteCache implements IntensiveCache {

    private Ignite ignite;
    private static Logger logger = LoggerFactory.getLogger(CustomizedIgniteCache.class);
    private ThreadLocal<CustomizedIgniteCache.TxContext> txCtx;
    private final IgniteInternalCache<Object, Object> cache;

    public CustomizedIgniteCache(Ignite ignite, IgniteInternalCache<Object, Object> cache) {
        this.ignite = ignite;
        this.cache = cache;
    }

    public void setTxCtx(ThreadLocal txCtx){
        this.txCtx = txCtx;
    }

    private void rollbackCurrentTx() {
        try {
            TxContext e = (TxContext) this.txCtx.get();
            if (e != null) {
                this.txCtx.remove();
                Transaction tx = this.cache.tx();
                if (tx != null) {
                    tx.rollback();
                }
            }
        } catch (IgniteException var3) {
            this.logger.error("Failed to rollback cache transaction.", var3);
        }
    }

    public String getName() {
        return this.cache.name();
    }

    public Object getNativeCache() {
        return this.cache;
    }

    public ValueWrapper get(Object key) {
        boolean success = false;
        Object val;
        try {
            val = this.cache.get(key);
            success = true;
        } catch (IgniteCheckedException var8) {
            throw new CacheException(var8);
        } finally {
            if(!success) {
                this.rollbackCurrentTx();
            }

        }
        return val != null?new SimpleValueWrapper(val):null;
    }

    public <T> T get(Object key, Class<T> type) {
        ValueWrapper wrapper = this.get(key);
        return wrapper == null ? null : (T) wrapper.get();
    }

    @Override
    public <T> T get(Object o, Callable<T> callable) {
        return null;
    }

    public void put(Object key, Object val) {
        boolean success = false;
        try {
            this.cache.put(key, val);
            success = true;
        } catch (IgniteCheckedException var8) {
            throw new CacheException(var8);
        } finally {
            if (!success) {
                this.rollbackCurrentTx();
            }
        }
    }

    public ValueWrapper putIfAbsent(Object key, Object val) {
        Boolean old;
        boolean success = false;
        try {
            old = Boolean.valueOf(this.cache.putIfAbsent(key, val));
            success = true;
        } catch (IgniteCheckedException var8) {
            throw new CacheException(var8);
        } finally {
            if (!success) {
                this.rollbackCurrentTx();
            }
        }
        return old != null?new SimpleValueWrapper(old):null;
    }

    public void evict(Object key) {
        try {
            ignite.compute(ignite.cluster()).call(new ClearKeyCallable(key, cache.name()));
        } catch (IgniteException var4) {
            throw new CacheException(var4);
        }
    }

    public void clear() {
        try {
            this.cache.clear();
        } catch (IgniteCheckedException var2) {
            throw new CacheException(var2);
        }
    }

    @Override
    public boolean containsKey(Object key) {
        return this.cache.containsKey(key);
    }

    @Override
    public long size() {
        return this.cache.size();
    }

    private void lockKey(Object key) throws IgniteCheckedException {
        if(this.cache.tx() == null) {
            this.cache.txStart(TransactionConcurrency.PESSIMISTIC, TransactionIsolation.REPEATABLE_READ);
        }
        this.cache.get(key);
    }

    @Override
    public SoftLock lock(Object key) {
        boolean success = false;
        try {
            TxContext e = (TxContext)this.txCtx.get();
            if(e == null) {
                this.txCtx.set(e = new TxContext());
            }
            this.lockKey(key);
            e.locked(key);
            success = true;
        } catch (IgniteCheckedException var8) {
            throw new CacheException(var8);
        } finally {
            if(!success) {
                this.rollbackCurrentTx();
            }
        }
        return null;
    }

    private void unlock(TxContext ctx, Object key) throws CacheException {
        if(ctx.unlocked(key)) {
            this.txCtx.remove();
            Transaction tx = this.cache.tx();
            assert tx != null;
            try {
                tx.commit();
            } finally {
                tx.close();
            }
            assert this.cache.tx() == null;
        }
    }

    @Override
    public void unlock(Object key, SoftLock lock) {
        boolean success = false;
        try {
            TxContext e = (TxContext)this.txCtx.get();
            if(e != null) {
                this.unlock(e, key);
            }
            success = true;
        } catch (Exception var8) {
            throw new CacheException(var8);
        } finally {
            if(!success) {
                this.rollbackCurrentTx();
            }
        }
    }

    private static class TxContext {
        private Set<Object> locked;

        private TxContext() {
            this.locked = new GridLeanSet();
        }

        void locked(Object key) {
            this.locked.add(key);
        }

        boolean unlocked(Object key) {
            this.locked.remove(key);
            return this.locked.isEmpty();
        }
    }

    private static class ClearKeyCallable implements IgniteCallable<Void>, Externalizable {
        private static final long serialVersionUID = 0L;
        @IgniteInstanceResource
        private Ignite ignite;
        private Object key;
        private String cacheName;

        public ClearKeyCallable() {
        }

        private ClearKeyCallable(Object key, String cacheName) {
            this.key = key;
            this.cacheName = cacheName;
        }

        public Void call() throws IgniteCheckedException {
            IgniteInternalCache cache = ((IgniteKernal)this.ignite).getCache(this.cacheName);
            assert cache != null;
            cache.clearLocally(this.key);
            return null;
        }

        public void writeExternal(ObjectOutput out) throws IOException {
            out.writeObject(this.key);
            U.writeString(out, this.cacheName);
        }

        public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
            this.key = in.readObject();
            this.cacheName = U.readString(in);
        }
    }

}
