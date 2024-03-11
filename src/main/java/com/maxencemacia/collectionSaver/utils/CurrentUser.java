package com.maxencemacia.collectionSaver.utils;

import org.springframework.security.core.annotation.AuthenticationPrincipal;

import java.lang.annotation.*;

@Target({ElementType.PARAMETER, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@AuthenticationPrincipal(expression = "@userDetailsServiceImpl.getCurrentUser()")
public @interface CurrentUser {
}
