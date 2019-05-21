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
import com.wf.ssm.core.sys.entity.Menu;
import com.wf.ssm.core.sys.service.SystemService;

/**
 * <P>菜单Controller</P>
 * 
 * @version 1.0
 * @author wangpf 2015-03-12 00:00:00
 * @since JDK 1.6
 */
@Controller
@RequestMapping(value = "${adminPath}/sys/menu")
public class MenuController extends BaseController {

	@Autowired
	private SystemService systemService;
	
	/**
	 * <p>根据id获取菜单实体对象<p>
	 * @param id 菜单实体表主键
	 * @return 菜单实体对象
	 */
	@ModelAttribute("menu")
	public Menu get(@RequestParam(required=false) String id) {
		if (StringUtils.isNotBlank(id)){
			return systemService.getMenu(id);
		}else{
			return new Menu();
		}
	}

	/**
	 * <p>获取视图名称"core/sys/menuList"<p>
	 * <p>处理list请求<p>
	 * @param model 模型
	 * @return 视图名称"core/sys/menuList"
	 */
	@RequiresPermissions("sys:menu:view")
	@RequestMapping(value = {"list", ""})
	public String list(Model model) {
		List<Menu> list = Lists.newArrayList();
		List<Menu> sourcelist = systemService.findAllMenu();
		Menu.sortList(list, sourcelist, "1");
        model.addAttribute("list", list);
		return "core/sys/menuList";
	}

	/**
	 * <p>获取视图名称"core/sys/menuForm"<p>
	 * <p>处理form请求<p>
	 * @param menu 菜单实体对象
	 * @param model 模型
	 * @return 视图名称"core/sys/menuForm"
	 */
	@RequiresPermissions("sys:menu:view")
	@RequestMapping(value = "form")
	public String form(Menu menu, Model model) {
		if (menu.getParent()==null||menu.getParent().getId()==null){
			menu.setParent(new Menu("1"));
		}
		menu.setParent(systemService.getMenu(menu.getParent().getId()));
		model.addAttribute("menu", menu);
		return "core/sys/menuForm";
	}
	
	/**
	 * <p>保存菜单<p>
	 * <p>处理save请求<p>
	 * @param menu 菜单实体对象
	 * @param model 模型
	 * @param redirectAttributes 重定向参数
	 * @return 视图名称"redirect:"+Global.getAdminPath()+"/sys/menu/"
	 */
	@RequiresPermissions("sys:menu:edit")
	@RequestMapping(value = "save")
	public String save(Menu menu, Model model, RedirectAttributes redirectAttributes) {
		if(Global.isDemoMode()){
			addMessage(redirectAttributes, "演示模式，不允许操作！");
			return "redirect:"+Global.getAdminPath()+"/sys/menu/";
		}
		if (!beanValidator(model, menu)){
			return form(menu, model);
		}
		systemService.saveMenu(menu);
		addMessage(redirectAttributes, "保存菜单'" + menu.getName() + "'成功");
		return "redirect:"+Global.getAdminPath()+"/sys/menu/";
	}
	
	/**
	 * <p>根据id,删除本实体数据和实体父ID下的子数据<p>
	 * <p>处理delete请求<p>
	 * @param id 菜单实体表主键
	 * @param redirectAttributes 重定向参数
	 * @return 视图名称 "redirect:"+Global.getAdminPath()+"/sys/menu/"
	 */
	@RequiresPermissions("sys:menu:edit")
	@RequestMapping(value = "delete")
	public String delete(String id, RedirectAttributes redirectAttributes) {
		if(Global.isDemoMode()){
			addMessage(redirectAttributes, "演示模式，不允许操作！");
			return "redirect:"+Global.getAdminPath()+"/sys/menu/";
		}
		if (Menu.isRoot(id)){
			addMessage(redirectAttributes, "删除菜单失败, 不允许删除顶级菜单或编号为空");
		}else{
			systemService.deleteMenu(id);
			addMessage(redirectAttributes, "删除菜单成功");
		}
		return "redirect:"+Global.getAdminPath()+"/sys/menu/";
	}

	@RequiresUser
	@RequestMapping(value = "tree")
	public String tree() {
		return "core/sys/menuTree";
	}
	
	/**
	 * <p>同步工作流权限数据<p>
	 * @param redirectAttributes 重定向参数
	 * @return 视图名称 "redirect:"+Global.getAdminPath()+"/sys/menu/"
	 */
	@RequiresPermissions("sys:menu:edit")
	@RequestMapping(value = "synToActiviti")
	public String synToActiviti(RedirectAttributes redirectAttributes) {
		if(Global.isDemoMode()){
			addMessage(redirectAttributes, "演示模式，不允许操作！");
			return "redirect:"+Global.getAdminPath()+"/sys/menu/";
		}
		systemService.synToActiviti();
    	addMessage(redirectAttributes, "同步工作流权限数据成功!");
		return "redirect:"+Global.getAdminPath()+"/sys/menu/";
	}
	
	
	/**
	 * <p>批量修改菜单排序<p>
	 */
	@RequiresPermissions("sys:menu:edit")
	@RequestMapping(value = "updateSort")
	public String updateSort(String[] ids, Integer[] sorts, RedirectAttributes redirectAttributes) {
		if(Global.isDemoMode()){
			addMessage(redirectAttributes, "演示模式，不允许操作！");
			return "redirect:"+Global.getAdminPath()+"/sys/menu/";
		}
    	int len = ids.length;
    	Menu[] menus = new Menu[len];
    	for (int i = 0; i < len; i++) {
    		menus[i] = systemService.getMenu(ids[i]);
    		menus[i].setSort(sorts[i]);
    		systemService.saveMenuSort(menus[i]);
    	}
    	addMessage(redirectAttributes, "保存菜单排序成功!");
		return "redirect:"+Global.getAdminPath()+"/sys/menu/";
	}
	
	/**
	 * <p>获取菜单树List<p>
	 * @param extId 排除的编号ID
	 * @param response 响应json
	 * @return 菜单树对象List
	 */
	@RequiresUser
	@ResponseBody
	@RequestMapping(value = "treeData")
	public List<Map<String, Object>> treeData(@RequestParam(required=false) String extId, HttpServletResponse response) {
		response.setContentType("application/json; charset=UTF-8");
		List<Map<String, Object>> mapList = Lists.newArrayList();
		List<Menu> list = systemService.findAllMenu();
		for (int i=0; i<list.size(); i++){
			Menu e = list.get(i);
			if (extId == null || (extId!=null && !extId.equals(e.getId()) && e.getParentIds().indexOf(","+extId+",")==-1)){
				Map<String, Object> map = Maps.newHashMap();
				map.put("id", e.getId());
				map.put("pId", e.getParent()!=null?e.getParent().getId():0);
				map.put("name", e.getName());
				mapList.add(map);
			}
		}
		return mapList;
	}
}
