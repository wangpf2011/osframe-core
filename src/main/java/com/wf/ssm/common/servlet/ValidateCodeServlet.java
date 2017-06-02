/*
 * Copyright &copy; 2011-2020 lnint Inc. All right reserved.
 * 
 * 修改信息：【与SVN提交信息一致】
 * A: 新增类 王朋飞 2015-03-12 12:02:25
 */
package com.wf.ssm.common.servlet;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Random;

import javax.imageio.ImageIO;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.math.NumberUtils;
import org.apache.commons.lang3.StringUtils;

/**
 * <P>一个servlet，生成随机验证码（登录验证码、内容评论、留言板留言）</P>
 * 
 * @version 1.0
 * @author 王朋飞  2015-03-12 12:02:25
 * @since JDK 1.6
 */
@SuppressWarnings("serial")
public class ValidateCodeServlet extends HttpServlet {
	
	public static final String VALIDATE_CODE = "validateCode";
	
	/**
	 * 验证码默认宽度
	 */
	private int w = 70;
	/**
	 * 验证码默认高度
	 */
	private int h = 26;
	
	public ValidateCodeServlet() {
		super();
	}
	
	public void destroy() {
		super.destroy(); 
	}
	
	/**
	 * <P>验证前段填写的验证码是否与session中验证码一致</P>
	 * @param request;
	 * @param validateCode 前段传输验证码;
	 * @return boolean
	 */
	public static boolean validate(HttpServletRequest request, String validateCode){
		String code = (String)request.getSession().getAttribute(VALIDATE_CODE);
		return validateCode.toUpperCase().equals(code); 
	}

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String validateCode = request.getParameter(VALIDATE_CODE); // AJAX验证，成功返回true
		if (StringUtils.isNotBlank(validateCode)){
			response.getOutputStream().print(validate(request, validateCode)?"true":"false");
		}else{
			this.doPost(request, response);
		}
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		createImage(request,response);
	}
	
	/**
	 * <P>生成验证码图片并返回，同时把验证码字符放到session中</P>
	 * @param request;
	 * @param response;
	 * @return void
	 */
	private void createImage(HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		
		response.setHeader("Pragma", "no-cache");
		response.setHeader("Cache-Control", "no-cache");
		response.setDateHeader("Expires", 0);
		response.setContentType("image/jpeg");
		
		/*
		 * 得到参数高，宽，都为数字时，则使用设置高宽，否则使用默认值
		 */
		String width = request.getParameter("width");
		String height = request.getParameter("height");
		if (StringUtils.isNumeric(width) && StringUtils.isNumeric(height)) {
			w = NumberUtils.toInt(width);
			h = NumberUtils.toInt(height);
		}
		
		BufferedImage image = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
		Graphics g = image.getGraphics();

		/*
		 * 生成背景
		 */
		createBackground(g);

		/*
		 * 生成字符
		 */
		String s = createCharacter(g);
		request.getSession().setAttribute(VALIDATE_CODE, s);

		g.dispose();
		OutputStream out = response.getOutputStream();
		ImageIO.write(image, "JPEG", out);
		out.close();

	}
	
	/**
	 * <P>根据参数提供的两个数字，随机生成Color对象</P>
	 * @param fc int;
	 * @param bc int;
	 * @return Color 
	 */
	private Color getRandColor(int fc,int bc) { 
		int f = fc;
		int b = bc;
		Random random=new Random();
        if(f>255) {
        	f=255; 
        }
        if(b>255) {
        	b=255; 
        }
        return new Color(f+random.nextInt(b-f),f+random.nextInt(b-f),f+random.nextInt(b-f)); 
	}
	
	/**
	 * <P>随机生成验证码的背景</P>
	 * @param g Graphics对象;
	 * @return void 
	 */
	private void createBackground(Graphics g) {
		// 填充背景
		g.setColor(getRandColor(220,250)); 
		g.fillRect(0, 0, w, h);
		// 加入干扰线条
		for (int i = 0; i < 8; i++) {
			g.setColor(getRandColor(40,150));
			Random random = new Random();
			int x = random.nextInt(w);
			int y = random.nextInt(h);
			int x1 = random.nextInt(w);
			int y1 = random.nextInt(h);
			g.drawLine(x, y, x1, y1);
		}
	}

	/**
	 * <P>随机生成验证码的字符</P>
	 * @param g Graphics对象;
	 * @return 验证码字符 
	 */
	private String createCharacter(Graphics g) {
		char[] codeSeq = {'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'J',
				'K', 'L', 'M', 'N', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W',
				'X', 'Y', 'Z', '2', '3', '4', '5', '6', '7', '8', '9' };
		String[] fontTypes = {"\u5b8b\u4f53","\u65b0\u5b8b\u4f53","\u9ed1\u4f53","\u6977\u4f53","\u96b6\u4e66"}; 
		Random random = new Random();
		StringBuilder s = new StringBuilder();
		for (int i = 0; i < 4; i++) {
			String r = String.valueOf(codeSeq[random.nextInt(codeSeq.length)]);//random.nextInt(10));
			g.setColor(new Color(50 + random.nextInt(100), 50 + random.nextInt(100), 50 + random.nextInt(100)));
			g.setFont(new Font(fontTypes[random.nextInt(fontTypes.length)],Font.BOLD,26)); 
			g.drawString(r, 15 * i + 5, 19 + random.nextInt(8));
//			g.drawString(r, i*w/4, h-5);
			s.append(r);
		}
		return s.toString();
	}
	
}
