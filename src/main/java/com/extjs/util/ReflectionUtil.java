package com.extjs.util;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.beans.BeansException;
import org.springframework.beans.MutablePropertyValues;
import org.springframework.beans.PropertyValue;


public class ReflectionUtil {

    /**
     * 获取指定对象的属性值
     *
     * @param source
     * @param property
     * @return 属性值
     */
    public static Object getProperty(Object source, String property) {
        BeanWrapper sourceBw = new BeanWrapperImpl(source);
        return sourceBw.getPropertyValue(property);
    }

    /**
     * 设置指定对象的属性
     *
     * @param source   对象
     * @param property 属性
     * @param value    新的属性值
     */
    public static void setProperty(Object source, String property, Object value) {
        BeanWrapper sourceBw = new BeanWrapperImpl(source);
        sourceBw.setPropertyValue(property, value);
    }

    /**
     * 把src中的内容Copy到指定target
     *
     * @param src    原对象
     * @param target 目标对象
     */
    public static void copyProperties(Object src, Object target) {
        copyProperties(src, target, null);
    }

    /**
     * 把src中的内容Copy到指定target，但是忽略指定的属性
     *
     * @param src
     * @param target
     * @param ignoreProps 指定忽略的属性
     */
    public static void copyProperties(Object source, Object target,
                                      String[] ignoreProperties) {
        if (source == null || target == null) {
            throw new IllegalArgumentException(
                    "Source and target must not be null");
        }
        List<String> ignoreList = (ignoreProperties != null) ? Arrays
                .asList(ignoreProperties) : null;
        BeanWrapper sourceBw = new BeanWrapperImpl(source);
        BeanWrapper targetBw = new BeanWrapperImpl(target);
        MutablePropertyValues values = new MutablePropertyValues();
        for (int i = 0; i < sourceBw.getPropertyDescriptors().length; i++) {
            PropertyDescriptor sourceDesc = sourceBw.getPropertyDescriptors()[i];
            String name = sourceDesc.getName();
            try {
                PropertyDescriptor targetDesc = targetBw
                        .getPropertyDescriptor(name);
                if (targetDesc.getWriteMethod() != null
                        && sourceDesc.getReadMethod() != null
                        && (ignoreProperties == null || (!ignoreList
                        .contains(name)))) {
                    values.addPropertyValue(new PropertyValue(name, sourceBw
                            .getPropertyValue(name)));
                }
            } catch (BeansException e) {

            }
        }
        targetBw.setPropertyValues(values);
    }

    public static void copyPropertiesNotNull(Object source, Object target) {
        if (source == null || target == null) {
            throw new IllegalArgumentException(
                    "Source and target must not be null");
        }

        BeanWrapper sourceBw = new BeanWrapperImpl(source);
        BeanWrapper targetBw = new BeanWrapperImpl(target);
        MutablePropertyValues values = new MutablePropertyValues();
        for (int i = 0; i < sourceBw.getPropertyDescriptors().length; i++) {
            PropertyDescriptor sourceDesc = sourceBw.getPropertyDescriptors()[i];
            String name = sourceDesc.getName();
            try {
                PropertyDescriptor targetDesc = targetBw
                        .getPropertyDescriptor(name);
                if (targetDesc.getWriteMethod() != null
                        && sourceDesc.getReadMethod() != null) {
                    if (sourceBw.getPropertyValue(name) != null)
                        values.addPropertyValue(new PropertyValue(name,
                                sourceBw.getPropertyValue(name)));
                }
            } catch (BeansException e) {

            }
        }
        targetBw.setPropertyValues(values);
    }

    /**
     * 通用toString方法,将一个对象的属性输出
     *
     * @param object
     * @return
     */
    public static String toString(Object obj) {
        StringBuffer sb = new StringBuffer();
        try {
            Class<?> c1 = obj.getClass();

            do {
                sb.append("[");
                Field[] fields = c1.getDeclaredFields();
                AccessibleObject.setAccessible(fields, true);
                for (int i = 0; i < fields.length; i++) {
                    Field f = fields[i];
                    sb.append(f.getName() + "=");
                    Object val = f.get(obj);
                    if (val != null)
                        sb.append(val.toString());

                    if (i < fields.length - 1)
                        sb.append(",");
                }
                sb.append("]");
                c1 = c1.getSuperclass();
            } while (c1 != Object.class);
        } catch (Exception e) {
            sb.append(e.toString() + "对象转换为字符串错误");
        }
        return sb.toString();
    }

    /**
     * Check if the given class represents a "simple" property, i.e. a
     * primitive, a String, a Class, or a corresponding array.
     */
    public static boolean isSimpleProperty(Class<?> clazz) {
        return clazz.isPrimitive() || isPrimitiveArray(clazz)
                || isPrimitiveWrapperArray(clazz) || clazz.equals(String.class)
                || clazz.equals(String[].class) || clazz.equals(Class.class)
                || clazz.equals(Class[].class);
    }

    /**
     * Check if the given class represents a primitive array, i.e. boolean,
     * byte, char, short, int, long, float, or double.
     */
    public static boolean isPrimitiveArray(Class<?> clazz) {
        return boolean[].class.equals(clazz) || byte[].class.equals(clazz)
                || char[].class.equals(clazz) || short[].class.equals(clazz)
                || int[].class.equals(clazz) || long[].class.equals(clazz)
                || float[].class.equals(clazz) || double[].class.equals(clazz);
    }

    /**
     * Check if the given class represents an array of primitive wrappers, i.e.
     * Boolean, Byte, Character, Short, Integer, Long, Float, or Double.
     */
    public static boolean isPrimitiveWrapperArray(Class<?> clazz) {
        return Boolean[].class.equals(clazz) || Byte[].class.equals(clazz)
                || Character[].class.equals(clazz)
                || Short[].class.equals(clazz) || Integer[].class.equals(clazz)
                || Long[].class.equals(clazz) || Float[].class.equals(clazz)
                || Double[].class.equals(clazz);
    }

    public static Object getFieldValue(Object obj, String fieldName) {
        Object result = null;
        Field field = ReflectionUtil.getField(obj, fieldName);
        if (field != null) {
            field.setAccessible(true);
            try {
                result = field.get(obj);
            } catch (IllegalArgumentException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        return result;
    }

    /**
     * 利用反射获取指定对象里面的指定属性
     *
     * @param obj       目标对象
     * @param fieldName 目标属性
     * @return 目标字段
     */
    private static Field getField(Object obj, String fieldName) {
        Field field = null;
        for (Class<?> clazz = obj.getClass(); clazz != Object.class; clazz = clazz
                .getSuperclass()) {
            try {
                field = clazz.getDeclaredField(fieldName);
                break;
            } catch (NoSuchFieldException e) {
                // 这里不用做处理，子类没有该字段可能对应的父类有，都没有就返回null。
            }
        }
        return field;
    }

    /**
     * 利用反射设置指定对象的指定属性为指定的值
     *
     * @param obj        目标对象
     * @param fieldName  目标属性
     * @param fieldValue 目标值
     */
    public static void setFieldValue(Object obj, String fieldName,
                                     String fieldValue) {
        Field field = ReflectionUtil.getField(obj, fieldName);
        if (field != null) {
            try {
                field.setAccessible(true);
                field.set(obj, fieldValue);
            } catch (IllegalArgumentException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

    /**
     * 实体类转化为Map通用方法--L.XM
     * @param bean
     * @return
     * @throws Exception
     */
    public static Map convertBeanToMap(Object bean) throws Exception {
        Class type = bean.getClass();
        Map resultMap = new HashMap();
        BeanInfo beanInfo = Introspector.getBeanInfo(type);
        PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
        for (PropertyDescriptor descriptor : propertyDescriptors) {
            String propertyName = descriptor.getName();
            if (!propertyName.equals("class")) {
                Method readMethod = descriptor.getReadMethod();
                Object reslut = readMethod.invoke(bean);
                if (null != reslut) {
                    resultMap.put(propertyName, reslut);
                } else {
                    resultMap.put(propertyName, "");
                }
            }
        }
        return resultMap;

    }

    /**
     * 转化Map为实体类
     * @param type
     * @param map
     * @return
     * @throws Exception
     */
    public static Object convertMapToBean(Class type,Map map) throws Exception{
        BeanInfo beanInfo=Introspector.getBeanInfo(type);
        Object obj=type.newInstance();
        PropertyDescriptor[] propertyDescriptors=beanInfo.getPropertyDescriptors();
        for (PropertyDescriptor descriptor:propertyDescriptors){
            String propertyName=descriptor.getName();
            if (map.containsKey(propertyName)){
                Object value=map.get(propertyName);
                descriptor.getWriteMethod().invoke(obj,value);
            }
        }
        return obj;
    }

}
