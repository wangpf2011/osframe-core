package com.wf.ssm.common.utils;

import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.Map;

import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;

import freemarker.template.Configuration;
import freemarker.template.Template;

/**
 * <P>FreeMarkers工具类</P>
 * 
 * @version 1.0
 * @author wangpf 2015-3-12 9:06:26
 * @since JDK 1.6
 */
public class FreeMarkers {

	/**
	 * <P>装载模板，用提供的数据渲染模板，返回输出字符串</P>
	 * @param templateString 字符串类型的模板信息,用来生成Template对象
	 * @param model 数据模型
	 * @return Configuration 表示返回配置信息对象实例
	 */
	public static String renderString(String templateString, Map<String, ?> model) {
		try {
			StringWriter result = new StringWriter();
			Template t = new Template("name", new StringReader(templateString), new Configuration());
			t.process(model, result);
			return result.toString();
		} catch (Exception e) {
			throw Exceptions.unchecked(e);
		}
	}

	/**
	 * <P>装载模板，用提供的数据渲染模板，返回输出字符串</P>
	 * @param template 模板
	 * @param model 数据模型
	 * @return String 输出的字符串
	 */
	public static String renderTemplate(Template template, Object model) {
		try {
			StringWriter result = new StringWriter();
			template.process(model, result);
			return result.toString();
		} catch (Exception e) {
			throw Exceptions.unchecked(e);
		}
	}

	/**
	 * <P>获取模板的配置信息对象</P>
	 * @param directory 配置所在的目录
	 * @return Configuration 表示返回配置信息对象实例
	 */
	public static Configuration buildConfiguration(String directory) throws IOException {
		Configuration cfg = new Configuration();
		Resource path = new DefaultResourceLoader().getResource(directory);
		cfg.setDirectoryForTemplateLoading(path.getFile());
		return cfg;
	}
	
}
