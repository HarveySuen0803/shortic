package com.harvey.user.service.impl;

import cn.hutool.core.util.ObjUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.harvey.common.exception.ClientException;
import com.harvey.user.result.UserResult;
import com.harvey.user.domain.*;
import com.harvey.user.mapper.UserMapper;
import com.harvey.user.service.*;
import com.harvey.user.vo.UserVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
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
 * @Date 2024-05-22
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, UserDo> implements UserService, UserDetailsService {
    @Autowired
    private AuthService authService;
    
    @Autowired
    private UserRoleService userRoleService;
    
    @Autowired
    private UserAuthService userAuthService;
    
    @Autowired
    private RoleAuthService roleAuthService;
    
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserDo userDo = lambdaQuery()
            .eq(UserDo::getUsername, username)
            .one();
        if (userDo == null) {
            throw new ClientException(UserResult.USER_NOT_FOUND);
        }

        Long userId = userDo.getId();

        Set<String> authNameSet = getAuthNameSet(userId);

        userDo.setAuthNameSet(authNameSet);

        return userDo;
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

        Set<String> authNameList = authDolist.stream()
            .map(AuthDo::getName)
            .collect(Collectors.toSet());

        return authNameList;
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
    
    @Override
    public UserVo getUser(String username) {
        UserDo userDo = lambdaQuery()
            .select(UserDo::getUsername, UserDo::getPhone, UserDo::getEmail)
            .eq(UserDo::getUsername, username)
            .one();
        if (ObjUtil.isNull(userDo)) {
            throw new ClientException(UserResult.USER_NOT_FOUND);
        }
        
        UserVo userVo = new UserVo();
        BeanUtils.copyProperties(userDo, userVo);
        
        return userVo;
    }
}