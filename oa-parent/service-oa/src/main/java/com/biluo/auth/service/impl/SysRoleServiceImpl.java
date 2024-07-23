package com.biluo.auth.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.biluo.auth.mapper.SysRoleMapper;
import com.biluo.auth.service.SysRoleService;
import com.biluo.auth.service.SysUserRoleService;
import com.biluo.model.system.SysRole;
import com.biluo.model.system.SysUserRole;
import com.biluo.vo.system.AssginRoleVo;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SysRoleServiceImpl extends ServiceImpl<SysRoleMapper, SysRole> implements SysRoleService {
	private final SysUserRoleService sysUserRoleService;

	@Override
	public Map<String, Object> findRoleDataByUserId(Long userId) {
		// 1 查询所有角色，返回list集合，返回
		List<SysRole> allRoleList =
				baseMapper.selectList(null);

		// 2 根据userid查询 角色用户关系表，查询userid对应所有角色id
		List<SysUserRole> existUserRoleList = sysUserRoleService.lambdaQuery()
				.eq(SysUserRole::getUserId, userId)
				.list();

		List<Long> existRoleIdList =
				existUserRoleList.stream().map(SysUserRole::getRoleId).collect(Collectors.toList());

		// 3 根据查询所有角色id，找到对应角色信息
		// 根据角色id到所有的角色的list集合进行比较
		List<SysRole> assignRoleList = new ArrayList<>();
		for (SysRole sysRole : allRoleList) {
			// 比较
			if (existRoleIdList.contains(sysRole.getId())) {
				assignRoleList.add(sysRole);
			}
		}

		// 4 把得到两部分数据封装map集合，返回
		Map<String, Object> roleMap = new HashMap<>();
		roleMap.put("assginRoleList", assignRoleList);
		roleMap.put("allRolesList", allRoleList);
		return roleMap;
	}

	@Override
	public void doAssign(AssginRoleVo assginRoleVo) {
		sysUserRoleService.lambdaUpdate()
				.eq(SysUserRole::getUserId, assginRoleVo.getUserId())
				.remove();

		// 重新进行分配
		List<Long> roleIdList = assginRoleVo.getRoleIdList();
		for (Long roleId : roleIdList) {
			if (ObjectUtils.isEmpty(roleId)) {
				continue;
			}
			SysUserRole sysUserRole = new SysUserRole();
			sysUserRole.setUserId(assginRoleVo.getUserId());
			sysUserRole.setRoleId(roleId);
			sysUserRoleService.save(sysUserRole);
		}
	}
}
