package atos.manolito.auth;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.TokenEnhancerChain;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;

import atos.manolito.Constants;

@Configuration
@EnableAuthorizationServer
public class AuthorizationServerConfig extends AuthorizationServerConfigurerAdapter {
	
	// JMM ../10/2019
	
	@Autowired
	private BCryptPasswordEncoder passwordEncoder;
	
	@Autowired
	private InfoAdditionalToken infoAdditionalToken;

	@Autowired
	@Qualifier("authenticationManager")
	private AuthenticationManager authenticationManager;

	@Override
	public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {
		//Permisos de rutas de acceso
		security.tokenKeyAccess("permitAll()") //para permitir a cualquier usuario para autenticarse en el endpoint
		.checkTokenAccess("isAuthenticated()"); //para que solo puedan acceder los usuarios autenticados
	}

	@Override
	public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
		
		clients.inMemory().withClient("angularapp")
		.secret(passwordEncoder.encode("12345")) //Credenciales para enlazar con Angular (AuthService en Angular)
		.scopes("read","write")
		.authorizedGrantTypes("password","refresh_token") //refresh token para poder entrar sin tener que volver a iniciar sesion
		.accessTokenValiditySeconds(36000) //tiempo de caducidad del token en milisegundos
		.refreshTokenValiditySeconds(36000); // Bajar a 3600. 36000 es para pruebas
	}

	@Override
	public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
		
		TokenEnhancerChain tokenEnhancerChain = new TokenEnhancerChain();
		tokenEnhancerChain.setTokenEnhancers(Arrays.asList(infoAdditionalToken,accessTokenConverter()));
		
		endpoints.authenticationManager(authenticationManager)
		.tokenStore(tokenStore())
		.accessTokenConverter(accessTokenConverter()) //almacena datos de autentificacion apartir de los token
		.tokenEnhancer(tokenEnhancerChain);
	}

	@Bean
	public JwtTokenStore tokenStore() {
		return new JwtTokenStore(accessTokenConverter());
	}

	@Bean
	public JwtAccessTokenConverter accessTokenConverter() {
		JwtAccessTokenConverter jwtAccessTokenConverter = new JwtAccessTokenConverter();
		//jwtAccessTokenConverter.setSigningKey(Constants.SECRET_KEY);
		//Para firmar la privada y para validar la publica,para confirmar que sean tokens autenticos
		//Se generan apartir del programa OPENSSL(video 14 jwt backend)
		jwtAccessTokenConverter.setSigningKey(Constants.RSA_PRIVATE);
		jwtAccessTokenConverter.setVerifierKey(Constants.RSA_PUBLIC);
		return jwtAccessTokenConverter;
	}
	
	
	
}
