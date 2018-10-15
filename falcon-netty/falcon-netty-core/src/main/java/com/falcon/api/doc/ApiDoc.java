package com.falcon.api.doc;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by fanshuai on 17/10/22.
 */
@Target({ ElementType.PARAMETER,ElementType.METHOD,ElementType.FIELD,ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
public @interface ApiDoc {
    /**
     * 参数说明
     * @return
     */
    String desc();
}
