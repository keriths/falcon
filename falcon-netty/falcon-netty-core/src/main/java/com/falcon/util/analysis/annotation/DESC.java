package com.falcon.util.analysis.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by fanshuai on 18/10/29.
 */
@Target({ ElementType.TYPE,ElementType.METHOD,ElementType.FIELD,ElementType.PARAMETER })
@Retention(RetentionPolicy.RUNTIME)
public @interface DESC {
    String value();
}
