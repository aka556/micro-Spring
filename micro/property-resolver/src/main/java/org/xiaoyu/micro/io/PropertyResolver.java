package org.xiaoyu.micro.io;

import jakarta.annotation.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.*;
import java.util.*;
import java.util.function.Function;


public class PropertyResolver {
    Logger logger = LoggerFactory.getLogger(getClass());

    Map<String, String> properties = new HashMap<String, String>();
    Map<Class<?>, Function<String, Object>> converters = new HashMap<Class<?>, Function<String, Object>>();

    public PropertyResolver(Properties props) {
        this.properties.putAll(System.getenv());
        Set<String> names = props.stringPropertyNames();
        for (String name : names) {
            this.properties.put(name, props.getProperty(name));
        }

        if (logger.isDebugEnabled()) {
            List<String> keys = new ArrayList<String>(this.properties.keySet());
            Collections.sort(keys);
            for (String key : keys) {
                logger.debug("PropertyResolver: {}={}", key, this.properties.get(key));
            }
        }

        // Add default converters
        converters.put(String.class, s -> s);
        converters.put(boolean.class, Boolean::parseBoolean);
        converters.put(Boolean.class, Boolean::parseBoolean);

        converters.put(byte.class, Byte::parseByte);
        converters.put(Byte.class, Byte::parseByte);

        converters.put(short.class, Short::parseShort);
        converters.put(Short.class, Short::parseShort);

        converters.put(int.class, Integer::parseInt);
        converters.put(Integer.class, Integer::parseInt);

        converters.put(long.class, Long::parseLong);
        converters.put(Long.class, Long::parseLong);

        converters.put(float.class, Float::parseFloat);
        converters.put(Float.class, Float::parseFloat);

        converters.put(double.class, Double::parseDouble);
        converters.put(Double.class, Double::parseDouble);

        converters.put(LocalDate.class, LocalDate::parse);
        converters.put(LocalTime.class, LocalTime::parse);
        converters.put(LocalDateTime.class, LocalDateTime::parse);
        converters.put(Duration.class, Duration::parse);
        converters.put(ZonedDateTime.class, ZonedDateTime::parse);
        converters.put(ZoneId.class, ZoneId::of);
    }

    public boolean containsKey(String key) {
        return this.properties.containsKey(key);
    }

    @Nullable
    public String getProperty(String key) {
        PropertyExpr keyExpr = parsePropertyExpr(key);
        if (keyExpr != null) {
            if (keyExpr.defValue != null) {
                return getProperty(keyExpr.key, keyExpr.defValue);
            } else {
                return getRequiredProperty(keyExpr.key);
            }
        }
        String value = this.properties.get(key);
        if (value != null) {
            return parseValue(value);
        }

        return value;
    }

    public String getProperty(String key, String defValue) {
        String value = getProperty(key);
        return value == null ? parseValue(defValue) : value;
    }

    @Nullable
    public <T> T getProperty(String key, Class<T> targetType) {
        String value = getProperty(key);
        if (value == null) {
            return null;
        }
        return convert(targetType, value);
    }

    public <T> T getProperty(String key, Class<T> targetType, T defaultValue) {
        String value = getProperty(key);
        if (value == null) {
            return defaultValue;
        }
        return convert(targetType, value);
    }

    public String getRequiredProperty(String key) {
        String value = getProperty(key);
        return Objects.requireNonNull(value, "Property '" + key + "' not found");
    }

    public <T> T getRequiredProperty(String key, Class<T> targetType) {
        T value = getProperty(key, targetType);
        return Objects.requireNonNull(value, "Property '" + key + "' not found");
    }

    @SuppressWarnings("unchecked")
    <T> T convert(Class<T> clazz, String value) {
        Function<String, Object> converter = this.converters.get(clazz);
        if (converter == null) {
            throw new IllegalArgumentException("Unsupported property type: " + clazz.getName());
        }
        return (T) converter.apply(value);
    }

    String parseValue(String value) {
        PropertyExpr expr = parsePropertyExpr(value);
        if (expr == null) {
            return value;
        }
        if (expr.defValue != null) {
            return getProperty(expr.key(), expr.defValue);
        } else {
            return getRequiredProperty(expr.key());
        }
    }

    PropertyExpr parsePropertyExpr(String key) {
        if (key.startsWith("${") && key.endsWith("}")) {
            int n = key.indexOf(':');
            if (n == -1) {
                // No default value
                String k = notEmpty(key.substring(2, key.length() - 1));
                return new PropertyExpr(k, null);
            } else {
                String k = notEmpty(key.substring(2, n));
                String d = key.substring(n + 1, key.length() - 1);
                return new PropertyExpr(k, d);
            }
        }
        return null;
    }

    String notEmpty(String s) {
        if (s.isEmpty()) {
            throw new IllegalArgumentException("Invalid property key: " + s);
        }
        return s;
    }

    record PropertyExpr(String key, String defValue) {

    }
}
