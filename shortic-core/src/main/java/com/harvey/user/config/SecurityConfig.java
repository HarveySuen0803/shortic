package com.harvey.user.config;

import cn.hutool.core.date.DateField;
import cn.hutool.core.date.DateTime;
import cn.hutool.json.JSONUtil;
import cn.hutool.jwt.JWT;
import cn.hutool.jwt.JWTPayload;
import cn.hutool.jwt.JWTUtil;
import com.harvey.common.http.ResponseUtil;
import com.harvey.common.result.Result;
import com.harvey.user.cache.UserCacheKey;
import com.harvey.user.constant.UserConstant;
import com.harvey.user.domain.UserDo;
import com.harvey.user.filter.LoginTokenFilter;
import com.harvey.user.result.UserResult;
import com.harvey.user.vo.LoginVo;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;

import java.util.HashMap;

/**
 * @Author harvey
 * @Email harveysuen0803@gmail.com
 * @Date 2024-05-24
 */
@EnableWebSecurity
@Configuration
public class SecurityConfig {
    @Bean
    SecurityFilterChain securityFilterChain(
        HttpSecurity httpSecurity,
        AuthenticationSuccessHandler authenticationSuccessHandler,
        AuthenticationFailureHandler authenticationFailureHandler,
        LogoutHandler logoutHandler,
        LogoutSuccessHandler logoutSuccessHandler,
        LoginTokenFilter loginTokenFilter,
        AuthenticationEntryPoint authenticationEntryPoint,
        AuthenticationManager authenticationManager,
        AccessDeniedHandler accessDeniedHandler
    ) throws Exception {
        httpSecurity.authorizeHttpRequests((authorize) -> {
            authorize.requestMatchers("/login", "/logout").permitAll()
                .anyRequest().authenticated();
        });
        
        httpSecurity.formLogin((formLogin) -> {
            formLogin.loginProcessingUrl("/login")
                .usernameParameter("username")
                .passwordParameter("password")
                .successHandler(authenticationSuccessHandler)
                .failureHandler(authenticationFailureHandler);
        });
        
        httpSecurity.logout((logout) -> {
            logout.logoutUrl("/logout")
                .addLogoutHandler(logoutHandler)
                .logoutSuccessHandler(logoutSuccessHandler);
        });
        
        httpSecurity.csrf(AbstractHttpConfigurer::disable);
        httpSecurity.cors(AbstractHttpConfigurer::disable);
        
        httpSecurity.authenticationManager(authenticationManager);
        
        httpSecurity.exceptionHandling((exception) -> {
            exception.authenticationEntryPoint(authenticationEntryPoint)
                .accessDeniedHandler(accessDeniedHandler);
        });
        
        httpSecurity.addFilterBefore(loginTokenFilter, UsernamePasswordAuthenticationFilter.class);
        
        return httpSecurity.build();
    }
    
    @Bean
    public AuthenticationSuccessHandler authenticationSuccessHandler(RedisTemplate redisTemplate) {
        return (HttpServletRequest request, HttpServletResponse response, Authentication authentication) -> {
            UserDo userDo = (UserDo) authentication.getPrincipal();
            
            Long userId = userDo.getId();
            
            UserDo userDetails = new UserDo();
            userDetails.setId(userId);
            userDetails.setUsername(userDo.getUsername());
            userDetails.setAuthNameSet(userDo.getAuthNameSet());
            
            String userDetailsJson = JSONUtil.toJsonStr(userDetails);
            
            // Todo: Add remember option
            // Long ttl = Boolean.parseBoolean(request.getParameter(ApiParamConstant.REMEMBER)) ? UserCacheKey.LOGIN_TOKEN.ttl : UserCache.LOGIN_TOKEN_S_TTL;
            
            HashMap<String, Object> jwtPayload = new HashMap<>();
            jwtPayload.put(JWTPayload.ISSUED_AT, DateTime.now());
            jwtPayload.put(JWTPayload.EXPIRES_AT, DateTime.now().offset(DateField.MONTH, 1));
            jwtPayload.put(JWTPayload.NOT_BEFORE, DateTime.now());
            jwtPayload.put(UserConstant.USER_DETAILS_KEY, userDetailsJson);
            String token = JWTUtil.createToken(jwtPayload, UserConstant.LOGIN_TOKEN_KEY);
            
            redisTemplate.opsForValue().set(
                UserCacheKey.LOGIN_TOKEN.getKey(userId),
                token,
                UserCacheKey.LOGIN_TOKEN.timeout,
                UserCacheKey.LOGIN_TOKEN.unit
            );
            
            LoginVo loginVo = new LoginVo();
            loginVo.setUserId(userId);
            loginVo.setUsername(userDo.getUsername());
            loginVo.setToken(token);
            
            Result<LoginVo> loginVoResult = Result.success(loginVo);
            
            ResponseUtil.write(response, loginVoResult);
        };
    }
    
    @Bean
    public AuthenticationFailureHandler authenticationFailureHandler() {
        return (HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) -> {
            ResponseUtil.write(response, Result.FORBIDDEN);
        };
    }
    
    @Bean
    public LogoutHandler logoutHandler(RedisTemplate redisTemplate) {
        return (HttpServletRequest request, HttpServletResponse response, Authentication authentication) -> {
            JWT tokenJwt = JWT.of(request.getHeader(HttpHeaders.AUTHORIZATION)).setKey(UserConstant.LOGIN_TOKEN_KEY);
            
            // If the token is invalid or expired.
            if (!tokenJwt.verify() || !tokenJwt.validate(0)) {
                ResponseUtil.write(response, UserResult.LOGIN_TOKEN_EXPIRED);
                return;
            }
            
            // Get user info from token.
            UserDo userDetails = JSONUtil.toBean(tokenJwt.getPayload(UserConstant.USER_DETAILS_KEY).toString(), UserDo.class);
            
            redisTemplate.delete(UserCacheKey.LOGIN_TOKEN.getKey(userDetails.getId()));
        };
    }
    
    @Bean
    public LogoutSuccessHandler logoutSuccessHandler() {
        return (HttpServletRequest request, HttpServletResponse response, Authentication authentication) -> {
            ResponseUtil.write(response, Result.SUCCESS);
        };
    }
    
    @Bean
    public AuthenticationEntryPoint authenticationEntryPoint() {
        return (HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) -> {
            ResponseUtil.write(response, Result.FORBIDDEN);
        };
    }
    
    @Bean
    public AccessDeniedHandler accessDeniedHandler() {
        return (HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) -> {
            ResponseUtil.write(response, Result.FORBIDDEN);
        };
    }
    
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
    
    @Bean
    public AuthenticationManager authenticationManager(UserDetailsService userDetailsService, PasswordEncoder passwordEncoder) {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userDetailsService);
        provider.setPasswordEncoder(passwordEncoder);
        return new ProviderManager(provider);
    }
}
