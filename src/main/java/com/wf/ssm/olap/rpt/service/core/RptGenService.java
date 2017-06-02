/*
 * Copyright &copy; 2011-2020 lnint Inc. All right reserved.
 * 
 * 修改信息：【与SVN提交信息一致】
 * A: 新增类 严娜 2015-11-27
 */
package com.wf.ssm.olap.rpt.service.core;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.export.HtmlExporter;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.engine.export.JRXlsExporter;
import net.sf.jasperreports.engine.export.ooxml.JRDocxExporter;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleHtmlExporterOutput;
import net.sf.jasperreports.export.SimpleHtmlReportConfiguration;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;
import net.sf.jasperreports.export.SimpleXlsReportConfiguration;
import net.sf.jasperreports.web.util.WebHtmlResourceHandler;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ckfinder.connector.ServletContextFactory;
import com.wf.ssm.common.config.Global;
import com.wf.ssm.common.persistence.Page;
import com.wf.ssm.common.persistence.Parameter;
import com.wf.ssm.common.service.BaseService;
import com.wf.ssm.common.utils.DateUtils;
import com.wf.ssm.common.utils.IdGen;
import com.wf.ssm.common.utils.StringUtils;
import com.wf.ssm.core.sys.service.OfficeService;
import com.wf.ssm.core.sys.utils.UserUtils;
import com.wf.ssm.olap.rpt.dao.core.RptGenDao;
import com.wf.ssm.olap.rpt.entity.core.RptCm;
import com.wf.ssm.olap.rpt.entity.core.RptGen;

/**
 * <P>生成报表Service</P>
 *
 * @version 1.0
 * @author 严娜 2015-11-27
 * @since JDK 1.6
 */
@Service
@Transactional(readOnly = true)
public class RptGenService extends BaseService {

  	@Autowired
	private RptGenDao rptGenDao;
  	@Autowired
	private RptCmService rptCmService;
	@Autowired
	private OfficeService officeService;
	
	/**
	 * <P>获得实体模型数据</P>
	 * @param id 实体主键;
	 * @return RptGen
	 */
	public RptGen get(String id) {
		return rptGenDao.get(id);
	}
	/**
	 * <P>获得分页数据</P>
	 * @param page 分页实体;
	 * @param rptGen 业务实体
	 * @return page 分页实体
	 */
	public Page<RptGen> find(Page<RptGen> page, RptGen rptGen) {
	    //HQL查询条件
		DetachedCriteria dc = rptGenDao.createDetachedCriteria();
		if (!UserUtils.getUser().isAdmin()){//管理员查看全部数据，不做数据筛选
			dc.createAlias("createBy", "createBy");
			 //方式1:硬编码 查看本人数据
            dc.add(Restrictions.eq("createBy.id", UserUtils.getUser().getId()));
		}
		  if (StringUtils.isNotEmpty(rptGen.getRptType())){
		     dc.add(Restrictions.eq("rptType", rptGen.getRptType()));
		  }
		  if (StringUtils.isNotEmpty(rptGen.getRptCategory())){
		     dc.add(Restrictions.eq("rptCategory", rptGen.getRptCategory()));
		  }
		dc.add(Restrictions.eq(RptGen.FIELD_DEL_FLAG, RptGen.DEL_FLAG_NORMAL));
		dc.addOrder(Order.desc("createDate"));
		//查询处理
		return rptGenDao.find(page, dc);
	}
	/**
	 * <P>保存实体模型数据</P>
	 * @param rptGen 业务实体
	 * @return void
	 */	
	@Transactional(readOnly = false)
	public void save(RptGen rptGen) {
		//clear()清除级联缓存，防止保存关联表报错，使用时请打开
		//rptGenDao.clear();
		rptGenDao.save(rptGen);
	}
	/**
	 * <P>删除实体模型数据</P>
	 * @param id 实体模型主键
	 * @return void
	 */	
	@Transactional(readOnly = false)
	public void delete(String id) {
		rptGenDao.deleteById(id);
	}
	
	/**
	 * <P>查询数据库最新的数据信息（不从缓存获取）</P>
	 * @param id 实体模型主键
	 * @return rptGen 业务实体
	 */	
	public RptGen getDBLatestEntity(String id) {
		rptGenDao.evict(this.get(id));//清指定对象缓存
		return rptGenDao.getByHql("from RptGen where id = :p1", new Parameter(id));
	}
	/**
	 * <P>日报定时任务</P>
	 */
  	@Transactional(readOnly = false)
	public void genDayReport(String day) {
  		//获取当前时间
  		//String day=DateUtils.getDateBefore(new Date(), 1);
  		//插入数据前先执行删除
  		rptGenDao.doDelete(day,"0");
  		RptCm rptCm =new RptCm();
  		rptCm.setRptCategory("0");
  	    //根据报表类型查询报表模板(日报)
  		Page<RptCm> temp=new Page<RptCm>();
  		Page<RptCm> page=rptCmService.find(temp, rptCm, "4");
		List<RptCm> list=page.getList();
		for(int i=0;i<list.size();i++){
			RptCm rptCmObj=list.get(i);
			//查询模板的所属运营商
			String  cor_off_id=rptCmObj.getCorporation().getId();
			Map<String, Object> parameters = new HashMap<String, Object>();
			parameters.put("nowday", day);
			parameters.put("officelike", "%,"+cor_off_id+",%");
			parameters.put("office", cor_off_id);
			String mainpath="";
			String file_rad= IdGen.uuid()+".xls";//随机文件名
			String file_rad1= IdGen.uuid()+".pdf";//随机文件名
			String file_rad2= IdGen.uuid()+".doc";//随机文件名
			String file_rad3= IdGen.uuid()+".html";//随机文件名
			try {
				mainpath=ServletContextFactory.getServletContext().getRealPath("/");
			} catch (Exception e1) {
				e1.printStackTrace();
			}
			parameters.put("SUBREPORT_DIR", mainpath+"/userfiles/upload/jasper/");
			File jasperFile = new File(mainpath+rptCmObj.getFilePath());
			//判断模板是否存在，如果模板不存在则不生成
			if(jasperFile.exists()){  
				try {
					JasperPrint jasperPrint = null;
				
					JasperReport jasperReport = (JasperReport) JRLoader.loadObject(jasperFile);
					Connection conn=getConnection();
					jasperPrint = JasperFillManager.fillReport(jasperReport, parameters,conn);
					
					File destFile = new File(mainpath+"/userfiles/download/jasper/");
			        if(!destFile.exists()) destFile.mkdirs();
			        //生成excel文件
					File fbos = new File(mainpath+"/userfiles/download/jasper/"+file_rad);
					JRXlsExporter exporter = new JRXlsExporter();
					exporter.setExporterInput(new SimpleExporterInput(jasperPrint));
					exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(fbos));
					SimpleXlsReportConfiguration configuration = new SimpleXlsReportConfiguration();
					configuration.setOnePagePerSheet(true);
					configuration.setDetectCellType(true);
					configuration.setCollapseRowSpan(false);
					exporter.setConfiguration(configuration);
		            exporter.exportReport();
		            //生成pdf文件
					File fbos1 = new File(mainpath+"/userfiles/download/jasper/"+file_rad1);
					JRPdfExporter exporter1 = new JRPdfExporter();
					exporter1.setExporterInput(new SimpleExporterInput(jasperPrint));
					exporter1.setExporterOutput(new SimpleOutputStreamExporterOutput(fbos1));
					exporter1.exportReport();
					//生成word文件
					File fbos2 = new File(mainpath+"/userfiles/download/jasper/"+file_rad2);
					JRDocxExporter exporter2 = new JRDocxExporter();
					exporter2.setExporterInput(new SimpleExporterInput(jasperPrint));
				    exporter2.setExporterOutput(new SimpleOutputStreamExporterOutput(fbos2));
					exporter2.exportReport();
					//生成html文件
					File fbos3 = new File(mainpath+"/userfiles/download/jasper/"+file_rad3);
					HtmlExporter exporter3 = new HtmlExporter();
					SimpleExporterInput exporterInput = new SimpleExporterInput(jasperPrint);
					exporter3.setExporterInput(exporterInput);
					SimpleHtmlExporterOutput exporterOutput = new SimpleHtmlExporterOutput(fbos3);
					exporterOutput.setImageHandler(new WebHtmlResourceHandler("image?image={0}"));
					exporter3.setExporterOutput(exporterOutput);
					
				    SimpleHtmlReportConfiguration reportExportConfiguration = new SimpleHtmlReportConfiguration();
					reportExportConfiguration.setWhitePageBackground(false);
					reportExportConfiguration.setRemoveEmptySpaceBetweenRows(true);
					exporter3.setConfiguration(reportExportConfiguration);
					exporter3.exportReport();
				} catch (Exception e) {
					e.printStackTrace();
				}
				//保存报表记录
				RptGen rptGen=new RptGen();
				rptGen.setRptCm(rptCmObj);
				rptGen.setCorporation(rptCmObj.getCorporation());
				rptGen.setFilePath("/userfiles/download/jasper/"+file_rad3);
				rptGen.setFileName(file_rad3);
				rptGen.setPathExcel("/userfiles/download/jasper/"+file_rad);
				rptGen.setNameExcel(file_rad);
				rptGen.setPathPdf("/userfiles/download/jasper/"+file_rad1);
				rptGen.setNamePdf(file_rad1);
				rptGen.setPathWord("/userfiles/download/jasper/"+file_rad2);
				rptGen.setNameWord(file_rad2);
				rptGen.setRptCategory(rptCmObj.getRptCategory());
				rptGen.setRptType(rptCmObj.getRptType());
				rptGen.setRptDate(DateUtils.parseDate(day));
				this.save(rptGen);
			}else{
				logger.info(mainpath+rptCmObj.getFilePath()+"文件不存在");
			}
		}
  	}
  	/**
	 * <P>月报定时任务</P>
	 */
  	@Transactional(readOnly = false)
	public void genMonthReport(String month) {
  		//获取当前时间
  		//String preday=DateUtils.getDateBefore(new Date(), 1);
  		//String month=preday.substring(0, 7);
  		String day=month+"-01";
  		//插入数据前先执行删除
  		rptGenDao.doDelete(day,"1");
  		RptCm rptCm=new RptCm();
  		rptCm.setRptCategory("1");
  	    //根据报表类型查询报表模板(月报)
  		Page<RptCm> temp=new Page<RptCm>();
  		Page<RptCm> page=rptCmService.find(temp, rptCm, "4");
		List<RptCm> list=page.getList();
		for(int i=0;i<list.size();i++){
			RptCm rptCmObj=list.get(i);
			//查询模板的所属运营商
			String  cor_off_id=rptCmObj.getCorporation().getId();
			Map<String, Object> parameters = new HashMap<String, Object>();
			parameters.put("nowmonth", month);
			parameters.put("officelike", "%,"+cor_off_id+",%");
			parameters.put("office", cor_off_id);
			String mainpath="";
			String file_rad= IdGen.uuid()+".xls";//随机文件名
			String file_rad1= IdGen.uuid()+".pdf";//随机文件名
			String file_rad2= IdGen.uuid()+".doc";//随机文件名
			String file_rad3= IdGen.uuid()+".html";//随机文件名
			try {
				mainpath=ServletContextFactory.getServletContext().getRealPath("/");
			} catch (Exception e1) {
				e1.printStackTrace();
			}
			parameters.put("SUBREPORT_DIR", mainpath+"/userfiles/upload/jasper/"); 
			File jasperFile = new File(mainpath+rptCmObj.getFilePath());
			//判断模板是否存在，如果模板不存在则不生成
			if(jasperFile.exists()){  
				try {
					JasperPrint jasperPrint = null;
				
					JasperReport jasperReport = (JasperReport) JRLoader.loadObject(jasperFile);
					Connection conn=getConnection();
					jasperPrint = JasperFillManager.fillReport(jasperReport, parameters,conn);
					
					File destFile = new File(mainpath+"/userfiles/download/jasper/");
			        if(!destFile.exists()) destFile.mkdirs();
			        //生成excel文件
					File fbos = new File(mainpath+"/userfiles/download/jasper/"+file_rad);
					JRXlsExporter exporter = new JRXlsExporter();
					exporter.setExporterInput(new SimpleExporterInput(jasperPrint));
					exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(fbos));
					SimpleXlsReportConfiguration configuration = new SimpleXlsReportConfiguration();
					configuration.setOnePagePerSheet(true);
					configuration.setDetectCellType(true);
					configuration.setCollapseRowSpan(false);
					exporter.setConfiguration(configuration);
		            exporter.exportReport();
		            //生成pdf文件
					File fbos1 = new File(mainpath+"/userfiles/download/jasper/"+file_rad1);
					JRPdfExporter exporter1 = new JRPdfExporter();
					exporter1.setExporterInput(new SimpleExporterInput(jasperPrint));
					exporter1.setExporterOutput(new SimpleOutputStreamExporterOutput(fbos1));
					exporter1.exportReport();
					//生成word文件
					File fbos2 = new File(mainpath+"/userfiles/download/jasper/"+file_rad2);
					JRDocxExporter exporter2 = new JRDocxExporter();
					exporter2.setExporterInput(new SimpleExporterInput(jasperPrint));
				    exporter2.setExporterOutput(new SimpleOutputStreamExporterOutput(fbos2));
					exporter2.exportReport();
					//生成html文件
					File fbos3 = new File(mainpath+"/userfiles/download/jasper/"+file_rad3);
					HtmlExporter exporter3 = new HtmlExporter();
					SimpleExporterInput exporterInput = new SimpleExporterInput(jasperPrint);
					exporter3.setExporterInput(exporterInput);
					SimpleHtmlExporterOutput exporterOutput = new SimpleHtmlExporterOutput(fbos3);
					exporterOutput.setImageHandler(new WebHtmlResourceHandler("image?image={0}"));
					exporter3.setExporterOutput(exporterOutput);
					
				    SimpleHtmlReportConfiguration reportExportConfiguration = new SimpleHtmlReportConfiguration();
					reportExportConfiguration.setWhitePageBackground(false);
					reportExportConfiguration.setRemoveEmptySpaceBetweenRows(true);
					exporter3.setConfiguration(reportExportConfiguration);
					exporter3.exportReport();
				} catch (Exception e) {
					e.printStackTrace();
				}
				//保存报表记录
				RptGen rptGen=new RptGen();
				rptGen.setRptCm(rptCmObj);
				rptGen.setCorporation(rptCmObj.getCorporation());
				rptGen.setFilePath("/userfiles/download/jasper/"+file_rad3);
				rptGen.setFileName(file_rad3);
				rptGen.setPathExcel("/userfiles/download/jasper/"+file_rad);
				rptGen.setNameExcel(file_rad);
				rptGen.setPathPdf("/userfiles/download/jasper/"+file_rad1);
				rptGen.setNamePdf(file_rad1);
				rptGen.setPathWord("/userfiles/download/jasper/"+file_rad2);
				rptGen.setNameWord(file_rad2);
				rptGen.setRptCategory(rptCmObj.getRptCategory());
				rptGen.setRptType(rptCmObj.getRptType());
				rptGen.setRptDate(DateUtils.parseDate(day));
				this.save(rptGen);
			}else{
				logger.info(mainpath+rptCmObj.getFilePath()+"文件不存在");
			}
		}
  	}
  	/**
	 * <P>年报定时任务</P>
	 */
  	@Transactional(readOnly = false)
	public void genYearReport(String preday) {
  		//获取当前时间
  		//String preday=DateUtils.getDateBefore(new Date(), 1);
  		String year=preday.substring(0, 4);
  		String day=year+"-01-01";
  		//插入数据前先执行删除
  		rptGenDao.doDelete(day,"2");
  		RptCm rptCm=new RptCm();
  		rptCm.setRptCategory("2");
  	    //根据报表类型查询报表模板(年报)
  		Page<RptCm> temp=new Page<RptCm>();
  		Page<RptCm> page=rptCmService.find(temp, rptCm, "4");
		List<RptCm> list=page.getList();
		for(int i=0;i<list.size();i++){
			RptCm rptCmObj=list.get(i);
			//查询模板的所属运营商
			String  cor_off_id=rptCmObj.getCorporation().getId();
			Map<String, Object> parameters = new HashMap<String, Object>();
			parameters.put("nowyear", year);
			parameters.put("officelike", "%,"+cor_off_id+",%");
			parameters.put("office", cor_off_id);
			String mainpath="";
			String file_rad= IdGen.uuid()+".xls";//随机文件名
			String file_rad1= IdGen.uuid()+".pdf";//随机文件名
			String file_rad2= IdGen.uuid()+".doc";//随机文件名
			String file_rad3= IdGen.uuid()+".html";//随机文件名
			try {
				mainpath=ServletContextFactory.getServletContext().getRealPath("/");
			} catch (Exception e1) {
				e1.printStackTrace();
			}
			parameters.put("SUBREPORT_DIR", mainpath+"/userfiles/upload/jasper/"); 
			File jasperFile = new File(mainpath+rptCmObj.getFilePath());
			if(jasperFile.exists()){  
				try {
					JasperPrint jasperPrint = null;
				
					JasperReport jasperReport = (JasperReport) JRLoader.loadObject(jasperFile);
					Connection conn=getConnection();
					jasperPrint = JasperFillManager.fillReport(jasperReport, parameters,conn);
					
					File destFile = new File(mainpath+"/userfiles/download/jasper/");
			        if(!destFile.exists()) destFile.mkdirs();
			        //生成excel文件
					File fbos = new File(mainpath+"/userfiles/download/jasper/"+file_rad);
					JRXlsExporter exporter = new JRXlsExporter();
					exporter.setExporterInput(new SimpleExporterInput(jasperPrint));
					exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(fbos));
					SimpleXlsReportConfiguration configuration = new SimpleXlsReportConfiguration();
					configuration.setOnePagePerSheet(true);
					configuration.setDetectCellType(true);
					configuration.setCollapseRowSpan(false);
					exporter.setConfiguration(configuration);
		            exporter.exportReport();
		            //生成pdf文件
					File fbos1 = new File(mainpath+"/userfiles/download/jasper/"+file_rad1);
					JRPdfExporter exporter1 = new JRPdfExporter();
					exporter1.setExporterInput(new SimpleExporterInput(jasperPrint));
					exporter1.setExporterOutput(new SimpleOutputStreamExporterOutput(fbos1));
					exporter1.exportReport();
					//生成word文件
					File fbos2 = new File(mainpath+"/userfiles/download/jasper/"+file_rad2);
					JRDocxExporter exporter2 = new JRDocxExporter();
					exporter2.setExporterInput(new SimpleExporterInput(jasperPrint));
				    exporter2.setExporterOutput(new SimpleOutputStreamExporterOutput(fbos2));
					exporter2.exportReport();
					//生成html文件
					File fbos3 = new File(mainpath+"/userfiles/download/jasper/"+file_rad3);
					HtmlExporter exporter3 = new HtmlExporter();
					SimpleExporterInput exporterInput = new SimpleExporterInput(jasperPrint);
					exporter3.setExporterInput(exporterInput);
					SimpleHtmlExporterOutput exporterOutput = new SimpleHtmlExporterOutput(fbos3);
					exporterOutput.setImageHandler(new WebHtmlResourceHandler("image?image={0}"));
					exporter3.setExporterOutput(exporterOutput);
					
				    SimpleHtmlReportConfiguration reportExportConfiguration = new SimpleHtmlReportConfiguration();
					reportExportConfiguration.setWhitePageBackground(false);
					reportExportConfiguration.setRemoveEmptySpaceBetweenRows(true);
					exporter3.setConfiguration(reportExportConfiguration);
					exporter3.exportReport();
				} catch (Exception e) {
					e.printStackTrace();
				}
				//保存报表记录
				RptGen rptGen=new RptGen();
				rptGen.setRptCm(rptCmObj);
				rptGen.setCorporation(rptCmObj.getCorporation());
				rptGen.setFilePath("/userfiles/download/jasper/"+file_rad3);
				rptGen.setFileName(file_rad3);
				rptGen.setPathExcel("/userfiles/download/jasper/"+file_rad);
				rptGen.setNameExcel(file_rad);
				rptGen.setPathPdf("/userfiles/download/jasper/"+file_rad1);
				rptGen.setNamePdf(file_rad1);
				rptGen.setPathWord("/userfiles/download/jasper/"+file_rad2);
				rptGen.setNameWord(file_rad2);
				rptGen.setRptCategory(rptCmObj.getRptCategory());
				rptGen.setRptType(rptCmObj.getRptType());
				rptGen.setRptDate(DateUtils.parseDate(day));
				this.save(rptGen);
			}else{
				logger.info(mainpath+rptCmObj.getFilePath()+"文件不存在");
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
  	/**  
	 * <P>获取定时生成报表的最新记录时间</P>
	 * @param type 报表分类
	 * @return String
	 */
  	@Transactional(readOnly = false)
	public String findLastDate(String type) {
  		String returnday="";
  		List<String> stats=rptGenDao.findLastDate(type);
  		if(stats.size()>0){
  			returnday=stats.get(0);
  		}
  		return returnday;
  	}
}