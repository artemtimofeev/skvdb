package org.skvdb.annotation;

import org.skvdb.security.AuthorityType;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface Authorization {
    AuthorityType authorityType() default AuthorityType.OWNER;
    boolean anyAuthority() default false;
}
