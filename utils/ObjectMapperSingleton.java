package com;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import lombok.extern.slf4j.Slf4j;

import java.util.Objects;

/**
 * @author 2020-04-01 14:53
 **/
@Slf4j
public class ObjectMapperSingleton<T> {

    private volatile static ObjectMapper OBJECT_MAPPER = ObjectMapperSingleton.get(); //声明成 volatile

    private ObjectMapperSingleton() {
    }

    public static ObjectMapper get() {
        if (instance == null) {
            synchronized (ObjectMapperSingleton.class) {
                if (instance == null) {
                    instance = new ObjectMapper();
                    instance.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
                    instance.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
                    instance.configure(SerializationFeature.FAIL_ON_SELF_REFERENCES, false);
                    instance.configure(SerializationFeature.FAIL_ON_UNWRAPPED_TYPE_IDENTIFIERS,
                            false);
                }
            }
        }
        return instance;
    }

    public static JavaType getCollectionType(ObjectMapper mapper, Class<?> collectionClass,
                                             Class<?>... elementClasses) {
        return mapper.getTypeFactory().constructParametricType(collectionClass, elementClasses);
    }

    public static <T> String writeValueAsString(T t) {
        if (Objects.isNull(t)) {
            return null;
        }

        try {
            return ObjectMapperSingleton.get().writeValueAsString(t);
        } catch (JsonProcessingException e) {
            log.error("ObjectMapperSingleton writeValueAsString fail,{}", e);
        }
        return null;
    }

    /**
     * 将对象转换为json字符串
     */
    public static String obj2string(Object obj) {
        StringWriter sw = new StringWriter();
        try {
            OBJECT_MAPPER.writeValue(sw, obj);
        } catch (Exception e) {
        }
        return sw.toString();
    }

    /**
     * 将字符串转list对象
     */
    public static <T> List<T> str2list(String jsonStr, Class<T> cls) {
        List<T> objList = null;
        try {
            JavaType t = OBJECT_MAPPER.getTypeFactory().constructParametricType(
                    List.class, cls);
            objList = OBJECT_MAPPER.readValue(jsonStr, t);
        } catch (Exception e) {
            log.error("json转换错误： 将字符串转list对象");
        }
        return objList;
    }

    /**
     * 将字符串转为对象
     */
    public static <T> T str2obj(String jsonStr, Class<T> cls) {

        T obj = null;
        try {
            obj = OBJECT_MAPPER.readValue(jsonStr, cls);
        } catch (Exception e) {
            log.error("Json转换错误,将字符串转对象 str2obj error:{}", e.getMessage());
        }
        return obj;
    }

    /**
     * 将字符串转为Page对象
     */
    public static <T> Page<T> str2page(String jsonStr, Class<T> cls) {

        Page<T> objList = null;
        try {
            JavaType t = OBJECT_MAPPER.getTypeFactory().constructParametricType(
                    Page.class, cls);
            objList = OBJECT_MAPPER.readValue(jsonStr, t);
        } catch (Exception e) {
        }
        return objList;
    }

    /**
     * 将字符串转为json节点
     */
    public static JsonNode str2node(String jsonStr) {

        try {
            return OBJECT_MAPPER.readTree(jsonStr);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
