package org.guman.beans.annotation;

import java.lang.annotation.*;

/**
 * 注解标识
 * @author duanhaoran
 * @since 2019/3/10 2:29 PM
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Component
public @interface Service {

    String value() default "";
}
