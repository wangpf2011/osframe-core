package com.wf.ssm.olap.rpt.entity.core;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import com.wf.ssm.common.annotation.Comment;
import com.wf.ssm.common.persistence.IdEntity;
import com.wf.ssm.core.sys.entity.Office;

/**
 * <P>生成报表Entity</P>
 *
 * @version 1.0
 * @author wangpf 2015-11-27
 * @since JDK 1.6
 */
@Entity
@Table(name = "LN_RPT_GEN")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class RptGen extends IdEntity<RptGen> {
	
	private static final long serialVersionUID = 1L;
	 @Comment(name ="报表时间")
	 private Date rptDate;		
	 @Comment(name ="使用报表模板")
	 private RptCm rptCm;    
	 @Comment(name ="报表类型",dictType = "ln_rpt_rpttype")
	 private String rptType;		
	 @Comment(name ="引用文件")
	 private String fileName;		
	 @Comment(name ="报表分类",dictType = "ln_rpt_rptcategory")
	 private String rptCategory;		
	 @Comment(name ="运营商")
	 private Office corporation;		
	 @Comment(name ="模板文件")
	 private String filePath;
	 @Comment(name ="excel文件路径")
	 private String pathExcel;
	 @Comment(name ="excel文件名")
	 private String nameExcel;
	 @Comment(name ="pdf文件路径")
	 private String pathPdf;
	 @Comment(name ="pdf文件名")
	 private String namePdf;
	 @Comment(name ="word文件路径")
	 private String pathWord;
	 @Comment(name ="word文件名")
	 private String nameWord;
	
	public RptGen() {
		super();
	}

	public RptGen(String id){
		this();
		this.id = id;
	}
	public Date getRptDate() {
		return rptDate;
	}

	public void setRptDate(Date rptDate) {
		this.rptDate = rptDate;
	}
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="RPT_CM_ID")
	public RptCm getRptCm() {
		return rptCm;
	}

	public void setRptCm(RptCm rptCm) {
		this.rptCm = rptCm;
	}
	public String getRptType() {
		return rptType;
	}

	public void setRptType(String rptType) {
		this.rptType = rptType;
	}
	
	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	
	public String getRptCategory() {
		return rptCategory;
	}

	public void setRptCategory(String rptCategory) {
		this.rptCategory = rptCategory;
	}
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="CORPORATION")
	public Office getCorporation() {
		return corporation;
	}

	public void setCorporation(Office corporation) {
		this.corporation = corporation;
	}
	
	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	public String getPathExcel() {
		return pathExcel;
	}

	public void setPathExcel(String pathExcel) {
		this.pathExcel = pathExcel;
	}

	public String getNameExcel() {
		return nameExcel;
	}

	public void setNameExcel(String nameExcel) {
		this.nameExcel = nameExcel;
	}

	public String getPathPdf() {
		return pathPdf;
	}

	public void setPathPdf(String pathPdf) {
		this.pathPdf = pathPdf;
	}

	public String getNamePdf() {
		return namePdf;
	}

	public void setNamePdf(String namePdf) {
		this.namePdf = namePdf;
	}

	public String getPathWord() {
		return pathWord;
	}

	public void setPathWord(String pathWord) {
		this.pathWord = pathWord;
	}

	public String getNameWord() {
		return nameWord;
	}

	public void setNameWord(String nameWord) {
		this.nameWord = nameWord;
	}
}