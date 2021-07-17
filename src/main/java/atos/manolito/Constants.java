package atos.manolito;

import java.math.BigInteger;

/**
 * Archivo para almacenar todas las constantes de la aplicación.
 * 
 * @author JMM
 * @since  29/11/2019
 * 
 */
public final class Constants {
	
	private Constants() {}
	
	// Constantes generales //
	
	public static final String ERROR = "errors";
	public static final String MESSAGE= "message";
	public static final String TIME= "Hora";
	
	
	// Validators //
	// IBAN
	public static final BigInteger IBANNUMBER_DIVISOR = new BigInteger("97");
	public static final BigInteger IBANNUMBER_ADDEND = new BigInteger("98");
	// IdCard
	public static String NIF_REGEX = "^([0-9]{8})([A-Z])$";
	public static String NIE_REGEX = "^([XYZ])([0-9]{7})([A-Z])$";
	public static String PASSPORT_REGEX = "^([A-Z]{3})([0-9]{6})$";
	// SSNumber
	public static final BigInteger SSNUMBER_DIVISOR = new BigInteger("97");
	
	// USERDATA STATE //
	// FGS 06/11/19. Antes llamabamos inactivo a lo que realmente era bloqueado.
	public static final int PEN_ACTIVATION = 1;
    public static final int ACTIVE = 2;
    public static final int LOCKED = 3;
    public static final int INACTIVE = 4;
	
	
	
	// JWT Config //
	//Sirve para asignar una key a jwt(asi es mas robusto que la autogenerada por la api)
	public static final String SECRET_KEY = "alguna.clave.secreta.12345678";
		
	//1er paso: Para generarla en el cmd poner => openssl genrsa -out jwt.pem
	//Para mostrar en el cmd la publica poner => openssl rsa -in jwt.pem -pubout
	public static final String RSA_PUBLIC = "-----BEGIN PUBLIC KEY-----\r\n" + 
			"	MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAs/Zs10Q25H20j3YIaPiu\r\n" + 
			"	D37rlmgcvFmBnDTO2/IncMmSK+1kXQzxTqmbzWWnyMcAL4e+9+TsZdHvM2EGvqBg\r\n" + 
			"	pSkAnXrpspfF4ow1HnNJnePTqRQfSB6enIijZ/FJN5daUYaEpIzSQhUaePsiDC04\r\n" + 
			"	uSCRau3tzIYfNjWCFHTrzU0BEb+cwY8rvq8CcvF4rkHQZpr520aZRsNRvcFe3jIw\r\n" + 
			"	8ANP4XAxYPaT7+Zi6Utx8wJf/UR3MCq1knmhXpf42qaKNKwt2MXm74GlRIdT4F2r\r\n" + 
			"	1gl8EerUghkV0J6Cg8hw4+824V4QoOXifhc72EQMjRMdksmMz0C5VGUPKUiFmZqb\r\n" + 
			"	LQIDAQAB\r\n" + 
			"	-----END PUBLIC KEY-----";
	//Para mostrar en el cmd la privada poner => openssl rsa -in jwt.pem
	public static final String RSA_PRIVATE = "-----BEGIN RSA PRIVATE KEY-----\r\n" + 
			"	MIIEpQIBAAKCAQEAs/Zs10Q25H20j3YIaPiuD37rlmgcvFmBnDTO2/IncMmSK+1k\r\n" + 
			"	XQzxTqmbzWWnyMcAL4e+9+TsZdHvM2EGvqBgpSkAnXrpspfF4ow1HnNJnePTqRQf\r\n" + 
			"	SB6enIijZ/FJN5daUYaEpIzSQhUaePsiDC04uSCRau3tzIYfNjWCFHTrzU0BEb+c\r\n" + 
			"	wY8rvq8CcvF4rkHQZpr520aZRsNRvcFe3jIw8ANP4XAxYPaT7+Zi6Utx8wJf/UR3\r\n" + 
			"	MCq1knmhXpf42qaKNKwt2MXm74GlRIdT4F2r1gl8EerUghkV0J6Cg8hw4+824V4Q\r\n" + 
			"	oOXifhc72EQMjRMdksmMz0C5VGUPKUiFmZqbLQIDAQABAoIBACKLVvErYu4RQyuW\r\n" + 
			"	mJ6tvZuz3T0N1xZAiCSX7m1B2lhIGQrdkrpZ1agn3oRa0w4zRNHa2Ml04/vXhHb0\r\n" + 
			"	VPsahfKuIDQad+mUQKPwDfI+Zw8rwGFgP3D0j7W0dMlrA4bsQqYLkEKV1XZh3qcC\r\n" + 
			"	bdKwi4bdvYwtB8yOokchlkqe9vR9VNnWk3h67vf1HJruETNT2ikGzaIAIbNynm+B\r\n" + 
			"	7JsdqLkdbDmVntkBFHfZojc7DTDYEwHX1Ro0qAquyWhJ6ASPzFeJumILPSG56JJf\r\n" + 
			"	vdhGcRoa5T6tJycDhg3UiBaEsPNKvOEeCygHw0sX3V/vO0em3ywVe5Wg2R1yfkHn\r\n" + 
			"	Bv0f3/ECgYEA6Ww4RyLmQvc1sv6giBV4lSPkvGUx9Z2yjG3fbZZZLnj0F2EUiwkI\r\n" + 
			"	UGGFb5HYHMcgpX1FzVEXzHOlz8lPKzBT970kk6ErtZ3LFt3yxMlXyKA15GtYrtGS\r\n" + 
			"	Pd8WBpcvruE9tXlY1v+sWnY/QpviDedEzO4x72Jb1yCqmV4ms5hhA4MCgYEAxV56\r\n" + 
			"	nAyUu8nAXDuwf+mDMrIgp+ozfuBpDOZh0W612TiAVo7POkYGPyRF4k/F8bQzDJe0\r\n" + 
			"	nKSsyl1ydq3hgHLkvxZMD4SSo6pLeEEhX/WkwB27gvF9mPJ8pZx/+sNkl4VDiCpX\r\n" + 
			"	g5RN5RfQo10snOj/bC1UTf98JgrK6KO1dcbNt48CgYEAphbC1JsYjH2qT/qt7yaX\r\n" + 
			"	7kbj8sluiv3ylcl4OVnJiy+1Pw1suKFssQUzFPgJdjCaIibJDBavgGKpkPgExQ5W\r\n" + 
			"	MOj8pa8AkvH8GAFFRJCfTbnxnt5i88Fa9XtPCNF6tEzAw+fcNt8lnBiXWqXga1Qr\r\n" + 
			"	uEWkcljiUGiF+yrrDs9V+FECgYEAxKPoBremLB7LT1mM9TQjKgw86u81FTDimR6j\r\n" + 
			"	0LS760sW2qZ4IrgHmzs5gUw3jCQcRGyVXwfWu9w0obeGF6Lg8t11NHTCYNvXS12g\r\n" + 
			"	semclmhJHSIbH5pgw+PTwnSQMUJt0SlWfdrxbNG4Zjr7qu2dJhtXhkqIV6KjHPuW\r\n" + 
			"	XFyRM1MCgYEAr/WLJsGp4hNF5RPWg0TpBIhw8pcJ/6tHdqgCfj2yk8N3Kb0vhB6R\r\n" + 
			"	iTKQgSondDIvWhKYZrxwASyZhBFMAzfXnFMkmtj3k05K1qW1Zbu5bivCfGUuWdnX\r\n" + 
			"	8VuKE1qgkP6mTUDXJIT7Ax20ohg+ncAbYVEfYoqkS+ZUbWPEuit+hPk=\r\n" + 
			"	-----END RSA PRIVATE KEY-----";

}
