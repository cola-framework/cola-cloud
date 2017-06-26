package com.cola.service.user.controller;

import com.cola.libs.beans.enums.ResponseCode;
import com.cola.libs.beans.vo.UserBean;
import com.cola.libs.beans.web.restful.ResponseHeader;
import com.cola.libs.beans.web.restful.ResponseMessage;
import com.cola.service.user.service.UserService;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * Created by jiachen.shi on 6/20/2017.
 */
@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @ApiOperation(value="Get User", notes="")
    @ApiImplicitParam(name = "name", value = "User Name/Mobile number/Email Address", required = true, dataType = "String")
    @RequestMapping(value = "/getUser", method = RequestMethod.GET)
    public @ResponseBody
    ResponseMessage<UserBean> getUser(@RequestParam("name") String name){

        ResponseMessage<UserBean> message = new ResponseMessage<>();
        ResponseHeader header = new ResponseHeader();
        message.setHeader(header);

        if(StringUtils.isNotEmpty(name)){
            UserBean userBean = userService.getUserByNameOrMobileOrEmail(name);
            if(null != userBean){
                message.setBody(userBean);
                header.setCode(ResponseCode.SUCCESS.getCode());
            }else{
                header.setCode(ResponseCode.ERROR.getCode());
            }
        }else{
            header.setCode(ResponseCode.ERROR.getCode());
        }
        return message;
    }

    @ApiOperation(value="Create User", notes="")
    @ApiImplicitParam(name = "userBean", value = "User Info", required = true, dataType = "UserBean")
    @RequestMapping(value = "/createUser", method = RequestMethod.POST)
    public @ResponseBody ResponseMessage<UserBean> createUser(@RequestBody UserBean userBean){

        ResponseMessage<UserBean> message = new ResponseMessage<>();
        ResponseHeader header = new ResponseHeader();
        message.setHeader(header);

        try{
            if(null != userBean){
                userService.createUser(userBean);
                header.setCode(ResponseCode.SUCCESS.getCode());
            }
        }catch (Exception e){
            header.setCode(ResponseCode.ERROR.getCode());
        }
        return message;

    }

    @ApiOperation(value="Update User", notes="")
    @ApiImplicitParam(name = "userBean", value = "User Info", required = true, dataType = "UserBean")
    @RequestMapping(value = "/createUser", method = RequestMethod.POST)
    public @ResponseBody ResponseMessage<UserBean> updateUser(@RequestBody UserBean userBean){

        ResponseMessage<UserBean> message = new ResponseMessage<>();
        ResponseHeader header = new ResponseHeader();
        message.setHeader(header);

        try{
            if(null != userBean){
                userService.updateUser(userBean);
                header.setCode(ResponseCode.SUCCESS.getCode());
            }
        }catch (Exception e){
            header.setCode(ResponseCode.ERROR.getCode());
        }
        return message;

    }
}
