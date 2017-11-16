/*
 * Copyright &copy; 2011-2020  Inc. All right reserved.
 * 
 * 修改信息：【与SVN提交信息一致】
 * A: 新增类 王磊 2015-03-09 11:55:39
 */
package com.wf.ssm.common.persistence.mybatis.proxy;

import org.apache.ibatis.binding.BindingException;
import org.apache.ibatis.binding.MapperMethod;
import org.apache.ibatis.session.SqlSession;

import com.wf.ssm.common.persistence.Page;
import com.wf.ssm.common.utils.Reflections;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.HashSet;
import java.util.Set;

/**
 * <p>
 * 自定义Mybatis执行代理类，提供给PaginationMapperRegistry注册类使用.
 * </p>
 *
 * @author wangpf 2015-03-09 11:55:39
 * @version 1.0 
 * @since JDK 1.5
 */
public class PaginationMapperProxy implements InvocationHandler {


    private static final Set<String> OBJECT_METHODS = new HashSet<String>() {
        private static final long serialVersionUID = -1782950882770203583L;
        {
            add("toString");
            add("getClass");
            add("hashCode");
            add("equals");
            add("wait");
            add("notify");
            add("notifyAll");
        }
    };

    private boolean isObjectMethod(Method method) {
        return OBJECT_METHODS.contains(method.getName());
    }

    private final SqlSession sqlSession;

    private PaginationMapperProxy(final SqlSession sqlSession) {
        this.sqlSession = sqlSession;
    }
	/**
	 * <P>生成具体代理类的函数</P>
	 * @param mapperInterface  接口类;
	 * @param sqlSession 数据库会话;
	 * @return 返回字符串
	 */
    @SuppressWarnings("unchecked")
    public static <T> T newMapperProxy(Class<T> mapperInterface, SqlSession sqlSession) {
        ClassLoader classLoader = mapperInterface.getClassLoader();//先初始化生成代理类所需的参数
        Class<?>[] interfaces = new Class[]{mapperInterface};//具体要代理的类
        //利用Java的Proxy类生成具体的代理类
        PaginationMapperProxy proxy = new PaginationMapperProxy(sqlSession);
        return (T) Proxy.newProxyInstance(classLoader, interfaces, proxy);
    }
	/**
	 * <P>java 反射</P>
	 * @param proxy  代理类实例 
	 * @param method 类方法;
	 * @param args 参数;
	 * @return 返回字符串
	 */
    @Override
    public Object invoke(Object proxy, Method method, Object[] args)
            throws Throwable {
        if (isObjectMethod(method)) {
            return null;
        }
        final Class<?> declaringInterface = findDeclaringInterface(proxy, method);
        if (Page.class.isAssignableFrom(method.getReturnType())) {
            // 分页处理
            return new PaginationMapperMethod(declaringInterface, method, sqlSession).execute(args);
        }
        // 原处理方式 /生成MapperMethod对象
        final MapperMethod mapperMethod = new MapperMethod(declaringInterface, method, sqlSession.getConfiguration());
        final Object result = mapperMethod.execute(sqlSession, args);
        if (result == null && method.getReturnType().isPrimitive()) {
            throw new BindingException(
                    "Mapper method '"
                            + method.getName()
                            + "' ("
                            + method.getDeclaringClass()
                            + ") attempted to return null from a method with a primitive return type ("
                            + method.getReturnType() + ").");
        }
        return result;
    }

    private Class<?> findDeclaringInterface(Object proxy, Method method) {
        Class<?> declaringInterface = null;
        for (Class<?> mapperFaces : proxy.getClass().getInterfaces()) {
            Method m = Reflections.getAccessibleMethod(mapperFaces,
                    method.getName(),
                    method.getParameterTypes());
            if (m != null) {
                declaringInterface = mapperFaces;
            }
        }
        if (declaringInterface == null) {
            throw new BindingException(
                    "Could not find interface with the given method " + method);
        }
        return declaringInterface;
    }
    

}
