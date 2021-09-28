package br.com.teddy.store.strategy;

import br.com.teddy.store.domain.EnumOperation;

import java.lang.annotation.*;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface StrategyAnnotation {
    Class target() default void.class;

    EnumOperation operation();
}
