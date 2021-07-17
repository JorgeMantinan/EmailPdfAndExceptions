package atos.manolito.services;

import atos.manolito.entity.UserData;

public interface IMailService {
	public void send(String from, String to, String subject, String text) throws Exception;

	void sendMail(UserData userData, String msgMailSent) throws Exception;
}
