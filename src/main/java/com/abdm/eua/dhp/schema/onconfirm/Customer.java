package com.abdm.eua.dhp.schema.onconfirm;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Customer{
    @JsonProperty("./nha.health_id") 
    public String nhaHealthId;
    @JsonProperty("./nha.phr_address") 
    public String nhaPhrAddress;
}
