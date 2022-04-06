package com.abdm.eua.dhp;

import com.abdm.eua.dhp.schema.EuaRequestBody;
import com.abdm.eua.dhp.schema.EuaRequestBodyStatus;
import com.abdm.eua.dhp.schema.ack.AckResponse;
import com.abdm.eua.dhp.schema.onconfirm.OnConfirmRequest;
import com.abdm.eua.dhp.schema.oninit.OnInitRequest;
import com.abdm.eua.dhp.schema.onsearch.OnSearchRequest;
import com.abdm.eua.dhp.schema.onstatus.OnStatusRequest;
import com.abdm.eua.message.Message;
import com.abdm.eua.message.Repository;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.sun.istack.NotNull;
import lombok.extern.slf4j.Slf4j;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.sql.Timestamp;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;
import org.slf4j.LoggerFactory;

@RequestMapping("api/v1/")
@RestController
@Slf4j
public class DHPController {

	private static final Logger LOGGER = LoggerFactory.getLogger(DHPController.class);
	private final Repository messageRepository;
	private final WebClient webClient;

	@Value("${abdm.gateway.url}")
	private String abdmGatewayURl;

	@Value("${abdm.registry.url}")
	private String abdmRegistryURl;

	@Value("${abdm.eua.url}")
	private String abdmEUAURl;

	public DHPController(Repository messageRepository, WebClient webClient) {

		this.messageRepository = messageRepository;
		this.webClient = webClient;

		LOGGER.info("ABDM Gateway :: " + abdmGatewayURl);

		LOGGER.info("ABDM Registry :: " + abdmRegistryURl);

	}

	@MessageMapping("/get-response-by-msgId")
	@SendTo("/client/flutter")
	public ResponseEntity<List<Optional<Message>>> getMessageById(Message message) {
		try {

			List<Optional<Message>> messageFromDb = messageRepository.findByDhpQueryTypeAndClientId(message.getDhpQueryType(), message.getClientId());


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

	@MessageMapping("/on_search")
	@SendTo("/client/flutter")
//	@PostMapping("/on_search")
	public ResponseEntity<AckResponse> onSearch(@RequestBody OnSearchRequest onRequest) throws Exception {

		LOGGER.info("Inside on_search API ");

		ObjectWriter ow = new ObjectMapper().writer();

		ObjectMapper objectMapper = new ObjectMapper();

		try {

			String onRequestString = ow.writeValueAsString(onRequest);

			String requestMessageId = onRequest.context.message_id;

			LOGGER.info("Request Body :" + onRequestString);

			Optional<com.abdm.eua.message.Message> messageData = messageRepository
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

			messageRepository.save(message);

			String onRequestAckJsonString = "{ \"message\": { \"ack\": { \"status\": \"ACK\" } } }";

			AckResponse onSearchAck = objectMapper.readValue(onRequestAckJsonString, AckResponse.class);

			return new ResponseEntity<>(onSearchAck, HttpStatus.OK);

		}
	}
	@MessageMapping("/on_select")
	@SendTo("/client/flutter")
//	@PostMapping("/on_select")
	public ResponseEntity<AckResponse> onSelect(@RequestBody EuaRequestBody onSelectRequest) throws Exception {

		LOGGER.info("Inside on_select API ");

		ObjectWriter ow = new ObjectMapper().writer();

		ObjectMapper objectMapper = new ObjectMapper();

		try {

			String onRequestString = ow.writeValueAsString(onSelectRequest);

			String requestMessageId = onSelectRequest.getContext().getMessage_id();

			LOGGER.info("Request Body :" + onRequestString);

			Optional<com.abdm.eua.message.Message> messageData = messageRepository
					.findByMessageIdAndDhpQueryType(requestMessageId, "on_select");

			return getAckResponseResponseEntity(objectMapper, onRequestString, messageData);

		} catch (Exception e) {

			LOGGER.error(e.toString());

			String onSearchAckJsonErrorString = "{ \"message\": { \"ack\": { \"status\": \"NACK\" } }, \"error\": { \"type\": \"\", \"code\": \"500\", \"path\": \"string\", \"message\": \"Something went wrong\" } }";

			AckResponse onSearchAck = objectMapper.readValue(onSearchAckJsonErrorString, AckResponse.class);

			return new ResponseEntity<>(onSearchAck, HttpStatus.INTERNAL_SERVER_ERROR);

		}

	}

	@MessageMapping("/on_init")
	@SendTo("/client/flutter")
//	@PostMapping("/on_init")
	public ResponseEntity<AckResponse> onInit(@RequestBody OnInitRequest onRequest) throws Exception {

		LOGGER.info("Inside on_init API ");

		ObjectWriter ow = new ObjectMapper().writer();

		ObjectMapper objectMapper = new ObjectMapper();

		try {

			String onRequestString = ow.writeValueAsString(onRequest);

			String requestMessageId = onRequest.context.message_id;

			LOGGER.info("Request Body :" + onRequestString);

			Optional<com.abdm.eua.message.Message> messageData = messageRepository
					.findByMessageIdAndDhpQueryType(requestMessageId, "on_init");

			return getAckResponseResponseEntity(objectMapper, onRequestString, messageData);

		} catch (Exception e) {

			LOGGER.error(e.toString());

			String onSearchAckJsonErrorString = "{ \"message\": { \"ack\": { \"status\": \"NACK\" } }, \"error\": { \"type\": \"\", \"code\": \"500\", \"path\": \"string\", \"message\": \"Something went wrong\" } }";

			AckResponse onSearchAck = objectMapper.readValue(onSearchAckJsonErrorString, AckResponse.class);

			return new ResponseEntity<>(onSearchAck, HttpStatus.INTERNAL_SERVER_ERROR);

		}

	}

	@MessageMapping("/on_confirm")
	@SendTo("/client/flutter")
//	@PostMapping("/on_confirm")
	public ResponseEntity<AckResponse> onConfirm(@RequestBody OnConfirmRequest onRequest) throws Exception {

		LOGGER.info("Inside on_confirm API ");

		ObjectWriter ow = new ObjectMapper().writer();

		ObjectMapper objectMapper = new ObjectMapper();

		try {

			String onRequestString = ow.writeValueAsString(onRequest);

			String requestMessageId = onRequest.context.message_id;

			LOGGER.info("Request Body :" + onRequestString);

			Optional<com.abdm.eua.message.Message> messageData = messageRepository
					.findByMessageIdAndDhpQueryType(requestMessageId, "on_confirm");

			return getAckResponseResponseEntity(objectMapper, onRequestString, messageData);

		} catch (Exception e) {

			LOGGER.error(e.toString());

			String onSearchAckJsonErrorString = "{ \"message\": { \"ack\": { \"status\": \"NACK\" } }, \"error\": { \"type\": \"\", \"code\": \"500\", \"path\": \"string\", \"message\": \"Something went wrong\" } }";

			AckResponse onSearchAck = objectMapper.readValue(onSearchAckJsonErrorString, AckResponse.class);

			return new ResponseEntity<>(onSearchAck, HttpStatus.INTERNAL_SERVER_ERROR);

		}

	}

	@MessageMapping("/on_status")
	@SendTo("/client/flutter")
//	@PostMapping("/on_status")
	public ResponseEntity<AckResponse> onStatus(EuaRequestBodyStatus onStatusRequest) throws Exception {

		LOGGER.info("Inside on_status API ");

		ObjectWriter ow = new ObjectMapper().writer();

		ObjectMapper objectMapper = new ObjectMapper();

		try {

			String onRequestString = ow.writeValueAsString(onStatusRequest);

			String requestMessageId = onStatusRequest.getContext().getMessage_id();

			LOGGER.info("Request Body :" + onRequestString);

			Optional<com.abdm.eua.message.Message> messageData = messageRepository
					.findByMessageIdAndDhpQueryType(requestMessageId, "on_status");

			return getAckResponseResponseEntity(objectMapper, onRequestString, messageData);

		} catch (Exception e) {

			LOGGER.error(e.toString());

			String onSearchAckJsonErrorString = "{ \"message\": { \"ack\": { \"status\": \"NACK\" } }, \"error\": { \"type\": \"\", \"code\": \"500\", \"path\": \"string\", \"message\": \"Something went wrong\" } }";

			AckResponse onSearchAck = objectMapper.readValue(onSearchAckJsonErrorString, AckResponse.class);

			return new ResponseEntity<>(onSearchAck, HttpStatus.INTERNAL_SERVER_ERROR);

		}

	}

	@MessageMapping("/search")
	@SendTo("/client/flutter")
//	@PostMapping(value = "/search", consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> search(EuaRequestBody searchRequest) throws Exception {

		LOGGER.info("Inside Search API ");
		String url;

		ObjectWriter ow = new ObjectMapper().writer();

		ObjectMapper objectMapper = new ObjectMapper();

		try {

			LOGGER.info("Gateway URI :: " + abdmGatewayURl);

			searchRequest.getContext().setConsumer_id(abdmEUAURl);

			searchRequest.getContext().setConsumer_uri(abdmEUAURl);

			String onRequestString = ow.writeValueAsString(searchRequest);

			String requestMessageId = searchRequest.getContext().getMessage_id();

			String clientId = searchRequest.getClientId();

			LOGGER.info("Request Body :" + onRequestString);
			url = abdmGatewayURl + "/ack";

			Message _message = messageRepository
					.save(new Message(requestMessageId, "", "on_search", Timestamp.from(ZonedDateTime.now().toInstant()), clientId));

			LOGGER.info("Request Body :" + onRequestString);

//            HttpRequest request = HttpRequest.newBuilder()
//                    .uri(URI.create(abdmGatewayURl + "/search"))
//                    .timeout(Duration.ofMinutes(1))
//                    .header("Content-Type", "application/json")
//                    .POST(HttpRequest.BodyPublishers.ofString(onRequestString))
//                    .build();

		} catch (Exception e) {

			LOGGER.error(e.toString());

			String onSearchAckJsonErrorString = "{ \"message\": { \"ack\": { \"status\": \"NACK\" } }, \"error\": { \"type\": \"\", \"code\": \"500\", \"path\": \"string\", \"message\": \"Something went wrong\" } }";

			AckResponse onSearchAck = objectMapper.readValue(onSearchAckJsonErrorString, AckResponse.class);

			return ResponseEntity.status(HttpStatus.OK).body(onSearchAck);

		}
		return ResponseEntity.status(HttpStatus.OK)
				.body(getAckResponseResponseEntity(url, searchRequest));
	}

	@MessageMapping("/select")
	@SendTo("/client/flutter")
//	@PostMapping("/select")
	public ResponseEntity<?> select(@RequestBody EuaRequestBody selectRequest) throws Exception {

		LOGGER.info("Inside select API ");
		String url;

		ObjectWriter ow = new ObjectMapper().writer();

		ObjectMapper objectMapper = new ObjectMapper();

		try {

			selectRequest.getContext().setConsumer_id(abdmEUAURl);

			selectRequest.getContext().setConsumer_uri(abdmEUAURl);

			String providerURI = selectRequest.getContext().getProvider_uri();

			String onRequestString = ow.writeValueAsString(selectRequest);

			String requestMessageId = selectRequest.getContext().getMessage_id();
			String clientId = selectRequest.getClientId();

			LOGGER.info("Provider URI :" + providerURI);

			LOGGER.info("Request Body :" + onRequestString);
			url = providerURI + "/select";

			Message _message = messageRepository
					.save(new Message(requestMessageId, "", "on_select", Timestamp.from(ZonedDateTime.now().toInstant()),clientId));

//            HttpRequest request = HttpRequest.newBuilder()
//                    .uri(URI.create(providerURI + "/select"))
//                    .timeout(Duration.ofMinutes(1))
//                    .header("Content-Type", "application/json")
//                    .POST(HttpRequest.BodyPublishers.ofString(onRequestString))
//                    .build();

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
//		AckResponse response = new AckResponse();
//		return ackResponseMono.doOnSuccess(ackResponse -> {return ackResponse;}).subscribe();

		return ackResponseMono.block();

	}

	@MessageMapping("/init")
	@SendTo("/client/flutter")
//	@PostMapping("/init")
	public ResponseEntity<?> init(@RequestBody EuaRequestBody initRequest) throws Exception {
		String url;
		LOGGER.info("Inside init API ");

		ObjectWriter ow = new ObjectMapper().writer();

		ObjectMapper objectMapper = new ObjectMapper();

		try {

			initRequest.getContext().setConsumer_id(abdmEUAURl);

			initRequest.getContext().setConsumer_uri(abdmEUAURl);

			String providerURI = initRequest.getContext().getProvider_uri();

			String onRequestString = ow.writeValueAsString(initRequest);

			String requestMessageId = initRequest.getContext().getMessage_id();
			String clientId = initRequest.getClientId();

			LOGGER.info("Provider URI :" + providerURI);

			LOGGER.info("Request Body :" + onRequestString);

			Message _message = messageRepository
					.save(new Message(requestMessageId, "", "on_init", Timestamp.from(ZonedDateTime.now().toInstant()), clientId));

			url = providerURI + "/init";

//            HttpRequest request = HttpRequest.newBuilder()
//                    .uri(URI.create(providerURI + "/init"))
//                    .timeout(Duration.ofMinutes(1))
//                    .header("Content-Type", "application/json")
//                    .POST(HttpRequest.BodyPublishers.ofString(onRequestString))
//                    .build();

		} catch (Exception e) {

			LOGGER.error(e.toString());

			String onSearchAckJsonErrorString = "{ \"message\": { \"ack\": { \"status\": \"NACK\" } }, \"error\": { \"type\": \"\", \"code\": \"500\", \"path\": \"string\", \"message\": \"Something went wrong\" } }";

			AckResponse onSearchAck = objectMapper.readValue(onSearchAckJsonErrorString, AckResponse.class);

			return ResponseEntity.status(HttpStatus.OK).body(onSearchAck);

		}
		return ResponseEntity.status(HttpStatus.OK).body(getAckResponseResponseEntity(url, initRequest));
	}

	@MessageMapping("/confirm")
	@SendTo("/client/flutter")
//	@PostMapping("/confirm")
	public ResponseEntity<?> confirm(@RequestBody EuaRequestBody confirmRequest) throws Exception {
		String url;
		LOGGER.info("Inside confirm API ");

		ObjectWriter ow = new ObjectMapper().writer();

		ObjectMapper objectMapper = new ObjectMapper();
		try {

			confirmRequest.getContext().setConsumer_id(abdmEUAURl);

			confirmRequest.getContext().setConsumer_uri(abdmEUAURl);

			String providerURI = confirmRequest.getContext().getProvider_uri();

			String onRequestString = ow.writeValueAsString(confirmRequest);

			String requestMessageId = confirmRequest.getContext().getMessage_id();
			String clientId = confirmRequest.getClientId();

			LOGGER.info("Provider URI :" + providerURI);

			LOGGER.info("Request Body :" + onRequestString);

			url = providerURI + "/confirm";

			Message _message = messageRepository.save(
					new Message(requestMessageId, "", "on_confirm", Timestamp.from(ZonedDateTime.now().toInstant()),clientId));

//            HttpRequest request = HttpRequest.newBuilder()
//                    .uri(URI.create(providerURI + "/confirm"))
//                    .timeout(Duration.ofMinutes(1))
//                    .header("Content-Type", "application/json")
//                    .POST(HttpRequest.BodyPublishers.ofString(onRequestString))
//                    .build();

		} catch (Exception e) {

			LOGGER.error(e.toString());

			String onSearchAckJsonErrorString = "{ \"message\": { \"ack\": { \"status\": \"NACK\" } }, \"error\": { \"type\": \"\", \"code\": \"500\", \"path\": \"string\", \"message\": \"Something went wrong\" } }";

			AckResponse onSearchAck = objectMapper.readValue(onSearchAckJsonErrorString, AckResponse.class);

			return ResponseEntity.status(HttpStatus.OK).body(onSearchAck);

		}
		return ResponseEntity.status(HttpStatus.OK)
				.body(getAckResponseResponseEntity(url, confirmRequest));
	}

	@MessageMapping("/status")
	@SendTo("/client/flutter")
//	@PostMapping("/status")
	public ResponseEntity<?> status(@RequestBody EuaRequestBody statusRequest) throws Exception {

		String url;

		LOGGER.info("Inside status API ");

		ObjectWriter ow = new ObjectMapper().writer();

		ObjectMapper objectMapper = new ObjectMapper();

		try {

			statusRequest.getContext().setConsumer_id(abdmEUAURl);

			statusRequest.getContext().setConsumer_uri(abdmEUAURl);

			String providerURI = statusRequest.getContext().getProvider_uri();

			String onRequestString = ow.writeValueAsString(statusRequest);

			String requestMessageId = statusRequest.getContext().getMessage_id();

			String clientId = statusRequest.getClientId();

			LOGGER.info("Provider URI :" + providerURI);

			LOGGER.info("Request Body :" + onRequestString);

			url = providerURI + "/status";

			Message _message = messageRepository
					.save(new Message(requestMessageId, "", "on_status", Timestamp.from(ZonedDateTime.now().toInstant()), clientId));

//            HttpRequest request = HttpRequest.newBuilder()
//                    .uri(URI.create(providerURI + "/status"))
//                    .timeout(Duration.ofMinutes(1))
//                    .header("Content-Type", "application/json")
//                    .POST(HttpRequest.BodyPublishers.ofString(onRequestString))
//                    .build();

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
