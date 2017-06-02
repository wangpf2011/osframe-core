/*
 * Copyright &copy; 2011-2020 lnint Inc. All right reserved.
 * 
 * 修改信息：【与SVN提交信息一致】
 * A: 新增类 位苏 2015-3-11 12:02:25
 */
package com.wf.ssm.core.sys.web;

import javax.servlet.http.HttpServletRequest;

import org.apache.shiro.authz.annotation.RequiresUser;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.wf.ssm.common.web.BaseController;

/**
 * <P>标签Controller</P>
 * <P>请求路径以"${adminPath}/tag"开头</P>
 * @version 1.0
 * @author weisu 2015-3-10
 * @since JDK 1.6
 */
@Controller
@RequestMapping(value = "${adminPath}/tag")
public class TagController extends BaseController {

	/**
	 * <P>树结构选择标签（treeselect.tag）</P>
	 */
	@RequiresUser
	@RequestMapping(value = "treeselect")
	public String treeselect(HttpServletRequest request, Model model) {
		model.addAttribute("url", request.getParameter("url")); // 树结构数据URL
		model.addAttribute("extId", request.getParameter("extId")); // 排除的编号ID
		model.addAttribute("checked", request.getParameter("checked")); // 是否可复选
		model.addAttribute("selectIds", request.getParameter("selectIds")); // 指定默认选中的ID
		model.addAttribute("module", request.getParameter("module")); // 过滤栏目模型（仅针对CMS的Category树）
		return "core/sys/tagTreeselect";
	}
	
	/**
     * <P>树结构选择标签（treeselect.tag）</P>
     */
    @RequiresUser
    @RequestMapping(value = "treeselect1")
    public String treeselect1(HttpServletRequest request, Model model) {
        model.addAttribute("url", request.getParameter("url")); // 树结构数据URL
        model.addAttribute("extId", request.getParameter("extId")); // 排除的编号ID
        model.addAttribute("checked", request.getParameter("checked")); // 是否可复选
        model.addAttribute("selectIds", request.getParameter("selectIds")); // 指定默认选中的ID
        model.addAttribute("module", request.getParameter("module")); // 过滤栏目模型（仅针对CMS的Category树）
        return "core/sys/tagTreeselect1";
    }

	/**
	 * <P>图标选择标签（iconselect.tag）</P>
	 */
	@RequiresUser
	@RequestMapping(value = "iconselect")
	public String iconselect(HttpServletRequest request, Model model) {
		model.addAttribute("value", request.getParameter("value"));
		return "core/sys/tagIconselect";
	}

}
