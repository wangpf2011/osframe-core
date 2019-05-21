package com.wf.ssm.olap.rpt.entity.core;
import java.util.Date;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;
import org.hibernate.annotations.Where;

import com.google.common.collect.Lists;
import com.wf.ssm.common.annotation.Comment;
import com.wf.ssm.common.persistence.IdEntity;
import com.wf.ssm.core.sys.entity.Office;
import com.wf.ssm.core.sys.entity.User;

/**
 * <P>报表模板维护Entity</P>
 *
 * @version 1.0
 * @author wangpf 2015-11-10
 * @since JDK 1.6
 */
@Entity
@Table(name = "LN_RPT_CM")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class RptCm extends IdEntity<RptCm> {
	
	private static final long serialVersionUID = 1L;
	 @Comment(name ="审核时间")
	 private Date auditTime;		
	 @Comment(name ="运营商")
	 private Office corporation;		
	 @Comment(name ="审核意见")
	 private String auditContent;		
	 @Comment(name ="发布时间")
	 private Date releaseDate;	
	 @Comment(name ="报表模板生成页面名称")
	 private String pageName;		
	 @Comment(name ="报表模板名称")
	 private String cmName;		
	 @Comment(name ="发布说明")
	 private String releaseRemark;		
	 @Comment(name ="文号")
	 private String fileNo;		
	 @Comment(name ="报表类型",dictType = "ln_rpt_rpttype")
	 private String rptType;		
	 @Comment(name ="审核状态",dictType = "ln_mb_status")
	 private String flowStatus;		
	 @Comment(name ="引用文件")
	 private String fileName;		
	 @Comment(name ="报表分类",dictType = "ln_rpt_rptcategory")
	 private String rptCategory;		
	 @Comment(name ="审核人")
	 private User auditPerson;		
	 @Comment(name ="状态")
	 private String status;		
	 @Comment(name ="模板文件")
	 private String filePath;		
	 @Comment(name ="真实文件名")
	 private String realFilename;		
	 @Comment(name ="审核结果")
	 private String auditResult;		
	 @Comment(name ="发布人")
	 private User releasePerson;
	 @Comment(name ="查询条件")
	 private List<RptQuerycondition> rptQueryconditionList = Lists.newArrayList();
	
	public RptCm() {
		super();
	}

	public RptCm(String id){
		this();
		this.id = id;
	}
	public Date getAuditTime() {
		return auditTime;
	}

	public void setAuditTime(Date auditTime) {
		this.auditTime = auditTime;
	}
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="CORPORATION")
	public Office getCorporation() {
		return corporation;
	}

	public void setCorporation(Office corporation) {
		this.corporation = corporation;
	}
	
	public String getAuditContent() {
		return auditContent;
	}

	public void setAuditContent(String auditContent) {
		this.auditContent = auditContent;
	}
	
	public Date getReleaseDate() {
		return releaseDate;
	}

	public void setReleaseDate(Date releaseDate) {
		this.releaseDate = releaseDate;
	}
	
	public String getCmName() {
		return cmName;
	}

	public void setCmName(String cmName) {
		this.cmName = cmName;
	}
	
	public String getReleaseRemark() {
		return releaseRemark;
	}

	public void setReleaseRemark(String releaseRemark) {
		this.releaseRemark = releaseRemark;
	}
	
	public String getFileNo() {
		return fileNo;
	}

	public void setFileNo(String fileNo) {
		this.fileNo = fileNo;
	}
	
	public String getRptType() {
		return rptType;
	}

	public void setRptType(String rptType) {
		this.rptType = rptType;
	}
	
	public String getFlowStatus() {
		return flowStatus;
	}

	public void setFlowStatus(String flowStatus) {
		this.flowStatus = flowStatus;
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
		
	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
	
	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}
	
	public String getRealFilename() {
		return realFilename;
	}

	public void setRealFilename(String realFilename) {
		this.realFilename = realFilename;
	}
	
	public String getAuditResult() {
		return auditResult;
	}

	public void setAuditResult(String auditResult) {
		this.auditResult = auditResult;
	}
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="AUDIT_PERSON")
	public User getAuditPerson() {
		return auditPerson;
	}

	public void setAuditPerson(User auditPerson) {
		this.auditPerson = auditPerson;
	}
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="RELEASE_PERSON")
	public User getReleasePerson() {
		return releasePerson;
	}

	public void setReleasePerson(User releasePerson) {
		this.releasePerson = releasePerson;
	}

	public String getPageName() {
		return pageName;
	}

	public void setPageName(String pageName) {
		this.pageName = pageName;
	}
	/**
	 * @return the queryList
	 */
	@OneToMany(mappedBy = "rptCm", fetch = FetchType.EAGER)
    @Where(clause = "del_flag='" + DEL_FLAG_NORMAL + "'")
    @OrderBy(value = "id")
    @Fetch(FetchMode.SUBSELECT)
    @NotFound(action = NotFoundAction.IGNORE)
	public List<RptQuerycondition> getRptQueryconditionList() {
		return rptQueryconditionList;
	}

	/**
	 * @param queryList the queryList to set
	 */
	public void setRptQueryconditionList(List<RptQuerycondition> rptQueryconditionList) {
		this.rptQueryconditionList = rptQueryconditionList;
	}
}