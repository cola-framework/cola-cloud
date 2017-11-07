package com.cola.api.i18n;

import com.cola.lib.beans.web.restful.ResponseMessage;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * Created by jiachen.shi on 7/20/2017.
 */
@FeignClient("i18n")
@RequestMapping("/i18n")
public interface I18nService {

    @RequestMapping(value = "/{website}/{page}", method = RequestMethod.GET)
    @ResponseBody ResponseMessage<Map<String, String>> geti18nMessage(@PathVariable("website") String website, @PathVariable("page") String page, @RequestParam("attributes") List<String> attributes);

    @RequestMapping(value ="/{website}/{page}/{attribute}", method = RequestMethod.GET)
    @ResponseBody ResponseMessage<Map<String, String>> geti18nMessage(@PathVariable("website")  String website, @PathVariable("page") String page, @PathVariable("attribute") String attribute, @RequestParam(value="args", required = false) List<Object> args);

    @RequestMapping(method = RequestMethod.GET)
    @ResponseBody ResponseMessage<Map<String, String>> geti18nMessage(@RequestParam("attributes") List<String> attributes);

}
