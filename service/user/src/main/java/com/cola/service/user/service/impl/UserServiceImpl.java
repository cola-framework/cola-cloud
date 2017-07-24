package com.cola.service.user.service.impl;

import com.cola.libs.beans.vo.UserBean;
import com.cola.libs.jpa.service.FlexibleSearchService;
import com.cola.libs.jpa.service.ModelService;
import com.cola.libs.jpa.support.FlexibleQueryBuilder;
import com.cola.service.user.entity.User;
import com.cola.service.user.service.UserService;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by jiachen.shi on 6/20/2017.
 */
public class UserServiceImpl implements UserService {

    private static Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    @Autowired
    private ModelService modelService;

    @Autowired
    private FlexibleSearchService flexibleSearchService;

    @Override
    @Transactional
    public void createUser(UserBean userBean) {
        if(null != userBean){
            User user = new User();
            user.setName(userBean.getName());
            user.setEmail(userBean.getEmail());
            user.setMobile(userBean.getMobile());
            user.setPassword(userBean.getPassword());
            user.setBirthday(userBean.getBirthday());
            user.setProviderId(userBean.getProviderId());
            user.setProviderType(userBean.getProviderType());
            modelService.save(user);
        }else{
            logger.error("userBean can't be null.");
        }
    }

    @Override
    @Transactional
    public void updateUser(UserBean userBean) {
        if(null != userBean){
            Long id = userBean.getId();
            if(null != id){
                User user = modelService.load(User.class, id);
                if(StringUtils.isNotEmpty(userBean.getName())){
                    user.setName(userBean.getName());
                }
                if(StringUtils.isNotEmpty(userBean.getPassword())){
                    user.setPassword(userBean.getPassword());
                }
                user.setEmail(userBean.getEmail());
                user.setMobile(userBean.getMobile());
                user.setBirthday(userBean.getBirthday());
                user.setProviderType(userBean.getProviderType());
                user.setProviderId(userBean.getProviderId());
                modelService.save(user);
            }else{
                logger.error("userBean's id can't be null.");
            }
        }else{
            logger.error("userBean can't be null.");
        }
    }

    @Override
    @Transactional(readOnly = true)
    public UserBean getUserByNameOrMobileOrEmail(String input) {

        if(StringUtils.isNotEmpty(input)){
            String hql = "from User u where u.name = ? or u.mobile = ? or u.email = ? ";
            FlexibleQueryBuilder builder = new FlexibleQueryBuilder(hql);
            builder.addParameter(input);
            builder.addParameter(input);
            builder.addParameter(input);
            User user = flexibleSearchService.uniqueQuery(builder, User.class);
            if(null != user){
                UserBean userBean = new UserBean();
                userBean.setId(user.getId());
                userBean.setName(user.getName());
                userBean.setMobile(user.getMobile());
                userBean.setEmail(user.getEmail());
                userBean.setPassword(user.getPassword());
                userBean.setBirthday(user.getBirthday());
                userBean.setProviderId(user.getProviderId());
                userBean.setProviderType(user.getProviderType());
                return userBean;
            }else{
                logger.error("query user results are empty.");
            }
        }else{
            logger.error("input can't be null.");
        }
        return null;
    }
}
