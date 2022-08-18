package com.web.filter;


import com.alibaba.druid.support.json.JSONUtils;
import com.alibaba.fastjson.JSON;
import com.web.common.BaseContext;
import com.web.common.CustomException;
import com.web.common.R;
import com.web.domain.Employee;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.*;
import javax.servlet.annotation.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebFilter(filterName = "LoginFilter",urlPatterns = "/*")
@Slf4j
public class LoginFilter implements Filter {
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws ServletException, IOException {
        request.setCharacterEncoding("utf-8");
        response.setContentType("application/json;charset=utf-8");

        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse resp = (HttpServletResponse) response;
        long id = Thread.currentThread().getId();
//        log.info("线程id{}",id);
        String[] urls={"/backend","/front","/employee/login","/employee/logout","/user/sendMsg","/user/login"};
        for (String url:urls) {
            if (req.getRequestURI().contains(url)){
                chain.doFilter(request, response);
                return;
            }
        }


        Long employeeId = (Long) req.getSession().getAttribute("employee");
        if(employeeId != null){
//            log.info("当前登录id{}",employeeId);

            //给threadLocal传入用户id
            BaseContext.setId(employeeId);

            chain.doFilter(request, response);
            return;

        }

        Long userId = (Long) req.getSession().getAttribute("user");
        if(userId != null){
//            log.info("当前登录id{}",employeeId);

            //给threadLocal传入用户id
            BaseContext.setId(userId);

            chain.doFilter(request, response);
            return;
        }

        //todo 有问题
        log.info("拦截到请求{}",req.getRequestURI());
        //resp.getWriter().write(JSON.toJSONString(R.error("未登录")));   //err:getOutputStream() has already been called for this response   和图片下载冲突 out变量实际上是通过response.getWriter得到的，程序中既用了response.getOutputStream，又用了out变量
        throw new CustomException("未登录");

    }
}
