package com.ll.annotation.conf;

import javax.servlet.*;
import java.io.IOException;

/**
 * @author liulei
 * @Description
 * @create 2022/4/1 22:02
 */
public class CreateFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        System.out.println("CreateFilter init........");
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        System.out.println("doFilter");
        filterChain.doFilter(servletRequest,servletResponse);
    }

    @Override
    public void destroy() {
        System.out.println("CreateFilter destroy........");
    }
}
