/**
 * Copyright &copy; 2011-2020 lnint Inc. All rights reserved.
 * 
 * 修改信息：v1.0 doc做成
 * A: 新增类  葛松  2015-03-11 15:20:21
 */
package com.wf.ssm.core.sys.web;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.wf.ssm.common.persistence.Page;
import com.wf.ssm.common.web.BaseController;
import com.wf.ssm.core.sys.entity.Log;
import com.wf.ssm.core.sys.service.LogService;

/**
 * <P>日志Controller</P>
 * 
 * @version 1.0
 * @author wangpf 2015-03-12 00:00:00
 * @since JDK 1.6
 */
@Controller
@RequestMapping(value = "${adminPath}/sys/log")
public class LogController extends BaseController {

	@Autowired
	private LogService logService;
	
	/**
	 * <p>获取视图名称"core/sys/logList"<p>
	 * <p>处理list请求<p>
	 * @param paramMap log实体对象
	 * @param request 请求
	 * @param response 响应
	 * @param model 模型
	 * @return 视图名称"core/sys/logList"
	 */
	@RequiresPermissions("sys:log:view")
	@RequestMapping(value = {"list", ""})
	public String list(@RequestParam Map<String, Object> paramMap, HttpServletRequest request, HttpServletResponse response, Model model) {
        Page<Log> page = logService.find(new Page<Log>(request, response), paramMap); 
        model.addAttribute("page", page);
        model.addAllAttributes(paramMap);
		return "core/sys/logList";
	}

	/**
	 * <P>上传文件共通弹出页面</P>
	 * @param upUrl 上传的URL;
	 * @param model    视图模型;
	 * @return view URL
	 */
	@RequestMapping(value = {"uploadPopup"})
	public String uploadPopup(String upUrl, HttpServletRequest request, HttpServletResponse response, Model model) {
		//设定列表每页记录数
		//转向页面
		model.addAttribute("upUrl", upUrl);
		return "core/common/fileUpLoadPopup";
	}
}
