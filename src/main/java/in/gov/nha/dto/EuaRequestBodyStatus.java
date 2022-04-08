package in.gov.nha.dto;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import in.gov.nha.beans.Context;
import in.gov.nha.beans.OnMessage;
import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class EuaRequestBodyStatus {
    private Context context;
    private OnMessage message;
    @JsonProperty("client_id")
    private String clientId;
}
