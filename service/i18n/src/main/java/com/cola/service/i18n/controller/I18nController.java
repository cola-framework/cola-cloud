package com.cola.service.i18n.controller;

import com.cola.libs.beans.web.restful.ResponseMessage;
import com.cola.service.i18n.support.LocaleMessageSourceService;
import io.swagger.annotations.*;
import org.apache.catalina.servlet4preview.http.HttpServletRequest;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by jiachen.shi on 7/19/2017.
 */
@RestController
@RequestMapping("/i18n")
@Api(value = "i18n", description = "get i18n Message Controller")
public class I18nController {

    private static Logger logger = LoggerFactory.getLogger(I18nController.class);

    @Resource
    private LocaleMessageSourceService localeMessageSourceService;

    private String DEFAULT_WEBSITE = "default";
    private String DEFAULT_PAGE = "main";
    private String MESSAGE_SEPARATOR = ".";

    @ApiOperation(value="geti18nMessage", notes="get i18n message for page", response = Map.class)
    @ApiResponse(code = 200, message = "OK")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "website", value = "website name",  required = true, dataType = "String"),
            @ApiImplicitParam(name = "page", value = "page name", required = true, dataType = "String"),
            @ApiImplicitParam(name = "attributes", value = "attribute list", required = true, dataTypeClass = List.class)
    })
    @RequestMapping(value = "/{website}/{page}", method = RequestMethod.GET)
    public @ResponseBody ResponseMessage<Map<String, String>> geti18nMessage(@PathVariable("website") String website, @PathVariable("page") String page, @RequestParam("attributes") List<String> attributes){
        Map<String,String> result = new HashMap<>();
        if(StringUtils.isEmpty(website)){
            website = DEFAULT_WEBSITE;
        }
        if(StringUtils.isEmpty(page)){
            page = DEFAULT_PAGE;
        }
        for(String attribute:attributes){
            if(StringUtils.isNotEmpty(attribute)){
                result.put(attribute, localeMessageSourceService.getMessage(website + MESSAGE_SEPARATOR + page + MESSAGE_SEPARATOR + attribute));
            }
        }
        return ResponseMessage.ok(result);
    }

    @ApiOperation(value="geti18nMessage", notes="get i18n message for attribute", response = Map.class)
    @ApiResponse(code = 200, message = "OK")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "website", value = "website name", required = true, dataType = "String"),
            @ApiImplicitParam(name = "page", value = "page name", required = true, dataType = "String"),
            @ApiImplicitParam(name = "attribute", value = "attribute name", required = true, dataType = "String"),
            @ApiImplicitParam(name = "args", value = "arguments", dataTypeClass = List.class)
    })
    @RequestMapping(value ="/{website}/{page}/{attribute}", method = RequestMethod.GET)
    public @ResponseBody ResponseMessage<Map<String, String>> geti18nMessage(@PathVariable("website") String website, @PathVariable("page") String page, @PathVariable("attribute") String attribute, @RequestParam(value="args", required = false) List<Object> args) {
        Map<String,String> result = new HashMap<>();
        if(StringUtils.isEmpty(website)){
            website = DEFAULT_WEBSITE;
        }
        if(StringUtils.isEmpty(page)){
            page = DEFAULT_PAGE;
        }
        result.put(attribute, localeMessageSourceService.getMessage(website + MESSAGE_SEPARATOR + page + MESSAGE_SEPARATOR + attribute, args));
        return ResponseMessage.ok(result);
    }

    @ApiOperation(value="geti18nMessage", notes="get i18n message for attributes", response = Map.class)
    @ApiResponse(code = 200, message = "OK")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "attributes", value = "attribute name", required = true, dataTypeClass = List.class)
    })
    @RequestMapping(method = RequestMethod.GET)
    public @ResponseBody ResponseMessage<Map<String, String>> geti18nMessage(@RequestParam("attributes") List<String> attributes){
        Map<String,String> result = new HashMap<>();
        for(String attribute:attributes){
            if(StringUtils.isNotEmpty(attribute)){
                result.put(attribute, localeMessageSourceService.getMessage(attribute));
            }
        }
        return ResponseMessage.ok(result);
    }
}
