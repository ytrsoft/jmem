package com.ytrsoft.ui.table;

import com.ytrsoft.utils.Transform;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface Formatter {
    Class<? extends Transform> value();
}
