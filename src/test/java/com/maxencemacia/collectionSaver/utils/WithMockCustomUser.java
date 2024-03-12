package com.maxencemacia.collectionSaver.utils;

import org.springframework.security.test.context.support.WithSecurityContext;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
@WithSecurityContext(factory = WithMockCustomUserSecurityContextFactory.class)
public @interface WithMockCustomUser {
    long id() default 1L;
    String uuid() default "1";
    String username() default "user";
    String email() default "user@mail.com";
    String password() default "password";
    String[] authorities() default {"ROLE_USER"};
}
