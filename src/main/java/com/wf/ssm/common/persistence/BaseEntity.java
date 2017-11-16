/*
 * Copyright &copy; 2011-2020 lnint Inc. All right reserved.
 * 
 * 修改信息：【与SVN提交信息一致】
 * A: 新增类 王磊 2014-12-17 12:02:25
 */
package com.wf.ssm.common.persistence;

import java.io.Serializable;
import java.util.Map;

import javax.persistence.MappedSuperclass;
import javax.persistence.Transient;
import javax.xml.bind.annotation.XmlTransient;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.common.collect.Maps;
import com.wf.ssm.core.sys.entity.User;
import com.wf.ssm.core.sys.utils.UserUtils;

/**
 * <P>Entity 基础支持类<br>
 * 数据支持基础类和所有业务类(Entity)共同继承的父类<br>
 * +封装了登陆用户信息，分页信息，自定义SQL，搜索多个ID共通信息<br>
 * </p> 
 * @version 1.0
 * @author wangpf 2015-03-11 12:02:25
 * @since JDK 1.6
 */
@MappedSuperclass
public abstract class BaseEntity<T> implements Serializable {

	private static final long serialVersionUID = 8886668120140527L;

	/**
	 * 当前用户
	 */
	protected User currentUser;
	
	/**
	 * 当前实体分页对象
	 */
	protected Page<T> page;

	/**
	 * 自定义SQL（SQL标识，SQL内容）
	 */
	protected Map<String, String> sqlMap;

	/**
	 *从页面搜索还是从菜单搜索（用于非页面搜索时设置默认搜索条件）
	 */
	private boolean searchFromPage;

	/**
	 *用于搜索多个ID的时候设置搜索条件
	 */
	private String ids;
	
	/**显示
	 */
	public static final String SHOW = "1";
	/**隐藏
	 */
	public static final String HIDE = "0";
	/**是
	 */
	public static final String YES = "1";
	/**否
	 */
	public static final String NO = "0";
	/**删除标记属性
	 */
	public static final String FIELD_DEL_FLAG = "delFlag";
	/**正常
	 */
	public static final String DEL_FLAG_NORMAL = "0";
	/**删除
	 */
	public static final String DEL_FLAG_DELETE = "1";
	/**审核
	 */
	public static final String DEL_FLAG_AUDIT = "2";
	
	/**
	 * <P>获得当前登陆用户实体<br>
	 * </P>
	 * @return User登陆用户
	 */
	@JsonIgnore
	@XmlTransient
	@Transient
	public User getCurrentUser() {
		if(currentUser == null){
			currentUser = UserUtils.getUser();
		}
		return currentUser;
	}
	/**
	 * <P>设置当前登陆用户实体<br>
	 * </P>
	 * @param currentUser User登陆用户
	 */
	public void setCurrentUser(User currentUser) {
		this.currentUser = currentUser;
	}
	/**
	 * <P>获得当前分页对象实体<br>
	 * </P>
	 * @return Page分页对象
	 */
	@JsonIgnore
	@XmlTransient
	@Transient
	public Page<T> getPage() {
		if (page == null){
			page = new Page<T>();
		}
		return page;
	}
	/**
	 * <P>设置当前分页对象实体<br>
	 * </P>
	 * @param page 分页对象
	 */
	public Page<T> setPage(Page<T> page) {
		this.page = page;
		return page;
	}
	/**
	 * <P>获得自定义SQL（SQL标识，SQL内容）<br>
	 * </P>
	 * @return 自定义SQL
	 */
	@JsonIgnore
	@XmlTransient
	@Transient
	public Map<String, String> getSqlMap() {
		if (sqlMap == null){
			sqlMap = Maps.newHashMap();
		}
		return sqlMap;
	}
	/**
	 * <P>设置自定义SQL（SQL标识，SQL内容）<br>
	 * </P>
	 * @param 自定义SQL
	 */
	public void setSqlMap(Map<String, String> sqlMap) {
		this.sqlMap = sqlMap;
	}
	/**
	 * <P>判断从页面搜索还是从菜单搜索<br>
	 * </P>
	 * @return 
	 */	
	@Transient
	public boolean isSearchFromPage() {
		return searchFromPage;
	}
	/**
	 * <P>设置 从页面搜索还是从菜单搜索属性<br>
	 * </P>
	 * @param searchFromPage
	 */	
	@Transient
	public void setSearchFromPage(boolean searchFromPage) {
		this.searchFromPage = searchFromPage;
	}
	/**
	 * <P>获得搜索多个ID<br>
	 * </P>
	 * @return 多个ID
	 */
	@Transient
	public String getIds() {
		return ids;
	}
	/**
	 * <P>设置获得搜索多个ID<br>
	 * </P>
	 * @param 多个ID
	 */
	@Transient
	public void setIds(String ids) {
		this.ids = ids;
	}

	

	
}
