package com.biluo.process.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.biluo.model.process.ProcessTemplate;
import com.biluo.model.process.ProcessType;
import com.biluo.process.mapper.OaProcessTypeMapper;
import com.biluo.process.service.OaProcessTemplateService;
import com.biluo.process.service.OaProcessTypeService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 审批类型 服务实现类
 * </p>
 *
 * @author atguigu
 * @since 2023-02-14
 */
@Service
@RequiredArgsConstructor
public class OaProcessTypeServiceImpl extends ServiceImpl<OaProcessTypeMapper, ProcessType> implements OaProcessTypeService {
	private final OaProcessTemplateService processTemplateService;

	// 查询所有审批分类和每个分类所有审批模板
	@Override
	public List<ProcessType> findProcessType() {
		// 1 查询所有审批分类，返回list集合
		List<ProcessType> processTypeList = baseMapper.selectList(null);

		// 2 遍历返回所有审批分类list集合
		/*for (ProcessType processType : processTypeList) {
			// 3 得到每个审批分类，根据审批分类id查询对应审批模板
			// 审批分类id
			Long typeId = processType.getId();
			// 根据审批分类id查询对应审批模板
			List<ProcessTemplate> processTemplateList = processTemplateService.lambdaQuery()
					.eq(ProcessTemplate::getProcessTypeId, typeId)
					.list();

			// 4 根据审批分类id查询对应审批模板数据（List）封装到每个审批分类对象里面
			processType.setProcessTemplateList(processTemplateList);
		}*/
		// 改进
		List<Long> typeIds = processTypeList.stream().map(ProcessType::getId).collect(Collectors.toList());
		List<ProcessTemplate> processTemplates = processTemplateService.lambdaQuery()
				.in(ProcessTemplate::getProcessTypeId, typeIds)
				.list();
		processTypeList.forEach(processType -> {
			List<ProcessTemplate> currProcessTemplates = processTemplates.stream()
					.filter(processTemplate -> processTemplate.getProcessTypeId().equals(processType.getId()))
					.collect(Collectors.toList());
			processType.setProcessTemplateList(currProcessTemplates);
		});
		return processTypeList;
	}
}
