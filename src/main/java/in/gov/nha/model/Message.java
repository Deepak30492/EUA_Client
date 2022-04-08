package in.gov.nha.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.*;
import java.sql.Timestamp;

@Table(schema = "eua")
@Entity
public class Message {

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Long id;

    @Column(nullable = false, columnDefinition = "text")
    @JsonProperty("message_id")
    private String messageId;

    @Column(nullable = false, columnDefinition = "text")
    @JsonProperty("consumer_id")
    private String consumerId;

    @Column(columnDefinition = "text")
    @JsonProperty("response")
    private String response;

    @Column(nullable = false)
    @JsonProperty("dhp_query_type")
    private String dhpQueryType;

    @Column( columnDefinition = "timestamp with time zone DEFAULT CURRENT_TIMESTAMP")
    @JsonProperty("created_at")
    private Timestamp createdAt;

    public Message() {};

    public Message(String message_id, String response, String dhp_query_type, Timestamp created_at, String consumerId){

        this.messageId = message_id;

        this.response = response;

        this.dhpQueryType = dhp_query_type;

        this.createdAt = created_at;
        this.consumerId = consumerId;

    }

    public Long getId() {
        return id;
    }

    public String getMessageId() {
        return messageId;
    }

    public String getResponse() {
        return response;
    }

    public String getDhpQueryType() {
        return dhpQueryType;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setId(Long id) {

        this.id = id;

    }

    public void setMessageId(String messageId) {

        this.messageId = messageId;

    }

    public void setResponse(String response) {

          this.response = response;

    }

    public void setDhpQueryType(String dhpQueryType) {

        this.dhpQueryType = dhpQueryType;

    }

    public void setCreatedAt(Timestamp createdAt) {

        this.createdAt = createdAt;
    }

    public String getConsumerId() {
        return consumerId;
    }

    public void setConsumerId(String clientId) {
        this.consumerId = clientId;
    }
}
