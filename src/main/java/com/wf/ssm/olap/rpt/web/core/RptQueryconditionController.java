/*
 * Copyright &copy; 2011-2020 lnint Inc. All right reserved.
 * 
 * 修改信息：【与SVN提交信息一致】
 * A: 新增类 严娜 2015-11-10
 */
package com.wf.ssm.olap.rpt.web.core;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.wf.ssm.common.config.Global;
import com.wf.ssm.common.persistence.Page;
import com.wf.ssm.common.utils.StringUtils;
import com.wf.ssm.common.web.BaseController;
import com.wf.ssm.olap.rpt.entity.core.RptQuerycondition;
import com.wf.ssm.olap.rpt.service.core.RptCmService;
import com.wf.ssm.olap.rpt.service.core.RptQueryconditionService;

/**
 * 报表查询条件维护Controller
 *
 * @version 1.0
 * @author wangpf 2015-11-10
 * @since JDK 1.6
 */
@Controller
@RequestMapping(value = "${adminPath}/rpt/core/rptQuerycondition")
public class RptQueryconditionController extends BaseController {

	@Autowired
	private RptQueryconditionService rptQueryconditionService;
	@Autowired
	private RptCmService rptCmService;
	/**
	 * <P>ModelAttribute 构造函数</P>
	 * @param id 实体主键;
	 * @return RptQuerycondition
	 */
	@ModelAttribute
	public RptQuerycondition get(@RequestParam(required=false) String id) {
		if (StringUtils.isNotBlank(id)){
			return rptQueryconditionService.get(id);
		}else{
			return new RptQuerycondition();
		}
	}
	/**
	 * <P>列表list查询函数</P>
	 * @param rptQuerycondition 实体模型;
	 * @param request  请求;
	 * @param response 返回;
	 * @param model    视图模型;
	 * @return view URL
	 */
	@RequiresPermissions("rpt:core:rptQuerycondition:view")
	@RequestMapping(value = {"list", ""})
	public String list(String rptCmId,RptQuerycondition rptQuerycondition, HttpServletRequest request, HttpServletResponse response, Model model) {
		//设定列表每页记录数
		rptQuerycondition.setRptCm(rptCmService.get(rptCmId));
		Page<RptQuerycondition> temp=new Page<RptQuerycondition>(request, response);
		//获得列表分页数据
		Page<RptQuerycondition> page = rptQueryconditionService.find(temp, rptQuerycondition); 
		model.addAttribute("page", page);
		model.addAttribute("rptCmId",rptCmId);
		//转向页面
		return "olap/rpt/core/rptQueryconditionList";
	}
    /**
	 * <P>表单form查询函数</P>
	 * @param rptQuerycondition 实体模型;
	 * @param model 视图模型;
	 * @return view URL
	 */
	@RequiresPermissions("rpt:core:rptQuerycondition:view")
	@RequestMapping(value = "form")
	public String form(RptQuerycondition rptQuerycondition, Model model) {
	    //由ModelAttribute默认构造rptQuerycondition实体模型
		model.addAttribute("rptQuerycondition", rptQuerycondition);
		//转向页面
		return "olap/rpt/core/rptQueryconditionForm";
	}
    /**
	 * <P>表单form保存函数</P>
	 * @param rptQuerycondition 实体模型;
	 * @param model 视图模型;
	 * @param redirectAttributes 重定向;
	 * @return view URL
	 */
	@RequiresPermissions("rpt:core:rptQuerycondition:edit")
	@RequestMapping(value = "save")
	public String save(RptQuerycondition rptQuerycondition, Model model, RedirectAttributes redirectAttributes) {
		//实体Validator
		if (!beanValidator(model, rptQuerycondition)){
			return form(rptQuerycondition, model);
		}
		//保存表单
		rptQueryconditionService.save(rptQuerycondition);
		//页面提示信息
		addMessage(redirectAttributes, "保存报表查询条件维护成功");
		//转向页面
		return "redirect:"+Global.getAdminPath()+"/olap/rpt/core/rptQuerycondition/?repage";
	}
	/**
	 * <P>列表list删除函数</P>
	 * @param id 实体主键;
	 * @param redirectAttributes 重定向;
	 * @return view URL
	 */	
	@RequiresPermissions("rpt:core:rptQuerycondition:edit")
	@RequestMapping(value = "delete")
	public String delete(String id,String rptCmId, RedirectAttributes redirectAttributes) {
		//删除操作
		rptQueryconditionService.delete(id);
		//页面提示信息
		addMessage(redirectAttributes, "删除报表查询条件成功");
		//转向页面
		return "redirect:"+Global.getAdminPath()+"/olap/rpt/core/rptCm/queryList?id="+rptCmId+"&repage";
	}

}