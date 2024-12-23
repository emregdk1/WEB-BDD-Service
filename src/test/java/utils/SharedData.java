package utils;

import org.apache.log4j.Logger;

import java.util.HashMap;
import java.util.Map;

public class SharedData {
    private static final Logger logger = Logger.getLogger(SharedData.class);
    private static final Map<String, Object> dataMap = new HashMap<>();

    public static void saveData(String key, Object value) {
        dataMap.put(key, value);
        logger.info("Data saved - Key: " + key + ", Value: " + value);
    }

    public static Object getData(String key) {
        Object value = dataMap.get(key);
        if (value != null) {
            logger.info("Data retrieved - Key: " + key + ", Value: " + value);
        } else {
            logger.warn("No data found for Key: " + key);
        }
        return value;
    }

    public static <T> T getData(Class<T> type, String key) {
        Object value = dataMap.get(key);
        if (value != null) {
            logger.info("Attempting to cast data - Key: " + key + ", Value: " + value + ", Target Type: " + type.getName());
            if (type.isInstance(value)) {
                logger.info("Successfully cast data - Key: " + key + ", Value: " + value);
                return type.cast(value);
            } else {
                String errorMessage = "Cannot cast " + value.getClass().getName() + " to " + type.getName();
                logger.error(errorMessage);
                throw new ClassCastException(errorMessage);
            }
        } else {
            logger.warn("No data found for Key: " + key);
            return null;
        }
    }
}

