package in.gov.nha.dto;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import in.gov.nha.beans.Context;
import in.gov.nha.beans.Message;
import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class EuaRequestBody {
    private Context context;
    private Message message;

}
