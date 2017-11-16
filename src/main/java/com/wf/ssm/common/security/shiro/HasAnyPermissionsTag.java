package com.wf.ssm.common.security.shiro;

import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.tags.PermissionTag;

/**
 * <p>Shiro HasAnyPermissions Tag.在shiros.tld用到</br>
 *  shiros一直没有提供HasAnyPermissions的Tag</br>
 *  判断用户是否拥有多个权限中的一个</p>
 *
 * @version 1.0 
 * @author wangpf  2015-03-11 16:00:00
 * @since JDK 1.6
 */

public class HasAnyPermissionsTag extends PermissionTag {

	private static final long serialVersionUID = 1L;
	private static final String PERMISSION_NAMES_DELIMETER = ",";

	@Override
	protected boolean showTagBody(String permissionNames) {
		boolean hasAnyPermission = false;

		Subject subject = getSubject();

		if (subject != null) {
			// Iterate through permissions and check to see if the user has one of the permissions
			for (String permission : permissionNames.split(PERMISSION_NAMES_DELIMETER)) {

				if (subject.isPermitted(permission.trim())) {
					hasAnyPermission = true;
					break;
				}

			}
		}

		return hasAnyPermission;
	}

}
