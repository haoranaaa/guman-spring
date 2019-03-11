package org.guman.beans.annotation;

import java.lang.annotation.*;

/**
 * @author duanhaoran
 * @since 2019/3/10 2:30 PM
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Component {

    String value() default "";
}
