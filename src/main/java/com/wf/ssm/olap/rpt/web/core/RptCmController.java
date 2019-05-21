package com.wf.ssm.olap.rpt.web.core;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRParameter;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.util.JRLoader;

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
import com.wf.ssm.common.utils.FileUtils;
import com.wf.ssm.common.utils.FreeMarkers;
import com.wf.ssm.common.utils.IdGen;
import com.wf.ssm.common.utils.StringUtils;
import com.wf.ssm.common.web.BaseController;
import com.wf.ssm.core.sys.entity.Office;
import com.wf.ssm.core.sys.entity.User;
import com.wf.ssm.core.sys.service.OfficeService;
import com.wf.ssm.core.sys.utils.UserUtils;
import com.wf.ssm.olap.rpt.entity.core.RptCm;
import com.wf.ssm.olap.rpt.entity.core.RptQuerycondition;
import com.wf.ssm.olap.rpt.service.core.RptCmService;
import com.wf.ssm.olap.rpt.service.core.RptQueryconditionService;

import freemarker.template.Configuration;
import freemarker.template.DefaultObjectWrapper;
import freemarker.template.Template;
import freemarker.template.TemplateExceptionHandler;

/**
 * 报表模板维护Controller
 *
 * @version 1.0
 * @author wangpf 2015-11-10
 * @since JDK 1.6
 */
@Controller
@RequestMapping(value = "${adminPath}/rpt/core/rptCm")
public class RptCmController extends BaseController {

	@Autowired
	private RptCmService rptCmService;
	@Autowired
	private RptQueryconditionService rptQueryconditionService;
	@Autowired
	private OfficeService officeService;
	/**
	 * <P>ModelAttribute 构造函数</P>
	 * @param id 实体主键;
	 * @return RptCm
	 */
	@ModelAttribute
	public RptCm get(@RequestParam(required=false) String id) {
		if (StringUtils.isNotBlank(id)){
			return rptCmService.get(id);
		}else{
			return new RptCm();
		}
	}
	/**
	 * <P>列表list查询函数</P>
	 * @param rptCm 实体模型;
	 * @param request  请求;
	 * @param response 返回;
	 * @param model    视图模型;
	 * @return view URL
	 */
	@RequiresPermissions("rpt:core:rptCm:view")
	@RequestMapping(value = {"list", ""})
	public String list(RptCm rptCm, HttpServletRequest request, HttpServletResponse response, Model model) {
		//设定列表每页记录数
		Page<RptCm> temp=new Page<RptCm>(request, response);
		//获得列表分页数据
		Page<RptCm> page = rptCmService.find(temp, rptCm,"1"); 
		model.addAttribute("page", page);
		//转向页面
		return "olap/rpt/core/rptCmList";
	}
	/**
	 * <P>审核列表list查询函数</P>
	 * @param rptCm 实体模型;
	 * @param request  请求;
	 * @param response 返回;
	 * @param model    视图模型;
	 * @return view URL
	 */
	@RequiresPermissions("rpt:core:rptCm:view")
	@RequestMapping(value = {"checkList"})
	public String checkList(RptCm rptCm, HttpServletRequest request, HttpServletResponse response, Model model) {
		//设定列表每页记录数
		Page<RptCm> temp=new Page<RptCm>(request, response);
		temp.setPageSize(10);
		//获得列表分页数据
		Page<RptCm> page = rptCmService.find(temp, rptCm,"2"); 
		model.addAttribute("page", page);
		//转向页面
		return "olap/rpt/core/rptCmCheckList";
	}
	/**
	 * <P>发布列表list查询函数</P>
	 * @param rptCm 实体模型;
	 * @param request  请求;
	 * @param response 返回;
	 * @param model    视图模型;
	 * @return view URL
	 */
	@RequiresPermissions("rpt:core:rptCm:view")
	@RequestMapping(value = {"publishList"})
	public String publishList(RptCm rptCm, HttpServletRequest request, HttpServletResponse response, Model model) {
		//设定列表每页记录数
		Page<RptCm> temp=new Page<RptCm>(request, response);
		temp.setPageSize(10);
		//获得列表分页数据
		Page<RptCm> page = rptCmService.find(temp, rptCm,"3"); 
		model.addAttribute("page", page);
		//转向页面
		return "olap/rpt/core/rptCmPublishList";
	}
    /**
	 * <P>表单form查询函数</P>
	 * @param rptCm 实体模型;
	 * @param model 视图模型;
	 * @return view URL
	 */
	@RequiresPermissions("rpt:core:rptCm:view")
	@RequestMapping(value = "form")
	public String form(RptCm rptCm, Model model) {
	    //由ModelAttribute默认构造rptCm实体模型
		model.addAttribute("rptCm", rptCm);
		//转向页面
		return "olap/rpt/core/rptCmForm";
	}
	/**
	 * <P>只读表单form查询函数</P>
	 * @param id 报表模板主键;
	 * @param model 视图模型;
	 * @return view URL
	 */
	@RequiresPermissions("rpt:core:rptCm:view")
	@RequestMapping(value = "readForm")
	public String readForm(String id, Model model) {
	    //由ModelAttribute默认构造rptCm实体模型
		RptCm rptCm=rptCmService.get(id);
		model.addAttribute("rptCm", rptCm);
		//转向页面
		return "olap/rpt/core/rptCmFormReadonly";
	}
	/**
	 * <P>查询条件列表查询函数</P>
	 * @param rptCm 实体模型;
	 * @param model 视图模型;
	 * @return view URL
	 */
	@RequiresPermissions("rpt:core:rptCm:view")
	@RequestMapping(value = "queryList")
	public String queryList(RptCm rptCm, HttpServletRequest request,Model model) {
		model.addAttribute("rptCm", rptCm);
		//转向页面
		return "olap/rpt/core/rptQueryconditionList";
	}
	/**
	 * <P>查询条件列表查询函数(只读)</P>
	 * @param rptCm 实体模型;
	 * @param model 视图模型;
	 * @return view URL
	 */
	@RequiresPermissions("rpt:core:rptCm:view")
	@RequestMapping(value = "readQueryList")
	public String readQueryList(RptCm rptCm, HttpServletRequest request,Model model) {
		model.addAttribute("rptCm", rptCm);
		//转向页面
		return "olap/rpt/core/rptQueryconditionListReadonly";
	}
	/**
	 * <P>审核表单form查询函数</P>
	 * @param rptCm 实体模型;
	 * @param model 视图模型;
	 * @return view URL
	 */
	@RequiresPermissions("rpt:core:rptCm:view")
	@RequestMapping(value = "check")
	public String check(String id, Model model) {
	    //由ModelAttribute默认构造rptCm实体模型
		RptCm rptCm=new RptCm();
		rptCm=rptCmService.get(id);
		model.addAttribute("rptCm", rptCm);
		//转向页面
		return "olap/rpt/core/rptCmCheckForm";
	}
	 /**
	 * <P>发布表单form查询函数</P>
	 * @param rptCm 实体模型;
	 * @param model 视图模型;
	 * @return view URL
	 */
	@RequiresPermissions("rpt:core:rptCm:view")
	@RequestMapping(value = "publish")
	public String publish(String id, Model model) {
	    //由ModelAttribute默认构造rptCm实体模型
		RptCm rptCm=new RptCm();
		rptCm=rptCmService.get(id);
		model.addAttribute("rptCm", rptCm);
		//转向页面
		return "olap/rpt/core/rptCmPublishForm";
	}
    /**
	 * <P>表单form保存函数</P>
	 * @param rptCm 实体模型;
	 * @param model 视图模型;
	 * @param redirectAttributes 重定向;
	 * @return view URL
	 */
	@RequiresPermissions("rpt:core:rptCm:edit")
	@RequestMapping(value = "save")
	public String save(RptCm rptCm, Model model, HttpServletRequest request,RedirectAttributes redirectAttributes) {
		//实体Validator
		if (!beanValidator(model, rptCm)){
			return form(rptCm, model);
		}
	    rptCm.setStatus("0");
		rptCm.setFlowStatus("0");
		//保存所属运营商
		//查询模板的所属运营商
		String  cor_off_id="";
		//获得用户所在的offiid
		Office curOfff=UserUtils.getUser().getOffice();
		//获得本用户的
		if(curOfff.getType().equals("1")){
			cor_off_id="1";
		}else if(curOfff.getType().equals("2")){//此用户直接挂在运营商下面
			//此用户所属的运营商officeid 
			cor_off_id=curOfff.getId();
		}else{
			//此用户所属的运营商officeid 为数组的第三个数据 
			cor_off_id=curOfff.getParentIds().split(",")[2];
		}
		rptCm.setCorporation(officeService.get(cor_off_id));
		//保存表单
		rptCmService.save(rptCm);

		//根据报表模板获取报表参数，保存报表参数（0 查询条件，1 系统参数，2 查询条件变式）
		rptQueryconditionService.deleteByCm(rptCm.getId());
		File jasperFile = new File(request.getRealPath("/")+rptCm.getFilePath());
		try {
			JasperReport jasperReport = (JasperReport) JRLoader.loadObject(jasperFile);
			JRParameter params[]=jasperReport.getParameters();
			for(int i=0;i<params.length;i++){
				String dataType=params[i].getValueClassName();
				String paramName=params[i].getName();
				//去掉内置参数
				int num=0;
				String filterParam[] ={"REPORT_CONTEXT","REPORT_PARAMETERS_MAP","JASPER_REPORT","JASPER_REPORTS_CONTEXT","REPORT_CONNECTION","REPORT_MAX_COUNT","REPORT_DATA_SOURCE","REPORT_SCRIPTLET","REPORT_LOCALE","REPORT_RESOURCE_BUNDLE","REPORT_TIME_ZONE","REPORT_FORMAT_FACTORY","REPORT_CLASS_LOADER","REPORT_URL_HANDLER_FACTORY","REPORT_FILE_RESOLVER","REPORT_VIRTUALIZER","IS_IGNORE_PAGINATION","REPORT_TEMPLATES","FILTER","SORT_FIELDS","SUBREPORT_DIR"};
				for(int j=0;j<filterParam.length;j++){
					if(paramName.equals(filterParam[j])){
						break;
					}
					num++;
				}
				if(num==filterParam.length){
					RptQuerycondition querys=new RptQuerycondition();
					querys.setShowId(paramName);
					querys.setRptCm(rptCm);
					if(paramName.startsWith("cur")){
						querys.setIsSystem("1");
					}else if(paramName.endsWith("like")){
						querys.setIsSystem("2");
					}else{
						querys.setIsSystem("0");
					}
					rptQueryconditionService.save(querys);
				}
			}
		} catch (JRException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//页面提示信息
		addMessage(redirectAttributes, "保存报表模板成功");
		//转向页面
		return "redirect:"+Global.getAdminPath()+"/olap/rpt/core/rptCm/?repage";
	}
	/**
	 * <P>查询条件保存函数</P>
	 * @param rptCm 实体模型;
	 * @param model 视图模型;
	 * @param redirectAttributes 重定向;
	 * @return view URL
	 */
	@RequiresPermissions("rpt:core:rptCm:edit")
	@RequestMapping(value = "saveQuery")
	public String saveQuery(RptCm rptCm, Model model, RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, rptCm)){
			return form(rptCm, model);
		}
		rptCmService.saveQuery(rptCm);
		//页面提示信息
		addMessage(redirectAttributes, "报表查询条件保存成功");
		//转向页面
		return "redirect:"+Global.getAdminPath()+"/olap/rpt/core/rptCm/?repage";
	}
	/**
	 * <P>审核信息保存表单form保存函数</P>
	 * @param rptCm 实体模型;
	 * @param model 视图模型;
	 * @param redirectAttributes 重定向;
	 * @return view URL
	 */
	@RequiresPermissions("rpt:core:rptCm:edit")
	@RequestMapping(value = "checkSave")
	public String checkSave(RptCm rptCm, Model model, RedirectAttributes redirectAttributes) {
		//实体Validator
		if (!beanValidator(model, rptCm)){
			return form(rptCm, model);
		}
		if("0".equals(rptCm.getAuditResult())){
			rptCm.setFlowStatus("3");
		}else if("1".equals(rptCm.getAuditResult())){
			rptCm.setFlowStatus("2");
		}
		User currentUser=UserUtils.getUser();
		rptCm.setAuditPerson(currentUser);
		rptCm.setAuditTime(new Date());
		//保存表单
		rptCmService.save(rptCm);
		//页面提示信息
		addMessage(redirectAttributes, "保存报表模板审核信息成功");
		//转向页面
		return "redirect:"+Global.getAdminPath()+"/olap/rpt/core/rptCm/checkList?repage";
	}
	/**
	 * <P>发布信息保存表单form保存函数</P>
	 * @param rptCm 实体模型;
	 * @param model 视图模型;
	 * @param redirectAttributes 重定向;
	 * @return view URL
	 */
	@RequiresPermissions("rpt:core:rptCm:edit")
	@RequestMapping(value = "publishSave")
	public String publishSave(RptCm rptCm, Model model, HttpServletRequest request,RedirectAttributes redirectAttributes) {
		//实体Validator
		if (!beanValidator(model, rptCm)){
			return form(rptCm, model);
		}
		//把同一报表模板类型同一运营商的已发布的合同模板置为失效
		RptCm rptCmNew=rptCmService.get(rptCm.getId());
		String corporation=rptCmNew.getCorporation().getId();
		String rptCategory=rptCmNew.getRptCategory();
		String rptType=rptCmNew.getRptType();
		rptCmService.cancelUpdate(corporation,rptCategory,rptType);
		//保存
		User currentUser=UserUtils.getUser();
		rptCm.setReleasePerson(currentUser);
		rptCm.setReleaseDate(new Date());
		rptCm.setFlowStatus("4");
		rptCm.setStatus("1");
		//保存表单
		rptCmService.save(rptCm);
		//只有查询报表生成查询页面
		if("3".equals(rptCm.getRptCategory())){
			generate(rptCm.getId(),request);
		}
		//页面提示信息
		addMessage(redirectAttributes, "报表模板发布成功");
		//转向页面
		return "redirect:"+Global.getAdminPath()+"/olap/rpt/core/rptCm/publishList?repage";
	}
	/**
	 * <P>提交审核函数</P>
	 * @param rptCm 实体模型;
	 * @param model 视图模型;
	 * @param redirectAttributes 重定向;
	 * @return view URL
	 */
	@RequiresPermissions("rpt:core:rptCm:edit")
	@RequestMapping(value = "forCheck")
	public String forCheck(RptCm rptCm, Model model, RedirectAttributes redirectAttributes) {
		//实体Validator
		if (!beanValidator(model, rptCm)){
			return form(rptCm, model);
		}
		rptCm.setFlowStatus("1");
		//保存表单
		rptCmService.save(rptCm);
		//页面提示信息
		addMessage(redirectAttributes, "报表模板提交审核成功");
		//转向页面
		return "redirect:"+Global.getAdminPath()+"/olap/rpt/core/rptCm/?repage";
	}
	/**
	 * <P>列表list删除函数</P>
	 * @param id 实体主键;
	 * @param redirectAttributes 重定向;
	 * @return view URL
	 */	
	@RequiresPermissions("rpt:core:rptCm:edit")
	@RequestMapping(value = "delete")
	public String delete(String id, RedirectAttributes redirectAttributes) {
		//删除操作
		rptCmService.delete(id);
		//页面提示信息
		addMessage(redirectAttributes, "删除报表模板成功");
		//转向页面
		return "redirect:"+Global.getAdminPath()+"/olap/rpt/core/rptCm/?repage";
	}
	/**
	 * 模板文件上传
	 */
	   @RequiresPermissions("card:cs:csCm:edit")
	   @RequestMapping(value = "regupload")
	    public String regupload(MultipartFile file,HttpServletRequest request,Model model) {
		   if (file == null) {   
			   return "/error/403";
           }   
           if(file.getSize()>5000000)        
           {   
        	   return "/error/403";
           }   
		   String fileName = file.getOriginalFilename();
           String extension = FilenameUtils.getExtension(fileName);
           String file_rad= IdGen.uuid()+"."+extension;//随机文件名
           String filepath="";
           if (extension.equals("jasper")) {
	   			if(file.getSize()>0){                  
	                try {   
	                	filepath =request.getSession().getServletContext().getRealPath("/") +"userfiles"+ File.separator+"upload"+ File.separator + "jasper";
	                	this.SaveFileFromInputStream(file.getInputStream(),filepath,file_rad);      
	                } catch (IOException e) {   
	   	                	e.printStackTrace();
	   	                }   
	   	         } 
		   }else{
			   return "/error/403";
		   }
		   
		   //向上传成功页面传递文件的服务器URL
           model.addAttribute("relfileName", fileName);
		   model.addAttribute("fileName", file_rad);
		   model.addAttribute("filePath", "/userfiles/upload/jasper/"+file_rad);
		   //转向上传成功页面
		   return "core/common/uploadSucess"; 
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
	   public String generate(String rptCmId,HttpServletRequest request) {
		    String content="";
		    RptCm rptCm=get(rptCmId);
		    String fileName=request.getSession().getServletContext().getRealPath("/")+"WEB-INF/views/olap/rpt/core/"+rptCm.getPageName()+".jsp";
		    Map<String, Object> model=new HashMap<String,Object>();
		    get(rptCmId).getRptQueryconditionList();
		    model.put("rptCm", rptCm);
	        Configuration cfg = new Configuration();
	        try {
					cfg.setDirectoryForTemplateLoading(new File(RptCmController.class.getResource("/").getPath()+"com/wf/ssm/modules/rpt/web/core/"));
					cfg.setObjectWrapper(new DefaultObjectWrapper());
					cfg.setTemplateExceptionHandler(TemplateExceptionHandler.IGNORE_HANDLER);
					Template template = cfg.getTemplate("list.ftl","UTF-8");
					content=FreeMarkers.renderTemplate(template, model);
		        } catch (IOException e) {
					e.printStackTrace();
				}
	     // 创建并写入文件
			if (FileUtils.createFile(fileName)){
				FileUtils.writeToFile(fileName, content, true);
				logger.debug(" file create === " + fileName);
				return "生成成功："+fileName+"<br/>";
			}else{
				logger.debug(" file extents === " + fileName);
				return "文件已存在："+fileName+"<br/>";
			}
	    }
}