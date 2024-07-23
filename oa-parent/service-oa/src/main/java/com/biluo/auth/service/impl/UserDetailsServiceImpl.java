package com.biluo.auth.service.impl;

import com.biluo.auth.service.SysMenuService;
import com.biluo.auth.service.SysUserService;
import com.biluo.model.system.SysUser;
import com.biluo.security.custom.CustomUser;
import com.biluo.security.custom.UserDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {
	private final SysUserService sysUserService;
	private final SysMenuService sysMenuService;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		SysUser sysUser = sysUserService.getUserByUserName(username);
		if (sysUser == null) {
			throw new UsernameNotFoundException("用户名不存在！");
		}
		if (sysUser.getStatus() == 0) {
			throw new RuntimeException("账号已停用！");
		}
		List<String> perms = sysMenuService.findUserPermsByUserId(sysUser.getId());
		List<SimpleGrantedAuthority> authorityList = perms.stream()
				.map(perm -> new SimpleGrantedAuthority(perm.trim()))
				.collect(Collectors.toList());
		return new CustomUser(sysUser, authorityList);
	}
}
