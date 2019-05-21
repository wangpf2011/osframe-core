package com.wf.ssm.core.sys.web;

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
import com.wf.ssm.common.web.BaseController;
import com.wf.ssm.common.utils.StringUtils;
import com.wf.ssm.core.sys.entity.SysSequences;
import com.wf.ssm.core.sys.service.SysSequencesService;

/**
 * 序列管理Controller
 *
 * @version 1.0
 * @author 王磊 2015-06-05
 * @since JDK 1.6
 */
@Controller
@RequestMapping(value = "${adminPath}/sys/sysSequences")
public class SysSequencesController extends BaseController {

	@Autowired
	private SysSequencesService sysSequencesService;
	/**
	 * <P>ModelAttribute 构造函数</P>
	 * @param id 实体主键;
	 * @return SysSequences
	 */
	@ModelAttribute
	public SysSequences get(@RequestParam(required=false) String id) {
		if (StringUtils.isNotBlank(id)){
			return sysSequencesService.get(id);
		}else{
			return new SysSequences();
		}
	}
	/**
	 * <P>列表list查询函数</P>
	 * @param sysSequences 实体模型;
	 * @param request  请求;
	 * @param response 返回;
	 * @param model    视图模型;
	 * @return view URL
	 */
	@RequiresPermissions("sys:sysSequences:view")
	@RequestMapping(value = {"list", ""})
	public String list(SysSequences sysSequences, HttpServletRequest request, HttpServletResponse response, Model model) {
		//设定列表每页记录数
		Page<SysSequences> temp=new Page<SysSequences>(request, response);
		temp.setPageSize(10);
		//获得列表分页数据
		Page<SysSequences> page = sysSequencesService.find(temp, sysSequences); 
		model.addAttribute("page", page);
		//转向页面
		return "core/sys/sysSequencesList";
	}
    /**
	 * <P>表单form查询函数</P>
	 * @param sysSequences 实体模型;
	 * @param model 视图模型;
	 * @return view URL
	 */
	@RequiresPermissions("sys:sysSequences:view")
	@RequestMapping(value = "form")
	public String form(SysSequences sysSequences, Model model) {
	    //由ModelAttribute默认构造sysSequences实体模型
		model.addAttribute("sysSequences", sysSequences);
		//转向页面
		return "core/sys/sysSequencesForm";
	}
    /**
	 * <P>表单form保存函数</P>
	 * @param sysSequences 实体模型;
	 * @param model 视图模型;
	 * @param redirectAttributes 重定向;
	 * @return view URL
	 */
	@RequiresPermissions("sys:sysSequences:edit")
	@RequestMapping(value = "save")
	public String save(SysSequences sysSequences, Model model, RedirectAttributes redirectAttributes) {
		//实体Validator
		if (!beanValidator(model, sysSequences)){
			return form(sysSequences, model);
		}
		//新增 进行重复鉴权
		if(StringUtils.isEmpty(sysSequences.getId())){//
			if(sysSequencesService.findSeqIsExist(sysSequences.getSeqName())){
				model.addAttribute("message", "失败，此序列已经存在，请更换序列名称！");
				return "core/sys/sysSequencesForm";
			}
		}
		//保存表单
		sysSequencesService.save(sysSequences);
		//页面提示信息
		addMessage(redirectAttributes, "保存序列管理成功");
		//转向页面
		return "redirect:"+Global.getAdminPath()+"/sys/sysSequences/?repage";
	}
	/**
	 * <P>列表list删除函数</P>
	 * @param id 实体主键;
	 * @param redirectAttributes 重定向;
	 * @return view URL
	 */	
	@RequiresPermissions("sys:sysSequences:edit")
	@RequestMapping(value = "delete")
	public String delete(String id, RedirectAttributes redirectAttributes) {
		//删除操作
		sysSequencesService.delete(id);
		//页面提示信息
		addMessage(redirectAttributes, "删除序列管理成功");
		//转向页面
		return "redirect:"+Global.getAdminPath()+"/sys/sysSequences/?repage";
	}

}