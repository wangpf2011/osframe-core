package com.wf.ssm.common.filter;

import com.wf.ssm.common.utils.CacheUtils;

import net.sf.ehcache.CacheManager;
import net.sf.ehcache.constructs.web.filter.SimplePageCachingFilter;

/**
 * <p>基于Ehcache的页面高速缓存过滤器</br>
 * 如需添加页面缓存,在web.xml中加入此过滤器</p>
 * @version 1.0
 * @author 严娜    2015-3-11 9:00:00
 * @since JDK 1.6
 */
public class PageCachingFilter extends SimplePageCachingFilter {

	@Override
	protected CacheManager getCacheManager() {
		return CacheUtils.getCacheManager();
	}
	
}
