package com.falcon.util.analysis.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by fanshuai on 18/10/29.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.TYPE})
public @interface ServiceID {
    String value();
}
