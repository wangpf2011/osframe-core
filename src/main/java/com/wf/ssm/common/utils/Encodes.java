package com.wf.ssm.common.utils;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;

import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.binary.Hex;
import org.apache.commons.lang3.StringEscapeUtils;

/**
 * <P>封装各种格式的编码解码工具类.</P>
 * 1.Commons-Codec的 hex/base64 编码
 * 2.自制的base62 编码
 * 3.Commons-Lang的xml/html escape
 * 4.JDK提供的URLEncoder
 * @version 1.0
 * @author wangpf 2015-3-12 9:06:26
 * @since JDK 1.6
 */
public class Encodes {

	private static final String DEFAULT_URL_ENCODING = "UTF-8";
	// char[] 数组
	private static final char[] BASE62 = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz".toCharArray();

	/**
	 * <P>Hex编码.</P>
	 * @param input 表示需要进行hex编码的byte[]
	 * @return String 表示进行hex编码以后的返回的字符串
	 */
	public static String encodeHex(byte[] input) {
		return Hex.encodeHexString(input);
	}

	/**
	 * <P>Hex解码.</P>
	 * @param input 表示需要进行hex解码的字符串对象
	 * @return byte[] 表示进行hex解码以后的返回的比特数组
	 */
	public static byte[] decodeHex(String input) {
		try {
			return Hex.decodeHex(input.toCharArray());
		} catch (DecoderException e) {
			throw Exceptions.unchecked(e);
		}
	}

	/**
	 * <P>Base64编码.</P>
	 * @param input 表示需要进行Base64编码的byte[]
	 * @return String 表示进行Base64编码以后的返回的字符串
	 */
	public static String encodeBase64(byte[] input) {
		return Base64.encodeBase64String(input);
	}

	/**
	 * <P>Base64编码, URL安全(将Base64中的URL非法字符'+'和'/'转为'-'和'_', 见RFC3548)</P>
	 * @param input 表示需要进行Base64编码的byte[]
	 * @return String 表示进行Base64编码以后的返回的字符串
	 */
	public static String encodeUrlSafeBase64(byte[] input) {
		return Base64.encodeBase64URLSafeString(input);
	}

	/**
	 * <P>Base64解码.</P>
	 * @param input 表示需要进行Base64解码的字符串
	 * @return String 表示进行Base64解码以后的返回的byte数组
	 */
	public static byte[] decodeBase64(String input) {
		return Base64.decodeBase64(input);
	}

	/**
	 * <P>Base62编码.</P>
	 * @param input 表示需要进行Base62编码的byte[]
	 * @return String 表示进行Base62编码以后的返回的字符串
	 */
	public static String encodeBase62(byte[] input) {
		char[] chars = new char[input.length];
		for (int i = 0; i < input.length; i++) {
			chars[i] = BASE62[((input[i] & 0xFF) % BASE62.length)];
		}
		return new String(chars);
	}

	/**
	 * <P>Html 转码.</P>
	 * @param html 表示需要进行Html转码的字符串
	 * @return String 表示经过转码以后的html字符串
	 */
	public static String escapeHtml(String html) {
		return StringEscapeUtils.escapeHtml4(html);
	}

	/**
	 * <P>Html 解码.</P>
	 * @param html 表示需要进行Html解码的字符串
	 * @return String 表示经过解码以后的html字符串
	 */
	public static String unescapeHtml(String htmlEscaped) {
		return StringEscapeUtils.unescapeHtml4(htmlEscaped);
	}

	/**
	 * <P>Xml 转码.</P>
	 * @param xml 表示需要进行xml转码的字符串
	 * @return String 表示经过转码以后的xml字符串
	 */
	public static String escapeXml(String xml) {
		return StringEscapeUtils.escapeXml(xml);
	}

	/**
	 * <P>Xml 解码.</P>
	 * @param xmlEscaped 表示需要进行xml解码的字符串
	 * @return String 表示经过解码以后的xml字符串
	 */
	public static String unescapeXml(String xmlEscaped) {
		return StringEscapeUtils.unescapeXml(xmlEscaped);
	}

	/**
	 * <P>URL 编码, Encode默认为UTF-8. </P>
	 * @param part 表示需要进行编码的url字符串
	 * @return String 表示经过了编码的字符串
	 */
	public static String urlEncode(String part) {
		try {
			return URLEncoder.encode(part, DEFAULT_URL_ENCODING);
		} catch (UnsupportedEncodingException e) {
			throw Exceptions.unchecked(e);
		}
	}

	/**
	 * <P>URL 解码, Encode默认为UTF-8. </P>
	 * @param part 表示需要进行解码的url字符串
	 * @return String 表示经过了解码的字符串
	 */
	public static String urlDecode(String part) {

		try {
			return URLDecoder.decode(part, DEFAULT_URL_ENCODING);
		} catch (UnsupportedEncodingException e) {
			throw Exceptions.unchecked(e);
		}
	}
}
