package com.cola.service.user.service;

import com.cola.libs.beans.vo.UserBean;

/**
 * Created by jiachen.shi on 6/20/2017.
 */
public interface UserService {

    public void createUser(UserBean userBean);

    public void updateUser(UserBean userBean);

    public UserBean getUserByNameOrMobileOrEmail(String input);

}
