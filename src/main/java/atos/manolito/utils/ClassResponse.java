package atos.manolito.utils;

import org.springframework.stereotype.Component;

import com.fasterxml.jackson.annotation.JsonProperty;

@Component
public class ClassResponse {
    @JsonProperty("CMUM")
    private String cmum;

    @JsonProperty("CPOS")
    private String cpos;

    @JsonProperty("CCOM")
    private String ccom;

    @JsonProperty("COM")
    private String com;

    @JsonProperty("CPRO")
    private String cpro;

    @JsonProperty("PRO")
    private String pro;

    @JsonProperty("DMUN50")
    private String nomMun50;

    @JsonProperty("CUN")
    private String cun;

    @JsonProperty("NNUCLE50")
    private String nnucle50;
    
    @JsonProperty("NENTSI50")
    private String nentsi50;
    

    public String getNentsi50() {
		return nentsi50;
	}

	public String getCmum() {
	return this.cmum;
    }

    public String getCpos() {
	return this.cpos;
    }

    public String getCcom() {
	return this.ccom;
    }

    public String getCom() {
	return this.com;
    }

    public String getCpro() {
	return this.cpro;
    }

    public String getPro() {
	return this.pro;
    }

    public String getNomMun50() {
	return this.nomMun50;
    }

    public String getCun() {
	return this.cun;
    }

    public String getNnucle50() {
	return this.nnucle50;
    }
}