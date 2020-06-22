package com.hdu.testboot.filter;

import org.springframework.core.annotation.Order;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import java.io.IOException;

/**
 * TODO
 *
 * @author: xs
 * @since: 2020-06-21
 */
@WebFilter(urlPatterns = "/hello")
@Order(3)
public class TestFilter2 implements Filter {
    @Override
    public void init(javax.servlet.FilterConfig filterConfig) throws ServletException {
        System.out.println("##############Filter2 init##############");
    }

//    public void init(FilterConfig filterConfig) throws ServletException {
//        System.out.println("##############Filter2 init##############");
//    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        //在DispatcherServlet之前执行
        System.out.println("##############doFilter2 before##############");
        filterChain.doFilter(servletRequest, servletResponse);
        // 在视图页面返回给客户端之前执行，但是执行顺序在Interceptor之后
        System.out.println("##############doFilter2 after##############");
    }

    @Override
    public void destroy() {
        System.out.println("##############Filter2 destroy##############");
    }
}
