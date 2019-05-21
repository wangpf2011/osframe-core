package com.wf.ssm.core.sys.web;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
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
import com.wf.ssm.core.sys.entity.Dict;
import com.wf.ssm.core.sys.service.DictService;

/**
 * <P>字典Controller</P>
 * 
 * @version 1.0
 * @author wangpf 2015-03-12 00:00:00
 * @since JDK 1.6
 */
@Controller
@RequestMapping(value = "${adminPath}/sys/dict")
public class DictController extends BaseController {

	@Autowired
	private DictService dictService;
	
	/**
	 * <p>根据id获取字典实体对象<p>
	 * @param id 字典实体表主键
	 * @return 字典实体对象
	 */
	@ModelAttribute
	public Dict get(@RequestParam(required=false) String id) {
		if (StringUtils.isNotBlank(id)){
			return dictService.get(id);
		}else{
			return new Dict();
		}
	}
	
	/**
	 * <p>获取视图名称"core/sys/dictList"<p>
	 * <p>处理list请求<p>
	 * @param dict 字典实体对象
	 * @param model 模型
	 * @return 视图名称"core/sys/dictList"
	 */
	@RequiresPermissions("sys:dict:view")
	@RequestMapping(value = {"list", ""})
	public String list(Dict dict, HttpServletRequest request, HttpServletResponse response, Model model) {
		List<String> typeList = dictService.findTypeList();
		model.addAttribute("typeList", typeList);
//        Page<Dict> page = dictService.find(new Page<Dict>(request, response), dict); 
//        model.addAttribute("page", page);
        Page<Dict> page = dictService.findPage(new Page<Dict>(request, response), dict); 
        model.addAttribute("page", page);
        
		return "core/sys/dictList";
	}

	/**
	 * <p>获取视图名称"core/sys/dictForm"<p>
	 * <p>处理form请求<p>
	 * @param dict 字典实体对象
	 * @param model 模型
	 * @return 视图名称"core/sys/dictForm"
	 */
	@RequiresPermissions("sys:dict:view")
	@RequestMapping(value = "form")
	public String form(Dict dict, Model model) {
		model.addAttribute("dict", dict);
		return "core/sys/dictForm";
	}

	/**
	 * <p>保存字典<p>
	 * <p>处理save请求<p>
	 * @param dict 区域实体对象
	 * @param request 请求
	 * @param model 模型
	 * @param redirectAttributes 重定向参数
	 * @return 视图名称 "redirect:"+Global.getAdminPath()+"/sys/dict/?repage&type="+dict.getType()
	 */
	@RequiresPermissions("sys:dict:edit")
	@RequestMapping(value = "save")//@Valid 
	public String save(Dict dict, HttpServletRequest request, Model model, RedirectAttributes redirectAttributes) {
		if(Global.isDemoMode()){
			addMessage(redirectAttributes, "演示模式，不允许操作！");
			return "redirect:"+Global.getAdminPath()+"/sys/dict/?repage&type="+dict.getType();
		}
		if (!beanValidator(model, dict)){
			return form(dict, model);
		}
		dictService.save(dict);
		addMessage(redirectAttributes, "保存字典'" + dict.getLabel() + "'成功");
		return "redirect:"+Global.getAdminPath()+"/sys/dict/?repage&type="+dict.getType();
	}
	
	/**
	 * <p>根据id,删除本实体数据和实体父ID下的子数据<p>
	 * <p>处理delete请求<p>
	 * @param id 字典实体表主键
	 * @param redirectAttributes 重定向参数
	 * @return 视图名称 "redirect:"+Global.getAdminPath()+"/sys/dict/?repage"
	 */
	@RequiresPermissions("sys:dict:edit")
	@RequestMapping(value = "delete")
	public String delete(Dict dict, RedirectAttributes redirectAttributes) {
		if(Global.isDemoMode()){
			addMessage(redirectAttributes, "演示模式，不允许操作！");
			return "redirect:"+Global.getAdminPath()+"/sys/dict/?repage";
		}
//		dictService.delete(id);
		dictService.delete(dict);
		addMessage(redirectAttributes, "删除字典成功");
		return "redirect:"+Global.getAdminPath()+"/sys/dict/?repage";
	}

}
