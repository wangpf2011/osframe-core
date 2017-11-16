/*
 * Copyright &copy; 2011-2020 lnint Inc. All right reserved.
 * 
 * 修改信息：【与SVN提交信息一致】
 * A: 新增类 严娜 2015-11-25
 */
package com.wf.ssm.olap.rpt.entity.core;
import java.util.Date;

import java.util.List;
import com.google.common.collect.Lists;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.persistence.Transient;



import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import com.wf.ssm.common.persistence.IdEntity;
import com.wf.ssm.common.persistence.DataEntity;
import com.wf.ssm.common.annotation.Comment;

/**
 * <P>子报表模板Entity</P>
 *
 * @version 1.0
 * @author wangpf 2015-11-25
 * @since JDK 1.6
 */
@Entity
@Table(name = "LN_RPT_CM_CHILDREN")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class RptCmChildren extends IdEntity<RptCmChildren> {
	
	private static final long serialVersionUID = 1L;
	 @Comment(name ="引用文件")
	 private String fileName;		
	 @Comment(name ="主报表模板标识")
	 private String cmId;    
	 @Comment(name ="模板文件")
	 private String filePath;		
	
	public RptCmChildren() {
		super();
	}

	public RptCmChildren(String id){
		this();
		this.id = id;
	}
	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	
	public String getCmId() {
		return cmId;
	}

	public void setCmId(String cmId) {
		this.cmId = cmId;
	}

	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}
	
	
	
}