package in.gov.nha.dto;

import lombok.Data;
import reactor.core.Disposable;

@Data
public class AckResponse implements Disposable {
    private Message message;
    private Error error;

    @Override
    public void dispose() {
        
    }

    @Override
    public boolean isDisposed() {
        return Disposable.super.isDisposed();
    }
}
