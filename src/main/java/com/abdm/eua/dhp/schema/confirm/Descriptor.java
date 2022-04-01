package com.abdm.eua.dhp.schema.confirm; 
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;
public class Descriptor{
    public String name;
    public String code;
    public String symbol;
    public String short_desc;
    public String long_desc;
    public ArrayList<String> images;
    public String audio;
    @JsonProperty("3d_render") 
    public String _3d_render;
}
