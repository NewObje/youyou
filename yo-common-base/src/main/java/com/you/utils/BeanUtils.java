package com.you.utils;

import com.alibaba.fastjson.JSON;
import org.apache.commons.lang3.StringUtils;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.*;

/**
 * @author Michael Liu
 * @create 2020-02-29 23:00
 */
public class BeanUtils extends org.apache.commons.beanutils.BeanUtils {

    /** 对象中用于存储需要验证的字段集合的属性名 */
    public static final String VALIDATE_FIELD_SET_NAME = "validFieldsSet";


    // zx 2019-12-2 10:48:57
    static <T> T parseObject(Object obj, Class<T> type) {
        return JSON.parseObject(JSON.toJSONString(obj), type);
    }

    // zx 2019-12-2 10:49:02
    static <T> List<T> parseArray(Object obj, Class<T> type) {
        return JSON.parseArray(JSON.toJSONString(obj), type);
    }

    public static <T> T clone(Object obj, Class<T> type) {
        T result = null;
        try {
            result = type.newInstance();
            if (obj instanceof Map) {
                BeanUtils.populate(result, (Map<String, ? extends Object>) obj);
            } else {
                BeanUtils.copyProperties(result, obj);
            }
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        /*
         * Groovy类，使用属性拷贝后，在使用时会报错类型转换异常或'object is not an instance of declaring class'，
         * 若使用java类则不会产生上述错误，因为在metaClass中存储了source对象的类型，所以使用下一句代码将metaClass置为null，
         * 若后续因业务需要须保留metaClass，此处添加参数进行判断处理。
         */
        //result.metaClass = null;

        return result;
    }

    static <S, T> List<T> cloneList(List<S> sourceList, List<T> destinationList, Class<T> type) throws IllegalAccessException, InvocationTargetException, InstantiationException {
        for (S source : sourceList) {
            T destination = clone(source, type);
            destinationList.add(destination);
        }
        return destinationList;
    }

    /**
     * 将对象转换为Map（移除key：class，若为Groovy类，再移除key：metaClass）
     * @param obj 对象
     * @return Map<String, String>
     */
    static Map<String, String> convertToMap(Object obj) throws IllegalAccessException, NoSuchMethodException, InvocationTargetException {
        if (null != obj) {
            Map<String, String> resultMap = BeanUtils.describe(obj);
            resultMap.remove("class");
            // groovyClass
            resultMap.remove("metaClass");
            return resultMap;
        } else {
            return new HashMap<String, String>();
        }
    }

    /**
     * 获取UUID类型的字符串
     * @return
     */
    static String getUUIDStr() {
        return UUID.randomUUID().toString();
    }

    /**
     * 对传入的对象属性进行验证，验证指定属性不为null（String不为null）
     * 使用时请注意对象中虽然成员变量未赋值，但具有默认值，如boolean默认false，int默认0等，建议使用包装类型
     * 若attrList长度大于0，使用其中的字段名进行验证，否则使用对象中的VALIDATE_FIELD_LIST_NAME进行验证
     * 若对象中的VALIDATE_FIELD_SET_NAME为null或空，最终返回true
     * 若对象中的VALIDATE_FIELD_SET_NAME与对象中的实际字段不匹配，最终返回false
     * 若attrList中的字段名无法与对象中的字段名匹配，最终返回false
     * 若obj为null，最终返回false
     * 可优化：返回未验证通过的字段集合
     * @appointment 约定对象中需要验证的字段类型为java.Util.Set，且名称与VALIDATE_FIELD_LIST_NAME一致
     * @param obj 具体对象
     * @param stringCannotBeBlank 为true时，对象的String属性值不能为空白（notBlank）
     * @return 验证通过返回true，不通过返回false
     */
    static boolean validateObjectAttributeNotNull(Object obj, boolean stringCannotBeBlank, Set<String> attrList) {
        if (null == obj) {
            return false;
        }
        Class clz = obj.getClass(); //得到类对象
        Field[] fields = clz.getDeclaredFields(); // 得到属性集合
        Set<String> validateAttr = null;
        if (CollectionUtils.isEmptyCollection(attrList)) {
            try {
                Field field = clz.getDeclaredField(VALIDATE_FIELD_SET_NAME);
                field.setAccessible(true);
                validateAttr = (Set<String>) field.get(obj);
                if (CollectionUtils.isEmptyCollection(validateAttr)) {
                    return true;
                }
            } catch (NoSuchFieldException e) {
                // obj中未声明需要验证的字段集合，不进行验证，返回true
                return true;
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        } else {
            validateAttr = attrList;
        }
        int totalNum = validateAttr.size();
        int validNum = 0;
        String value;
        // 遍历进行处理
        for (Field f : fields) {
            f.setAccessible(true);
            if (validateAttr.contains(f.getName())) {
                validNum++;
                if ("String" == f.getType().getSimpleName()) {
                    // String进行额外处理
                    try {
                        value = (String) f.get(obj);
                        if (null == value) {
                            return false;
                        }
                        if (stringCannotBeBlank && StringUtils.isBlank(value)) {
                            return false;
                        }
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                } else {
                    try {
                        if (null == f.get(obj)) {
                            return false;
                        }
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        if (totalNum != validNum) {
            return false;
        }
        return true;
    }

    static PropertyDescriptor[] getPropertyDescriptors(Class aClass) {
        return org.springframework.beans.BeanUtils.getPropertyDescriptors(aClass);
    }
}
