package atos.manolito.security.constraint;

import javax.validation.Payload;
import javax.validation.Constraint;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Documented
@Constraint(validatedBy = IbanConstraintValidator.class)
@Target({ FIELD, METHOD})
//@Target({FIELD, TYPE, ANNOTATION_TYPE})
@Retention(RUNTIME)
public @interface ValidIban {
	String message() default "{atos.manolito.security.constraint.ValidIban.message}";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
    // FGS. Para permitir que el dato sea opcional.
    boolean optional();

}