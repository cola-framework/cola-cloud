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
package com.cola.platform.auth.controller;

import com.cola.libs.beans.ResponseHeader;
import com.cola.libs.beans.ResponseMessage;
import com.cola.libs.enums.ResponseCode;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * cola
 * Created by jiachen.shi on 6/21/2016.
 */
@RestController
@RequestMapping("/oauth2")
public class Oauth2Controller {

    @Autowired
    private HttpServletRequest request;

    public
    @ResponseBody
    ResponseMessage oauth2(@RequestHeader("providerType") String providerType, @RequestHeader("accessToken") String accessToken) {

        ResponseMessage responseMessage = new ResponseMessage();
        ResponseHeader responseHeader = new ResponseHeader();
        responseMessage.setHeader(responseHeader);

        responseHeader.setCode(ResponseCode.SUCCESS.getCode());

        return responseMessage;
    }

    @RequestMapping(value = "/token")
    public @ResponseBody ResponseMessage token(){

        ResponseMessage responseMessage = new ResponseMessage();
        ResponseHeader responseHeader = new ResponseHeader();
        responseMessage.setHeader(responseHeader);

        responseHeader.setCode(ResponseCode.SUCCESS.getCode());

        return responseMessage;
    }

}
