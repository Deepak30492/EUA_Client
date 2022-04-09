package in.gov.nha.repository;

import in.gov.nha.model.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface Repository extends JpaRepository<Message, Long> {

//    List<Optional<Message>> findByMessageIdAndDhpQueryType(String messageId, String DhpQueryType);

     List<Optional<Message>> findByMessageId(String messageId);
}
