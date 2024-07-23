package com.biluo.auth.controller;


import com.biluo.auth.service.SysMenuService;
import com.biluo.common.result.Result;
import com.biluo.model.system.SysMenu;
import com.biluo.vo.system.AssginMenuVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api(tags = "菜单管理接口")
@RestController
@RequestMapping("/admin/system/sysMenu")
@RequiredArgsConstructor
public class SysMenuController {
	private final SysMenuService sysMenuService;

	// 查询所有菜单和角色分配的菜单
	@ApiOperation("查询所有菜单和角色分配的菜单")
	@GetMapping("toAssign/{roleId}")
	public Result<List<SysMenu>> toAssign(@PathVariable Long roleId) {
		List<SysMenu> list = sysMenuService.findMenuByRoleId(roleId);
		return Result.ok(list);
	}

	@ApiOperation("角色分配菜单")
	@PostMapping("/doAssign")
	public Result doAssign(@RequestBody AssginMenuVo assginMenuVo) {
		sysMenuService.doAssign(assginMenuVo);
		return Result.ok();
	}

	// 菜单列表接口
	@ApiOperation("菜单列表")
	@GetMapping("findNodes")
	public Result<List<SysMenu>> findNodes() {

		List<SysMenu> list = sysMenuService.findNodes();
		return Result.ok(list);
	}

	@ApiOperation(value = "新增菜单")
	@PostMapping("save")
	public Result save(@RequestBody SysMenu sysMenu) {
		sysMenuService.save(sysMenu);
		return Result.ok();
	}

	@ApiOperation(value = "修改菜单")
	@PutMapping("update")
	public Result updateById(@RequestBody SysMenu sysMenu) {
		sysMenuService.updateById(sysMenu);
		return Result.ok();
	}

	@ApiOperation(value = "删除菜单")
	@DeleteMapping("remove/{id}")
	public Result remove(@PathVariable Long id) {
		sysMenuService.removeMenuById(id);
		return Result.ok();
	}
}

