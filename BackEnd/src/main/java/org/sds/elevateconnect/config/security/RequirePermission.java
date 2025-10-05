package org.sds.elevateconnect.config.security;

import org.sds.elevateconnect.model.auth.Permission;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface RequirePermission {
    Permission[] value();
    boolean requireAll() default false; // true = ALL permissions required, false = ANY permission required
}