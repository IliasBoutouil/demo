package me.ilias.validation;

import javax.validation.Constraint;
import javax.validation.Payload;

import static java.lang.annotation.ElementType.*;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import static java.lang.annotation.RetentionPolicy.*;
import java.lang.annotation.Target;

@Target({FIELD})
@Constraint(validatedBy = ContentValidator.class)
@Documented
@Retention(RUNTIME)
public @interface SafeContent {
    String message() default "invalid content, please use safe words";

    Class<?>[] groups() default { };

    Class<? extends Payload>[] payload() default { };
}
