/*
 * Copyright &copy; 2011-2020  Inc. All right reserved.
 * 
 * 修改信息：【与SVN提交信息一致】
 * A: 新增类 王磊 2015-03-09 11:55:39
 */
package com.wf.ssm.common.persistence.mybatis.dialect;


/**
 * <P>Mybatis数据库Dialect‘方言’接口</P>
 * <P>1，此接口定义出分页部分</P>
 * <P>2，Mybatis根据你选择的“方言”，针对每种数据库，作调整，如生成不同的SQL语句</P>
 * @version 1.0
 * @author 王磊 2015-02-09 11:55:39
 * @since JDK 1.6
 */
public interface Dialect {

	/**
	 * <P>数据库本身是否支持分页当前的分页查询方式</P>
	 * <P>如果数据库不支持的话，则不进行数据库分页</P>
	 * @return true：支持当前的分页查询方式
	 */
    public boolean supportsLimit();

    /**
     * 
	 * <P>将sql转换为分页SQL，分别调用分页sql</P>
     * @param sql    SQL语句
     * @param offset 开始条数
     * @param limit  每页显示多少纪录条数
     * @return 分页查询的sql
     */
    public String getLimitString(String sql, int offset, int limit);

}
