/*
 * Copyright &copy; 2011-2020  Inc. All right reserved.
 * 
 * 修改信息：【与SVN提交信息一致】
 * A: 新增类 王磊 2015-03-09 11:55:39
 */
package com.wf.ssm.common.persistence.mybatis.proxy;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.binding.BindingException;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlCommandType;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.RowBounds;
import org.apache.ibatis.session.SqlSession;

import com.wf.ssm.common.persistence.Page;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 自定义Mybatis基础类，提供给PaginationMapperProxy代理使用.
 * </p>
 * <p>
 * MapperProxy关联到了这个类，MapperRegistry又关联到了MapperProxy，
 * </p>
 * @author 王磊 2015-03-09 11:55:39
 * @version 1.0 
 * @since JDK 1.5
 */
public class PaginationMapperMethod {

    private final SqlSession sqlSession;
    private final Configuration config;

    private SqlCommandType type;
    private String commandName;
    private String commandCountName;

    private final Class<?> declaringInterface;
    private final Method method;

    private Integer rowBoundsIndex;
    private Integer paginationIndex;

    private final List<String> paramNames;
    private final List<Integer> paramPositions;

    private boolean hasNamedParameters;

    /**
     * <P>PaginationMapperMethod</P>
     * @param declaringInterface 已定义的mapper接口
     * @param method            具体的一个函数
     * @param sqlSession            已打开的一个sqlSession
     */
    public PaginationMapperMethod(Class<?> declaringInterface, Method method,
                                  SqlSession sqlSession) {
        paramNames = new ArrayList<String>();
        paramPositions = new ArrayList<Integer>();
        this.sqlSession = sqlSession;
        this.method = method;
        this.config = sqlSession.getConfiguration();
        this.declaringInterface = declaringInterface;
        this.hasNamedParameters = false;
        setupFields();
        setupMethodSignature();
        setupCommandType();
        validateStatement();
    }

    /**
     * <P>向外提供服务</P>
     * @param args 参数信息
     * @return 执行结果
     */
    @SuppressWarnings("unchecked")
    public Object execute(Object[] args) {
        final Object param = getParam(args);
        Page<Object> page;
        RowBounds rowBounds;
        if (paginationIndex != null) {
            page = (Page<Object>) args[paginationIndex];
            rowBounds =  new RowBounds(page.getFirstResult(), page.getMaxResults());
        } else if (rowBoundsIndex != null) {
            rowBounds = (RowBounds) args[rowBoundsIndex];
            page = new Page<Object>();
        } else {
            throw new BindingException("Invalid bound statement (not found rowBounds or pagination in paramenters)");
        }
        page.setCount(executeForCount(param));
        page.setList(executeForList(param, rowBounds));
        return page;
    }

    /**
     * 
     * <P>执行总数的方法，调用方法执行计算总数，取得总结果</P>
     * @param param 参数信息
     * @return 查询的总记录数
     */
    private long executeForCount(Object param) {
        Number result = (Number) sqlSession.selectOne(commandCountName, param);
        return result.longValue();
    }

    /**
     *  <P>取得分页的执行结果，返回的是纪录信息</P>
     *
     * @param param     参数
     * @param rowBounds row
     * @return 纪录列表
     */
    private List<Object> executeForList(Object param, RowBounds rowBounds) {
        return sqlSession.selectList(commandName, param, rowBounds);
    }

    /**
     * <P>取得当前执行的参数信息</P>
     *
     * @param args 参数
     * @return 参数信息
     */
    private Object getParam(Object[] args) {
        final int paramCount = paramPositions.size();
        if (args == null || paramCount == 0) {
            return null;
        } else if (!hasNamedParameters && paramCount == 1) {
            return args[paramPositions.get(0)];
        } else {
            Map<String, Object> param = new HashMap<String, Object>();
            for (int i = 0; i < paramCount; i++) {
                param.put(paramNames.get(i), args[paramPositions.get(i)]);
            }
            return param;
        }
    }

    private void setupMethodSignature() {
        final Class<?>[] argTypes = method.getParameterTypes();
        for (int i = 0; i < argTypes.length; i++) {
            if (Page.class.isAssignableFrom(argTypes[i])) {
                paginationIndex = i;
            } else if (RowBounds.class.isAssignableFrom(argTypes[i])) {
                rowBoundsIndex = i;
            } else {
                String paramName = String.valueOf(paramPositions.size());
                paramName = getParamNameFromAnnotation(i, paramName);
                paramNames.add(paramName);
                paramPositions.add(i);
            }
        }
    }

    private String getParamNameFromAnnotation(int i, String paramName) {
        Object[] annotations = method.getParameterAnnotations()[i];
        for (Object annotation : annotations) {
            if (annotation instanceof Param) {
                hasNamedParameters = true;
                paramName = ((Param) annotation).value();
            }
        }
        return paramName;
    }

    /**
     * 设置当前的查询总记录数的ID
     */
    private void setupFields() {
        commandName = declaringInterface.getName() + "." + method.getName();
        commandCountName = commandName + "Count"; // 命名约定
    }

    /**
     * 设置当前的参数的类型信息
     */
    private void setupCommandType() {
        MappedStatement ms = config.getMappedStatement(commandName);
        type = ms.getSqlCommandType();
        if (type != SqlCommandType.SELECT) {
            throw new BindingException("Unsupport execution method for: " + commandName);
        }
    }

    /**
     * 验证Statement
     */
    private void validateStatement() {
        if (!config.hasStatement(commandName)) {
            throw new BindingException("Invalid bound statement (not found): " + commandName);
        }
        if (!config.hasStatement(commandCountName)) {
            throw new BindingException("Invalid bound statement (not found): " + commandCountName);
        }
    }
}
