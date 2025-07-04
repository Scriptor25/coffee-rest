package io.scriptor.annotation;

import io.scriptor.http.HTTPMethod;
import org.jetbrains.annotations.NotNull;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Resource {

    @NotNull String path();

    @NotNull HTTPMethod method() default HTTPMethod.GET;

    @NotNull String accept() default "*/*";

    @NotNull String result() default "*/*";
}
