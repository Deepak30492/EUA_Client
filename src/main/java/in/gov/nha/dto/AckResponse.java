package in.gov.nha.dto;

import lombok.Data;

@Data
public class AckResponse{
    private Message message;
    private Error error;
 }
