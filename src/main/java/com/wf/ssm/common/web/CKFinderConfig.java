/*
 * Copyright &copy; 2011-2020 lnint Inc. All right reserved.
 * 
 * 修改信息：【与SVN提交信息一致】
 * A: 新增类 王朋飞 2015-03-12 12:02:25
 */
package com.wf.ssm.common.web;

import javax.servlet.ServletConfig;
import javax.servlet.http.HttpServletRequest;

import org.apache.shiro.SecurityUtils;

import com.ckfinder.connector.ServletContextFactory;
import com.ckfinder.connector.configuration.Configuration;
import com.ckfinder.connector.data.AccessControlLevel;
import com.ckfinder.connector.utils.AccessControlUtil;
import com.wf.ssm.core.sys.security.SystemAuthorizingRealm.Principal;

/**
 * <P>CKFinder配置</P>
 * 
 * @version 1.0
 * @author 王朋飞  2014-12-17 12:02:25
 * @since JDK 1.6
 */
public class CKFinderConfig extends Configuration {

	public static final String CK_BASH_URL = "/userfiles/";

	public CKFinderConfig(ServletConfig servletConfig) {
        super(servletConfig);  
    }
	
	/**
	 * <P>扩展 CKFinder配置</P>
	 * 
	 * @return Configuration
	 */
	@Override
    protected Configuration createConfigurationInstance() {
//		boolean isView = SecurityUtils.getSubject().isPermitted("cms:ckfinder:view");
//		boolean isUpload = SecurityUtils.getSubject().isPermitted("cms:ckfinder:upload");
//		boolean isEdit = SecurityUtils.getSubject().isPermitted("cms:ckfinder:edit");
		boolean isView = true;
		boolean isUpload = true;
		boolean isEdit = true;
		
		AccessControlLevel alc = this.getAccessConrolLevels().get(0);
		alc.setFolderView(isView);
		alc.setFolderCreate(isEdit);
		alc.setFolderRename(isEdit);
		alc.setFolderDelete(isEdit);
		alc.setFileView(isView);
		alc.setFileUpload(isUpload);
		alc.setFileRename(isEdit);
		alc.setFileDelete(isEdit);
//		for (AccessControlLevel a : this.getAccessConrolLevels()){
//			System.out.println(a.getRole()+", "+a.getResourceType()+", "+a.getFolder()
//					+", "+a.isFolderView()+", "+a.isFolderCreate()+", "+a.isFolderRename()+", "+a.isFolderDelete()
//					+", "+a.isFileView()+", "+a.isFileUpload()+", "+a.isFileRename()+", "+a.isFileDelete());
//		}
		AccessControlUtil.getInstance(this).loadACLConfig();
		try {
			Principal principal = (Principal)SecurityUtils.getSubject().getPrincipal();
			this.baseURL = ServletContextFactory.getServletContext().getContextPath()+"/userfiles/"+
					(principal!=null?principal.getId():0)+"/";
			/*Principal principal = (Principal) SecurityUtils.getSubject().getPrincipal();
			String parentDir = principal != null ? principal.getId() : "0";
			this.baseURL = ServletContextFactory.getServletContext().getContextPath() + CK_BASH_URL + parentDir + "/";
			this.baseDir = Global.getCkBaseDir() + parentDir + File.separator;*/
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		return new CKFinderConfig(this.servletConf);
    }
	
	/**
	 * <P>检测CKFinder的授权，CKFinder是否可用</P>
	 * 
	 * @return Configuration
	 */
    @Override  
    public boolean checkAuthentication(final HttpServletRequest request) {
        return SecurityUtils.getSubject().getPrincipal()!=null;
    }

}
