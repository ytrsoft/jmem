package com.ytrsoft.ui.table;

import com.ytrsoft.utils.Transform;

import java.lang.reflect.Field;

public final class FormatterUtils {

    private FormatterUtils() {
        throw new UnsupportedOperationException();
    }

    public static String get(Field field, Object rowData) throws Exception {
        Object value = field.get(rowData);
        if (field.isAnnotationPresent(Formatter.class)) {
            Formatter formatter = field.getAnnotation(Formatter.class);
            Class<? extends Transform> transformClass = formatter.value();
            Transform transform = transformClass.getDeclaredConstructor().newInstance();
            return transform.transform(value);
        }
        return String.valueOf(value);
    }


}
