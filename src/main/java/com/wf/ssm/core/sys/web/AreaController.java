/**
 * Copyright &copy; 2011-2020 lnint Inc. All rights reserved.
 * 
 * 修改信息：v1.0 doc做成
 * A: 新增类  葛松  2015-03-11 15:20:21
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
import com.wf.ssm.core.sys.entity.Area;
import com.wf.ssm.core.sys.service.AreaService;
import com.wf.ssm.core.sys.utils.UserUtils;

/**
 * <P>区域Controller</P>
 * 
 * @version 1.0
 * @author wangpf 2015-03-12 00:00:00
 * @since JDK 1.6
 */
@Controller
@RequestMapping(value = "${adminPath}/sys/area")
public class AreaController extends BaseController {

	@Autowired
	private AreaService areaService;
	
	/**
	 * <p>根据id获取区域实体对象<p>
	 * @param id 区域实体表主键
	 * @return 区域实体对象
	 */
	@ModelAttribute("area")
	public Area get(@RequestParam(required=false) String id) {
		if (StringUtils.isNotBlank(id)){
			return areaService.get(id);
		}else{
			return new Area();
		}
	}

	/**
	 * <p>获取视图名称"core/sys/areaList"<p>
	 * <p>处理list请求<p>
	 * @param area 区域实体对象
	 * @param model 模型
	 * @return 视图名称"core/sys/areaList"
	 */
	@RequiresPermissions("sys:area:view")
	@RequestMapping(value = {"list", ""})
	public String list(Area area, Model model) {
//		User user = UserUtils.getUser();
//		if(user.isAdmin()){
			area.setId("1");
//		}else{
//			area.setId(user.getArea().getId());
//		}
		model.addAttribute("area", area);
		List<Area> list = Lists.newArrayList();
		List<Area> sourcelist = areaService.findAll();
		Area.sortList(list, sourcelist, area.getId());
        model.addAttribute("list", list);
		return "core/sys/areaList";
	}

	/**
	 * <p>获取视图名称"core/sys/areaForm"<p>
	 * <p>处理form请求<p>
	 * @param area 区域实体对象
	 * @param model 模型
	 * @return 视图名称"core/sys/areaForm"
	 */
	@RequiresPermissions("sys:area:view")
	@RequestMapping(value = "form")
	public String form(Area area, Model model) {
		if (area.getParent()==null||area.getParent().getId()==null){
			area.setParent(UserUtils.getUser().getOffice().getArea());
		}
		area.setParent(areaService.get(area.getParent().getId()));
		model.addAttribute("area", area);
		return "core/sys/areaForm";
	}
	
	/**
	 * <p>保存区域<p>
	 * <p>处理save请求<p>
	 * @param area 区域实体对象
	 * @param model 模型
	 * @param redirectAttributes 重定向参数
	 * @return 视图名称"redirect:"+Global.getAdminPath()+"/sys/area/"
	 */
	@RequiresPermissions("sys:area:edit")
	@RequestMapping(value = "save")
	public String save(Area area, Model model, RedirectAttributes redirectAttributes) {
		if(Global.isDemoMode()){
			addMessage(redirectAttributes, "演示模式，不允许操作！");
			return "redirect:"+Global.getAdminPath()+"/sys/area";
		}
		if (!beanValidator(model, area)){
			return form(area, model);
		}
		areaService.save(area);
		addMessage(redirectAttributes, "保存区域'" + area.getName() + "'成功");
		return "redirect:"+Global.getAdminPath()+"/sys/area/";
	}
	
	/**
	 * <p>根据id,删除本实体数据和实体父ID下的子数据<p>
	 * <p>处理delete请求<p>
	 * @param id 区域实体表主键
	 * @param redirectAttributes 重定向参数
	 * @return 视图名称 "redirect:"+Global.getAdminPath()+"/sys/area/"
	 */
	@RequiresPermissions("sys:area:edit")
	@RequestMapping(value = "delete")
	public String delete(String id, RedirectAttributes redirectAttributes) {
		if(Global.isDemoMode()){
			addMessage(redirectAttributes, "演示模式，不允许操作！");
			return "redirect:"+Global.getAdminPath()+"/sys/area";
		}
		if (Area.isAdmin(id)){
			addMessage(redirectAttributes, "删除区域失败, 不允许删除顶级区域或编号为空");
		}else{
			areaService.delete(id);
			addMessage(redirectAttributes, "删除区域成功");
		}
		return "redirect:"+Global.getAdminPath()+"/sys/area/";
	}

	/**
	 * <p>获取区域树List<p>
	 * @param extId 排除的编号ID
	 * @param response 响应json
	 * @return 区域树对象List
	 */
	@RequiresUser
	@ResponseBody
	@RequestMapping(value = "treeData")
	public List<Map<String, Object>> treeData(@RequestParam(required=false) String extId, HttpServletResponse response) {
		response.setContentType("application/json; charset=UTF-8");
		List<Map<String, Object>> mapList = Lists.newArrayList();
//		User user = UserUtils.getUser();
		List<Area> list = areaService.findAll();
		for (int i=0; i<list.size(); i++){
			Area e = list.get(i);
			if (extId == null || (extId!=null && !extId.equals(e.getId()) && e.getParentIds().indexOf(","+extId+",")==-1)){
				Map<String, Object> map = Maps.newHashMap();
				map.put("id", e.getId());
//				map.put("pId", !user.isAdmin()&&e.getId().equals(user.getArea().getId())?0:e.getParent()!=null?e.getParent().getId():0);
				map.put("pId", e.getParent()!=null?e.getParent().getId():0);
				map.put("name", e.getName());
				mapList.add(map);
			}
		}
		return mapList;
	}
	
	@RequiresUser
	@ResponseBody
	@RequestMapping(value = "treeData2")
	public List<Map<String, Object>> treeData2(@RequestParam(required=false) String pid, HttpServletResponse response) {
		response.setContentType("application/json; charset=UTF-8");
		List<Map<String, Object>> mapList = Lists.newArrayList();
//		User user = UserUtils.getUser();
		List<Area> list = areaService.findAll();
		for (int i=0; i<list.size(); i++){
			Area e = list.get(i);
			if(pid == null || pid.equals(e.getId()) || e.getParentIds().indexOf(pid+",")!=-1) {
				Map<String, Object> map = Maps.newHashMap();
				map.put("id", e.getId());
//				map.put("pId", !user.isAdmin()&&e.getId().equals(user.getArea().getId())?0:e.getParent()!=null?e.getParent().getId():0);
				map.put("pId", e.getParent()!=null?e.getParent().getId():0);
				map.put("name", e.getName());
				mapList.add(map);
			}
		}
		return mapList;
	}
	@RequiresUser
	@ResponseBody
	@RequestMapping(value = "treeData3")
	public List<Map<String, Object>> treeData3(@RequestParam(required=false) String pid, HttpServletResponse response) {
		response.setContentType("application/json; charset=UTF-8");
		List<Map<String, Object>> mapList = Lists.newArrayList();
//		User user = UserUtils.getUser();
		List<Area> list = areaService.findDown();
		for (int i=0; i<list.size(); i++){
			Area e = list.get(i);
			if(pid == null || pid.equals(e.getId()) || e.getParentIds().indexOf(pid+",")!=-1) {
				Map<String, Object> map = Maps.newHashMap();
				map.put("id", e.getId());
//				map.put("pId", !user.isAdmin()&&e.getId().equals(user.getArea().getId())?0:e.getParent()!=null?e.getParent().getId():0);
				map.put("pId", e.getParent()!=null?e.getParent().getId():0);
				map.put("name", e.getName());
				mapList.add(map);
			}
		}
		return mapList;
	}
	
}
