package com.wf.ssm.olap.rpt.web.core;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.export.JRHtmlExporter;
import net.sf.jasperreports.engine.export.JRHtmlExporterParameter;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.engine.export.JRXlsExporter;
import net.sf.jasperreports.engine.export.ooxml.JRDocxExporter;
import net.sf.jasperreports.engine.util.FileBufferedOutputStream;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;
import net.sf.jasperreports.export.SimpleXlsReportConfiguration;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.wf.ssm.common.config.Global;
import com.wf.ssm.common.web.BaseController;
import com.wf.ssm.core.sys.entity.User;
import com.wf.ssm.core.sys.service.OfficeService;
import com.wf.ssm.core.sys.utils.UserUtils;
import com.wf.ssm.olap.rpt.entity.core.RptCm;
import com.wf.ssm.olap.rpt.entity.core.RptQuerycondition;
import com.wf.ssm.olap.rpt.service.core.RptCmService;

@Controller
@RequestMapping(value = "${adminPath}/rpt/core/rptShow")
public class RptShowController extends BaseController{
	
	@Autowired
	private RptCmService rptCmService;
	@Autowired
	private OfficeService officeService;
	/**
	 * <P>报表初始化</P>
	 * @return String
	 */
	@RequestMapping(value = {"initRptShow"})
	public String initRptShow(String rptType,HttpServletRequest request, HttpServletResponse response, Model model) {
		User currentUser=UserUtils.getUser();
		User corporation=currentUser.getOffice().getCreateBy();
		//根据报表类型查询报表模板文件和jsp页面名称
		RptCm rptCm=rptCmService.findByRptType(corporation,rptType);
		
		String pageName=rptCm.getPageName();
		
		model.addAttribute("sbuffer","");
		model.addAttribute("rptCm",rptCm);
		model.addAttribute("bz", "0");
		//转向页面
		return "olap/rpt/core/"+pageName;
	}
	/**
	 * <P>报表预览</P>
	 * @return String
	 */
	@RequestMapping(value = {"rptShow"})
	public String rptShow(String rptType,HttpServletRequest request, HttpServletResponse response, Model model) {
		User currentUser=UserUtils.getUser();
		User corporation=currentUser.getOffice().getCreateBy();
		//根据报表类型查询报表模板文件和jsp页面名称
		RptCm rptCm=rptCmService.findByRptType(corporation,rptType);
		
		String pageName=rptCm.getPageName();
		StringBuffer sbuffer = new StringBuffer();
		JasperPrint jasperPrint=genReport(rptType,request,response,model);
		if(null != jasperPrint){
			//5.6版本已经废弃的方法和类（暂时新的方法还没找到）
			JRHtmlExporter  exporter = new JRHtmlExporter();
			exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
			exporter.setParameter(JRExporterParameter.OUTPUT_STRING_BUFFER, sbuffer);
			exporter.setParameter(JRHtmlExporterParameter.IS_USING_IMAGES_TO_ALIGN, false);
			//exporter.setParameter(JRHtmlExporterParameter.IMAGES_URI, "../image?image=");
			//exporter.setParameter(JRExporterParameter.PAGE_INDEX, new Integer(pageIndex));
			exporter.setParameter(JRHtmlExporterParameter.HTML_HEADER, "");
			exporter.setParameter(JRHtmlExporterParameter.BETWEEN_PAGES_HTML, "");
			exporter.setParameter(JRHtmlExporterParameter.HTML_FOOTER, "");
			exporter.setParameter(JRHtmlExporterParameter.SIZE_UNIT,JRHtmlExporterParameter.SIZE_UNIT_POINT);
			try {
				exporter.exportReport();
			} catch (JRException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			model.addAttribute("bz", "1");
		}else{
			model.addAttribute("message","模板文件不存在");
			model.addAttribute("bz", "0");
		}
		model.addAttribute("sbuffer",sbuffer);
		model.addAttribute("rptCm",rptCm);
		//转向页面
		return "olap/rpt/core/"+pageName;
	}
	/**
	 * <P>导出excel报表</P>
	 */
	@ResponseBody
	@RequestMapping(value = {"expExl"})
	public void expExl(String rptType,HttpServletRequest request, HttpServletResponse response, Model model) {
		JasperPrint jasperPrint=genReport(rptType,request,response,model);
		if(null != jasperPrint){
			FileBufferedOutputStream fbos = new FileBufferedOutputStream();
			JRXlsExporter exporter = new JRXlsExporter();
			exporter.setExporterInput(new SimpleExporterInput(jasperPrint));
			exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(fbos));
			SimpleXlsReportConfiguration configuration = new SimpleXlsReportConfiguration();
			configuration.setOnePagePerSheet(true);
			configuration.setDetectCellType(true);
			configuration.setCollapseRowSpan(false);
			exporter.setConfiguration(configuration);
//			exporter.setParameter(JRExporterParameter.OUTPUT_STREAM, fbos);
//			exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
//			exporter.setParameter(JRXlsAbstractExporterParameter.IS_ONE_PAGE_PER_SHEET, Boolean.FALSE);
//			exporter.setParameter(JRXlsAbstractExporterParameter.IS_WHITE_PAGE_BACKGROUND, Boolean.FALSE);
			try {
				exporter.exportReport();
				fbos.close();
				if (fbos.size() > 0) {
					response.setContentType("application/xls");
					response.setHeader("Content-Disposition", "inline; filename=\"person.xls\"");
					response.setContentLength(fbos.size());
					ServletOutputStream ouputStream = response.getOutputStream();
					try {
						fbos.writeData(ouputStream);
						fbos.dispose();
						ouputStream.flush();
					} finally {
						if (null != ouputStream) {
							ouputStream.close();
						}
					}
				}
			} catch (Exception e1) {
				e1.printStackTrace();
			}finally{
				if(null !=fbos){
					try {
						fbos.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
					fbos.dispose();
				}
			}
		}
		
	}
	/**
	 * <P>导出pdf报表</P>
	 */
	@ResponseBody
	@RequestMapping(value = {"expPdf"})
	public void expPdf(String rptType,HttpServletRequest request, HttpServletResponse response, Model model) {
		JasperPrint jasperPrint=genReport(rptType,request,response,model);
		if (null != jasperPrint) {
			FileBufferedOutputStream fbos = new FileBufferedOutputStream();
			JRPdfExporter exporter = new JRPdfExporter();
			exporter.setExporterInput(new SimpleExporterInput(jasperPrint));
			exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(fbos));
			//exporter.setParameter(JRExporterParameter.OUTPUT_STREAM, fbos);
			//exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
			try {
				exporter.exportReport();
				fbos.close();
				if (fbos.size() > 0) {
					response.setContentType("application/x-download");
					response.setHeader("Content-Disposition", "inline; filename=\"person.pdf\"");
					response.setContentLength(fbos.size());
					ServletOutputStream ouputStream = response.getOutputStream();
					try {
						fbos.writeData(ouputStream);
						fbos.dispose();
						ouputStream.flush();
					} finally {
						if (null != ouputStream) {
							ouputStream.close();
						}
					}
				}
			} catch (Exception e1) {
				e1.printStackTrace();
			}finally{
				if(null !=fbos){
					try {
						fbos.close();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					fbos.dispose();
				}
			}
		}
	}
	/**
	 * <P>导出word报表</P>
	 */
	@ResponseBody
	@RequestMapping(value = {"expWord"})
	public void expWord(String rptType,HttpServletRequest request, HttpServletResponse response, Model model) {
		JasperPrint jasperPrint=genReport(rptType,request,response,model);
		if (null != jasperPrint) {
			FileBufferedOutputStream fbos = new FileBufferedOutputStream();
//			JRExporter exporter = new JRRtfExporter(); 
//			exporter.setParameter(JRExporterParameter.OUTPUT_STREAM, fbos);
//			exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
			
			JRDocxExporter exporter = new JRDocxExporter();
			exporter.setExporterInput(new SimpleExporterInput(jasperPrint));
		    exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(fbos));
			try {
				exporter.exportReport();
				fbos.close();
				if (fbos.size() > 0) {
					response.setContentType("application/msword;charset=utf-8");
					response.setHeader("Content-Disposition", "inline; filename=\"person.doc\"");
					response.setContentLength(fbos.size());
					ServletOutputStream ouputStream = response.getOutputStream();
					try {
						fbos.writeData(ouputStream);
						fbos.dispose();
						ouputStream.flush();
					} finally {
						if (null != ouputStream) {
							ouputStream.close();
						}
					}
				}
			} catch (Exception e1) {
				e1.printStackTrace();
			}finally{
				if(null !=fbos){
					try {
						fbos.close();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					fbos.dispose();
				}
			}
		}
}
	//获取数据库连接
	public static Connection getConnection(){  
		String driver =Global.getConfig("jdbc.driver");
		String url=Global.getConfig("jdbc.url");
		String username=Global.getConfig("jdbc.username");
		String password=Global.getConfig("jdbc.password");
        Connection conn = null;  
        try {  
            Class.forName(driver);  
            conn = DriverManager.getConnection(url,username,password);  
        } catch (ClassNotFoundException e) {  
            e.printStackTrace();  
        } catch (SQLException e) {  
            e.printStackTrace();  
        }  
        return conn;  
    }  
	//根据报表类型查询报表模板，并装填数据生成报表
	public JasperPrint genReport(String rptType,HttpServletRequest request, HttpServletResponse response, Model model){
		User currentUser=UserUtils.getUser();
		User corporation=currentUser.getOffice().getCreateBy();
		//根据报表类型查询报表模板文件和jsp页面名称
		RptCm rptCm=rptCmService.findByRptType(corporation,rptType);
		
		String filePath=rptCm.getFilePath();
		File jasperFile = new File(request.getRealPath("/")+filePath);
		//判断模板是否存在，如果模板不存在则不生成
		if(jasperFile.exists()){ 
			Map<String, Object> parameters = new HashMap<String, Object>();
			User user = UserUtils.getUser();
			String useroffice=user.getOffice().getId();
			
			List<RptQuerycondition> querylist=rptCm.getRptQueryconditionList();
			for(int i=0;i<querylist.size();i++){
				RptQuerycondition condition=querylist.get(i);
				String fieldname=condition.getShowId();
				//系统固定参数
				if("curuser".equals(fieldname)){
					parameters.put("curuser", user.getId());
				}else if("curoffice".equals(fieldname)){
					parameters.put("curoffice", useroffice);
				}else if("curofficelike".equals(fieldname)){
					parameters.put("curofficelike", "%,"+useroffice+",%");
				}else if("officelike".equals(fieldname)){//officelike类型参数
					if(StringUtils.isNotEmpty(request.getParameter("office"))){
						parameters.put(fieldname, "%,"+request.getParameter("office").toString()+",%");
					}else{
						parameters.put(fieldname,"");
					}
				}else{
					if(StringUtils.isNotEmpty(request.getParameter(fieldname))){
						parameters.put(fieldname, request.getParameter(fieldname).toString());
						if("office".equals(fieldname)){
							model.addAttribute("officeObj", officeService.get(request.getParameter(fieldname).toString()));
						}else{
							model.addAttribute(fieldname,request.getParameter(fieldname).toString());
						}
					}else{
						parameters.put(fieldname,"");
					}
				}
			}
			parameters.put("SUBREPORT_DIR", request.getRealPath("/")+"/userfiles/upload/jasper/"); 
			JasperPrint jasperPrint = null;
			response.setCharacterEncoding("utf-8");
			try {
				JasperReport jasperReport = (JasperReport) JRLoader.loadObject(jasperFile);
				Connection conn=getConnection();
				jasperPrint = JasperFillManager.fillReport(jasperReport, parameters,conn);
			} catch (Exception e) {
				e.printStackTrace();
			}
			return jasperPrint;
		}else{
			return null;
		}
	}
}
