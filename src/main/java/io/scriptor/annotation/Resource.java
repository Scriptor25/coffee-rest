package io.scriptor.annotation;

import io.scriptor.http.HTTPMethod;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Resource {

    String path();

    HTTPMethod method() default HTTPMethod.GET;

    String accept() default "*/*";

    String result() default "*/*";
}
