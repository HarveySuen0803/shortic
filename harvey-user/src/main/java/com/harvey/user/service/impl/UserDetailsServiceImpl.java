package com.harvey.user.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.harvey.common.exception.ClientException;
import com.harvey.security.model.dto.UserDetailsDto;
import com.harvey.user.common.constant.UserResult;
import com.harvey.user.common.model.po.*;
import com.harvey.user.service.*;
import jakarta.annotation.Resource;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @Author harvey
 * @Email harveysuen0803@gmail.com
 * @Date 2024-06-03
 */
@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    @Resource
    private UserService userService;
    
    @Resource
    private AuthService authService;
    
    @Resource
    private UserRoleService userRoleService;
    
    @Resource
    private UserAuthService userAuthService;
    
    @Resource
    private RoleAuthService roleAuthService;
    
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserPo userPo = userService.lambdaQuery()
            .eq(UserPo::getUsername, username)
            .one();
        if (userPo == null) {
            throw new ClientException(UserResult.USER_NOT_FOUND);
        }
        
        UserDetailsDto userDetailsDto = BeanUtil.copyProperties(userPo, UserDetailsDto.class);
        
        // Get authorities
        Long userId = userPo.getId();
        Set<String> authNameSet = getAuthNameSet(userId);
        String[] array = authNameSet.toArray(String[]::new);
        List<GrantedAuthority> authorities = AuthorityUtils.createAuthorityList(array);
        
        userDetailsDto.setAuthorities(authorities);
        
        return userDetailsDto;
    }
    
    /**
     * Get AuthNameSet by UserId.
     */
    private Set<String> getAuthNameSet(Long userId) {
        Set<Long> authIdSetFromAuthTable = getAuthIdSetFromAuthTable(userId);
        
        Set<Long> authIdSetFromRoleTable = getAuthIdSetFromRoleTable(userId);
        
        Set<Long> authIdSet = new HashSet<>();
        authIdSet.addAll(authIdSetFromAuthTable);
        authIdSet.addAll(authIdSetFromRoleTable);
        
        List<AuthPo> authPolist = authService.lambdaQuery()
            .in(AuthPo::getId, authIdSet)
            .list();
        
        Set<String> authNameSet = authPolist.stream()
            .map(AuthPo::getName)
            .collect(Collectors.toSet());
        
        return authNameSet;
    }
    
    /**
     * Get AuthIdSet from t_user_auth by UserId.
     */
    private Set<Long> getAuthIdSetFromAuthTable(Long userId) {
        List<UserAuthPo> userAuthPoList = userAuthService.lambdaQuery()
            .eq(UserAuthPo::getUserId, userId)
            .select(UserAuthPo::getAuthId)
            .list();
        
        Set<Long> authIdSet = userAuthPoList.stream()
            .map(UserAuthPo::getAuthId)
            .collect(Collectors.toSet());
        
        return authIdSet;
    }
    
    /**
     * Get RoleIdSet from t_user_role by UserId, then get AuthIdSet from t_role_auth by RoleIdSet.
     */
    private Set<Long> getAuthIdSetFromRoleTable(Long userId) {
        List<UserRolePo> userRolePoList = userRoleService.lambdaQuery()
            .eq(UserRolePo::getUserId, userId)
            .select(UserRolePo::getRoleId)
            .list();
        
        Set<Long> roleIdSet = userRolePoList.stream()
            .map(UserRolePo::getRoleId)
            .collect(Collectors.toSet());
        
        List<RoleAuthPo> roleAuthPoList = roleAuthService.lambdaQuery()
            .in(RoleAuthPo::getRoleId, roleIdSet)
            .list();
        
        Set<Long> authIdSet = roleAuthPoList.stream()
            .map(RoleAuthPo::getAuthId)
            .collect(Collectors.toSet());
        
        return authIdSet;
    }
}
