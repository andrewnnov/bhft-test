package com.bhft.todo.generators;

import java.lang.reflect.Field;
import java.util.Random;

public class TestDataGenerator {
    private static final Random RANDOM = new Random();

    public static <T> T generateTestData(Class<T> clazz) {
        try {
            T instance = clazz.getDeclaredConstructor().newInstance();

            Field[] fields = clazz.getDeclaredFields();
            for (Field field : fields) {
                field.setAccessible(true);

                if (field.getType() == long.class || field.getType() == Long.class) {
                    field.set(instance, Math.abs(RANDOM.nextLong()));
                } else if (field.getType() == String.class) {
                    field.set(instance, generateRandomString(10));
                } else if (field.getType() == boolean.class || field.getType() == Boolean.class) {
                    field.set(instance, RANDOM.nextBoolean());
                }
            }

            return instance;
        } catch (Exception e) {
            throw new RuntimeException("Failed to generate test data for class: " + clazz.getName(), e);
        }
    }

    private static String generateRandomString(int length) {
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < length; i++) {
            builder.append(characters.charAt(RANDOM.nextInt(characters.length())));
        }
        return builder.toString();
    }
}
