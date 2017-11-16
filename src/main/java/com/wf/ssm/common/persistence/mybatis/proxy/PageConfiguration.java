/*
 * Copyright &copy; 2011-2020  Inc. All right reserved.
 * 
 * 修改信息：【与SVN提交信息一致】
 * A: 新增类 王磊 2015-03-09 11:55:39
 */
package com.wf.ssm.common.persistence.mybatis.proxy;

import org.apache.ibatis.binding.MapperRegistry;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.SqlSession;

/**
 * <p>
 * 自定义Mybatis的配置分页，扩展.
 * </p>
 *
 * @author wangpf 2015-03-09 11:55:39
 * @version 1.0 
 * @since JDK 1.5
 */
public class PageConfiguration extends Configuration {
	// mybatis中mapper的注册类及对外提供生成代理类接口的类
    protected MapperRegistry mapperRegistry = new PaginationMapperRegistry(this);
    /**
     * <P>设置映射</P>
     * @param type 实体类
     */
    @Override
    public <T> void addMapper(Class<T> type) {
        mapperRegistry.addMapper(type);
    }
    /**
     * <P>获得映射类实体</P>
     * @param type 实体类
     * @param sqlSession 对象
     * @return 实体类
     */
    @Override
    public <T> T getMapper(Class<T> type, SqlSession sqlSession) {
        return mapperRegistry.getMapper(type, sqlSession);
    }
    /**
     * <P>是否有实体</P>
     * @param type 实体类
     * @return true:有,false:无
     */
    @Override
    public boolean hasMapper(Class<?> type) {
        return mapperRegistry.hasMapper(type);
    }
}
