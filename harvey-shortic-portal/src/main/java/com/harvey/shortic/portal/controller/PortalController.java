package com.harvey.shortic.portal.controller;

import com.harvey.common.exception.ServerException;
import com.harvey.common.constant.Result;
import com.harvey.shortic.link.common.service.LinkRpcService;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

/**
 * @Author harvey
 * @Email harveysuen0803@gmail.com
 * @Date 2024-06-06
 */
@RestController
public class PortalController {
    @Resource
    private HttpServletResponse response;
    
    @DubboReference(url = "dubbo://127.0.0.1:30115", parameters = {"serialization", "fastjson2"})
    private LinkRpcService linkRpcService;
    
    @GetMapping("/{shortUri}")
    public Result<Void> redirectToLongUrl(@PathVariable String shortUri) {
        String longUrl = linkRpcService.getLongUrl(shortUri);
        
        try {
            response.sendRedirect(longUrl);
        } catch (IOException e) {
            throw new ServerException(e);
        }
        
        return Result.success();
    }
}
