package com.cola.lib.ruleengine.beans;

import java.io.Serializable;
import java.util.Map;
import java.util.Set;

/**
 * Created by jiachen.shi on 11/9/2016.
 */
public class RuleEvaluationContext implements Serializable {

    private Set<Object> facts;
    private Map<String,Object> globals;
    private Object filter;
    private Set<Object> eventListeners;

    public Set<Object> getFacts() {
        return facts;
    }

    public void setFacts(Set<Object> facts) {
        this.facts = facts;
    }

    public Map<String, Object> getGlobals() {
        return globals;
    }

    public void setGlobals(Map<String, Object> globals) {
        this.globals = globals;
    }

    public Object getFilter() {
        return filter;
    }

    public void setFilter(Object filter) {
        this.filter = filter;
    }

    public Set<Object> getEventListeners() {
        return eventListeners;
    }

    public void setEventListeners(Set<Object> eventListeners) {
        this.eventListeners = eventListeners;
    }

}
