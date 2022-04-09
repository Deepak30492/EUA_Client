package in.gov.nha.controller;

import in.gov.nha.model.Message;
import in.gov.nha.repository.Repository;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.slf4j.LoggerFactory;

@RequestMapping("api/v1/message")
@RestController
public class DbController {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(DbController.class);

	private final Repository messageRepository;

	@Autowired
	public DbController(Repository messageRepository) {

		this.messageRepository = messageRepository;

	}

	@PostMapping
	public ResponseEntity<Message> insert(@RequestBody Message message) {

		try {

			Message _message = messageRepository.save(new Message(message.getMessageId(), message.getResponse(),
					message.getDhpQueryType(), Timestamp.from(ZonedDateTime.now().toInstant()), message.getConsumerId()));

			return new ResponseEntity<>(_message, HttpStatus.CREATED);

		} catch (Exception e) {

			LOGGER.error(e.toString());

			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);

		}

	}

	@GetMapping
	public ResponseEntity<List<Message>> getAll() {

		try {

			List<Message> messages = new ArrayList<>(messageRepository.findAll());

			if (messages.isEmpty()) {

				return new ResponseEntity<>(HttpStatus.NO_CONTENT);

			}

			return new ResponseEntity<>(messages, HttpStatus.OK);

		} catch (Exception e) {

			LOGGER.error(e.toString());

			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);

		}

	}

	@GetMapping("/{id}")
	public ResponseEntity<List<Optional<Message>>> getMessageById(@PathVariable("id") String message_id) {

		try {

			List<Optional<Message>> message = messageRepository.findByMessageId(message_id);

			if (message.isEmpty()) {

				return new ResponseEntity<>(HttpStatus.NO_CONTENT);

			} else {

				return new ResponseEntity<>(message, HttpStatus.OK);

			}

		} catch (Exception e) {

			LOGGER.error(e.toString());

			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);

		}

	}

}
