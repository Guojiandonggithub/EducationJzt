package org.springside.examples.bootapi.ToolUtils.common.util;

import java.lang.reflect.ParameterizedType;

public class GenericSuperClass {
	
	/**范类转换，转换对应的数据类型，用于底层方法的封装*/
	@SuppressWarnings("rawtypes")
	public static Class getGenericSuperClass(Class entity) {
		ParameterizedType parameterizedType = (ParameterizedType) entity.getGenericSuperclass();
		Class entityClass = (Class) parameterizedType.getActualTypeArguments()[0];
		return entityClass;
	}
}
