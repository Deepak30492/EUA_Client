package in.gov.nha.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.sun.istack.NotNull;
import in.gov.nha.dto.AckResponse;
import in.gov.nha.dto.EuaRequestBody;
import in.gov.nha.dto.EuaRequestBodyStatus;
import in.gov.nha.model.Message;
import in.gov.nha.repository.Repository;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.sql.Timestamp;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;

import static in.gov.nha.constants.ConstantsUtils.*;

@RequestMapping("api/v1/")
@RestController
@Slf4j
public class EuaController {

	private static final Logger LOGGER = LoggerFactory.getLogger(EuaController.class);

	private final Repository messageRepository;
	private final WebClient webClient;

	@Value("${abdm.gateway.url}")
	private String abdmGatewayURl;

	@Value("${abdm.registry.url}")
	private String abdmRegistryURl;

	@Value("${abdm.eua.url}")
	private String abdmEUAURl;

	public EuaController(Repository messageRepository, WebClient webClient) {

		this.messageRepository = messageRepository;
		this.webClient = webClient;

		LOGGER.info("ABDM Gateway :: " + abdmGatewayURl);

		LOGGER.info("ABDM Registry :: " + abdmRegistryURl);

	}



	@MessageMapping("/get-response-by-msgId")
	@SendTo("/client/flutter")
	public ResponseEntity<List<Optional<Message>>> getMessageById(Message message) {
		try {

			List<Optional<Message>> messageFromDb = messageRepository.findByDhpQueryTypeAndConsumerId(message.getDhpQueryType(), message.getConsumerId());


			if (messageFromDb.isEmpty()) {

				return new ResponseEntity<>(HttpStatus.NO_CONTENT);

			} else {

				return new ResponseEntity<>(messageFromDb, HttpStatus.OK);

			}

		} catch (Exception e) {

			LOGGER.error(e.toString());

			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@PostMapping("/on_search")
	public ResponseEntity<AckResponse> onSearch(@RequestBody EuaRequestBody onSearchRequest) throws JsonProcessingException {

		LOGGER.info("Inside on_search API ");

		ObjectWriter ow = new ObjectMapper().writer();

		ObjectMapper objectMapper = new ObjectMapper();

		try {

			String onRequestString = ow.writeValueAsString(onSearchRequest);

			String requestMessageId = onSearchRequest.getContext().getMessage_id();

			LOGGER.info("Request Body :" + onRequestString);

			Optional<Message> messageData = messageRepository
					.findByMessageIdAndDhpQueryType(requestMessageId, "on_search");

			return getAckResponseResponseEntity(objectMapper, onRequestString, messageData);

		} catch (Exception e) {

			LOGGER.error(e.toString());

			String onSearchAckJsonErrorString = "{ \"message\": { \"ack\": { \"status\": \"NACK\" } }, \"error\": { \"type\": \"\", \"code\": \"500\", \"path\": \"string\", \"message\": \"Something went wrong\" } }";

			AckResponse onSearchAck = objectMapper.readValue(onSearchAckJsonErrorString, AckResponse.class);

			return new ResponseEntity<>(onSearchAck, HttpStatus.INTERNAL_SERVER_ERROR);

		}

	}

	@NotNull
	private ResponseEntity<AckResponse> getAckResponseResponseEntity(ObjectMapper objectMapper, String onRequestString,
			Optional<Message> messageData) throws com.fasterxml.jackson.core.JsonProcessingException {
		if (messageData.isEmpty()) {

			String onSearchAckJsonErrorString = "{ \"message\": { \"ack\": { \"status\": \"NACK\" } }, \"error\": { \"type\": \"\", \"code\": \"400\", \"path\": \"string\", \"message\": \"Message ID not present\" } }";

			AckResponse onSearchAck = objectMapper.readValue(onSearchAckJsonErrorString, AckResponse.class);

			return new ResponseEntity<>(onSearchAck, HttpStatus.OK);

		} else {

			Message message = messageData.get();

			message.setResponse(onRequestString);

			messageRepository.delete(message);

			messageRepository.save(message);

			String onRequestAckJsonString = "{ \"message\": { \"ack\": { \"status\": \"ACK\" } } }";

			AckResponse onSearchAck = objectMapper.readValue(onRequestAckJsonString, AckResponse.class);

			return new ResponseEntity<>(onSearchAck, HttpStatus.OK);

		}
	}
	@PostMapping("/on_select")
	public ResponseEntity<AckResponse> onSelect(@RequestBody EuaRequestBody onSelectRequest) throws JsonProcessingException {

		LOGGER.info("Inside on_select API ");

		ObjectWriter ow = new ObjectMapper().writer();

		ObjectMapper objectMapper = new ObjectMapper();

		try {

			String onRequestString = ow.writeValueAsString(onSelectRequest);

			String requestMessageId = onSelectRequest.getContext().getMessage_id();

			LOGGER.info("Request Body :" + onRequestString);

			Optional<Message> messageData = messageRepository
					.findByMessageIdAndDhpQueryType(requestMessageId, "on_select");

			return getAckResponseResponseEntity(objectMapper, onRequestString, messageData);

		} catch (Exception e) {

			LOGGER.error(e.toString());

			String onSearchAckJsonErrorString = "{ \"message\": { \"ack\": { \"status\": \"NACK\" } }, \"error\": { \"type\": \"\", \"code\": \"500\", \"path\": \"string\", \"message\": \"Something went wrong\" } }";

			AckResponse onSearchAck = objectMapper.readValue(onSearchAckJsonErrorString, AckResponse.class);

			return new ResponseEntity<>(onSearchAck, HttpStatus.INTERNAL_SERVER_ERROR);

		}

	}

	@PostMapping("/on_init")
	public ResponseEntity<AckResponse> onInit(@RequestBody EuaRequestBodyStatus onInitRequest) throws JsonProcessingException {

		LOGGER.info("Inside on_init API ");

		ObjectWriter ow = new ObjectMapper().writer();

		ObjectMapper objectMapper = new ObjectMapper();

		try {

			String onRequestString = ow.writeValueAsString(onInitRequest);

			String requestMessageId = onInitRequest.getContext().getMessage_id();

			LOGGER.info("Request Body :" + onRequestString);

			Optional<Message> messageData = messageRepository
					.findByMessageIdAndDhpQueryType(requestMessageId, "on_init");

			return getAckResponseResponseEntity(objectMapper, onRequestString, messageData);

		} catch (Exception e) {

			LOGGER.error(e.toString());

			String onSearchAckJsonErrorString = "{ \"message\": { \"ack\": { \"status\": \"NACK\" } }, \"error\": { \"type\": \"\", \"code\": \"500\", \"path\": \"string\", \"message\": \"Something went wrong\" } }";

			AckResponse onSearchAck = objectMapper.readValue(onSearchAckJsonErrorString, AckResponse.class);

			return new ResponseEntity<>(onSearchAck, HttpStatus.INTERNAL_SERVER_ERROR);

		}

	}

	@PostMapping("/on_confirm")
	public ResponseEntity<AckResponse> onConfirm(@RequestBody EuaRequestBodyStatus onConfirmRequest) throws JsonProcessingException {

		LOGGER.info("Inside on_confirm API ");

		ObjectWriter ow = new ObjectMapper().writer();

		ObjectMapper objectMapper = new ObjectMapper();

		try {

			String onRequestString = ow.writeValueAsString(onConfirmRequest);

			String requestMessageId = onConfirmRequest.getContext().getMessage_id();

			LOGGER.info("Request Body :" + onRequestString);

			Optional<Message> messageData = messageRepository
					.findByMessageIdAndDhpQueryType(requestMessageId, "on_confirm");

			return getAckResponseResponseEntity(objectMapper, onRequestString, messageData);

		} catch (Exception e) {

			LOGGER.error(e.toString());

			String onSearchAckJsonErrorString = "{ \"message\": { \"ack\": { \"status\": \"NACK\" } }, \"error\": { \"type\": \"\", \"code\": \"500\", \"path\": \"string\", \"message\": \"Something went wrong\" } }";

			AckResponse onSearchAck = objectMapper.readValue(onSearchAckJsonErrorString, AckResponse.class);

			return new ResponseEntity<>(onSearchAck, HttpStatus.INTERNAL_SERVER_ERROR);

		}

	}

	@PostMapping("/on_status")
	public ResponseEntity<AckResponse> onStatus(@RequestBody EuaRequestBodyStatus onStatusRequest) throws JsonProcessingException {

		LOGGER.info("Inside on_status API ");

		ObjectWriter ow = new ObjectMapper().writer();

		ObjectMapper objectMapper = new ObjectMapper();

		try {

			String onRequestString = ow.writeValueAsString(onStatusRequest);

			String requestMessageId = onStatusRequest.getContext().getMessage_id();

			LOGGER.info("Request Body :" + onRequestString);

			Optional<Message> messageData = messageRepository
					.findByMessageIdAndDhpQueryType(requestMessageId, "on_status");

			return getAckResponseResponseEntity(objectMapper, onRequestString, messageData);

		} catch (Exception e) {

			LOGGER.error(e.toString());

			String onSearchAckJsonErrorString = "{ \"message\": { \"ack\": { \"status\": \"NACK\" } }, \"error\": { \"type\": \"\", \"code\": \"500\", \"path\": \"string\", \"message\": \"Something went wrong\" } }";

			AckResponse onSearchAck = objectMapper.readValue(onSearchAckJsonErrorString, AckResponse.class);

			return new ResponseEntity<>(onSearchAck, HttpStatus.INTERNAL_SERVER_ERROR);

		}

	}

	@PostMapping("/search")
	public ResponseEntity<?> search(@RequestBody EuaRequestBody searchRequest) throws JsonProcessingException {

		LOGGER.info("Inside Search API ");
		String url;

		ObjectWriter ow = new ObjectMapper().writer();

		ObjectMapper objectMapper = new ObjectMapper();

		try {

			LOGGER.info("Gateway URI :: " + abdmGatewayURl);

//			searchRequest.getContext().setConsumer_id(abdmEUAURl);

			searchRequest.getContext().setConsumer_uri(abdmEUAURl);

			String onRequestString = ow.writeValueAsString(searchRequest);

			String requestMessageId = searchRequest.getContext().getMessage_id();

			String clientId = searchRequest.getContext().getConsumer_id();

			LOGGER.info("Request Body :" + onRequestString);
			url = abdmGatewayURl + SEARCH_ENDPOINT;

			messageRepository
					.save(new Message(requestMessageId, "", "on_search", Timestamp.from(ZonedDateTime.now().toInstant()), clientId));

			LOGGER.info("Request Body :" + onRequestString);

		} catch (Exception e) {

			LOGGER.error(e.toString());

			String onSearchAckJsonErrorString = "{ \"message\": { \"ack\": { \"status\": \"NACK\" } }, \"error\": { \"type\": \"\", \"code\": \"500\", \"path\": \"string\", \"message\": \"Something went wrong\" } }";

			AckResponse onSearchAck = objectMapper.readValue(onSearchAckJsonErrorString, AckResponse.class);

			return ResponseEntity.status(HttpStatus.OK).body(onSearchAck);

		}
		return ResponseEntity.status(HttpStatus.OK)
				.body(getAckResponseResponseEntity(url, searchRequest));
	}

	@PostMapping("/select")
	public ResponseEntity<?> select(@RequestBody EuaRequestBody selectRequest) throws JsonProcessingException {

		LOGGER.info("Inside select API ");
		String url;

		ObjectWriter ow = new ObjectMapper().writer();

		ObjectMapper objectMapper = new ObjectMapper();

		try {

//			selectRequest.getContext().setConsumer_id(abdmEUAURl);

			selectRequest.getContext().setConsumer_uri(abdmEUAURl);

			String providerURI = selectRequest.getContext().getProvider_uri();

			String onRequestString = ow.writeValueAsString(selectRequest);

			String requestMessageId = selectRequest.getContext().getMessage_id();
			String clientId = selectRequest.getContext().getConsumer_id();

			LOGGER.info("Provider URI :" + providerURI);

			LOGGER.info("Request Body :" + onRequestString);
			url = providerURI + SELECT_ENDPOINT;

			messageRepository
					.save(new Message(requestMessageId, "", "on_select", Timestamp.from(ZonedDateTime.now().toInstant()), clientId));

		} catch (Exception e) {

			LOGGER.error(e.toString());

			String onSearchAckJsonErrorString = "{ \"message\": { \"ack\": { \"status\": \"ACK\" } }, \"error\": { \"type\": \"\", \"code\": \"500\", \"path\": \"string\", \"message\": \"Something went wrong\" } }";

			AckResponse onSearchAck = objectMapper.readValue(onSearchAckJsonErrorString, AckResponse.class);

			return ResponseEntity.status(HttpStatus.OK).body(onSearchAck);

		}
		return ResponseEntity.status(HttpStatus.OK)
				.body(getAckResponseResponseEntity(url, selectRequest));
	}

	private AckResponse getAckResponseResponseEntity(String url,
													EuaRequestBody initRequest) {

		Mono<AckResponse> ackResponseMono = webClient.post().uri(url).body(Mono.just(initRequest), AckResponse.class)
				.retrieve() // By default .retrieve() will check for error statuses for you.
				.bodyToMono(AckResponse.class);

		return ackResponseMono.block();

	}

	@PostMapping("/init")
	public ResponseEntity<?> init(@RequestBody EuaRequestBody initRequest) throws JsonProcessingException {
		String url;
		LOGGER.info("Inside init API ");

		ObjectWriter ow = new ObjectMapper().writer();

		ObjectMapper objectMapper = new ObjectMapper();

		try {


			initRequest.getContext().setConsumer_uri(abdmEUAURl);

			String providerURI = initRequest.getContext().getProvider_uri();

			String onRequestString = ow.writeValueAsString(initRequest);

			String requestMessageId = initRequest.getContext().getMessage_id();
			String clientId = initRequest.getContext().getConsumer_id();

			LOGGER.info("Provider URI :" + providerURI);

			LOGGER.info("Request Body :" + onRequestString);

			messageRepository
					.save(new Message(requestMessageId, "", "on_init", Timestamp.from(ZonedDateTime.now().toInstant()), clientId));

			url = providerURI + INIT_ENDPOINT;

		} catch (Exception e) {

			LOGGER.error(e.toString());

			String onSearchAckJsonErrorString = "{ \"message\": { \"ack\": { \"status\": \"NACK\" } }, \"error\": { \"type\": \"\", \"code\": \"500\", \"path\": \"string\", \"message\": \"Something went wrong\" } }";

			AckResponse onSearchAck = objectMapper.readValue(onSearchAckJsonErrorString, AckResponse.class);

			return ResponseEntity.status(HttpStatus.OK).body(onSearchAck);

		}
		return ResponseEntity.status(HttpStatus.OK).body(getAckResponseResponseEntity(url, initRequest));
	}

	@PostMapping("/confirm")
	public ResponseEntity<?> confirm(@RequestBody EuaRequestBody confirmRequest) throws JsonProcessingException {
		String url;
		LOGGER.info("Inside confirm API ");

		ObjectWriter ow = new ObjectMapper().writer();

		ObjectMapper objectMapper = new ObjectMapper();
		try {


			confirmRequest.getContext().setConsumer_uri(abdmEUAURl);

			String providerURI = confirmRequest.getContext().getProvider_uri();

			String onRequestString = ow.writeValueAsString(confirmRequest);

			String requestMessageId = confirmRequest.getContext().getMessage_id();
			String clientId = confirmRequest.getContext().getConsumer_id();

			LOGGER.info("Provider URI :" + providerURI);

			LOGGER.info("Request Body :" + onRequestString);

			url = providerURI + CONFIRM_ENDPOINT;

			messageRepository.save(
					new Message(requestMessageId, "", "on_confirm", Timestamp.from(ZonedDateTime.now().toInstant()), clientId));


		} catch (Exception e) {

			LOGGER.error(e.toString());

			String onSearchAckJsonErrorString = "{ \"message\": { \"ack\": { \"status\": \"NACK\" } }, \"error\": { \"type\": \"\", \"code\": \"500\", \"path\": \"string\", \"message\": \"Something went wrong\" } }";

			AckResponse onSearchAck = objectMapper.readValue(onSearchAckJsonErrorString, AckResponse.class);

			return ResponseEntity.status(HttpStatus.OK).body(onSearchAck);

		}
		return ResponseEntity.status(HttpStatus.OK)
				.body(getAckResponseResponseEntity(url, confirmRequest));
	}

	@PostMapping("/status")
	public ResponseEntity<?> status(@RequestBody EuaRequestBody statusRequest) throws JsonProcessingException {

		String url;

		LOGGER.info("Inside status API ");

		ObjectWriter ow = new ObjectMapper().writer();

		ObjectMapper objectMapper = new ObjectMapper();

		try {


			statusRequest.getContext().setConsumer_uri(abdmEUAURl);

			String providerURI = statusRequest.getContext().getProvider_uri();

			String onRequestString = ow.writeValueAsString(statusRequest);

			String requestMessageId = statusRequest.getContext().getMessage_id();

			String clientId = statusRequest.getContext().getConsumer_id();

			LOGGER.info("Provider URI :" + providerURI);

			LOGGER.info("Request Body :" + onRequestString);

			url = providerURI + STATUS_ENDPOINT;

			messageRepository
					.save(new Message(requestMessageId, "", "on_status", Timestamp.from(ZonedDateTime.now().toInstant()), clientId));


		} catch (Exception e) {

			LOGGER.error(e.toString());

			String onSearchAckJsonErrorString = "{ \"message\": { \"ack\": { \"status\": \"NACK\" } }, \"error\": { \"type\": \"\", \"code\": \"500\", \"path\": \"string\", \"message\": \"Something went wrong\" } }";

			AckResponse onSearchAck = objectMapper.readValue(onSearchAckJsonErrorString, AckResponse.class);

			return ResponseEntity.status(HttpStatus.OK).body(onSearchAck);

		}
		return ResponseEntity.status(HttpStatus.OK)
				.body(getAckResponseResponseEntity(url, statusRequest));
	}

}
