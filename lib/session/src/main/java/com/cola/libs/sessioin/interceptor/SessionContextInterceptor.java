package com.cola.libs.sessioin.interceptor;

import com.cola.libs.beans.web.context.ExecutionContext;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Enumeration;
import java.util.Map;

/**
 * Created by jiachen.shi on 7/24/2017.
 */
public class SessionContextInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object handler) throws Exception {
        Map<String, Object> currentContext = ExecutionContext.getCurrentContext();
        HttpSession session = httpServletRequest.getSession();
        if(null != session){
            Enumeration attributeNames = session.getAttributeNames();
            if(null != attributeNames){
                while (attributeNames.hasMoreElements()){
                    String attributeName = (String)attributeNames.nextElement();
                    currentContext.put(attributeName, session.getAttribute(attributeName));
                }
            }

            Object localeObject = currentContext.get(SessionLocaleResolver.LOCALE_SESSION_ATTRIBUTE_NAME);
            if(null == localeObject){
                currentContext.put(SessionLocaleResolver.LOCALE_SESSION_ATTRIBUTE_NAME, LocaleContextHolder.getLocale());
            }

            String sessionId = session.getId();
            currentContext.put("sessionId", sessionId);
            ExecutionContext.setCurrnetContext(currentContext);
        }
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {
        /** nothing to do **/
    }

    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {
        /** nothing to do **/
    }
}
