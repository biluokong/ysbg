package com.biluo.process.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.biluo.model.process.ProcessTemplate;
import com.biluo.model.process.ProcessType;
import com.biluo.process.mapper.OaProcessTemplateMapper;
import com.biluo.process.mapper.OaProcessTypeMapper;
import com.biluo.process.service.OaProcessService;
import com.biluo.process.service.OaProcessTemplateService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 审批模板 服务实现类
 * </p>
 *
 * @author atguigu
 * @since 2023-02-14
 */
@Service
@RequiredArgsConstructor
public class OaProcessTemplateServiceImpl extends ServiceImpl<OaProcessTemplateMapper, ProcessTemplate> implements OaProcessTemplateService {
	private final OaProcessTypeMapper processTypeMapper;
	private final OaProcessService processService;

	// 分页查询审批模板，把审批类型对应名称查询
	@Override
	public IPage<ProcessTemplate> selectPageProcessTempate(Page<ProcessTemplate> pageParam) {
		// 1 调用mapper的方法实现分页查询
		Page<ProcessTemplate> processTemplatePage = baseMapper.selectPage(pageParam, null);

		// 2 第一步分页查询返回分页数据，从分页数据获取列表list集合
		List<ProcessTemplate> processTemplateList = processTemplatePage.getRecords();

		// 3 遍历list集合，得到每个对象的审批类型id
        /*for(ProcessTemplate processTemplate : processTemplateList) {
            //得到每个对象的审批类型id
            Long processTypeId = processTemplate.getProcessTypeId();
            //4 根据审批类型id，查询获取对应名称
            ProcessType processType = processTypeService.lambdaQuery().eq(ProcessType::getId,processTypeId).one();
            if(processType == null) {
                continue;
            }
            //5 完成最终封装processTypeName
            processTemplate.setProcessTypeName(processType.getName());
        }*/

		// 以上写法在循环里查表，不推荐；改为一此性把所有需要的类型查出来，在匹配给模板
		// 3 根据模版集合获取类型集合
		List<Long> processTypeIds = processTemplateList.stream().map(ProcessTemplate::getProcessTypeId).collect(Collectors.toList());
		List<ProcessType> processTypes = processTypeMapper.selectBatchIds(processTypeIds);

		// 4 把类型匹配给模板
		processTemplateList.forEach(processTemplate -> {
			processTypes.stream()
					.filter(processType -> processType.getId().equals(processTemplate.getProcessTypeId()))
					.findFirst()
					.ifPresent(processType -> processTemplate.setProcessTypeName(processType.getName()));
		});

		return processTemplatePage;
	}

	// 修改模板发布状态 1 已经发布
	// 流程定义部署
	@Override
	public void publish(Long id) {
		// 修改模板发布状态 1 已经发布
		ProcessTemplate processTemplate = baseMapper.selectById(id);
		processTemplate.setStatus(1);
		baseMapper.updateById(processTemplate);

		// 流程定义部署
		if (!StringUtils.isEmpty(processTemplate.getProcessDefinitionPath())) {
			processService.deployByZip(processTemplate.getProcessDefinitionPath());
		}
	}

}
