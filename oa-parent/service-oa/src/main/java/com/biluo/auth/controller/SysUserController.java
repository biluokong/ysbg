package com.biluo.auth.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.biluo.auth.service.SysUserService;
import com.biluo.common.result.Result;
import com.biluo.common.utils.MD5;
import com.biluo.model.system.SysUser;
import com.biluo.vo.system.SysUserQueryVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

@Api(tags = "用户管理接口")
@RestController
@RequestMapping("/admin/system/sysUser")
@RequiredArgsConstructor
public class SysUserController {
	private final SysUserService sysUserService;

	// 用户条件分页查询
	@ApiOperation("用户条件分页查询")
	@GetMapping("{page}/{limit}")
	public Result<IPage<SysUser>> index(@PathVariable Long page,
						@PathVariable Long limit,
						SysUserQueryVo sysUserQueryVo) {
		// 创建page对象
		Page<SysUser> pageParam = new Page<>(page, limit);

		// 封装条件，判断条件值不为空
		LambdaQueryWrapper<SysUser> wrapper = new LambdaQueryWrapper<>();
		// 获取条件值
		String username = sysUserQueryVo.getKeyword();
		String createTimeBegin = sysUserQueryVo.getCreateTimeBegin();
		String createTimeEnd = sysUserQueryVo.getCreateTimeEnd();

		// 调用mp的方法实现条件分页查询
		IPage<SysUser> pageModel = sysUserService.lambdaQuery()
				.like(StringUtils.hasText(username), SysUser::getUsername, username)
				.ge(ObjectUtils.isNotEmpty(createTimeBegin), SysUser::getCreateTime, createTimeBegin)
				.le(ObjectUtils.isNotEmpty(createTimeEnd), SysUser::getCreateTime, createTimeEnd)
				.page(pageParam);
		return Result.ok(pageModel);
	}

	@ApiOperation(value = "获取用户")
	@GetMapping("get/{id}")
	public Result get(@PathVariable Long id) {
		SysUser user = sysUserService.getById(id);
		return Result.ok(user);
	}

	@ApiOperation(value = "保存用户")
	@PostMapping("save")
	public Result save(@RequestBody SysUser user) {
		//密码进行加密，使用MD5
		String passwordMD5 = MD5.encrypt(user.getPassword());
		user.setPassword(passwordMD5);

		sysUserService.save(user);
		return Result.ok();
	}

	@ApiOperation(value = "更新用户")
	@PutMapping("update")
	public Result updateById(@RequestBody SysUser user) {
		sysUserService.updateById(user);
		return Result.ok();
	}

	@ApiOperation(value = "删除用户")
	@DeleteMapping("remove/{id}")
	public Result remove(@PathVariable Long id) {
		sysUserService.removeById(id);
		return Result.ok();
	}

	@ApiOperation(value = "更新状态")
	@GetMapping("updateStatus/{id}/{status}")
	public Result updateStatus(@PathVariable Long id, @PathVariable Integer status) {
		sysUserService.updateStatus(id,status);
		return Result.ok();
	}
}

