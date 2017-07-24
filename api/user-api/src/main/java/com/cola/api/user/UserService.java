package com.cola.api.user;

import com.cola.libs.beans.vo.UserBean;
import com.cola.libs.beans.web.restful.ResponseMessage;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.*;

/**
 * Created by jiachen.shi on 6/27/2017.
 */
@FeignClient("user")
@RequestMapping("/user")
public interface UserService {

    @RequestMapping(value = "/getUser", method = RequestMethod.GET)
    public @ResponseBody ResponseMessage<UserBean> getUser(@RequestParam("name") String name);

    @RequestMapping(value = "/createUser", method = RequestMethod.POST)
    public @ResponseBody ResponseMessage<UserBean> createUser(@RequestBody UserBean userBean);

    @RequestMapping(value = "/createUser", method = RequestMethod.POST)
    public @ResponseBody ResponseMessage<UserBean> updateUser(@RequestBody UserBean userBean);

}
