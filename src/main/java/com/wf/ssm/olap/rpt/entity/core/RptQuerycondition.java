/*
 * Copyright &copy; 2011-2020 lnint Inc. All right reserved.
 * 
 * 修改信息：【与SVN提交信息一致】
 * A: 新增类 严娜 2015-11-10
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
 * <P>报表查询条件维护Entity</P>
 *
 * @version 1.0
 * @author wangpf 2015-11-10
 * @since JDK 1.6
 */
@Entity
@Table(name = "LN_RPT_QUERYCONDITION")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class RptQuerycondition extends IdEntity<RptQuerycondition> {
	
	private static final long serialVersionUID = 1L;
	 @Comment(name ="类型",dictType = "ln_rpt_showtype")
	 private String showType;		
	 @Comment(name ="报表模板主键")
	 private RptCm rptCm;		
	 @Comment(name ="显示名称")
	 private String showName;		
	 @Comment(name ="显示id")
	 private String showId;	
	 @Comment(name ="数据类型")
	 private String dataType;	
	 @Comment(name ="数据字典")
	 private String showDict;
	 @Comment(name ="是否系统参数")
	 private String isSystem;
	
	public RptQuerycondition() {
		super();
	}

	public RptQuerycondition(String id){
		this();
		this.id = id;
	}
   
	public String getShowType() {
		return showType;
	}

	public void setShowType(String showType) {
		this.showType = showType;
	}
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="RPT_CM_ID")
	public RptCm getRptCm() {
		return rptCm;
	}

	public void setRptCm(RptCm rptCm) {
		this.rptCm = rptCm;
	}

	public String getShowName() {
		return showName;
	}

	public void setShowName(String showName) {
		this.showName = showName;
	}
	
	public String getShowId() {
		return showId;
	}

	public void setShowId(String showId) {
		this.showId = showId;
	}

	public String getDataType() {
		return dataType;
	}

	public void setDataType(String dataType) {
		this.dataType = dataType;
	}

	public String getShowDict() {
		return showDict;
	}

	public void setShowDict(String showDict) {
		this.showDict = showDict;
	}

	public String getIsSystem() {
		return isSystem;
	}

	public void setIsSystem(String isSystem) {
		this.isSystem = isSystem;
	}
}