package com.philips.onespace.util;

import java.lang.reflect.InvocationTargetException;

public class EnumUtil {

    public static String getEnumNameByValue(Class<? extends Enum<?>> enumClass, Object value) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        for (Enum<?> enumConstant : enumClass.getEnumConstants()) {
            Object enumValue = enumClass.getMethod("getValue").invoke(enumConstant);
            if (enumValue.equals(value)) {
                return enumConstant.name();
            }
        }
        return null;
    }
}
