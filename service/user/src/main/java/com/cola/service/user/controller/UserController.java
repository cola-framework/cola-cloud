package com.cola.service.user.controller;

import com.cola.api.i18n.I18nService;
import com.cola.lib.beans.enums.ResponseStatus;
import com.cola.lib.beans.vo.UserBean;
import com.cola.lib.beans.web.restful.ResponseMessage;
import com.cola.service.user.service.UserService;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by jiachen.shi on 6/20/2017.
 */
@RestController
@RequestMapping("/user")
public class UserController {

    private static Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private UserService userService;

    @Autowired
    private I18nService i18nService;

    @ApiOperation(value="getUser", notes="find user by name")
    @ApiImplicitParam(name = "name", value = "User Name/Mobile number/Email Address", required = true, dataType = "String")
    @RequestMapping(value = "/getUser", method = RequestMethod.GET)
    public @ResponseBody
    ResponseMessage<UserBean> getUser(@RequestParam("name") String name){
        List<String> attributes = new ArrayList<>();
        attributes.add("default.main.login");
        attributes.add("default.main.size");
        ResponseMessage<Map<String, String>> mapResponseMessage = i18nService.geti18nMessage(attributes);

        ResponseMessage<UserBean> result;
        if(StringUtils.isNotEmpty(name)){
            UserBean userBean = userService.getUserByNameOrMobileOrEmail(name);
            if(null != userBean){
                result = ResponseMessage.ok(userBean);
            }else{
                result = ResponseMessage.error(ResponseStatus.ERROR.getCode(), ResponseStatus.ERROR.getMsgKey());
            }
        }else{
            result = ResponseMessage.error(ResponseStatus.ERROR.getCode(), ResponseStatus.ERROR.getMsgKey());
        }
        return result;
    }

    @ApiOperation(value="createUser", notes="create user info")
    @ApiImplicitParam(name = "userBean", value = "User Info", required = true, dataType = "UserBean")
    @RequestMapping(value = "/createUser", method = RequestMethod.POST)
    public @ResponseBody ResponseMessage<UserBean> createUser(@RequestBody UserBean userBean){
        ResponseMessage<UserBean> result = null;
        try{
            if(null != userBean){
                userService.createUser(userBean);
                result = ResponseMessage.ok(userBean);
            }
        }catch (Exception e){
            result = ResponseMessage.error(ResponseStatus.ERROR.getCode(), ResponseStatus.ERROR.getMsgKey());
        }
        return result;
    }

    @ApiOperation(value="updateUser", notes="update user info")
    @ApiImplicitParam(name = "userBean", value = "User Info", required = true, dataType = "UserBean")
    @RequestMapping(value = "/updateUser", method = RequestMethod.POST)
    public @ResponseBody ResponseMessage<UserBean> updateUser(@RequestBody UserBean userBean){
        ResponseMessage<UserBean> result = null;
        try{
            if(null != userBean){
                userService.updateUser(userBean);
                result = ResponseMessage.ok(userBean);
            }
        }catch (Exception e){
            result = ResponseMessage.error(ResponseStatus.ERROR.getCode(), ResponseStatus.ERROR.getMsgKey());
        }
        return result;
    }
}
