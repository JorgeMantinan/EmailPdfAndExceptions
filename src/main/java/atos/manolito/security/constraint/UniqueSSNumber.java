package atos.manolito.security.constraint;

import static java.lang.annotation.ElementType.ANNOTATION_TYPE;
import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

/**
 * 
 * @author FGS
 * @since  12/11/2019
 * 
 *
 */
@Documented
@Constraint(validatedBy = UniqueSSNumberConstraintValidator.class)
@Target({ FIELD, TYPE, ANNOTATION_TYPE })
@Retention(RUNTIME)
public @interface UniqueSSNumber {
	String message() default "{atos.manolito.security.constraint.UniqueSSNumber.message}";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}