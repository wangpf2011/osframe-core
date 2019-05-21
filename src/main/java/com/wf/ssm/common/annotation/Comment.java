package com.wf.ssm.common.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * <P>entity层，字段注释说明 注解<br>
 *+实现entity字段注释说明的注解<br>
 *+使用： (name ="字段注释说明",dictType = "dict_name"，oid="User.id") </P>
 * @version 1.0
 * @author wangpf 2015-03-15 12:02:25
 * @since JDK 1.6
 */
@Target({ElementType.FIELD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface Comment{

    /**
     * 如果是字段备注，请设置备注名称
     */
    String name() default "";

    /**
     * 如果是字典类型，请设置字典的type值
     */
    String dictType() default "";

    /**
     * 如果是对象，请设置对象的关联id
     */
    String oid() default "";
}
