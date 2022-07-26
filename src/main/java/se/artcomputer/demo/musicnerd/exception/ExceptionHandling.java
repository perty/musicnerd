package se.artcomputer.demo.musicnerd.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.reactive.function.client.WebClientResponseException;

@ControllerAdvice
public class ExceptionHandling {
    private static final Logger LOG = LoggerFactory.getLogger(ExceptionHandling.class);

    @ExceptionHandler({GatewayException.class, WebClientResponseException.BadGateway.class})
    public ResponseEntity<String> allExceptions(Exception exception) {
        LOG.error("Gateway problem {}", exception.getMessage());
        return new ResponseEntity<>(exception.getMessage(), HttpStatus.GATEWAY_TIMEOUT);
    }

    @ExceptionHandler({NotFoundException.class, WebClientResponseException.NotFound.class})
    public ResponseEntity<String> notFound(Exception exception) {
        LOG.info(exception.getMessage());
        return new ResponseEntity<>(exception.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<String> illegalState(Exception exception) {
        LOG.error(exception.getMessage());
        return new ResponseEntity<>(exception.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({BadRequestException.class, WebClientResponseException.BadRequest.class})
    public ResponseEntity<String> basRequest(Exception exception) {
        LOG.error(exception.getMessage());
        return new ResponseEntity<>(exception.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<String> methodNot(Exception exception) {
        LOG.error(exception.getMessage());
        return new ResponseEntity<>(exception.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
    public ResponseEntity<String> mediaNot(Exception exception) {
        LOG.warn(exception.getMessage());
        return new ResponseEntity<>(exception.getMessage(), HttpStatus.BAD_REQUEST);
    }
}
