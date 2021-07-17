package atos.manolito.utils;
import java.util.List;

import org.springframework.stereotype.Component;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@Component
@JsonIgnoreProperties(ignoreUnknown = true)
public class Response {

    @JsonProperty("update_date")
    private String updateDate;

    @JsonProperty("size")
    private String size;

    @JsonProperty("data")
    private List<ClassResponse> dataJson;

    public String getUpdateDate() {
	return this.updateDate;
    }

    /**
     * 
     * COMENTARIOS:
     * <hr>
     *
     * <hr>
     * 
     * @return
     */
    public String getSize() {
	return this.size;
    }

    public List<ClassResponse> getDataJson() {
	return this.dataJson;
    }
} 


