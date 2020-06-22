package com.hdu.testboot.interceptor;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * TODO
 *
 * @author: xs
 * @since: 2020-06-21
 */
@Component
public class TestInterceptor1 implements HandlerInterceptor {

    //在DispatcherServlet之前执行
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        System.out.println("##############TestInterceptor1 preHandle##############");
        return true;
    }

    //在Controller之后的DispatcherServlet之后执行
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        System.out.println("##############TestInterceptor1 postHandle##############");
    }

    // 在页面渲染完成之后返回给客户端执行
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        System.out.println("##############TestInterceptor1 afterCompletion##############");
    }
}
