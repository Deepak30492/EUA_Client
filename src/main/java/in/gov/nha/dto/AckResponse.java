package in.gov.nha.dto;

import lombok.Data;

@Data
public class AckResponse {
    private MessageResponse message;
    private Error error;
}
