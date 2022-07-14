package com.my.reggie.filter;

import com.alibaba.fastjson.JSON;
import com.my.reggie.common.BaseContext;
import com.my.reggie.common.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.AntPathMatcher;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebFilter(filterName = "LoginCheckFilter", urlPatterns = "/*")
@Slf4j
public class LoginCheckFilter implements Filter {
    public static final AntPathMatcher PATH_MATCHER = new AntPathMatcher();

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        //获取请求的URI
        String requestURI = request.getRequestURI();
        //不需要过滤的页面
        String[] urls = new String[]{
                //放行登录登出
                "/employee/login",
                "/employee/logout",
                //放行静态资源
                "/backend/**",
                "/front/**",
                //移动端登陆页面放行
                "/user/sendMsg",//发送验证码
                "/user/login"
        };
        log.info("获得请求:" + requestURI);
        //判断请求页面是否需要拦截
        boolean check = check(urls, requestURI);
        //不需要处理
        if (check) {
            log.info(requestURI + "该页面放行");
            filterChain.doFilter(request, response);
            return;
        }
        //判断后台用户是否登录
        if (request.getSession().getAttribute("employee") != null) {
            log.info("已登录，用户id为:" + request.getSession().getAttribute("employee"));
            //获取登录用户id
            Long empId = (Long) request.getSession().getAttribute("employee");
            BaseContext.setCurrentId(empId);

            filterChain.doFilter(request, response);
            return;
        }
        //判断user用户是否登录
        if (request.getSession().getAttribute("user") != null) {
            log.info("已登录，用户id为:" + request.getSession().getAttribute("user"));
            //获取登录用户id
            Long userId = (Long) request.getSession().getAttribute("user");
            BaseContext.setCurrentId(userId);

            filterChain.doFilter(request, response);
            return;
        }
        //未登录则通过输出流响应数据
        log.info("用户未登录,不可访问" + requestURI);
        response.getWriter().write(JSON.toJSONString(R.error("NOTLOGIN")));
        return;
    }

    public boolean check(String[] urls, String requestURI) {
        for (String uri : urls) {
            boolean match = PATH_MATCHER.match(uri, requestURI);
            if (match) {
                return true;
            }
        }
        return false;
    }
}
