package atos.manolito.auth;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;
import org.springframework.stereotype.Component;

import atos.manolito.entity.UserData;
import atos.manolito.services.IUserDataService;


@Component
public class InfoAdditionalToken implements TokenEnhancer{
	
	@Autowired
	private IUserDataService userDataServiceImpl;

	@Override
	public OAuth2AccessToken enhance(OAuth2AccessToken accessToken, OAuth2Authentication authentication) {
		
		UserData userData = userDataServiceImpl.findByDasId(authentication.getName());
		Map<String, Object> info = new HashMap<>();
		
		info.put("id", userData.getId());
		info.put("attemptsNum", userData.getLogin().getAttemptsNum());
		info.put("state", userData.getState());
		
		((DefaultOAuth2AccessToken) accessToken).setAdditionalInformation(info);
		
		return accessToken;
	}
}
