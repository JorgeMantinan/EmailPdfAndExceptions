package atos.manolito.security.constraint;

import javax.validation.Payload;
import javax.validation.Constraint;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Documented
@Constraint(validatedBy = NifConstraintValidator.class)
@Target({ FIELD})
//@Target({ FIELD, TYPE, ANNOTATION_TYPE })
@Retention(RUNTIME)
public @interface ValidNif {
	String message() default "{atos.manolito.security.constraint.ValidNif.message}";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
    // FGS. Para permitir que el dato sea opcional.
    boolean optional();
}