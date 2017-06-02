/*
 * Copyright &copy; 2011-2020 lnint Inc. All right reserved.
 * 
 * 修改信息：【与SVN提交信息一致】
 * A: 新增类 位苏 2015-3-11 12:02:25
 */
package com.wf.ssm.core.sys.web;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.authz.annotation.RequiresUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.wf.ssm.common.config.Global;
import com.wf.ssm.common.utils.StringUtils;
import com.wf.ssm.common.web.BaseController;
import com.wf.ssm.core.sys.entity.Office;
import com.wf.ssm.core.sys.entity.User;
import com.wf.ssm.core.sys.service.OfficeService;
import com.wf.ssm.core.sys.utils.UserUtils;

/**
 * 机构Controller
 * @version 1.0
 * @author weisu 2015-3-10
 * @since JDK 1.6
 */
@Controller
@RequestMapping(value = "${adminPath}/sys/office")
public class OfficeController extends BaseController {

	@Autowired
	private OfficeService officeService;

	/**
	 * 根据机构id，获取机构实体
	 */
	@ModelAttribute("office")
	public Office get(@RequestParam(required = false) String id) {
		if (StringUtils.isNotBlank(id)) {
//			return officeService.get(id);
			return officeService.getPersistent(id);
		} else {
			return new Office();
		}
	}

	/**
	 * 查询机构列表
	 */
	@RequiresPermissions("sys:office:view")
	@RequestMapping(value = { "list", "" })
	public String list(Office office, Model model) {
		// User user = UserUtils.getUser();
		// if(user.isAdmin()){
		office.setId("1");
		// }else{
		// office.setId(user.getOffice().getId());
		// }
		model.addAttribute("office", office);
		List<Office> list = Lists.newArrayList();
		List<Office> sourcelist = officeService.findAll();
		Office.sortList(list, sourcelist, office.getId());
		model.addAttribute("list", list);
		return "core/sys/officeList";
	}

	/**
	 * 获取机构表单数据
	 */
	@RequiresPermissions("sys:office:view")
	@RequestMapping(value = "form")
	public String form(Office office, Model model) {
		User user = UserUtils.getUser();
		if (office.getParent() == null || office.getParent().getId() == null) {
			office.setParent(user.getOffice());
		}
		office.setParent(officeService.get(office.getParent().getId()));
		if (office.getArea() == null) {
			office.setArea(office.getParent().getArea());
		}
		model.addAttribute("office", office);
		return "core/sys/officeForm";
	}

	/**
	 * 保存机构
	 */
	@RequiresPermissions("sys:office:edit")
	@RequestMapping(value = "save")
	public String save(Office office, Model model, RedirectAttributes redirectAttributes) {
		if (Global.isDemoMode()) {
			addMessage(redirectAttributes, "演示模式，不允许操作！");
			return "redirect:" + Global.getAdminPath() + "/sys/office/list";
		}
//		if (!beanValidator(model, office)) {
//			return form(office, model);
//		}
		try {
		    officeService.save(office);
        } catch (Exception e) {
            e.printStackTrace();
        }
		addMessage(redirectAttributes, "保存机构'" + office.getName() + "'成功");
		return "redirect:" + Global.getAdminPath() + "/sys/office/list";
	}

	/**
	 * 删除机构
	 */
	@RequiresPermissions("sys:office:edit")
	@RequestMapping(value = "delete")
	public String delete(String id, RedirectAttributes redirectAttributes) {
		if (Global.isDemoMode()) {
			addMessage(redirectAttributes, "演示模式，不允许操作！");
			return "redirect:" + Global.getAdminPath() + "/sys/office/";
		}
		if (Office.isRoot(id)) {
			addMessage(redirectAttributes, "删除机构失败, 不允许删除顶级机构或编号空");
		} else {
			officeService.delete(id);
			addMessage(redirectAttributes, "删除机构成功");
		}
		return "redirect:" + Global.getAdminPath() + "/sys/office/";
	}

	/**
	 * 查询机构，按条件过滤，并以map列表的形式返回
	 */
	@RequiresUser
	@ResponseBody
	@RequestMapping(value = "treeData")
	public List<Map<String, Object>> treeData(@RequestParam(required = false) String extId, @RequestParam(required = false) Long type, @RequestParam(required = false) Long grade,
			HttpServletRequest request, HttpServletResponse response) {
		response.setContentType("application/json; charset=UTF-8");
		List<Map<String, Object>> mapList = Lists.newArrayList();
		// User user = UserUtils.getUser();
		List<Office> list = officeService.findChildOfficeListByCorp();
		for (int i = 0; i < list.size(); i++) {
			Office e = list.get(i);
			if ((extId == null || (extId != null && !extId.equals(e.getId()) && e.getParentIds().indexOf("," + extId + ",") == -1))
					&& (type == null || (type != null && Integer.parseInt(e.getType()) <= type.intValue())) && (grade == null || (grade != null && Integer.parseInt(e.getGrade()) <= grade.intValue()))) {
				Map<String, Object> map = Maps.newHashMap();
				map.put("id", e.getId());
				// map.put("pId", !user.isAdmin() &&
				// e.getId().equals(user.getOffice().getId())?0:e.getParent()!=null?e.getParent().getId():0);
				map.put("pId", e.getParent() != null ? e.getParent().getId() : 0);
				map.put("name", e.getName());
				if("4".equals(e.getType())) {
				    map.put("icon", request.getContextPath()+"/static/jquery-ztree/3.5.12/css/zTreeStyle/img/diy/person.png");
				}else {
				    map.put("icon", request.getContextPath()+"/static/jquery-ztree/3.5.12/css/zTreeStyle/img/diy/1_open.png");
				    map.put("iconOpen", request.getContextPath()+"/static/jquery-ztree/3.5.12/css/zTreeStyle/img/diy/1_open.png");
				    map.put("iconClose", request.getContextPath()+"/static/jquery-ztree/3.5.12/css/zTreeStyle/img/diy/1_close.png");
				}
				map.put("name", e.getName());
				mapList.add(map);
			}
		}
		return mapList;
	}

	/**
	 * 查询所有机构，并以map列表的形式返回
	 */
	@RequiresUser
	@ResponseBody
	@RequestMapping(value = "treeDataAll")
	public List<Map<String, Object>> treeDataAll(@RequestParam(required = false) String extId, @RequestParam(required = false) Long type, @RequestParam(required = false) Long grade,
			HttpServletResponse response) {
		response.setContentType("application/json; charset=UTF-8");
		List<Map<String, Object>> mapList = Lists.newArrayList();
		// User user = UserUtils.getUser();
		List<Office> list = officeService.findAllOffice();
		for (int i = 0; i < list.size(); i++) {
			Office e = list.get(i);
			if ((extId == null || (extId != null && !extId.equals(e.getId()) && e.getParentIds().indexOf("," + extId + ",") == -1))
					&& (type == null || (type != null && Integer.parseInt(e.getType()) <= type.intValue())) && (grade == null || (grade != null && Integer.parseInt(e.getGrade()) <= grade.intValue()))) {
				Map<String, Object> map = Maps.newHashMap();
				map.put("id", e.getId());
				// map.put("pId", !user.isAdmin() &&
				// e.getId().equals(user.getOffice().getId())?0:e.getParent()!=null?e.getParent().getId():0);
				map.put("pId", e.getParent() != null ? e.getParent().getId() : 0);
				map.put("name", e.getName());
				mapList.add(map);
			}
		}
		return mapList;
	}
}
