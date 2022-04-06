package com.abdm.eua.message;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface Repository extends JpaRepository<Message, Long> {

    Optional<Message> findByMessageIdAndDhpQueryType(String messageId, String DhpQueryType);

    List<Optional<Message>> findByDhpQueryTypeAndClientId(String dhp_query_type, String clientId);
}
