package com.abdm.eua.dhp.schema.onselect;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Breakup{
    @JsonProperty("./dhp-0_7_1.consultation") 
    public String dhp071Consultation;
    @JsonProperty("./dhp-0_7_1.cgst") 
    public String dhp071Cgst;
    @JsonProperty("./dhp-0_7_1.sgst")
    public String dhp071Sgst;
}
