package com.abdm.eua.dhp.schema.onstatus;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Customer{
    @JsonProperty("./dhp-0_7_1.health_id") 
    public String dhp071HealthId;
    @JsonProperty("./dhp-0_7_1.phr_address") 
    public String dhp071PhrAddress;
}
