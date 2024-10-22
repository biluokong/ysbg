package com.biluo.process.controller;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.biluo.common.result.Result;
import com.biluo.model.process.ProcessType;
import com.biluo.process.service.OaProcessTypeService;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 审批类型 前端控制器
 * </p>
 *
 * @author atguigu
 * @since 2023-02-14
 */
@RestController
@RequestMapping(value = "/admin/process/processType")
@RequiredArgsConstructor
public class OaProcessTypeController {
	private final OaProcessTypeService processTypeService;

	// 查询所有审批分类
	@GetMapping("findAll")
	public Result<List<ProcessType>> findAll() {
		List<ProcessType> list = processTypeService.list();
		return Result.ok(list);
	}

	@ApiOperation(value = "获取分页列表")
	@GetMapping("{page}/{limit}")
	public Result<IPage<ProcessType>> index(@PathVariable Long page,
											@PathVariable Long limit) {
		Page<ProcessType> pageParam = new Page<>(page, limit);
		IPage<ProcessType> pageModel = processTypeService.page(pageParam);
		return Result.ok(pageModel);
	}

	@ApiOperation(value = "获取")
	@GetMapping("get/{id}")
	public Result<ProcessType> get(@PathVariable Long id) {
		ProcessType processType = processTypeService.getById(id);
		return Result.ok(processType);
	}

	@ApiOperation(value = "新增")
	@PostMapping("save")
	public Result save(@RequestBody ProcessType processType) {
		processTypeService.save(processType);
		return Result.ok();
	}

	@ApiOperation(value = "修改")
	@PutMapping("update")
	public Result updateById(@RequestBody ProcessType processType) {
		processTypeService.updateById(processType);
		return Result.ok();
	}

	@ApiOperation(value = "删除")
	@DeleteMapping("remove/{id}")
	public Result remove(@PathVariable Long id) {
		processTypeService.removeById(id);
		return Result.ok();
	}
}

