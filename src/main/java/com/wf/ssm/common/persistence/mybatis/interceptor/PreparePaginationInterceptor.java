/*
 * Copyright &copy; 2011-2020  Inc. All right reserved.
 * 
 * 修改信息：【与SVN提交信息一致】
 * A: 新增类 王磊 2015-03-09 11:55:39
 */
package com.wf.ssm.common.persistence.mybatis.interceptor;

import org.apache.ibatis.executor.statement.BaseStatementHandler;
import org.apache.ibatis.executor.statement.RoutingStatementHandler;
import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Plugin;
import org.apache.ibatis.plugin.Signature;

import com.wf.ssm.common.persistence.Page;
import com.wf.ssm.common.utils.Reflections;

import java.sql.Connection;
import java.util.Properties;

/**
 * <P>Mybatis数据库分页拦截器Interceptor,只拦截查询prepare方法</P>
 * @version 1.0
 * @author wangpf 2015-02-09 11:55:39
 * @since JDK 1.6
 */
@Intercepts({
	@Signature(type = StatementHandler.class, method = "prepare", args = {Connection.class})
})
public class PreparePaginationInterceptor extends BaseInterceptor {
    
    private static final long serialVersionUID = 1L;

    public PreparePaginationInterceptor() {
        super();
    }

	/**
	 * <P>对方法进行拦截的抽象方法  </P>
	 * @return Object
	 */
    @Override
    public Object intercept(Invocation ivk) throws Throwable {
        if (ivk.getTarget().getClass().isAssignableFrom(RoutingStatementHandler.class)) {
            final RoutingStatementHandler statementHandler = (RoutingStatementHandler) ivk.getTarget();
            final BaseStatementHandler delegate = (BaseStatementHandler) Reflections.getFieldValue(statementHandler, DELEGATE);
            final MappedStatement mappedStatement = (MappedStatement) Reflections.getFieldValue(delegate, MAPPED_STATEMENT);

//            //拦截需要分页的SQL
////            if (mappedStatement.getId().matches(_SQL_PATTERN)) { 
//            if (StringUtils.indexOfIgnoreCase(mappedStatement.getId(), _SQL_PATTERN) != -1) {
                BoundSql boundSql = delegate.getBoundSql();
                //分页SQL<select>中parameterType属性对应的实体参数，即Mapper接口中执行分页方法的参数,该参数不得为空
                Object parameterObject = boundSql.getParameterObject();
                if (parameterObject == null) {
                    log.error("参数未实例化");
                    throw new NullPointerException("parameterObject尚未实例化！");
                } else {
                    final Connection connection = (Connection) ivk.getArgs()[0];
                    final String sql = boundSql.getSql();
                    //记录统计
                    final int count = SQLHelper.getCount(sql, connection, mappedStatement, parameterObject, boundSql, log);
                    Page<Object> page = null;
                    page = convertParameter(parameterObject, page);
                    page.setCount(count);
                    String pagingSql = SQLHelper.generatePageSql(sql, page, DIALECT);
                    if (log.isDebugEnabled()) {
                        log.debug("PAGE SQL:" + pagingSql);
                    }
                    //将分页sql语句反射回BoundSql.
                    Reflections.setFieldValue(boundSql, "sql", pagingSql);
                }
                
                if (boundSql.getSql() == null || "".equals(boundSql.getSql())){
                    return null;
                }
                
            }
//        }
        return ivk.proceed();
    }

	/**
	 * <P>把拦截器插入到目标对象的方法  </P>
	 * @return Object
	 */
    @Override
    public Object plugin(Object o) {
        return Plugin.wrap(o, this);
    }

    @Override
    public void setProperties(Properties properties) {
        initProperties(properties);
    }
}
