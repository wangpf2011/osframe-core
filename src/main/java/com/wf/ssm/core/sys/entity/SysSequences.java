/*
 * Copyright &copy; 2011-2020 lnint Inc. All right reserved.
 * 
 * 修改信息：【与SVN提交信息一致】
 * A: 新增类 王磊 2015-06-05
 */
package com.wf.ssm.core.sys.entity;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import com.wf.ssm.common.annotation.Comment;
import com.wf.ssm.common.persistence.IdEntity;

/**
 * <P>序列管理Entity</P>
 *
 * @version 1.0
 * @author 王磊 2015-06-05
 * @since JDK 1.6
 */
@Entity
@Table(name = "SYS_SEQUENCES")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class SysSequences extends IdEntity<SysSequences> {
	
	private static final long serialVersionUID = 1L;
	 @Comment(name ="当前序列值")
	 private Long seqVal;		
	 @Comment(name ="序列位数(前面不足补充0)")
	 private String seqWidth;		
	 @Comment(name ="后缀")
	 private String rightpad;		
	 @Comment(name ="序列名称（不可重复）")
	 private String seqName;		
	 @Comment(name ="前缀")
	 private String leftpad;		
	
	public SysSequences() {
		super();
	}

	public SysSequences(String id){
		this();
		this.id = id;
	}
	@NotNull(message="当前序列值不能为空")
	public Long getSeqVal() {
		return seqVal;
	}

	public void setSeqVal(Long seqVal) {
		this.seqVal = seqVal;
	}
	
	public String getSeqWidth() {
		return seqWidth;
	}

	public void setSeqWidth(String seqWidth) {
		this.seqWidth = seqWidth;
	}
	
	public String getRightpad() {
		return rightpad;
	}

	public void setRightpad(String rightpad) {
		this.rightpad = rightpad;
	}
	
	public String getSeqName() {
		return seqName;
	}

	public void setSeqName(String seqName) {
		this.seqName = seqName;
	}
	
	public String getLeftpad() {
		return leftpad;
	}

	public void setLeftpad(String leftpad) {
		this.leftpad = leftpad;
	}
	
	
	
}