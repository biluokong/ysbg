package com.biluo.auth.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.biluo.auth.service.SysMenuService;
import com.biluo.auth.service.SysUserService;
import com.biluo.common.config.exception.GlobalException;
import com.biluo.common.jwt.JwtHelper;
import com.biluo.common.result.Result;
import com.biluo.common.utils.MD5;
import com.biluo.model.system.SysUser;
import com.biluo.vo.system.LoginVo;
import com.biluo.vo.system.RouterVo;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Api(tags = "后台登录管理")
@RestController
@RequestMapping("/admin/system/index")
@RequiredArgsConstructor
public class IndexController {
	private final SysUserService sysUserService;
	private final SysMenuService sysMenuService;

	@PostMapping("login")
	public Result<Map<String, Object>> login(@RequestBody LoginVo loginVo) {
		// 1 获取输入用户名和密码
		// 2 根据用户名查询数据库
		String username = loginVo.getUsername();
		SysUser sysUser = sysUserService.lambdaQuery().eq(SysUser::getUsername, username).one();

		// 3 用户信息是否存在
		if (sysUser == null) {
			throw new GlobalException(201, "用户不存在");
		}

		// 4 判断密码
		// 数据库存密码（MD5）
		String password_db = sysUser.getPassword();
		// 获取输入的密码
		String password_input = MD5.encrypt(loginVo.getPassword());
		if (!password_db.equals(password_input)) {
			throw new GlobalException(201, "密码错误");
		}

		// 5 判断用户是否被禁用  1 可用 0 禁用
		if (sysUser.getStatus() == 0) {
			throw new GlobalException(201, "用户已经被禁用");
		}

		// 6 使用jwt根据用户id和用户名称生成token字符串
		String token = JwtHelper.createToken(sysUser.getId(), sysUser.getUsername());
		// 7 返回
		Map<String, Object> map = new HashMap<>();
		map.put("token", token);
		return Result.ok(map);
	}

	@GetMapping("info")
	public Result<Map<String, Object>> info(HttpServletRequest request) {
		// 1 从请求头获取用户信息（获取请求头token字符串）
		String token = request.getHeader("token");

		// 2 从token字符串获取用户id 或者 用户名称
		Long userId = JwtHelper.getUserId(token);

		// 3 根据用户id查询数据库，把用户信息获取出来
		SysUser sysUser = sysUserService.getById(userId);

		// 4 根据用户id获取用户可以操作菜单列表
		// 查询数据库动态构建路由结构，进行显示
		List<RouterVo> routerList = sysMenuService.findUserMenuListByUserId(userId);

		// 5 根据用户id获取用户可以操作按钮列表
		List<String> permsList = sysMenuService.findUserPermsByUserId(userId);

		// 6 返回相应的数据
		Map<String, Object> map = new HashMap<>();
		map.put("roles", "[admin]");
		map.put("name", sysUser.getName());
		map.put("avatar", "https://oss.aliyuncs.com/aliyun_id_photo_bucket/default_handsome.jpg");
		// 返回用户可以操作菜单
		map.put("routers", routerList);
		// 返回用户可以操作按钮
		map.put("buttons", permsList);
		return Result.ok(map);
	}

	@PostMapping("logout")
	public Result logout() {
		return Result.ok();
	}

}
