package io.github.tiscs.scp.annotations;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.core.annotation.AliasFor;
import org.springframework.stereotype.Component;

import java.lang.annotation.*;

@Documented
@Inherited
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD, ElementType.FIELD, ElementType.PARAMETER})
@Mapper
@Component
public @interface MapperComponent {
    @AliasFor(
            annotation = Component.class
    )
    String value() default "";
}
