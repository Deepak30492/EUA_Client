package in.gov.nha.repository;

import in.gov.nha.model.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface Repository extends JpaRepository<Message, Long> {

    Optional<Message> findByMessageIdAndDhpQueryType(String messageId, String DhpQueryType);

    List<Optional<Message>> findByDhpQueryTypeAndConsumerId(String dhp_query_type, String clientId);
}
