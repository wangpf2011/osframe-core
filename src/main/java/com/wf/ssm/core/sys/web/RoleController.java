/*
 * Copyright &copy; 2011-2020 lnint Inc. All right reserved.
 * 
 * 修改信息：【与SVN提交信息一致】
 * A: 新增类 位苏 2015-3-11 12:02:25
 */
package com.wf.ssm.core.sys.web;

import java.util.List;
import java.util.Map;

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
import com.wf.ssm.core.sys.dao.UserDao;
import com.wf.ssm.core.sys.entity.Office;
import com.wf.ssm.core.sys.entity.Role;
import com.wf.ssm.core.sys.entity.User;
import com.wf.ssm.core.sys.service.OfficeService;
import com.wf.ssm.core.sys.service.SystemService;
import com.wf.ssm.core.sys.utils.UserUtils;

/**
 * 角色Controller
 * @version 1.0
 * @author weisu 2015-3-10
 * @since JDK 1.6
 */
@Controller
@RequestMapping(value = "${adminPath}/sys/role")
public class RoleController extends BaseController {

	@Autowired
	private SystemService systemService;

	@Autowired
	private OfficeService officeService;
	@Autowired
	private UserDao userDao;
	
	/**
	 * 根据角色id，获得角色信息
	 */
	@ModelAttribute("role")
	public Role get(@RequestParam(required = false) String id) {
		if (StringUtils.isNotBlank(id)) {
//			return systemService.getRole(id);
			return systemService.getPersistent(id);
		} else {
			return new Role();
		}
	}

	/**
	 * 获取当前用户所有角色
	 */
	@RequiresPermissions("sys:role:view")
	@RequestMapping(value = { "list", "" })
	public String list(Role role, Model model) {
		List<Role> list = systemService.findAllRole();
		model.addAttribute("list", list);
		return "core/sys/roleList";
	}

	/**
	 * 获取角色表单数据
	 */
	@RequiresPermissions("sys:role:view")
	@RequestMapping(value = "form")
	public String form(Role role, Model model) {
		if (role.getOffice() == null) {
			role.setOffice(UserUtils.getUser().getOffice());
		}
		model.addAttribute("role", role);
		model.addAttribute("menuList", systemService.findAllMenu());
		model.addAttribute("officeList", officeService.findAll());
		return "core/sys/roleForm";
	}

	/**
	 * 保存角色
	 */
	@RequiresPermissions("sys:role:edit")
	@RequestMapping(value = "save")
	public String save(Role role, Model model, String oldName, RedirectAttributes redirectAttributes) {
		if (Global.isDemoMode()) {
			addMessage(redirectAttributes, "演示模式，不允许操作！");
			return "redirect:" + Global.getAdminPath() + "/sys/role/?repage";
		}
		if (!beanValidator(model, role)) {
			return form(role, model);
		}
		if (!"true".equals(checkName(oldName, role.getName()))) {
			addMessage(model, "保存角色'" + role.getName() + "'失败, 角色名已存在");
			return form(role, model);
		}
		systemService.saveRole(role);
		addMessage(redirectAttributes, "保存角色'" + role.getName() + "'成功");
		return "redirect:" + Global.getAdminPath() + "/sys/role/?repage";
	}

	/**
	 * 删除角色
	 */
	@RequiresPermissions("sys:role:edit")
	@RequestMapping(value = "delete")
	public String delete(@RequestParam String id, RedirectAttributes redirectAttributes) {
		if (Global.isDemoMode()) {
			addMessage(redirectAttributes, "演示模式，不允许操作！");
			return "redirect:" + Global.getAdminPath() + "/sys/role/?repage";
		}
		if (Role.isAdmin(id)) {
			addMessage(redirectAttributes, "删除角色失败, 不允许内置角色或编号空");
			// }else if (UserUtils.getUser().getRoleIdList().contains(id)){
			// addMessage(redirectAttributes, "删除角色失败, 不能删除当前用户所在角色");
		} else {
			systemService.deleteRole(id);
			addMessage(redirectAttributes, "删除角色成功");
		}
		return "redirect:" + Global.getAdminPath() + "/sys/role/?repage";
	}

	/**
	 * 获取拥有该角色的所有用户，返回到角色分配页
	 */
	@RequiresPermissions("sys:role:edit")
	@RequestMapping(value = "assign")
	public String assign(Role role, Model model) {
//		List<User> users = role.getUserList();
		//获得角色下所有的用户列表，实体关联效率提升
		List<User> users =userDao.findUserListByRole(role.getId());
		
		model.addAttribute("users", users);
		return "core/sys/roleAssign";
	}

	@RequiresPermissions("sys:role:view")
	@RequestMapping(value = "usertorole")
	public String selectUserToRole(Role role, Model model) {
		model.addAttribute("role", role);
		//
		//获得角色下所有的用户列表，实体关联效率提升
		List<User> userList =userDao.findUserListByRole(role.getId());
		List<String> nameIdList = Lists.newArrayList();
		for (User user : userList) {
			nameIdList.add(user.getId());
		}
		StringUtils.join(nameIdList, ",");
		model.addAttribute("selectIds", nameIdList);
		model.addAttribute("roleuserList", userList);
		
		model.addAttribute("officeList", officeService.findAll());
		return "core/sys/selectUserToRole";
	}

	/**
	 * 根据机构id，获取该机构的用户
	 */
	@RequiresPermissions("sys:role:view")
	@ResponseBody
	@RequestMapping(value = "users")
	public List<Map<String, Object>> users(String officeId, HttpServletResponse response) {
		response.setContentType("application/json; charset=UTF-8");
		List<Map<String, Object>> mapList = Lists.newArrayList();
		Office office = officeService.getPersistent(officeId);
		List<User> userList = office.getUserList();
		for (User user : userList) {
			Map<String, Object> map = Maps.newHashMap();
			map.put("id", user.getId());
			map.put("pId", 0);
			map.put("name", user.getName());
			mapList.add(map);
		}
		return mapList;
	}

	/**
	 * 用户从角色中移除
	 */
	@RequiresPermissions("sys:role:edit")
	@RequestMapping(value = "outrole")
	public String outrole(String userId, String roleId, RedirectAttributes redirectAttributes) {
		if (Global.isDemoMode()) {
			addMessage(redirectAttributes, "演示模式，不允许操作！");
			return "redirect:" + Global.getAdminPath() + "/sys/role/assign?id=" + roleId;
		}
		Role role = systemService.getRole(roleId);
		User user = systemService.getUser(userId);
		if (user.equals(UserUtils.getUser())) {
			addMessage(redirectAttributes, "无法从角色【" + role.getName() + "】中移除用户【" + user.getName() + "】自己！");
		} else {
			Boolean flag = systemService.outUserInRole(role, userId);
			if (!flag) {
				addMessage(redirectAttributes, "用户【" + user.getName() + "】从角色【" + role.getName() + "】中移除失败！");
			} else {
				addMessage(redirectAttributes, "用户【" + user.getName() + "】从角色【" + role.getName() + "】中移除成功！");
			}
		}
		return "redirect:" + Global.getAdminPath() + "/sys/role/assign?id=" + role.getId();
	}

	/**
	 * 新增用户到角色中
	 */
	@RequiresPermissions("sys:role:edit")
	@RequestMapping(value = "assignrole")
	public String assignRole(Role role, String[] idsArr, RedirectAttributes redirectAttributes) {
		if (Global.isDemoMode()) {
			addMessage(redirectAttributes, "演示模式，不允许操作！");
			return "redirect:" + Global.getAdminPath() + "/sys/role/assign?id=" + role.getId();
		}
		StringBuilder msg = new StringBuilder();
		int newNum = 0;
		for (int i = 0; i < idsArr.length; i++) {
			User user = systemService.assignUserToRole(role, idsArr[i]);
			if (null != user) {
				msg.append("<br/>新增用户【" + user.getName() + "】到角色【" + role.getName() + "】！");
				newNum++;
			}
		}
		addMessage(redirectAttributes, "已成功分配 " + newNum + " 个用户" + msg);
		return "redirect:" + Global.getAdminPath() + "/sys/role/assign?id=" + role.getId();
	}

	@RequiresUser
	@ResponseBody
	@RequestMapping(value = "checkName")
	public String checkName(String oldName, String name) {
		if (name != null && name.equals(oldName)) {
			return "true";
		} else if (name != null && systemService.findRoleByName(name) == null) {
			return "true";
		}
		return "false";
	}

}
