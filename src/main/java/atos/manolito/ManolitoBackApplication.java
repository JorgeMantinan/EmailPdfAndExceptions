package atos.manolito;

import java.util.Locale;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;


@SpringBootApplication
//@EnableConfigurationProperties(MyProperties.class) // Spring 2.2
public class ManolitoBackApplication implements CommandLineRunner {
	
	//Para los usuarios de prueba(tambien CommandLineRunner)
	public static void main(String[] args) {
		SpringApplication.run(ManolitoBackApplication.class, args);
		//SpringApplication.run(ManolitoBackApplication.class, "--debug");
	
	}

	//Para ejecutar algo antes de la aplicacion. En este caso los usuarios de prueba
	@Override
	public void run(String... args) throws Exception {

	}
	
	@Bean
	public SessionLocaleResolver localeResolver() {
		SessionLocaleResolver slr = new SessionLocaleResolver();
		slr.setDefaultLocale(new Locale("es_ES"));
		return slr;
	}
	
	@Bean
	public ReloadableResourceBundleMessageSource messageSource() {
		ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
		messageSource.setBasename("classpath:messages");
		messageSource.setDefaultEncoding("UTF-8");
		return messageSource;
	}

}
