package com.abdm.eua.dhp.schema.ack;

import com.abdm.eua.dhp.schema.init.Context;
import lombok.Data;

import java.util.Optional;

@Data
public class AckResponse{
    private Message message;
    private Error error;
 }
