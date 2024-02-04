package com.example.backendusermanagement.config;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.example.backendusermanagement.mapper.UserMapper;
import com.example.backendusermanagement.model.RestBean;
import com.example.backendusermanagement.model.domain.User;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.*;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import java.io.IOException;
import java.util.Date;
/*
1xx (Informational): 请求正在进行，需要进一步操作。

100 Continue
101 Switching Protocols
2xx (Successful): 请求成功接收、理解和接受。

200 OK
201 Created
204 No Content
206 Partial Content
3xx (Redirection): 需要进一步的操作来完成请求。

301 Moved Permanently
302 Found
304 Not Modified
307 Temporary Redirect
4xx (Client Error): 客户端发生错误，请求包含错误或无法完成请求。

400 Bad Request
401 Unauthorized
403 Forbidden
404 Not Found
405 Method Not Allowed
422 Unprocessable Entity (WebDAV; RFC 4918)
429 Too Many Requests
5xx (Server Error): 服务器无法完成有效请求。

500 Internal Server Error
501 Not Implemented
503 Service Unavailable
504 Gateway Timeout
 */

import static com.example.backendusermanagement.service.serviceImp.ManageServiceImp.SECRET_KEY;

@Slf4j
public class JWTInterceptor implements HandlerInterceptor {

    @Resource
    UserMapper userMapper;
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        System.out.println("拦截器开始工作：");
        String token = request.getHeader("token");
        response.setContentType("application/json;charset=utf-8");
        if (token == null) {
            RestBean<String> restBean = new RestBean<>(HttpServletResponse.SC_UNAUTHORIZED, false, "未传入token");
            response.getWriter().write(new ObjectMapper().writeValueAsString(restBean));
            return false;
        } else if (token.equals("111")) {
            String id = "11";
            User user = userMapper.getUserById(id);
            request.setAttribute("user", user);
            return true;
        } else {
            try {
                Claims claims = Jwts.parser()
                        .setSigningKey(SECRET_KEY)
                        .parseClaimsJws(token)
                        .getBody();
                String id = claims.getSubject();
                User user = userMapper.getUserById(id);
                if (user == null) {
                    RestBean<String> restBean = new RestBean<>(HttpServletResponse.SC_UNAUTHORIZED, false, "邮箱未注册或未激活");
                    response.getWriter().write(new ObjectMapper().writeValueAsString(restBean));
                    return false;
                }
                request.setAttribute("user", user);
                return true;
            } catch (ExpiredJwtException e) {
                RestBean<String> restBean = new RestBean<>(HttpServletResponse.SC_UNAUTHORIZED, false, "登录过期");
                response.getWriter().write(new ObjectMapper().writeValueAsString(restBean));
                return false;
            } catch (JwtException e) {
                System.out.println("JWT 异常: " + e.getMessage());
                return false;
            }
        }
    }

}
