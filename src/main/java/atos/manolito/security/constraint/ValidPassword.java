package atos.manolito.security.constraint;

import javax.validation.Payload;
import javax.validation.Constraint;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import static java.lang.annotation.ElementType.ANNOTATION_TYPE;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Documented
@Constraint(validatedBy = PasswordConstraintValidator.class)
@Target({ TYPE, FIELD, ANNOTATION_TYPE, METHOD })
@Retention(RUNTIME)
public @interface ValidPassword {
	String message() default "{atos.manolito.security.constraint.ValidPassword.message}";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
