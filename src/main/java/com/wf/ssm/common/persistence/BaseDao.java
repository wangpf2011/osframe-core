/*
 * Copyright &copy; 2011-2020 lnint Inc. All right reserved.
 * 
 * 修改信息：【与SVN提交信息一致】
 * A: 新增类 王磊 2015-03-10 12:02:25
 */
package com.wf.ssm.common.persistence;

import java.io.IOException;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.persistence.Id;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.search.BooleanClause;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.QueryWrapperFilter;
import org.apache.lucene.search.Sort;
import org.apache.lucene.search.BooleanClause.Occur;
import org.apache.lucene.search.highlight.Formatter;
import org.apache.lucene.search.highlight.Highlighter;
import org.apache.lucene.search.highlight.InvalidTokenOffsetsException;
import org.apache.lucene.search.highlight.QueryScorer;
import org.apache.lucene.search.highlight.SimpleFragmenter;
import org.apache.lucene.search.highlight.SimpleHTMLFormatter;
import org.apache.lucene.util.Version;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.internal.CriteriaImpl;
import org.hibernate.search.FullTextQuery;
import org.hibernate.search.FullTextSession;
import org.hibernate.search.Search;
import org.hibernate.search.filter.impl.CachingWrapperFilter;
import org.hibernate.search.query.DatabaseRetrievalMethod;
import org.hibernate.search.query.ObjectLookupMethod;
import org.hibernate.transform.ResultTransformer;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.wltea.analyzer.lucene.IKAnalyzer;

import com.wf.ssm.common.utils.Reflections;
import com.wf.ssm.common.utils.StringUtils;

/**
 * <P>Hibernate DAO层，基本支持类实现<br>
 * 实现持久化类的基本CRUD操作(插入、查询、更新、删除)操作<br>
 * +支持原生SQL CRUD操作<br>
 * +支持HQL CRUD操作<br>
 * +支持DetachedCriteria面向对象 CRUD操作</P>
 * @version 1.0
 * @author wangpf 2015-03-10 12:02:25
 * @since JDK 1.6
 */
public class BaseDao<T> {

	/**
	 * SessionFactory
	 */
	@Autowired
	private SessionFactory sessionFactory;

	/**
	 * 实体类类型(由构造方法自动赋值)
	 */
	private Class<?> entityClass;
	
	/**
	 * 构造方法，根据实例类自动获取实体类类型
	 */
	public BaseDao() {
		entityClass = Reflections.getClassGenricType(getClass());
	}

	/**
	 * <P>获取 Hibernate Session对象</P>
	 * @return Session对象
	 */	
	public Session getSession(){  
	  return sessionFactory.getCurrentSession();
	}

	/**
	 * <P>强制与数据库同步</P>
	 */
	public void flush(){
		getSession().flush();
	}

	/**
	 * <P>清除session缓存数据-清除缓存中所有对象</P>
	 */
	public void clear(){ 
		getSession().clear();
	}
	/**
	 * <P>清除session缓存数据-清除缓存中指定的对象</P>
	 */
	public void evict(T entity){ 
		getSession().evict(entity);
	}
	/////------标准原生SQL-START-------------------------------------------

	
	
	/**
	 * <P>标准原生SQL,分页查询-无参数</P>
	 * <code><b>例子：</b><br>
	 * 	Page&lt;User> entityPage = new Page&lt;User>(1, 3);<br>
	 * 	sqlString = "select u.* from sys_user u join sys_office o on o.id=u.office_id";<br>
	 * 	entityPage = userDao.findBySql(entityPage, sqlString, User.class);<br>
	 * </code>
	 * @param page Page对象
	 * @param sqlString SQL查询语句
	 * @return  Page对象
	 */ 
	public <E> Page<E> findBySql(Page<E> page, String sqlString){
    	return findBySql(page, sqlString, null, null);
    }

	/**
	 * <P>标准原生SQL,分页查询-有参数</P>
	 * <code><b>例子：</b><br>
	 * 	Page&lt;User> entityPage = new Page&lt;User>(1, 3);<br>
	 * 	sqlString = "select u.* from sys_user u join sys_office o on o.id=u.office_id";<br>
	 * 	entityPage = userDao.findBySql(entityPage, sqlString, User.class);
	 * </code>
	 * @param page Page对象
	 * @param sqlString SQL查询语句
	 * @param parameter 参数对象
	 * @return  Page对象
	 */ 
	public <E> Page<E> findBySql(Page<E> page, String sqlString, Parameter parameter){
    	return findBySql(page, sqlString, parameter, null);
    }

	/**
	 * <P>标准原生SQL,分页查询(无参数+返回实体)</P>
	 * <code><b>例子：</b><br>
	 * 	Page&lt;User> entityPage = new Page&lt;User>(1, 3);<br>
	 * 	sqlString = "select u.* from sys_user u join sys_office o on o.id=u.office_id";<br>
	 * 	entityPage = userDao.findBySql(entityPage, sqlString, User.class);<br>
	 * </code>
	 * @param page Page对象
	 * @param sqlString SQL查询语句
	 * @param resultClass 参数对象
	 * @return  Page对象
	 */ 
	public <E> Page<E> findBySql(Page<E> page, String sqlString, Class<?> resultClass){
    	return findBySql(page, sqlString, null, resultClass);
    }
    
	/**
	 * <P>标准原生SQL,分页查询-(有参数+返回实体)</P>
	 * <code><b>例子：</b><br>
	 * 	Page&lt;User> entityPage = new Page&lt;User>(1, 3);<br>
	 * 	sqlString = "select u.* from sys_user u join sys_office o on o.id=u.office_id where u.id=:p1";<br>
	 * 	entityPage = userDao.findBySql(entityPage, sqlString,new Parameter("1") User.class);<br>
	 * </code>
	 * @param page Page对象
	 * @param sqlString SQL查询语句
	 * @param parameter 参数对象
	 * @param resultClass 参数对象
	 * @return  Page对象
	 */ 
    @SuppressWarnings("unchecked")
	public <E> Page<E> findBySql(Page<E> page, String sqlString, Parameter parameter, Class<?> resultClass){
		// get count
    	if (!page.isDisabled() && !page.isNotCount()){
	        String countSqlString = "select count(*) " + removeSelect(removeOrders(sqlString));  
//	        page.setCount(Long.valueOf(createSqlQuery(countSqlString, parameter).uniqueResult().toString()));
	        Query query = createSqlQuery(countSqlString, parameter);
	        List<Object> list = query.list();
	        if (list.size() > 0){
	        	page.setCount(Long.valueOf(list.get(0).toString()));
	        }else{
	        	page.setCount(list.size());
	        }
			if (page.getCount() < 1) {
				return page;
			}
    	}
    	// order by
    	String sql = sqlString;
		if (StringUtils.isNotBlank(page.getOrderBy())){
			sql += " order by " + page.getOrderBy();
		}
        SQLQuery query = createSqlQuery(sql, parameter); 
		// set page
        if (!page.isDisabled()){
	        query.setFirstResult(page.getFirstResult());
	        query.setMaxResults(page.getMaxResults()); 
        }
        setResultTransformer(query, resultClass);
        page.setList(query.list());
		return page;
    }

	/**
	 * <P>标准原生SQL,无参数</P>
	 * @param sqlString SQL查询语句
	 * @return  List对象
	 */ 
	public <E> List<E> findBySql(String sqlString){
		return findBySql(sqlString, null, null);
	}
	
	/**
	 * <P>标准原生SQL,有参数</P>
	 * 	 * <code><b>例子：</b><br>
	 * 	String qlString = "select u.name, u.office.name as office_name from User u where u.id=:p1";<br>
	 *  find(qlString, newParameter("1"));<br>
	 * </code>
	 * @param sqlString SQL查询语句
	 * @param parameter 参数对象
	 * @return  List对象
	 */ 
	public <E> List<E> findBySql(String sqlString, Parameter parameter){
		return findBySql(sqlString, parameter, null);
	}
	/**
	 * <P>标准原生SQL,有参数+有返回实体</P>
	 * @param sqlString SQL查询语句
	 * @param parameter 参数对象
	 * @param resultClass 返回实体类
	 * @return  List对象
	 */ 	
	@SuppressWarnings("unchecked")
	public <E> List<E> findBySql(String sqlString, Parameter parameter, Class<?> resultClass){
		SQLQuery query = createSqlQuery(sqlString, parameter);
		setResultTransformer(query, resultClass);
		return query.list();
	}
	
	/**
	 * <P>标准原生SQL,有参数+有返回实体</P>
	 * @param sqlString SQL查询语句
	 * @param parameter 参数对象
	 * @param resultClass 返回实体类
	 * @param firstResult 结果集返回的开始位置，默认是0
	 * @param maxResults 结果集返回的的最大数量
	 * @return  List对象
	 * add by weisu 2015.4.23
	 */ 	
	@SuppressWarnings("unchecked")
	public <E> List<E> findBySql(String sqlString, Parameter parameter, Class<?> resultClass, int firstResult, int maxResults){
		SQLQuery query = createSqlQuery(sqlString, parameter);
		query.setFirstResult(firstResult);
		query.setMaxResults(maxResults);
		setResultTransformer(query, resultClass);
		return query.list();
	}
	
	/**
	 * <P>标准原生SQL,更新删除数据库</P>
	 * <code><b>例子：</b><br>
	 * updateBySql("delete from dual where id=:p1", new Parameter("1"));
	 * </code>
	 * @param sqlString SQL更新删除语句
	 * @param parameter 参数对象
	 * @return  List对象
	 */ 
	public int updateBySql(String sqlString, Parameter parameter){
		return createSqlQuery(sqlString, parameter).executeUpdate();
	}
	
	/**
	 * <P>创建SQL查询对象,标准原生SQL</P>
	 * <code><b>例子：</b><br>
	 * createSqlQuery("select * from dual where id=:p1", new Parameter("1"));
	 * </code>
	 * @param sqlString SQL查询语句
	 * @param parameter 参数对象
	 * @return  SQLQuery对象
	 */ 
	public SQLQuery createSqlQuery(String sqlString, Parameter parameter){
		//对原生SQL查询执行
		SQLQuery query = getSession().createSQLQuery(sqlString);
		setParameter(query, parameter);
		return query;
	}
	/**
	 * <P>标准原生SQL,去除qlString的select子句</P>
	 * @param qlString SQL查询语句
	 * @return  sql
	 */ 
    private String removeSelect(String qlString){  
        int beginPos = qlString.toLowerCase().indexOf("from");  
        return qlString.substring(beginPos);  
    }  
      
	/**
	 * <P>标准原生SQL,去除qlString的orderBy子句</P>
	 * @param qlString SQL查询语句
	 * @return  sql
	 */ 
    private String removeOrders(String qlString) {  
        Pattern p = Pattern.compile("order\\s*by[\\w|\\W|\\s|\\S]*", Pattern.CASE_INSENSITIVE);  
        Matcher m = p.matcher(qlString);  
        StringBuffer sb = new StringBuffer();  
        while (m.find()) {  
            m.appendReplacement(sb, "");  
        }
        m.appendTail(sb);
        return sb.toString();  
    } 
	/////------标准原生SQL-END-------------------------------------------
	
	/////------HQL语句-START-------------------------------------------
    /**
	 * <P>根据HQL语句 分页查询-无参数</P>
	 * <code><b>例子：</b><br>
	 * </code>
	 * @param page 分页对象
	 * @param qlString 查询字符串
	 * @return  page 分页对象
	 */
	public <E> Page<E> find(Page<E> page, String qlString){
    	return find(page, qlString, null);
    }
    
	/**
	 * <P>根据HQL语句，分页查询-有参数</P>
	 * @param page 分页对象
	 * @param qlString 查询字符串
	 * @param parameter 参数对象
	 * @return  page 分页对象
	 */
    @SuppressWarnings("unchecked")
	public <E> Page<E> find(Page<E> page, String qlString, Parameter parameter){
		// get count
    	if (!page.isDisabled() && !page.isNotCount()){
	        String countQlString = "select count(*) " + removeSelect(removeOrders(qlString));  
//	        page.setCount(Long.valueOf(createQuery(countQlString, parameter).uniqueResult().toString()));
	        Query query = createQuery(countQlString, parameter);
	        List<Object> list = query.list();
	        if (list.size() > 0){
	        	page.setCount(Long.valueOf(list.get(0).toString()));
	        }else{
	        	page.setCount(list.size());
	        }
			if (page.getCount() < 1) {
				return page;
			}
    	}
    	// order by
    	String ql = qlString;
		if (StringUtils.isNotBlank(page.getOrderBy())){
			ql += " order by " + page.getOrderBy();
		}
        Query query = createQuery(ql, parameter); 
    	// set page
        if (!page.isDisabled()){
	        query.setFirstResult(page.getFirstResult());
	        query.setMaxResults(page.getMaxResults()); 
        }
        page.setList(query.list());
		return page;
    }
	/**
	/**
	 * <P>根据HQL语句，查询获取实体对象-无参数</P>
	 * <code><b>例子：</b><br>
	 * find("from Role");
	 * </code>
	 * @param qlString 查询字符串
	 * @return  List 对象
	 */   
	public <E> List<E> find(String qlString){
		return find(qlString, null);
	}
    
	/**
	 * <P>根据HQL语句，查询获取实体对象-有参数</P>
	 * <code><b>例子：</b><br>
	 * find("from Role where delFlag = :p1 and name = :p2", new Parameter("id", "name"));
	 * </code>
	 * @param qlString 查询字符串
	 * @param parameter 参数对象
	 * @return  List 对象
	 */   
	@SuppressWarnings("unchecked")
	public <E> List<E> find(String qlString, Parameter parameter){
		Query query = createQuery(qlString, parameter);
		return query.list();
	}
	/**
	 * <P>查询所有数据</P>
	 * @return  List 对象
	 */ 
	@SuppressWarnings("unchecked")
	public List<T> findAll(){
		//设置HB 持久化模式 实体对象 为脱管(detached)模式 托管态（有id，不与session关联）
		List<T> r=getSession().createCriteria(entityClass).list();
//		getSession().evict(r);//把某个持久化对象从session的缓存中清空。
		return r;
	}
	
	/**
	 * <P>根据ID 查询获取实体对象(脱管状态）</P>
	 * @param id 实体数据表主键
	 * @return  T 实体对象
	 */ 
	@SuppressWarnings("unchecked")
	public T get(Serializable id){
		T r=(T)getSession().get(entityClass, id);
		//设置HB 持久化模式 实体对象 为脱管(detached)模式 托管态（有id，不与session关联）
		if(r!=null){
			getSession().evict(r);//把某个持久化对象从session的缓存中清空,清空之后关联的子实体可能访问不到
		}
		return  r;
	}
	/**
	 * <P>根据ID 查询获取实体对象(持久状态）</P>
	 * @param id 实体数据表主键
	 * @return  T 实体对象
	 */ 
	@SuppressWarnings("unchecked")
	public T getPersistent(Serializable id){
		T r=(T)getSession().get(entityClass, id);
		return  r;
	}
	/**
	 * <P>根据HQL语句，查询获取实体对象-无参数</P>
	 * <code><b>例子：</b><br>
	 * getByHql("from Role "));
	 * </code>
	 * @param qlString HQL查询语句
	 * @return  T 实体对象
	 */ 
	public T getByHql(String qlString){
		return getByHql(qlString, null);
	}
	/**
	 * <P>根据HQL语句，查询获取实体对象-有参数</P>
	 * <code><b>例子：</b><br>
	 * getByHql("from Role where delFlag = :p1 and name = :p2", new Parameter("id", "name"));
	 * </code>
	 * @param qlString HQL查询语句
	 * @param parameter 参数对象
	 * @return  T 实体对象
	 */ 	
	@SuppressWarnings("unchecked")
	public T getByHql(String qlString, Parameter parameter){
		Query query = createQuery(qlString, parameter);
		return (T)query.uniqueResult();
	}
	
	/**
	 * <P>单个保存单实体对象到数据库 </P>
	 * <code><b>例子：</b><br>
	 * areaDao.save(area);
	 * </code>
	 * @param entity 实体对象
	 * @return  void
	 */ 
	public void save(T entity){
		try {
			// 获取实体编号
			Object id = null;
			for (Method method : entity.getClass().getMethods()){
				Id idAnn = method.getAnnotation(Id.class);
				if (idAnn != null){
					id = method.invoke(entity);
					break;
				}
			}
			// 插入前执行方法
			if (StringUtils.isBlank((String)id)){
				for (Method method : entity.getClass().getMethods()){
					PrePersist pp = method.getAnnotation(PrePersist.class);
					if (pp != null){
						method.invoke(entity);
						break;
					}
				}
			}
			// 更新前执行方法
			else{
				for (Method method : entity.getClass().getMethods()){
					PreUpdate pu = method.getAnnotation(PreUpdate.class);
					if (pu != null){
						method.invoke(entity);
						break;
					}
				}
			}
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
		getSession().saveOrUpdate(entity);
		
	}
	
	/**
	 * <P>批量保存实体 列表LIST</P>
	 * <P>
	 * </P>
	 * @param entityList 实体对象LIST
	 * @return  void
	 */ 
	public void save(List<T> entityList){
		for (T entity : entityList){
			save(entity);
		}
	}

	/**
	 * <P>根据HQL语句,更新数据库-无参数</P>
	 * <code><b>例子：</b><br>
	 * update(" update Role set delFlag =0 where id=1");
	 * </code>
	 * @param qlString HQL更新语句
	 * @return  int 更新成功的数量
	 */ 
	public int update(String qlString){
		return update(qlString, null);
	}
	
	/**
	 * <P>根据HQL语句,更新数据库-有参数</P>
	 * <code><b>例子：</b><br>
	 * update(" update Role set delFlag = :p1 where id = :p2", new Parameter("del", "id"));
	 * </code>
	 * @param qlString HQL更新语句
	 * @param parameter 参数对象
	 * @return  int 更新成功的数量
	 */ 
	public int update(String qlString, Parameter parameter){
		return createQuery(qlString, parameter).executeUpdate();
	}
	
	/**
	 * <P>根据ID,删除数据库数据</P>
	 * <P>
	 * 逻辑删除
	 * </P>
	 * @param id 实体数据主键
	 * @return  int 删除成功的数量
	 */ 
	public int deleteById(Serializable id){
		return update("update "+entityClass.getSimpleName()+" set delFlag='" + BaseEntity.DEL_FLAG_DELETE + "' where id = :p1", 
				new Parameter(id));
	}
	/**
	 * <P>根据ID,删除本实体数据和实体父ID下的子数据</P>
	 * <P>
	 * 逻辑删除
	 * </P>
	 * @param id 实体数据主键
	 * @param likeParentIds 实体父ID
	 * @return  int 删除成功的数量
	 */ 	
	public int deleteById(Serializable id, String likeParentIds){
		return update("update "+entityClass.getSimpleName()+" set delFlag = '" + BaseEntity.DEL_FLAG_DELETE + "' where id = :p1 or parentIds like :p2",
				new Parameter(id, likeParentIds));
	}
	/**
	 * <P>更新删除标记</P>
	 * @param id 实体数据主键
	 * @param delFlag 删除标志
	 * @return  int 更新成功的数量
	 */ 	
	public int updateDelFlag(Serializable id, String delFlag){
		return update("update "+entityClass.getSimpleName()+" set delFlag = :p2 where id = :p1", 
				new Parameter(id, delFlag));
	}
	
	/**
	 * <P>创建 HQL 查询对象</P>
	 * @param qlString HQL 查询语句
	 * @param parameter 参数对象
	 * @return  Query 查询对象
	 */ 
	public Query createQuery(String qlString, Parameter parameter){
		////对HQL查询
		Query query = getSession().createQuery(qlString);
		setParameter(query, parameter);
		return query;
	}
	/////------HQL语句-END-------------------------------------------	
	
	///// -------------- 检索标准对象 Criteria START--------------
	/**
	 * <P>分页查询</P>
	 * @param page 分页对象
	 * @return  Page 分页对象
	 */ 
	public Page<T> find(Page<T> page) {
		return find(page, createDetachedCriteria());
	}
	
	/**
	 * <P>使用检索标准对象Criteria，分页查询</P>
	 * <P>对查询条件进行面向对象的方式来组装.</P>
	 * <code><b>例子：</b><br>
	 * DetachedCriteria dc = dictDao.createDetachedCriteria();<br>
	 * articleDao.find(page, dc);
	 * </code>
	 * @param page 分页对象
	 * @param detachedCriteria 参数对象
	 * @return  Page 分页对象
	 */ 
	public Page<T> find(Page<T> page, DetachedCriteria detachedCriteria) {
		return find(page, detachedCriteria, Criteria.DISTINCT_ROOT_ENTITY);
	}
	
	/**
	 * <P>使用检索标准对象Criteria，分页查询(自定义ResultTransformer）</P>
	 * <P>对查询条件进行面向对象的方式来组装.</P>
	 * <code><b>例子：</b><br>
	 * find(page, detachedCriteria, Criteria.DISTINCT_ROOT_ENTITY)
	 * </code>
	 * @param page 分页对象
	 * @param detachedCriteria 参数对象
	 * @param resultTransformer 结果集转换参数
	 * @return  Page 分页对象
	 */ 
	@SuppressWarnings("unchecked")
	public Page<T> find(Page<T> page, DetachedCriteria detachedCriteria, ResultTransformer resultTransformer) {
		// get count
		if (!page.isDisabled() && !page.isNotCount()){
			page.setCount(count(detachedCriteria));
			if (page.getCount() < 1) {
				return page;
			}
		}
		Criteria criteria = detachedCriteria.getExecutableCriteria(getSession());
		criteria.setResultTransformer(resultTransformer);
		// set page
		if (!page.isDisabled()){
	        criteria.setFirstResult(page.getFirstResult());
	        criteria.setMaxResults(page.getMaxResults()); 
		}
		// order by
		if (StringUtils.isNotBlank(page.getOrderBy())){
			for (String order : StringUtils.split(page.getOrderBy(), ",")){
				String[] o = StringUtils.split(order, " ");
				if (o.length==1){
					criteria.addOrder(Order.asc(o[0]));
				}else if (o.length==2){
					if ("DESC".equals(o[1].toUpperCase())){
						criteria.addOrder(Order.desc(o[0]));
					}else{
						criteria.addOrder(Order.asc(o[0]));
					}
				}
			}
		}
		page.setList(criteria.list());
		return page;
	}

	/**
	 * <P>使用检索标准对象Criteria，查询所有</P>
	 * <P>对查询条件进行面向对象的方式来组装.</P>
	 * @param detachedCriteria 参数对象
	 * @return  List 对象
	 */ 
	public List<T> find(DetachedCriteria detachedCriteria) {
		return find(detachedCriteria, Criteria.DISTINCT_ROOT_ENTITY);
	}
	
	/**
	 * <P>使用检索标准对象Criteria，查询所有(自定义ResultTransformer）</P>
	 * <P>对查询条件进行面向对象的方式来组装.</P>
	 * @param detachedCriteria 参数对象
	 * @param resultTransformer 结果集转换参数
	 * @return  List 对象
	 */ 
	@SuppressWarnings("unchecked")
	public List<T> find(DetachedCriteria detachedCriteria, ResultTransformer resultTransformer) {
		Criteria criteria = detachedCriteria.getExecutableCriteria(getSession());
		criteria.setResultTransformer(resultTransformer);
		return criteria.list(); 
	}
	
	/**
	 * <P>使用检索标准对象Criteria，查询总记录数</P>
	 * <P>对查询条件进行面向对象的方式来组装.</P>
	 * @param detachedCriteria 参数对象
	 * @return  long 总记录数
	 */ 
	@SuppressWarnings("rawtypes")
	public long count(DetachedCriteria detachedCriteria) {
		Criteria criteria = detachedCriteria.getExecutableCriteria(getSession());
		long totalCount = 0;
		try {
			// Get orders
			Field field = CriteriaImpl.class.getDeclaredField("orderEntries");
			field.setAccessible(true);
			List orderEntrys = (List)field.get(criteria);
			// Remove orders
			field.set(criteria, new ArrayList());
			// Get count
			criteria.setProjection(Projections.rowCount());
			totalCount = Long.valueOf(criteria.uniqueResult().toString());
			// Clean count
			criteria.setProjection(null);
			// Restore orders
			field.set(criteria, orderEntrys);
		} catch (NoSuchFieldException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		return totalCount;
	}

	/**
	 * <P>创建与会话无关的检索标准对象</P>
	 * <code><b>例子：</b><br>
	 * DetachedCriteria dc = articleDao.createDetachedCriteria();
	 * </code>
	 * @param criterions Restrictions.eq("name", value);
	 * @return DetachedCriteria
	 */
	public DetachedCriteria createDetachedCriteria(Criterion... criterions) {
		DetachedCriteria dc = DetachedCriteria.forClass(entityClass);
		for (Criterion c : criterions) {
			dc.add(c);
		}
		return dc;
	}
	///// -------------- 检索标准对象 Criteria END--------------
	
	///// -------------- Query Tools START--------------
	/**
	 * <P>设置查询结果类型</P>
	 * @param query SQLQuery对象
	 * @param resultClass 返回实体对象
	 * @return  void
	 */ 
	private void setResultTransformer(SQLQuery query, Class<?> resultClass){
		if (resultClass != null){
			if (resultClass == Map.class){
				query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
			}else if (resultClass == List.class){
				query.setResultTransformer(Transformers.TO_LIST);
			}else{
				query.addEntity(resultClass);
			}
		}
	}
	
	/**
	 * <P>设置查询参数</P>
	 * <P>兼容原生标准SQL和HQL</P>
	 * @param query SQLQuery对象
	 * @param parameter 参数对象
	 * @return  void
	 */ 
	private void setParameter(Query query, Parameter parameter){
		if (parameter != null) {
            Set<String> keySet = parameter.keySet();
            for (String string : keySet) {
                Object value = parameter.get(string);
                //这里考虑传入的参数是什么类型，不同类型使用的方法不同  
                if(value instanceof Collection<?>){
                    query.setParameterList(string, (Collection<?>)value);
                }else if(value instanceof Object[]){
                    query.setParameterList(string, (Object[])value);
                }else{
                    query.setParameter(string, value);
                }
            }
        }
	}
	///// -------------- Query Tools END--------------
	
	// -------------- Hibernate  search  orm 全文检索--------------
	/**
	 *<P>Hibernate全文检索：获取全文Session</P>
	 */
	public FullTextSession getFullTextSession(){
		return Search.getFullTextSession(getSession());
	}
	
	/**
	 * <P>Hibernate全文检索：建立索引</P>
	 */
	public void createIndex(){
		try {
			getFullTextSession().createIndexer(entityClass).startAndWait();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * <P>Hibernate全文检索：全文检索</P>
	 * @param page 分页对象
	 * @param query 关键字查询对象
	 * @param queryFilter 查询过滤对象
	 * @param sort 排序对象
	 * @return 分页对象
	 */
	@SuppressWarnings("unchecked")
	public Page<T> search(Page<T> page, BooleanQuery query, BooleanQuery queryFilter, Sort sort){
		
		// 按关键字查询
		FullTextQuery fullTextQuery = getFullTextSession().createFullTextQuery(query, entityClass);
        
		// 过滤无效的内容
		if (queryFilter!=null){
			fullTextQuery.setFilter(new CachingWrapperFilter(new QueryWrapperFilter(queryFilter)));
		}
        
        // 设置排序
		if (sort!=null){
			fullTextQuery.setSort(sort);
		}

		// 定义分页
		page.setCount(fullTextQuery.getResultSize());
		fullTextQuery.setFirstResult(page.getFirstResult());
		fullTextQuery.setMaxResults(page.getMaxResults()); 

		// 先从持久化上下文中查找对象，如果没有再从二级缓存中查找
        fullTextQuery.initializeObjectsWith(ObjectLookupMethod.SECOND_LEVEL_CACHE, DatabaseRetrievalMethod.QUERY); 
        
		// 返回结果
		page.setList(fullTextQuery.list());
        
		return page;
	}
	
	/**
	 * <P>Hibernate全文检索：获取全文查询对象</P>
	 */
	public BooleanQuery getFullTextQuery(BooleanClause... booleanClauses){
		BooleanQuery booleanQuery = new BooleanQuery();
		for (BooleanClause booleanClause : booleanClauses){
			booleanQuery.add(booleanClause);
		}
		return booleanQuery;
	}

	/**
	 * <P>Hibernate全文检索：获取全文查询对象</P>
	 * @param q 查询关键字
	 * @param fields 查询字段
	 * @return 全文查询对象
	 */
	public BooleanQuery getFullTextQuery(String q, String... fields){
		Analyzer analyzer = new IKAnalyzer();
		BooleanQuery query = new BooleanQuery();
		try {
			if (StringUtils.isNotBlank(q)){
				for (String field : fields){
					QueryParser parser = new QueryParser(Version.LUCENE_36, field, analyzer);   
					query.add(parser.parse(q), Occur.SHOULD);
				}
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return query;
	}
	
	/**
	 * <P>Hibernate全文检索：设置关键字高亮</P>
	 * @param query 查询对象
	 * @param list 设置高亮的内容列表
	 * @param subLength 截取长度
	 * @param fields 字段名
	 */
	public List<T> keywordsHighlight(BooleanQuery query, List<T> list, int subLength, String... fields){
		Analyzer analyzer = new IKAnalyzer();
		Formatter formatter = new SimpleHTMLFormatter("<span class=\"highlight\">", "</span>");   
		Highlighter highlighter = new Highlighter(formatter, new QueryScorer(query)); 
		highlighter.setTextFragmenter(new SimpleFragmenter(subLength)); 
		for(T entity : list){ 
			try {
				for (String field : fields){
					String text = StringUtils.replaceHtml((String)Reflections.invokeGetter(entity, field));
					String description = highlighter.getBestFragment(analyzer,field, text);
					if(description!=null){
						Reflections.invokeSetter(entity, fields[0], description);
						break;
					}
					Reflections.invokeSetter(entity, fields[0], StringUtils.abbr(text, subLength*2));
				}
			} catch (IOException e) {
				e.printStackTrace();
			} catch (InvalidTokenOffsetsException e) {
				e.printStackTrace();
			} 
		}
		return list;
	}
}