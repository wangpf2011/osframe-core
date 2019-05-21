package com.wf.ssm.common.web;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.ckfinder.connector.ConnectorServlet;
import com.wf.ssm.common.utils.FileUtils;
import com.wf.ssm.core.sys.entity.User;
import com.wf.ssm.core.sys.utils.UserUtils;

/**
 * <P>CKFinder的Servlet</P>
 * 
 * @version 1.0
 * @author 王朋飞  2015-03-12 12:02:25
 * @since JDK 1.6
 */
public class CKFinderConnectorServlet extends ConnectorServlet {
	
	private static final long serialVersionUID = 1L;
	
	@Override
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		prepareGetResponse(request, response, false);
		super.doGet(request, response);
	}
	
	@Override
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		prepareGetResponse(request, response, true);
		super.doPost(request, response);
	}
	
	/**
	 * <P>处理CKFinder请求：初始化文件上传路径;文件快捷上传。</P>
	 * 
	 * @param request 用户请求
	 * @param response 用户响应
	 * @param post 请求类型（POST/GET）
	 */
	private void prepareGetResponse(final HttpServletRequest request,
			final HttpServletResponse response, final boolean post) throws ServletException {
		String command = request.getParameter("command");
		String type = request.getParameter("type");
		// 初始化时，如果startupPath文件夹不存在，则自动创建startupPath文件夹
		if ("Init".equals(command)){
			User user = UserUtils.getUser();
			if (user!=null){
				String startupPath = request.getParameter("startupPath");// 当前文件夹可指定为模块名
				if (startupPath!=null){
					String[] ss = startupPath.split(":");
					if (ss.length==2){
						String path = "/userfiles/"+user.getId()+"/"+ss[0]+ss[1];
						String realPath = request.getSession().getServletContext().getRealPath(path);
						FileUtils.createDirectory(realPath);
					}
				}
			}
		}
		// 快捷上传，自动创建当前文件夹，并上传到该路径
		else if ("QuickUpload".equals(command) && type!=null){
			User user = UserUtils.getUser();
			if (user!=null){
				String currentFolder = request.getParameter("currentFolder");// 当前文件夹可指定为模块名
				String path = "/userfiles/"+user.getId()+"/"+type+(currentFolder!=null?currentFolder:"");
				String realPath = request.getSession().getServletContext().getRealPath(path);
				FileUtils.createDirectory(realPath);
			}
		}
//		System.out.println("------------------------");
//		for (Object key : request.getParameterMap().keySet()){
//			System.out.println(key + ": " + request.getParameter(key.toString()));
//		}
	}
	
}
