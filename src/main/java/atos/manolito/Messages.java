package atos.manolito;

import java.util.Locale;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

/**
 * Esta clase busca en src/main/resources el archivo messages_*.properties.
 * Dependiendo del idioma del usuario, se le mostrarán los mensajes de un archivo properties u otro.
 * 
 * @author JMM
 * @since 26/11/19
 */
@Component
public class Messages {
	
	@Autowired
    private ReloadableResourceBundleMessageSource messageSource;
    //private MessageSourceAccessor accessor;

    @PostConstruct
    private void init() {
    }

    public String get(String code,@Nullable Object[] args) {
    	Locale locale = LocaleContextHolder.getLocale();
        return messageSource.getMessage(code, args,locale);
    }

}
