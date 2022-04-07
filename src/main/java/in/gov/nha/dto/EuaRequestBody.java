package in.gov.nha.dto;

import com.dhp.sdk.beans.Context;
import com.dhp.sdk.beans.Message;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class EuaRequestBody {
    private Context context;
    private Message message;
    @JsonProperty("client_id")
    private String clientId;

}
