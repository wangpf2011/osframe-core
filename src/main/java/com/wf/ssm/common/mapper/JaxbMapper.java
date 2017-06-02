package com.wf.ssm.common.mapper;

import java.io.StringReader;
import java.io.StringWriter;
import java.util.Collection;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlAnyElement;
import javax.xml.namespace.QName;

import org.springframework.http.converter.HttpMessageConversionException;
import org.springframework.util.Assert;

import com.wf.ssm.common.utils.Exceptions;
import com.wf.ssm.common.utils.Reflections;
import com.wf.ssm.common.utils.StringUtils;

/**
 * <p>使用Jaxb2.0实现XML<->Java Object的转换.</br>
 * 
 * 在创建时需要设定所有需要序列化的Root对象的Class.</br>
 * 特别支持Root对象是Collection的情形.</p>
 * 
 * @version 1.0 
 * @author 严娜  2015-03-11 15:00:00
 * @since JDK 1.6
 */
@SuppressWarnings("rawtypes")
public class JaxbMapper {

	private static ConcurrentMap<Class, JAXBContext> jaxbContexts = new ConcurrentHashMap<Class, JAXBContext>();

	/**
	 * <p>Java Object->Xml without encoding.</p>
	 */
	public static String toXml(Object root) {
		Class clazz = Reflections.getUserClass(root);
		return toXml(root, clazz, null);
	}

	/**
	 * <p>Java Object->Xml with encoding.<p>
	 */
	public static String toXml(Object root, String encoding) {
		Class clazz = Reflections.getUserClass(root);
		return toXml(root, clazz, encoding);
	}

	/**
	 * <p>Java Object->Xml with encoding.</p>
	 */
	public static String toXml(Object root, Class clazz, String encoding) {
		try {
			StringWriter writer = new StringWriter();
			createMarshaller(clazz, encoding).marshal(root, writer);
			return writer.toString();
		} catch (JAXBException e) {
			throw Exceptions.unchecked(e);
		}
	}

	/**
	 * <p>Java Collection->Xml without encoding, 特别支持Root Element是Collection的情形.</p>
	 */
	public static String toXml(Collection<?> root, String rootName, Class clazz) {
		return toXml(root, rootName, clazz, null);
	}

	/**
	 * <p>Java Collection->Xml with encoding, 特别支持Root Element是Collection的情形.</p>
	 */
	public static String toXml(Collection<?> root, String rootName, Class clazz, String encoding) {
		try {
			CollectionWrapper wrapper = new CollectionWrapper();
			wrapper.collection = root;

			JAXBElement<CollectionWrapper> wrapperElement = new JAXBElement<CollectionWrapper>(new QName(rootName),
					CollectionWrapper.class, wrapper);

			StringWriter writer = new StringWriter();
			createMarshaller(clazz, encoding).marshal(wrapperElement, writer);

			return writer.toString();
		} catch (JAXBException e) {
			throw Exceptions.unchecked(e);
		}
	}

	/**
	 *<p> Xml->Java Object.</p>
	 */
	@SuppressWarnings("unchecked")
	public static <T> T fromXml(String xml, Class<T> clazz) {
		try {
			StringReader reader = new StringReader(xml);
			return (T) createUnmarshaller(clazz).unmarshal(reader);
		} catch (JAXBException e) {
			throw Exceptions.unchecked(e);
		}
	}

	/**
	 * <p>创建Marshaller并设定encoding(可为null).</br>
	 * 线程不安全，需要每次创建或pooling。</p>
	 */
	public static Marshaller createMarshaller(Class clazz, String encoding) {
		try {
			JAXBContext jaxbContext = getJaxbContext(clazz);

			Marshaller marshaller = jaxbContext.createMarshaller();

			marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);

			if (StringUtils.isNotBlank(encoding)) {
				marshaller.setProperty(Marshaller.JAXB_ENCODING, encoding);
			}

			return marshaller;
		} catch (JAXBException e) {
			throw Exceptions.unchecked(e);
		}
	}

	/**
	 * <p>创建UnMarshaller.</br>
	 * 线程不安全，需要每次创建或pooling。</p>
	 */
	public static Unmarshaller createUnmarshaller(Class clazz) {
		try {
			JAXBContext jaxbContext = getJaxbContext(clazz);
			return jaxbContext.createUnmarshaller();
		} catch (JAXBException e) {
			throw Exceptions.unchecked(e);
		}
	}
	/**
	 * <p>获取JAXBContext实例.</p>
	 */
	protected static JAXBContext getJaxbContext(Class clazz) {
		Assert.notNull(clazz, "'clazz' must not be null");
		JAXBContext jaxbContext = jaxbContexts.get(clazz);
		if (jaxbContext == null) {
			try {
				jaxbContext = JAXBContext.newInstance(clazz, CollectionWrapper.class);
				jaxbContexts.putIfAbsent(clazz, jaxbContext);
			} catch (JAXBException ex) {
				throw new HttpMessageConversionException("Could not instantiate JAXBContext for class [" + clazz
						+ "]: " + ex.getMessage(), ex);
			}
		}
		return jaxbContext;
	}

	/**
	 * <p>封装Root Element 是 Collection的情况.</p>
	 */
	public static class CollectionWrapper {

		@XmlAnyElement
		protected Collection<?> collection;
	}
	
}
