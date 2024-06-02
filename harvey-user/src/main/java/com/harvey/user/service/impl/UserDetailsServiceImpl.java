package com.harvey.user.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.harvey.common.exception.ClientException;
import com.harvey.security.entity.dto.UserDetailsDto;
import com.harvey.user.common.constant.UserResult;
import com.harvey.user.common.entity.domain.*;
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
        UserDo userDo = userService.lambdaQuery()
            .eq(UserDo::getUsername, username)
            .one();
        if (userDo == null) {
            throw new ClientException(UserResult.USER_NOT_FOUND);
        }
        
        UserDetailsDto userDetailsDto = BeanUtil.copyProperties(userDo, UserDetailsDto.class);
        
        // Get authorities
        Long userId = userDo.getId();
        Set<String> authNameSet = getAuthNameSet(userId);
        List<GrantedAuthority> authorities = AuthorityUtils.createAuthorityList(authNameSet);
        
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
        
        List<AuthDo> authDolist = authService.lambdaQuery()
            .in(AuthDo::getId, authIdSet)
            .list();
        
        Set<String> authNameSet = authDolist.stream()
            .map(AuthDo::getName)
            .collect(Collectors.toSet());
        
        return authNameSet;
    }
    
    /**
     * Get AuthIdSet from t_user_auth by UserId.
     */
    private Set<Long> getAuthIdSetFromAuthTable(Long userId) {
        List<UserAuthDo> userAuthDoList = userAuthService.lambdaQuery()
            .eq(UserAuthDo::getUserId, userId)
            .select(UserAuthDo::getAuthId)
            .list();
        
        Set<Long> authIdSet = userAuthDoList.stream()
            .map(UserAuthDo::getAuthId)
            .collect(Collectors.toSet());
        
        return authIdSet;
    }
    
    /**
     * Get RoleIdSet from t_user_role by UserId, then get AuthIdSet from t_role_auth by RoleIdSet.
     */
    private Set<Long> getAuthIdSetFromRoleTable(Long userId) {
        List<UserRoleDo> userRoleDoList = userRoleService.lambdaQuery()
            .eq(UserRoleDo::getUserId, userId)
            .select(UserRoleDo::getRoleId)
            .list();
        
        Set<Long> roleIdSet = userRoleDoList.stream()
            .map(UserRoleDo::getRoleId)
            .collect(Collectors.toSet());
        
        List<RoleAuthDo> roleAuthDoList = roleAuthService.lambdaQuery()
            .in(RoleAuthDo::getRoleId, roleIdSet)
            .list();
        
        Set<Long> authIdSet = roleAuthDoList.stream()
            .map(RoleAuthDo::getAuthId)
            .collect(Collectors.toSet());
        
        return authIdSet;
    }
}
