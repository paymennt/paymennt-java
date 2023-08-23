/************************************************************************
 * Copyright PointCheckout Ltd.
 */

package com.paymennt.client.utils;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.fasterxml.jackson.datatype.joda.JodaModule;
import com.paymennt.client.exception.PaymenntClientException;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 *
 * @author Ankur
 */
public class JsonUtils {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
    private static final Map<Class<?>, Boolean> ENCODE_DECODE_MAP = new ConcurrentHashMap<Class<?>, Boolean>();

    static {
        ObjectMapper mapper = JsonUtils.OBJECT_MAPPER;
        mapper.configure(JsonParser.Feature.ALLOW_SINGLE_QUOTES, true);
        mapper.configure(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES, true);
        mapper.configure(SerializationFeature.WRITE_CHAR_ARRAYS_AS_JSON_ARRAYS, true);
        mapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        mapper.configure(MapperFeature.SORT_PROPERTIES_ALPHABETICALLY, true);
        mapper.configure(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true);
        mapper.registerModule(new JodaModule());
        SimpleFilterProvider filters = new SimpleFilterProvider();
        filters.setDefaultFilter(SimpleBeanPropertyFilter.serializeAllExcept(new String[] {}));
        filters.setFailOnUnknownId(false);
        mapper.setFilterProvider(filters);
    }

    private JsonUtils() {
    }

    /**
     * @return the mapper
     */
    public static ObjectMapper getObjectMapper() {
        return JsonUtils.OBJECT_MAPPER;
    }

    public static boolean canEncodeDecode(Object object) {
        Class<?> objectClass = object.getClass();
        if (JsonUtils.ENCODE_DECODE_MAP.containsKey(objectClass))
            return JsonUtils.ENCODE_DECODE_MAP.get(objectClass);
        JavaType objectType = TypeFactory.defaultInstance().constructType(objectClass);
        boolean canEncodeDecode =
                JsonUtils.OBJECT_MAPPER.canSerialize(objectClass) && JsonUtils.OBJECT_MAPPER.canDeserialize(objectType);
        JsonUtils.ENCODE_DECODE_MAP.put(objectClass, canEncodeDecode);
        return canEncodeDecode;
    }

    public static String encode(Object object) throws PaymenntClientException {
        try {
            return JsonUtils.OBJECT_MAPPER.writeValueAsString(object);
        } catch (Exception e) {
            throw new PaymenntClientException("Failed to encode object", e);
        }
    }

    public static String encodePretty(Object object) throws PaymenntClientException {
        try {
            JsonUtils.OBJECT_MAPPER.enable(SerializationFeature.INDENT_OUTPUT);
            return JsonUtils.OBJECT_MAPPER.writeValueAsString(object);
        } catch (Exception e) {
            throw new PaymenntClientException("Failed to encode object", e);
        } finally {
            JsonUtils.OBJECT_MAPPER.disable(SerializationFeature.INDENT_OUTPUT);
        }
    }

    public static Map<String, Object> decode(InputStream inputStream) throws PaymenntClientException {
        try {
            TypeReference<HashMap<String, Object>> typeReference = new TypeReference<HashMap<String, Object>>() {};
            return JsonUtils.OBJECT_MAPPER.readValue(inputStream, typeReference);
        } catch (Exception e) {
            throw new PaymenntClientException("Failed to decode inputstring", e);
        }
    }

    public static <T> T decode(InputStream inputStream, Class<T> toValueType) throws PaymenntClientException {
        try {
            return JsonUtils.OBJECT_MAPPER.readValue(inputStream, toValueType);
        } catch (Exception e) {
            throw new PaymenntClientException("Failed to decode inputstream", e);
        }
    }

    public static Map<String, Object> decode(String jsonString) throws PaymenntClientException {
        try {
            TypeReference<HashMap<String, Object>> typeReference = new TypeReference<HashMap<String, Object>>() {};
            return JsonUtils.OBJECT_MAPPER.readValue(jsonString, typeReference);
        } catch (Exception e) {
            throw new PaymenntClientException("Failed to decode input string", e);
        }
    }

    public static <T> T decode(String jsonString, Class<T> toValueType) throws PaymenntClientException {
        try {
            return JsonUtils.OBJECT_MAPPER.readValue(jsonString, toValueType);
        } catch (Exception e) {
            throw new PaymenntClientException("Failed to decode input string", e);
        }
    }

    public static <T> T decode(String jsonString, JavaType javaType) throws PaymenntClientException {
        try {
            return JsonUtils.OBJECT_MAPPER.readValue(jsonString, javaType);
        } catch (Exception e) {
            throw new PaymenntClientException("Failed to decode input string", e);
        }
    }

    public static <T> T decode(String jsonString, TypeReference<T> typeReference) throws PaymenntClientException {
        try {
            return JsonUtils.OBJECT_MAPPER.readValue(jsonString, typeReference);
        } catch (Exception e) {
            throw new PaymenntClientException("Failed to decode input string", e);
        }
    }

    public static <T> T decode(Object object, Class<T> toValueType) throws PaymenntClientException {
        try {
            return JsonUtils.OBJECT_MAPPER.convertValue(object, toValueType);
        } catch (Exception e) {
            throw new PaymenntClientException("Failed to decode input string", e);
        }
    }

}