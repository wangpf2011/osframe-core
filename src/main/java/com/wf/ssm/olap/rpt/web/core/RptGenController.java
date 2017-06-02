/*
 * Copyright &copy; 2011-2020 lnint Inc. All right reserved.
 * 
 * 修改信息：【与SVN提交信息一致】
 * A: 新增类 严娜 2015-11-27
 */
package com.wf.ssm.olap.rpt.web.core;

import java.io.File;
import java.io.FileInputStream;
import java.io.OutputStream;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.google.common.collect.Maps;
import com.wf.ssm.common.config.Global;
import com.wf.ssm.common.mapper.JsonMapper;
import com.wf.ssm.common.persistence.Page;
import com.wf.ssm.common.utils.StringUtils;
import com.wf.ssm.common.web.BaseController;
import com.wf.ssm.olap.rpt.entity.core.RptGen;
import com.wf.ssm.olap.rpt.service.core.RptGenService;

/**
 * 生成报表Controller
 *
 * @version 1.0
 * @author 严娜 2015-11-27
 * @since JDK 1.6
 */
@Controller
@RequestMapping(value = "${adminPath}/rpt/core/rptGen")
public class RptGenController extends BaseController {

	@Autowired
	private RptGenService rptGenService;
	/**
	 * <P>ModelAttribute 构造函数</P>
	 * @param id 实体主键;
	 * @return RptGen
	 */
	@ModelAttribute
	public RptGen get(@RequestParam(required=false) String id) {
		if (StringUtils.isNotBlank(id)){
			return rptGenService.get(id);
		}else{
			return new RptGen();
		}
	}
	/**
	 * <P>列表list查询函数</P>
	 * @param rptGen 实体模型;
	 * @param request  请求;
	 * @param response 返回;
	 * @param model    视图模型;
	 * @return view URL
	 */
	@RequiresPermissions("rpt:core:rptGen:view")
	@RequestMapping(value = {"list", ""})
	public String list(RptGen rptGen, HttpServletRequest request, HttpServletResponse response, Model model) {
		//设定列表每页记录数
		Page<RptGen> temp=new Page<RptGen>(request, response);
		//获得列表分页数据
		Page<RptGen> page = rptGenService.find(temp, rptGen); 
		model.addAttribute("page", page);
		//转向页面
		return "olap/rpt/core/rptGenList";
	}
    /**
	 * <P>表单form查询函数</P>
	 * @param rptGen 实体模型;
	 * @param model 视图模型;
	 * @return view URL
	 */
	@RequiresPermissions("rpt:core:rptGen:view")
	@RequestMapping(value = "form")
	public String form(RptGen rptGen, Model model) {
	    //由ModelAttribute默认构造rptGen实体模型
		model.addAttribute("rptGen", rptGen);
		//转向页面
		return "olap/rpt/core/rptGenForm";
	}
    /**
	 * <P>表单form保存函数</P>
	 * @param rptGen 实体模型;
	 * @param model 视图模型;
	 * @param redirectAttributes 重定向;
	 * @return view URL
	 */
	@RequiresPermissions("rpt:core:rptGen:edit")
	@RequestMapping(value = "save")
	public String save(RptGen rptGen, Model model, RedirectAttributes redirectAttributes) {
		//实体Validator
		if (!beanValidator(model, rptGen)){
			return form(rptGen, model);
		}
		//保存表单
		rptGenService.save(rptGen);
		//页面提示信息
		addMessage(redirectAttributes, "保存生成报表成功");
		//转向页面
		return "redirect:"+Global.getAdminPath()+"/olap/rpt/core/rptGen/?repage";
	}
	/**
	 * <P>列表list删除函数</P>
	 * @param id 实体主键;
	 * @param redirectAttributes 重定向;
	 * @return view URL
	 */	
	@RequiresPermissions("rpt:core:rptGen:edit")
	@RequestMapping(value = "delete")
	public String delete(String id, RedirectAttributes redirectAttributes) {
		//删除操作
		rptGenService.delete(id);
		//页面提示信息
		addMessage(redirectAttributes, "删除生成报表成功");
		//转向页面
		return "redirect:"+Global.getAdminPath()+"/olap/rpt/core/rptGen/?repage";
	}
	/**
	 * <P>判断文件是否存在</P>
	 * @param file  文件路径;
	 * @return String
	 */
	@RequestMapping(value = {"isExist"})
	@ResponseBody
	public String isExist(String path,HttpServletRequest request) {
		path=request.getSession().getServletContext().getRealPath("/")+path;
		File docFile = new File(path);
		Map<String, Object> map = Maps.newHashMap();
		map.put("result", "success");
	    if (!docFile.exists()) {
	    	map.put("result", "false");
	    }	
		return JsonMapper.nonDefaultMapper().toJson(map);
	}
	/**
	 * <P>pdf文件下载</P>
	 * @param file  文件路径;
	 * @return String
	 */
	@RequestMapping(value = {"downloadPdf"})
	@ResponseBody
	public void downloadPdf(String path,String filename,HttpServletRequest request,HttpServletResponse response) {
		response.setContentType("application/x-download");
		response.setHeader("Content-Disposition", "inline; filename=\""+filename+"\"");
		try {
			FileInputStream fileInput = new FileInputStream(request.getRealPath("/")+path);
			int i = fileInput.available();
			byte[] content = new byte[i];
			fileInput.read(content);
			OutputStream output = response.getOutputStream();
	
			output.write(content);
			output.flush();
			fileInput.close();
			output.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	  /**
		 * <P>报表预览</P>
		 * @param path 报表路径;
		 * @param model 视图模型;
		 * @return view URL
		 */
		@RequiresPermissions("rpt:core:rptGen:view")
		@RequestMapping(value = "viewRpt")
		public String viewRpt(String path, Model model) {
		    //由ModelAttribute默认构造rptGen实体模型
			model.addAttribute("path", path);
			//转向页面
			return "olap/rpt/core/rptGenView";
		}
}