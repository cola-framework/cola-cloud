package com.cola.service.website.support;

import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Created by jiachen.shi on 7/19/2017.
 */
public class LocaleMessageSourceService {

    @Resource
    private MessageSource messageSource;

    public String getMessage(String code){
        return this.getMessage(code,new ArrayList<>());
    }

    public String getMessage(String code,String defaultMessage){
        return this.getMessage(code, null,defaultMessage);
    }

    public String getMessage(String code,String defaultMessage,Locale locale){
        return this.getMessage(code, null,defaultMessage,locale);
    }

    public String getMessage(String code,Locale locale){
        return this.getMessage(code,null,"",locale);
    }

    public String getMessage(String code,List<Object> args){
        return this.getMessage(code, args,"");
    }

    public String getMessage(String code, List<Object> args,Locale locale){
        return this.getMessage(code, args,"",locale);
    }

    public String getMessage(String code, List<Object> args,String defaultMessage){
        Locale  locale = LocaleContextHolder.getLocale();
        return this.getMessage(code, args, defaultMessage, locale);
    }

    public String getMessage(String code, List<Object> args,String defaultMessage,Locale  locale){
        if(null == args){
            args = new ArrayList<>();
        }
        return messageSource.getMessage(code, args.toArray(), defaultMessage,locale);
    }

}