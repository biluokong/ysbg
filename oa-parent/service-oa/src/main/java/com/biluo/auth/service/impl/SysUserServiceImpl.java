package com.biluo.auth.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.biluo.auth.mapper.SysUserMapper;
import com.biluo.auth.service.SysUserService;
import com.biluo.model.system.SysUser;
import com.biluo.security.custom.LoginUserInfoHelper;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class SysUserServiceImpl extends ServiceImpl<SysUserMapper, SysUser> implements SysUserService {

	@Override
	public void updateStatus(Long id, Integer status) {
		// 根据userid查询用户对象
		SysUser sysUser = baseMapper.selectById(id);
		// 设置修改状态值
		sysUser.setStatus(status);
		// 调用方法进行修改
		baseMapper.updateById(sysUser);
	}

	// 根据用户名进行查询
	@Override
	public SysUser getUserByUserName(String username) {
		return lambdaQuery().eq(SysUser::getUsername, username).one();
	}

	@Override
	public Map<String, Object> getCurrentUser() {
		SysUser sysUser = baseMapper.selectById(LoginUserInfoHelper.getUserId());
		Map<String, Object> map = new HashMap<>();
		map.put("name", sysUser.getName());
		map.put("phone", sysUser.getPhone());
		return map;
	}
}
