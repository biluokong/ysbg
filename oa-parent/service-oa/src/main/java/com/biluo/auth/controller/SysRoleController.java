package com.biluo.auth.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.biluo.auth.service.SysRoleService;
import com.biluo.common.result.Result;
import com.biluo.model.system.SysRole;
import com.biluo.vo.system.AssginRoleVo;
import com.biluo.vo.system.SysRoleQueryVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.Authorization;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Api(tags = "角色管理接口")
@RestController
@RequestMapping("/admin/system/sysRole")
@RequiredArgsConstructor
public class SysRoleController {
	private final SysRoleService sysRoleService;

	// 查询所有角色 和 当前用户所属角色
	@ApiOperation("获取角色")
	@GetMapping("/toAssign/{userId}")
	public Result<Map<String, Object>> toAssign(@PathVariable Long userId) {
		Map<String, Object> map = sysRoleService.findRoleDataByUserId(userId);
		return Result.ok(map);
	}

	// 为用户分配角色
	@ApiOperation("为用户分配角色")
	@PostMapping("/doAssign")
	public Result doAssign(@RequestBody AssginRoleVo assginRoleVo) {
		sysRoleService.doAssign(assginRoleVo);
		return Result.ok();
	}

	// 条件分页查询
	@PreAuthorize("hasAuthority('bnt.sysRole.list')")
	@ApiOperation("条件分页查询")
	@GetMapping("{page}/{limit}")
	public Result<IPage<SysRole>> pageQueryRole(@PathVariable Long page,
												@PathVariable Long limit,
												SysRoleQueryVo sysRoleQueryVo) {
		// 调用service的方法实现
		// 1 创建Page对象，传递分页相关参数
		// page 当前页  limit 每页显示记录数
		Page<SysRole> pageParam = new Page<>(page, limit);

		// 2 封装条件，判断条件是否为空，不为空进行封装
		String roleName = sysRoleQueryVo.getRoleName();
		return Result.ok(sysRoleService.lambdaQuery()
				.eq(StringUtils.hasText(roleName), SysRole::getRoleName, roleName)
				.page(pageParam));
	}

	// 添加角色
	@PreAuthorize("hasAuthority('bnt.sysRole.add')")
	@ApiOperation("添加角色")
	@PostMapping("save")
	public Result save(@RequestBody SysRole role) {
		// 调用service的方法
		boolean success = sysRoleService.save(role);
		if (success) {
			return Result.ok();
		} else {
			return Result.fail();
		}
	}

	// 修改角色-根据id查询
	@PreAuthorize("hasAuthority('bnt.sysRole.list')")
	@ApiOperation("根据id查询")
	@GetMapping("get/{id}")
	public Result<SysRole> get(@PathVariable Long id) {
		SysRole sysRole = sysRoleService.getById(id);
		return Result.ok(sysRole);
	}

	// 修改角色-最终修改
	@PreAuthorize("hasAuthority('bnt.sysRole.update')")
	@ApiOperation("修改角色")
	@PutMapping("update")
	public Result update(@RequestBody SysRole role) {
		// 调用service的方法
		boolean success = sysRoleService.updateById(role);
		if (success) {
			return Result.ok();
		} else {
			return Result.fail();
		}
	}

	// 根据id删除
	@PreAuthorize("hasAuthority('bnt.sysRole.remove')")
	@ApiOperation("根据id删除")
	@DeleteMapping("remove/{id}")
	public Result remove(@PathVariable Long id) {
		boolean is_success = sysRoleService.removeById(id);
		if (is_success) {
			return Result.ok();
		} else {
			return Result.fail();
		}
	}

	// 批量删除
	@PreAuthorize("hasAuthority('bnt.sysRole.remove')")
	@ApiOperation("批量删除")
	@DeleteMapping("batchRemove")
	public Result batchRemove(@RequestBody List<Long> idList) {
		boolean success = sysRoleService.removeByIds(idList);
		if (success) {
			return Result.ok();
		} else {
			return Result.fail();
		}
	}
}
