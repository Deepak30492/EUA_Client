package com.abdm.eua.dhp.schema;

import com.dhp.sdk.beans.Context;
import com.dhp.sdk.beans.OnMessage;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
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
