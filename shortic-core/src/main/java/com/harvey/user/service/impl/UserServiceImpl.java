package com.harvey.user.service.impl;

import cn.hutool.core.util.ObjUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.harvey.common.exception.ClientException;
import com.harvey.user.result.UserResult;
import com.harvey.user.domain.*;
import com.harvey.user.mapper.UserMapper;
import com.harvey.user.service.*;
import com.harvey.user.vo.UserVo;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
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
 * @Date 2024-05-22
 */
@Service
@RequiredArgsConstructor
public class UserServiceImpl extends ServiceImpl<UserMapper, UserDo> implements UserService, UserDetailsService {
    private final AuthService authService;
    
    private final UserRoleService userRoleService;
    
    private final UserAuthService userAuthService;
    
    private final RoleAuthService roleAuthService;
    
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
        
        // Convert AuthName to SimpleGrantedAuthority, Convert AuthNameSet to Authorities.
        List<GrantedAuthority> authorities = AuthorityUtils.createAuthorityList(authNameSet);
        userDo.setAuthorities(authorities);

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
    
    @Override
    public UserVo getUser(String username) {
        UserDo userDo = lambdaQuery()
            .select(UserDo::getUsername, UserDo::getEmail)
            .eq(UserDo::getUsername, username)
            .one();
        if (ObjUtil.isNull(userDo)) {
            throw new ClientException(UserResult.USER_NOT_FOUND);
        }

        UserVo userVo = new UserVo();
        BeanUtils.copyProperties(userDo, userVo);

        return userVo;
    }
    
    @Override
    public boolean isUserExists(String username, String email) {
        return isUsernameExists(username) || isEmailExists(email);
    }
    
    @Override
    public boolean isUsernameExists(String username) {
        boolean isExists = lambdaQuery()
            .eq(UserDo::getUsername, username)
            .exists();
        
        return isExists;
    }
    
    @Override
    public boolean isEmailExists(String email) {
        boolean isExists = lambdaQuery()
            .eq(UserDo::getEmail, email)
            .exists();
        
        return isExists;
    }
}