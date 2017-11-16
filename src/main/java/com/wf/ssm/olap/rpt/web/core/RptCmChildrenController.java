/*
 * Copyright &copy; 2011-2020 lnint Inc. All right reserved.
 * 
 * 修改信息：【与SVN提交信息一致】
 * A: 新增类 严娜 2015-11-25
 */
package com.wf.ssm.olap.rpt.web.core;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FilenameUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.wf.ssm.common.config.Global;
import com.wf.ssm.common.persistence.Page;
import com.wf.ssm.common.utils.IdGen;
import com.wf.ssm.common.utils.StringUtils;
import com.wf.ssm.common.web.BaseController;
import com.wf.ssm.olap.rpt.entity.core.RptCmChildren;
import com.wf.ssm.olap.rpt.service.core.RptCmChildrenService;
import com.wf.ssm.olap.rpt.service.core.RptCmService;

/**
 * 子报表模板Controller
 *
 * @version 1.0
 * @author wangpf 2015-11-25
 * @since JDK 1.6
 */
@Controller
@RequestMapping(value = "${adminPath}/rpt/core/rptCmChildren")
public class RptCmChildrenController extends BaseController {

	@Autowired
	private RptCmChildrenService rptCmChildrenService;
	@Autowired
	private RptCmService rptCmService;
	/**
	 * <P>ModelAttribute 构造函数</P>
	 * @param id 实体主键;
	 * @return RptCmChildren
	 */
	@ModelAttribute
	public RptCmChildren get(@RequestParam(required=false) String id) {
		if (StringUtils.isNotBlank(id)){
			return rptCmChildrenService.get(id);
		}else{
			return new RptCmChildren();
		}
	}
	/**
	 * <P>列表list查询函数</P>
	 * @param rptCmChildren 实体模型;
	 * @param request  请求;
	 * @param response 返回;
	 * @param model    视图模型;
	 * @return view URL
	 */
	@RequiresPermissions("rpt:core:rptCmChildren:view")
	@RequestMapping(value = {"list", ""})
	public String list(String cmId,RptCmChildren rptCmChildren, HttpServletRequest request, HttpServletResponse response, Model model) {
		rptCmChildren.setCmId(cmId);
		//设定列表每页记录数
		Page<RptCmChildren> temp=new Page<RptCmChildren>(request, response);
		//获得列表分页数据
		Page<RptCmChildren> page = rptCmChildrenService.find(temp, rptCmChildren); 
		model.addAttribute("page", page);
		model.addAttribute("cmId", cmId);
		//转向页面
		return "olap/rpt/core/rptCmChildrenList";
	}
	/**
	 * <P>列表list查询函数(只读)</P>
	 * @param rptCmChildren 实体模型;
	 * @param request  请求;
	 * @param response 返回;
	 * @param model    视图模型;
	 * @return view URL
	 */
	@RequiresPermissions("rpt:core:rptCmChildren:view")
	@RequestMapping(value = {"readList"})
	public String readList(String cmId,RptCmChildren rptCmChildren, HttpServletRequest request, HttpServletResponse response, Model model) {
		rptCmChildren.setCmId(cmId);
		//设定列表每页记录数
		Page<RptCmChildren> temp=new Page<RptCmChildren>(request, response);
		//获得列表分页数据
		Page<RptCmChildren> page = rptCmChildrenService.find(temp, rptCmChildren); 
		model.addAttribute("page", page);
		model.addAttribute("cmId", cmId);
		model.addAttribute("rptCm", rptCmService.get(cmId));
		//转向页面
		return "olap/rpt/core/rptCmChildrenListReadonly";
	}
    /**
	 * <P>表单form查询函数</P>
	 * @param rptCmChildren 实体模型;
	 * @param model 视图模型;
	 * @return view URL
	 */
	@RequiresPermissions("rpt:core:rptCmChildren:view")
	@RequestMapping(value = "form")
	public String form(RptCmChildren rptCmChildren, Model model) {
	    //由ModelAttribute默认构造rptCmChildren实体模型
		model.addAttribute("rptCmChildren", rptCmChildren);
		//转向页面
		return "olap/rpt/core/rptCmChildrenForm";
	}
    /**
	 * <P>表单form保存函数</P>
	 * @param rptCmChildren 实体模型;
	 * @param model 视图模型;
	 * @param redirectAttributes 重定向;
	 * @return view URL
	 */
	@RequiresPermissions("rpt:core:rptCmChildren:edit")
	@RequestMapping(value = "save")
	public String save(RptCmChildren rptCmChildren, Model model, RedirectAttributes redirectAttributes) {
		//实体Validator
		if (!beanValidator(model, rptCmChildren)){
			return form(rptCmChildren, model);
		}
		//保存表单
		rptCmChildrenService.save(rptCmChildren);
		//页面提示信息
		addMessage(redirectAttributes, "保存子报表模板成功");
		//转向页面
		return "redirect:"+Global.getAdminPath()+"/olap/rpt/core/rptCmChildren/?repage";
	}
	 /**
		 * <P>表单form保存函数</P>
		 * @param rptCmChildren 实体模型;
		 * @param model 视图模型;
		 * @param redirectAttributes 重定向;
		 * @return view URL
		 */
		@RequiresPermissions("rpt:core:rptCmChildren:edit")
		@RequestMapping(value = "saveSingle")
		public String saveSingle(String cmId,String fileName,String filePath, Model model, RedirectAttributes redirectAttributes) {
			RptCmChildren rptCmChildren=new RptCmChildren();
			rptCmChildren.setCmId(cmId);
			rptCmChildren.setFileName(fileName);
			rptCmChildren.setFilePath(filePath);
			//查询是否有重复的文件
			Page<RptCmChildren> page= rptCmChildrenService.find(new Page<RptCmChildren>(),rptCmChildren);
			if(page.getList().size()==0){
				//保存表单
				rptCmChildrenService.save(rptCmChildren);
			}
			//页面提示信息
			addMessage(redirectAttributes, "保存子报表模板成功");
			//转向页面
			return "redirect:"+Global.getAdminPath()+"/olap/rpt/core/rptCmChildren/list?cmId="+cmId+"&repage";
		}
	/**
	 * <P>列表list删除函数</P>
	 * @param id 实体主键;
	 * @param redirectAttributes 重定向;
	 * @return view URL
	 */	
	@RequiresPermissions("rpt:core:rptCmChildren:edit")
	@RequestMapping(value = "delete")
	public String delete(String id,String cmId, RedirectAttributes redirectAttributes) {
		//删除操作
		rptCmChildrenService.delete(id);
		//页面提示信息
		addMessage(redirectAttributes, "删除子报表模板成功");
		//转向页面
		return "redirect:"+Global.getAdminPath()+"/olap/rpt/core/rptCmChildren/list?cmId="+cmId+"&repage";
	}
	/**
	 * 模板文件上传
	 */
	   @RequiresPermissions("card:cs:csCm:edit")
	   @RequestMapping(value = "regupload")
	    public String regupload(MultipartFile file,HttpServletRequest request,Model model) {
		   if (file == null) {   
//			   jo.put("success",false);
//			   jo.put("message", "上传失败：文件为空");
//			   return JsonMapper.nonDefaultMapper().toJson(jo);
			   return "/error/403";
           }   
           if(file.getSize()>5000000)        
           {   
//        	   jo.put("success",false);
//			   jo.put("message", "上传失败：文件大小不能超过5M");
//			   return JsonMapper.nonDefaultMapper().toJson(jo);
        	   return "/error/403";
           }   
		   String fileName = file.getOriginalFilename();
           String extension = FilenameUtils.getExtension(fileName);
           String filepath="";
           if (extension.equals("jasper")) {
	   			if(file.getSize()>0){                  
	                try {   
	                	filepath =request.getSession().getServletContext().getRealPath("/") +"userfiles"+ File.separator+"upload"+ File.separator + "jasper";
	                	this.SaveFileFromInputStream(file.getInputStream(),filepath,fileName);      
	                } catch (IOException e) {   
	   	                	e.printStackTrace();
	   	                }   
	   	         } 
        	   
		   }else{
//			   jo.put("success",false);
//			   jo.put("message", "上传失败：文件格式非法");
//			   return JsonMapper.nonDefaultMapper().toJson(jo);
			   return "/error/403";
		   }
//	           jo.put("success",true);
//			   jo.put("message", "上传成功");
//			   jo.put("fileName", fileName);
//			   jo.put("filePath", "/upload/ht/"+fileName);
			   
			   //向上传成功页面传递文件的服务器URL
			   model.addAttribute("fileName", fileName);
			   model.addAttribute("filePath", "/userfiles/upload/jasper/"+fileName);
			   //转向上传成功页面
			   return "modules/common/uploadSucess"; 
//			   return JsonMapper.nonDefaultMapper().toJson(jo);
	    }
	   //文件保存
	   public void SaveFileFromInputStream(InputStream stream,String path,String filename) throws IOException {     
	        
	       //创建目录
           File destFile = new File(path);
          if(!destFile.exists()) destFile.mkdirs();
       	
	        FileOutputStream fs=new FileOutputStream( path + "/"+ filename);   
	        byte[] buffer =new byte[1024*1024];   
	        int bytesum = 0;   
	        int byteread = 0;    
	        while ((byteread=stream.read(buffer))!=-1)   
	        {   
	           bytesum+=byteread;   
	           fs.write(buffer,0,byteread);   
	           fs.flush();   
	        }    
	        fs.close();   
	        stream.close();         
	    } 
}