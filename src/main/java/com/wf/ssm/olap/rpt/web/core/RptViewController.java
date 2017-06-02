package com.wf.ssm.olap.rpt.web.core;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JRParameter;
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

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.wf.ssm.common.config.Global;
import com.wf.ssm.common.web.BaseController;

@Controller
@RequestMapping(value="${adminPath}/rpt/view")
public class RptViewController extends BaseController{
	/**
	 * <P>报表预览</P>
	 * @return String
	 */
	@RequestMapping(value = {"rptView"})
	public String rptView(HttpServletRequest request, HttpServletResponse response, Model model) {
		File jasperFile = new File(request.getRealPath("/")+"/userfiles/upload/jasper/report6.jasper");
		Map<String, Object> parameters = new HashMap<String, Object>();
		//parameters.put("id", "1");
		parameters.put("SUBREPORT_DIR", request.getRealPath("/")+"/userfiles/upload/jasper/"); 
		parameters.put("companyId", "1"); 
		JasperPrint jasperPrint = null;
		response.setCharacterEncoding("utf-8");
		try {
			JasperReport jasperReport = (JasperReport) JRLoader.loadObject(jasperFile);
			JRParameter params[]=jasperReport.getParameters();
			for(int i=0;i<params.length;i++){
				System.out.println(params[i].getName());
				System.out.println(params[i].getValueClassName());
			}
			Connection conn=getConnection();
			jasperPrint = JasperFillManager.fillReport(jasperReport, parameters,conn);
		} catch (Exception e){
			e.printStackTrace();
		}
		if(null != jasperPrint){
			//5.6版本已经废弃的方法和类（暂时新的方法还没找到）
			StringBuffer sbuffer = new StringBuffer();
			JRHtmlExporter  exporter = new JRHtmlExporter();
			exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
			exporter.setParameter(JRExporterParameter.OUTPUT_STRING_BUFFER, sbuffer);
			exporter.setParameter(JRHtmlExporterParameter.IS_USING_IMAGES_TO_ALIGN, false);
			//exporter.setParameter(JRHtmlExporterParameter.IMAGES_URI, "../image?image=");
			//exporter.setParameter(JRExporterParameter.PAGE_INDEX, new Integer(pageIndex));
			exporter.setParameter(JRHtmlExporterParameter.HTML_HEADER, "");
			exporter.setParameter(JRHtmlExporterParameter.BETWEEN_PAGES_HTML, "");
			exporter.setParameter(JRHtmlExporterParameter.HTML_FOOTER, "");
			try {
				exporter.exportReport();
			} catch (JRException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
//			StringBuffer sbuffer = new StringBuffer();
//			HtmlExporter exporterHTML = new HtmlExporter();
//			SimpleExporterInput exporterInput = new SimpleExporterInput(jasperPrint);
//			exporterHTML.setExporterInput(exporterInput);
//			String outStr="";
//			try {
//				SimpleHtmlExporterOutput exporterOutput = new SimpleHtmlExporterOutput(sbuffer);
//				exporterOutput.setImageHandler(new WebHtmlResourceHandler("image?image={0}"));
//				exporterHTML.setExporterOutput(exporterOutput);
//				
//			    SimpleHtmlReportConfiguration reportExportConfiguration = new SimpleHtmlReportConfiguration();
//				reportExportConfiguration.setWhitePageBackground(false);
//				reportExportConfiguration.setRemoveEmptySpaceBetweenRows(true);
//				exporterHTML.setConfiguration(reportExportConfiguration);
//				exporterHTML.exportReport();
//				outStr=sbuffer.toString();
//			} catch (JRException e) {
//				e.printStackTrace();
//			}
//			try {
//				JasperExportManager.exportReportToHtmlFile(jasperPrint, "d://1.html");
//			} catch (JRException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}  
//			model.addAttribute("filePath","d://1.html");
			model.addAttribute("sbuffer",sbuffer);
			
		}
		//转向页面
		return "olap/rpt/core/rptView";
	}
	/**
	 * <P>导出excel报表</P>
	 */
	@ResponseBody
	@RequestMapping(value = {"expExl"})
	public void expExl(HttpServletRequest request, HttpServletResponse response, Model model) {
		File jasperFile = new File(request.getRealPath("/")+"/userfiles/upload/jasper/report4.jasper");
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("id", "1");
		JasperPrint jasperPrint = null;
		response.setCharacterEncoding("utf-8");
		try {
			JasperReport jasperReport = (JasperReport) JRLoader.loadObject(jasperFile);
			Connection conn=getConnection();
			jasperPrint = JasperFillManager.fillReport(jasperReport, parameters,conn);
		} catch (Exception e) {
			e.printStackTrace();
		}
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
	public void expPdf(HttpServletRequest request, HttpServletResponse response, Model model) {
		File jasperFile = new File(request.getRealPath("/")+"/userfiles/upload/jasper/report4.jasper");
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("id", "1");
		JasperPrint jasperPrint = null;
		response.setCharacterEncoding("utf-8");
		try {
			JasperReport jasperReport = (JasperReport) JRLoader.loadObject(jasperFile);
			Connection conn=getConnection();
			jasperPrint = JasperFillManager.fillReport(jasperReport, parameters,conn);
		} catch (Exception e) {
			e.printStackTrace();
		}
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
	public void expWord(HttpServletRequest request, HttpServletResponse response, Model model) {
		File jasperFile = new File(request.getRealPath("/")+"/userfiles/upload/jasper/report4.jasper");
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("id", "1");
		JasperPrint jasperPrint = null;
		response.setCharacterEncoding("utf-8");
		try {
			JasperReport jasperReport = (JasperReport) JRLoader.loadObject(jasperFile);
			Connection conn=getConnection();
			jasperPrint = JasperFillManager.fillReport(jasperReport, parameters,conn);
		} catch (Exception e) {
			e.printStackTrace();
		}
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
}
