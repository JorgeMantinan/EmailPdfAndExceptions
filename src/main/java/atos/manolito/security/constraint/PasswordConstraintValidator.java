package atos.manolito.security.constraint;

import org.passay.*;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class PasswordConstraintValidator implements ConstraintValidator<ValidPassword, String> {
	@Override
    public void initialize(ValidPassword arg0) {
    }

    @Override
    public boolean isValid(String password, ConstraintValidatorContext context) {
        // Creo el objeto que contendrá las reglas de validación.
    	PasswordValidator validator = new PasswordValidator(Arrays.asList(
            // Regla: Al menos 8 caracteres.
            new LengthRule(8,75),

            // Regla: Al menos una letra mayúscula
            new CharacterRule(EnglishCharacterData.UpperCase, 1),

            // Regla: Al menos una letra minúscula
            new CharacterRule(EnglishCharacterData.LowerCase, 1),

            // Regla:  Al menos un dígito numérico
            new CharacterRule(EnglishCharacterData.Digit, 1),

            // Regla: Al menos un carácter especial (símbolo)
            new CharacterRule(EnglishCharacterData.Special, 1),

            // Regla:  No se permiten espacios en blanco
            new WhitespaceRule()

        ));

        // Ejecuto la validación.
        RuleResult result = validator.validate(new PasswordData(password));
        
        if (result.isValid()) {
            return true;
        } 
        // Recopilo los errores de validación.
        List<String> messages = validator.getMessages(result);
        
        String messageTemplate = messages.stream()
            .collect(Collectors.joining(","));
        if (context != null) {
        	context.buildConstraintViolationWithTemplate(messageTemplate)
        		.addConstraintViolation()
        		.disableDefaultConstraintViolation();
        }

        return false;
    }
}
