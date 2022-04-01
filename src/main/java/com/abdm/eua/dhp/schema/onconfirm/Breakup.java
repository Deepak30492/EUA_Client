package com.abdm.eua.dhp.schema.onconfirm;
import com.fasterxml.jackson.annotation.JsonProperty; 
public class Breakup{
    @JsonProperty("./dhp-0_7_1.consultation") 
    public String dhp071Consultation;
    @JsonProperty("./dhp-0_7_1.phr_handling_fees") 
    public String dhp071PhrHandlingFees;
    @JsonProperty("./ind-gstin.cgst") 
    public String indGstinCgst;
    @JsonProperty("./ind-gstin.sgst") 
    public String indGstinSgst;
}
