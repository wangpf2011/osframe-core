/*
 * Copyright &copy; 2011-2020 lnint Inc. All right reserved.
 * 
 * 修改信息：【与SVN提交信息一致】
 * A: 新增类 王磊 2014-12-17 12:02:25
 */
package com.wf.ssm.common.persistence;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;
import org.hibernate.search.annotations.Analyze;
import org.hibernate.search.annotations.DateBridge;
import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.Index;
import org.hibernate.search.annotations.Resolution;
import org.hibernate.search.annotations.Store;
import org.hibernate.validator.constraints.Length;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.wf.ssm.common.utils.DateUtils;
import com.wf.ssm.core.sys.entity.User;
import com.wf.ssm.core.sys.utils.UserUtils;

/**
 * <P>Entity 数据支持类<br>
 * 所有业务Entity继承的数据支持类<br>
 * +extends继承自BaseEntity类<br>
 * +扩展封装了数据库共通字段信息属性(备注.创建日期.创建者.删除标记等信息)<br>
 * </p> 
 * @version 1.0
 * @author 王磊 2015-03-11 12:02:25
 * @since JDK 1.6
 */
@MappedSuperclass
public abstract class DataEntity<T> extends BaseEntity<T> implements Serializable {

	private static final long serialVersionUID = 985763120140527L;

	protected String remarks;	// 备注
	protected User createBy;	// 创建者
	protected Date createDate;// 创建日期
	protected User updateBy;	// 更新者
	protected Date updateDate;// 更新日期
	protected String delFlag; // 删除标记（0：正常；1：删除；2：审核）
	
	protected Date createDateStart;//创建开始时间：临时字段，虚拟字段
	protected Date createDateEnd;//创建结束时间：临时字段，虚拟字段
	protected Date updateDateStart;//更新开始时间：临时字段，虚拟字段
	protected Date updateDateEnd;//更新结束时间：临时字段，虚拟字段
	/**
	 * <P>构造方法，初始化删除标志0：未删除</P>
	 * @return void
	 */
	public DataEntity() {
		super();
		this.delFlag = DEL_FLAG_NORMAL;
	}
	/**
	 * <P>新增数据之前操作，初始化数据<br>
	 * +仅适用于 使用检索标准对象进行save操作<br>
	 * +初始化（创建者，创建日期，更新者，更新日期）<br>
	 * </P>
	 * @return void
	 */
	@PrePersist
	public void prePersist(){
		User user = UserUtils.getUser();
		if (StringUtils.isNotBlank(user.getId()) && this.createBy==null ){
			//修正新增时候，显现改变createBy bug
			this.updateBy = user;
			this.createBy = user;
		}
		this.updateDate = new Date();
		this.createDate = this.updateDate;
	}
	/**
	 * <P>更新数据之前操作，初始化数据<br>
	 * +仅适用于 使用检索标准对象进行save操作<br>
	 * +初始化（更新者，更新日期）<br>
	 * </P>
	 * @return void
	 */	
	@PreUpdate
	public void preUpdate(){
		User user = UserUtils.getUser();
		if (StringUtils.isNotBlank(user.getId())){
			this.updateBy = user;
		}
		this.updateDate = new Date();
	}
	/**
	 * <P>获得备注信息</P>
	 * @return  备注
	 */
	@Length(min=0, max=255)
	public String getRemarks() {
		return remarks;
	}
	/**
	 * <P>设置备注信息</P>
	 * @param remarks 备注
	 */
	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}
	/**
	 * <P>获得创建人信息</P>
	 * @return  创建人实体对象
	 */
	@JsonIgnore
	@ManyToOne(fetch=FetchType.LAZY)
	@NotFound(action = NotFoundAction.IGNORE)
	public User getCreateBy() {
		return createBy;
	}
	/**
	 * <P>设置创建人信息</P>
	 * @param createBy 创建人实体对象
	 */
	public void setCreateBy(User createBy) {
		this.createBy = createBy;
	}
	/**
	 * <P>获得创建日期</P>
	 * @return  创建日期
	 */
	@Temporal(TemporalType.TIMESTAMP)
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	public Date getCreateDate() {
		return createDate;
	}
	/**
	 * <P>设置创建日期</P>
	 * @param createDate java.util.Date创建日期
	 */
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
	/**
	 * <P>获得更新人信息</P>
	 * @return  更新人实体对象
	 */
	@JsonIgnore
	@ManyToOne(fetch=FetchType.LAZY)
	@NotFound(action = NotFoundAction.IGNORE)
	public User getUpdateBy() {
		return updateBy;
	}
	/**
	 * <P>设置更新人信息</P>
	 * @param updateBy 更新人实体对象
	 */
	public void setUpdateBy(User updateBy) {
		this.updateBy = updateBy;
	}
	/**
	 * <P>获得更新日期</P>
	 * @return  更新日期
	 */
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@Field(index=Index.YES, analyze=Analyze.NO, store=Store.YES)
	@DateBridge(resolution = Resolution.DAY)
	public Date getUpdateDate() {
		return updateDate;
	}
	/**
	 * <P>设置更新日期</P>
	 * @param updateDate java.util.Date更新日期
	 */
	public void setUpdateDate(Date updateDate) {
		this.updateDate = updateDate;
	}
	/**
	 * <P>获得删除标志</P>
	 * @return  删除标志（0,1）
	 */
	@Length(min=1, max=1)
	@Field(index=Index.YES, analyze=Analyze.NO, store=Store.YES)
	public String getDelFlag() {
		return delFlag;
	}
	/**
	 * <P>设置删除标志</P>
	 * @param  delFlag 删除标志（0,1）
	 */
	public void setDelFlag(String delFlag) {
		this.delFlag = delFlag;
	}

	/**
	 * <P>获得创建日期-开始日期(页面日期查询条件使用)<br>
	 * +(临时字段，虚拟字段@Transient)
	 * </P>
	 * @return  创建日期开始日期
	 */
	@Temporal(TemporalType.DATE)
	@Transient
	public Date getCreateDateStart() {
		return DateUtils.getDateStart(createDateStart);
	}
	/**
	 * <P>设置创建日期-开始日期(页面日期查询条件使用)<br>
	 * +(临时字段，虚拟字段@Transient)
	 * </P>
	 * @param createDateStart 创建日期开始日期
	 */
	public void setCreateDateStart(Date createDateStart) {
		this.createDateStart = createDateStart;
	}
	/**
	 * <P>获得创建日期-结束日期(页面日期查询条件使用)<br>
	 * +(临时字段，虚拟字段@Transient)
	 * </P>
	 * @return  创建日期结束日期
	 */
	@Temporal(TemporalType.DATE)
	@Transient
	public Date getCreateDateEnd() {
		return DateUtils.getDateEnd(createDateEnd);
	}
	/**
	 * <P>设置创建日期-结束日期(页面日期查询条件使用)<br>
	 * +(临时字段，虚拟字段@Transient)
	 * </P>
	 * @param createDateStart 创建日期结束日期
	 */
	public void setCreateDateEnd(Date createDateEnd) {
		this.createDateEnd = createDateEnd;
	}
	/**
	 * <P>获得更新日期-开始日期(页面日期查询条件使用)<br>
	 * +(临时字段，虚拟字段@Transient)
	 * </P>
	 * @return  更新日期开始日期
	 */
	@Temporal(TemporalType.DATE)
	@Transient
	public Date getUpdateDateStart() {
		return DateUtils.getDateStart(updateDateStart);
	}
	/**
	 * <P>设置更新日期-开始日期(页面日期查询条件使用)<br>
	 * +(临时字段，虚拟字段@Transient)
	 * </P>
	 * @param updateDateStart 更新日期开始日期
	 */
	public void setUpdateDateStart(Date updateDateStart) {
		this.updateDateStart = updateDateStart;
	}
	/**
	 * <P>获得更新日期-结束日期(页面日期查询条件使用)<br>
	 * +(临时字段，虚拟字段@Transient)
	 * </P>
	 * @return  更新日期结束日期
	 */
	@Temporal(TemporalType.DATE)
	@Transient
	public Date getUpdateDateEnd() {
		return DateUtils.getDateEnd(updateDateEnd);
	}
	/**
	 * <P>设置更新日期-结束日期(页面日期查询条件使用)<br>
	 * +(临时字段，虚拟字段@Transient)
	 * </P>
	 * @param updateDateStart 更新日期结束日期
	 */
	public void setUpdateDateEnd(Date updateDateEnd) {
		this.updateDateEnd = updateDateEnd;
	}
}
