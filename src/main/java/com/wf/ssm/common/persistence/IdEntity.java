/*
 * Copyright &copy; 2011-2020 lnint Inc. All right reserved.
 * 
 * 修改信息：【与SVN提交信息一致】
 * A: 新增类 王磊 2014-12-17 12:02:25
 */
package com.wf.ssm.common.persistence;

import java.io.Serializable;

import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.PrePersist;

import com.wf.ssm.common.utils.IdGen;

/**
 * <P>Entity 数据支持类<br>
 * 所有业务Entity继承的数据支持类<br>
 * +extends继承自DataEntity类<br>
 * +扩展了主键生成策略<br>
 * </p> 
 * @version 1.0
 * @author 王磊 2015-03-11 12:02:25
 * @since JDK 1.6
 */
@MappedSuperclass
public abstract class IdEntity<T> extends DataEntity<T> implements Serializable {

	private static final long serialVersionUID =77889920140527L;

	protected String id;		// 编号
	/**
	 * <P>构造方法</P>
	 * @return void
	 */
	public IdEntity() {
		super();
	}
	/**
	 * <P>新增数据之前操作，初始化数据<br>
	 * +仅适用于 使用检索标准对象进行save操作<br>
	 * +初始化ID主键信息(uuid格式)<br>
	 * </P>
	 * @return void
	 */
	@PrePersist
	public void prePersist(){
		super.prePersist();
		this.id = IdGen.uuid();
	}
	/**
	 * <P>获得主键ID</P>
	 * @return 主键UUID序列
	 */
	@Id
	public String getId() {
		return id;
	}
	/**
	 * <P>设置主键ID</P>
	 * @param id 主键UUID序列
	 */
	public void setId(String id) {
		this.id = id;
	}
	
}
