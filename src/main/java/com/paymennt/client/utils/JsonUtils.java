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
import lombok.extern.log4j.Log4j2;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Utility class for JSON-related operations using Jackson ObjectMapper.
 * This class provides methods for encoding, decoding, and validating JSON objects.
 * It also configures the default ObjectMapper with specific features.
 *
 * @author Ankur
 */

@Log4j2
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
        log.debug("JsonUtils initialized");
    }

    private JsonUtils() {
    }

    /**
     * Returns the configured ObjectMapper instance.
     *
     * @return The ObjectMapper instance.
     */
    public static ObjectMapper getObjectMapper() {
        return JsonUtils.OBJECT_MAPPER;
    }

    /**
     * Checks if an object can be encoded and decoded as JSON.
     *
     * @param object The object to check.
     * @return True if encoding and decoding is possible, otherwise false.
     */
    public static boolean canEncodeDecode(Object object) {
        Class<?> objectClass = object.getClass();
        log.debug("Checking if object can be encoded and decoded: {}", objectClass.getSimpleName());

        if (JsonUtils.ENCODE_DECODE_MAP.containsKey(objectClass))
            return JsonUtils.ENCODE_DECODE_MAP.get(objectClass);
        JavaType objectType = TypeFactory.defaultInstance().constructType(objectClass);
        boolean canEncodeDecode =
                JsonUtils.OBJECT_MAPPER.canSerialize(objectClass) && JsonUtils.OBJECT_MAPPER.canDeserialize(objectType);
        JsonUtils.ENCODE_DECODE_MAP.put(objectClass, canEncodeDecode);
        return canEncodeDecode;
    }

    /**
     * Encodes an object to JSON string.
     *
     * @param object The object to encode.
     * @return The JSON string representation of the object.
     * @throws PaymenntClientException If encoding fails.
     */
    public static String encode(Object object) throws PaymenntClientException {
        try {
            log.debug("Encoding object to JSON: {}", object.getClass().getSimpleName());
            return JsonUtils.OBJECT_MAPPER.writeValueAsString(object);
        } catch (Exception e) {
            throw new PaymenntClientException("Failed to encode object to JSON: " + object.getClass().getSimpleName(), e);
        }
    }

    /**
     * Encodes an object to a pretty-printed JSON string.
     *
     * @param object The object to encode.
     * @return The pretty-printed JSON string representation of the object.
     * @throws PaymenntClientException If encoding fails.
     */
    public static String encodePretty(Object object) throws PaymenntClientException {
        try {
            log.debug("Encoding object to pretty-printed JSON: {}", object.getClass().getSimpleName());
            JsonUtils.OBJECT_MAPPER.enable(SerializationFeature.INDENT_OUTPUT);
            return JsonUtils.OBJECT_MAPPER.writeValueAsString(object);
        } catch (Exception e) {
            throw new PaymenntClientException("Failed to encode object to pretty-printed JSON: " + object.getClass().getSimpleName(), e);
        } finally {
            JsonUtils.OBJECT_MAPPER.disable(SerializationFeature.INDENT_OUTPUT);
        }
    }

    /**
     * Decodes an InputStream to a map of key-value pairs.
     *
     * @param inputStream The InputStream to decode.
     * @return The decoded map.
     * @throws PaymenntClientException If decoding fails.
     */
    public static Map<String, Object> decode(InputStream inputStream) throws PaymenntClientException {
        try {
            log.debug("Decoding input stream to map");
            TypeReference<HashMap<String, Object>> typeReference = new TypeReference<HashMap<String, Object>>() {};
            return JsonUtils.OBJECT_MAPPER.readValue(inputStream, typeReference);
        } catch (Exception e) {
            throw new PaymenntClientException("Failed to decode input stream to map", e);
        }
    }

    /**
     * Decodes an InputStream to an object of a specified class.
     *
     * @param inputStream The InputStream to decode.
     * @param toValueType The class to decode to.
     * @param <T> The type of the result.
     * @return The decoded object.
     * @throws PaymenntClientException If decoding fails.
     */
    public static <T> T decode(InputStream inputStream, Class<T> toValueType) throws PaymenntClientException {
        try {
            log.debug("Converting object to type: {}", toValueType.getSimpleName());
            return JsonUtils.OBJECT_MAPPER.readValue(inputStream, toValueType);
        } catch (Exception e) {
            throw new PaymenntClientException("Failed to convert object to type " + toValueType.getSimpleName(), e);
        }
    }

    /**
     * Decodes a JSON string to a map of key-value pairs.
     *
     * @param jsonString The JSON string to decode.
     * @return The decoded map.
     * @throws PaymenntClientException If decoding fails.
     */
    public static Map<String, Object> decode(String jsonString) throws PaymenntClientException {
        try {
            log.debug("Decoding JSON string to a map of key-value pairs");
            TypeReference<HashMap<String, Object>> typeReference = new TypeReference<HashMap<String, Object>>() {};
            return JsonUtils.OBJECT_MAPPER.readValue(jsonString, typeReference);
        } catch (Exception e) {
            throw new PaymenntClientException("Failed to decode JSON string to a map of key-value pairs", e);
        }
    }

    /**
     * Decodes a JSON string to an object of a specified class.
     *
     * @param jsonString The JSON string to decode.
     * @param toValueType The class to decode to.
     * @param <T> The type of the result.
     * @return The decoded object.
     * @throws PaymenntClientException If decoding fails.
     */
    public static <T> T decode(String jsonString, Class<T> toValueType) throws PaymenntClientException {
        try {
            log.debug("Decoding JSON string to an object of class {}", toValueType.getSimpleName());
            return JsonUtils.OBJECT_MAPPER.readValue(jsonString, toValueType);
        } catch (Exception e) {
            throw new PaymenntClientException("Failed to decode JSON string to an object of class " + toValueType.getSimpleName(), e);
        }
    }

    /**
     * Decodes a JSON string to an object of a specified JavaType.
     *
     * @param jsonString The JSON string to decode.
     * @param javaType The JavaType to decode to.
     * @param <T> The type of the result.
     * @return The decoded object.
     * @throws PaymenntClientException If decoding fails.
     */
    public static <T> T decode(String jsonString, JavaType javaType) throws PaymenntClientException {
        try {
            log.debug("Decoding JSON string to an object of type {}", javaType);
            return JsonUtils.OBJECT_MAPPER.readValue(jsonString, javaType);
        } catch (Exception e) {
            throw new PaymenntClientException("Failed to decode JSON string to an object of type "+ javaType, e);
        }
    }

    /**
     * Decodes a JSON string to an object using a TypeReference.
     *
     * @param jsonString The JSON string to decode.
     * @param typeReference The TypeReference describing the target type.
     * @param <T> The type of the result.
     * @return The decoded object.
     * @throws PaymenntClientException If decoding fails.
     */
    public static <T> T decode(String jsonString, TypeReference<T> typeReference) throws PaymenntClientException {
        try {
            log.debug("Decoding JSON string to an object of type {}", typeReference.getType());
            return JsonUtils.OBJECT_MAPPER.readValue(jsonString, typeReference);
        } catch (Exception e) {
            throw new PaymenntClientException("Failed to decode JSON string to an object of type "+ typeReference.getType(), e);
        }
    }

    /**
     * Decodes an object to another class using ObjectMapper's conversion.
     *
     * @param object The object to convert.
     * @param toValueType The class to convert to.
     * @param <T> The type of the result.
     * @return The converted object.
     * @throws PaymenntClientException If conversion fails.
     */
    public static <T> T decode(Object object, Class<T> toValueType) throws PaymenntClientException {
        try {
            log.debug("Converting object to an instance of class {}", toValueType.getSimpleName());
            return JsonUtils.OBJECT_MAPPER.convertValue(object, toValueType);
        } catch (Exception e) {
            throw new PaymenntClientException("Failed to converting object to an instance of class " + toValueType.getSimpleName(), e);
        }
    }

}