package com.wf.ssm.common.mapper;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.JsonParser.Feature;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.util.JSONPObject;
import com.fasterxml.jackson.module.jaxb.JaxbAnnotationModule;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

/**
 * <p>简单封装Jackson，实现JSON String<->Java Object的转换</br>.
 * 
 * 封装不同的输出风格, 使用不同的builder函数创建实例.</p>
 * 
 * @version 1.0 
 * @author 严娜  2015-03-11 15:50:00
 * @since JDK 1.6
 */
public class JsonMapper extends ObjectMapper {

	private static final long serialVersionUID = 1L;

	private static Logger logger = LoggerFactory.getLogger(JsonMapper.class);

	private static JsonMapper mapper;

	public JsonMapper() {
		this(Include.NON_EMPTY);
	}
	/**
	 * 构造函数
	 * @param include 属性风格
	 * <p>Include.Include.ALWAYS 默认 </br>
	 * Include.NON_DEFAULT 属性为默认值不序列化 </br>
	 * Include.NON_EMPTY 属性为 空（“”）  或者为 NULL 都不序列化  </br>
	 * Include.NON_NULL 属性为NULL 不序列化  </p>
	 */
	public JsonMapper(Include include) {
		// 设置输出时包含属性的风格
		if (include != null) {
			this.setSerializationInclusion(include);
		}
		// 允许单引号、允许不带引号的字段名称
		this.enableSimple();
		// 设置输入时忽略在JSON字符串中存在但Java对象实际没有的属性
		this.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
		
        // 空值处理为空串
		this.getSerializerProvider().setNullValueSerializer(new JsonSerializer<Object>(){
			@Override
			public void serialize(Object value, JsonGenerator jgen,
					SerializerProvider provider) throws IOException,
					JsonProcessingException {
				jgen.writeString("");
			}
        });
	}

	/**
	 * <p>创建只输出非Null且非Empty(如List.isEmpty)的属性到Json字符串的Mapper,建议在外部接口中使用.</p>
	 */
	public static JsonMapper getInstance() {
		if (mapper == null){
			mapper = new JsonMapper().enableSimple();
		}
		mapper.configure(Feature.ALLOW_SINGLE_QUOTES, true);
		mapper.configure(Feature.ALLOW_SINGLE_QUOTES, true);
		mapper.configure(Feature.ALLOW_COMMENTS, true);
		mapper.configure(Feature.ALLOW_UNQUOTED_FIELD_NAMES, true);
		mapper.configure(Feature.ALLOW_SINGLE_QUOTES, true);
		mapper.configure(Feature.ALLOW_UNQUOTED_CONTROL_CHARS, true);
		return mapper;
	}

	/**
	 * <p>创建只输出初始值被改变的属性到Json字符串的Mapper, 最节约的存储方式，建议在内部接口中使用。</p>
	 */
	public static JsonMapper nonDefaultMapper() {
		if (mapper == null){
			mapper = new JsonMapper(Include.NON_DEFAULT);
		}
		return mapper;
	}
	
	/**
	 * <p>Object可以是POJO，也可以是Collection或数组。</br>
	 * 如果对象为Null, 返回"null".</br>
	 * 如果集合为空集合, 返回"[]".</p>
	 */
	public String toJson(Object object) {

		try {
			return this.writeValueAsString(object);
		} catch (IOException e) {
			logger.warn("write to json string error:" + object, e);
			return null;
		}
	}

	/**
	 * <p>反序列化POJO或简单Collection如List<String>.</br>
	 * 
	 * 如果JSON字符串为Null或"null"字符串, 返回Null.</br>
	 * 如果JSON字符串为"[]", 返回空集合.</br>
	 * 
	 * 如需反序列化复杂Collection如List&lt;MyBean>, 请使用fromJson(String,JavaType)</p>
	 * @param jsonString json串
	 * @param clazz 对象class
	 */
	public <T> T fromJson(String jsonString, Class<T> clazz) {
		if (StringUtils.isEmpty(jsonString)) {
			return null;
		}
		//替换html字符为单引号
//        Pattern p_space = Pattern.compile("\\s*|\\t|\\r|\\n", Pattern.CASE_INSENSITIVE);
//        Matcher m_space = p_space.matcher(jsonString);
//        jsonString= m_space.replaceAll(""); // 过滤空格回车标签
        //替换掉&符号，产生的word生成的bug
	    jsonString=jsonString.replaceAll("&quot;", "'");
		try {
			return this.readValue(jsonString, clazz);
		} catch (IOException e) {
			logger.warn("parse json string error:" + jsonString, e);
			return null;
		}
	}
	/**
	 * <P>判断字符串是否是符合json合法格式</P>
	 * @param revMsg 
	 */ 
	public boolean checkMsgIsJson(String jsonString) {
		boolean ret=true;//默认是josn
		
		if (StringUtils.isEmpty(jsonString)) {
			return false;
		}
		//替换html字符为单引号
//        Pattern p_space = Pattern.compile("\\s*|\\t|\\r|\\n", Pattern.CASE_INSENSITIVE);
//        Matcher m_space = p_space.matcher(jsonString);
//        jsonString= m_space.replaceAll(""); // 过滤空格回车标签
        //替换掉&符号，产生的word生成的bug
	    jsonString=jsonString.replaceAll("&quot;", "'");
		try {
			this.readValue(jsonString, Map.class);
		} catch (IOException e) {
			return false;
		}
		return  ret;
		
		
	}
	/**
	 * <p>反序列化复杂Collection如List&lt;Bean>, 先使用函數createCollectionType构造类型,然后调用本函数.</p>
	 * @see #createCollectionType(Class, Class...)
	 */
	@SuppressWarnings("unchecked")
	public <T> T fromJson(String jsonString, JavaType javaType) {
		if (StringUtils.isEmpty(jsonString)) {
			return null;
		}
        jsonString=jsonString.replaceAll("&quot;", "'");
        
		try {
			return (T) this.readValue(jsonString, javaType);
		} catch (IOException e) {
			logger.warn("parse json string error:" + jsonString, e);
			return null;
		}
	}

	/**
	 * <p>構造泛型的Collection Type如:</br>
	 * ArrayList&lt;MyBean>, 则调用constructCollectionType(ArrayList.class,MyBean.class)</br>
	 * HashMap&lt;String,MyBean>, 则调用(HashMap.class,String.class, MyBean.class)</p>
	 */
	public JavaType createCollectionType(Class<?> collectionClass, Class<?>... elementClasses) {
		return this.getTypeFactory().constructParametricType(collectionClass, elementClasses);
	}

	/**
	 * <p>當JSON裡只含有Bean的部分屬性時，更新一個已存在Bean，只覆蓋該部分的屬性.</p>
	 */
	@SuppressWarnings("unchecked")
	public <T> T update(String jsonString, T object) {
		try {
			return (T) this.readerForUpdating(object).readValue(jsonString);
		} catch (JsonProcessingException e) {
			logger.warn("update json string:" + jsonString + " to object:" + object + " error.", e);
		} catch (IOException e) {
			logger.warn("update json string:" + jsonString + " to object:" + object + " error.", e);
		}
		return null;
	}

	/**
	 * <p>輸出JSONP格式數據.</p>
	 */
	public String toJsonP(String functionName, Object object) {
		return toJson(new JSONPObject(functionName, object));
	}

	/**
	 * <p>設定是否使用Enum的toString函數來讀寫Enum,</br>
	 * 為False時時使用Enum的name()函數來讀寫Enum, 默認為False.</br>
	 * 注意本函數一定要在Mapper創建後, 所有的讀寫動作之前調用.</p>
	 */
	public JsonMapper enableEnumUseToString() {
		this.enable(SerializationFeature.WRITE_ENUMS_USING_TO_STRING);
		this.enable(DeserializationFeature.READ_ENUMS_USING_TO_STRING);
		return this;
	}

	/**
	 * <p>支持使用Jaxb的Annotation，使得POJO上的annotation不用与Jackson耦合。</br>
	 * 默认会先查找jaxb的annotation，如果找不到再找jackson的。</p>
	 */
	public JsonMapper enableJaxbAnnotation() {
		JaxbAnnotationModule module = new JaxbAnnotationModule();
		this.registerModule(module);
		return this;
	}

	/**
	 * <p>允许单引号,允许不带引号的字段名称</p>
	 */
	public JsonMapper enableSimple() {
		this.configure(Feature.ALLOW_SINGLE_QUOTES, true);
		this.configure(Feature.ALLOW_UNQUOTED_FIELD_NAMES, true);
		return this;
	}
	
	/**
	 * <p>取出Mapper做进一步的设置或使用其他序列化API.</p>
	 */
	public ObjectMapper getMapper() {
		return this;
	}
	
	/**
	 * <p>转换为JSON字符串</p>
	 * @param object
	 * @return String
	 */
	public static String toJsonString(Object object){
		return JsonMapper.getInstance().toJson(object);
	}
	
	/**
	 * 测试
	 */
	public static void main(String[] args) {
		List<Map<String, Object>> list = Lists.newArrayList();
		Map<String, Object> map = Maps.newHashMap();
		map.put("id", 1);
		map.put("pId", -1);
		map.put("name", "根节点");
		list.add(map);
		map = Maps.newHashMap();
		map.put("id", 2);
		map.put("pId", 1);
		map.put("name", "你好");
		map.put("open", true);
		list.add(map);
		String json = JsonMapper.getInstance().toJson(list);
	}
	
}
