package se.artcomputer.demo.musicnerd.musicbrainz.exception;

public class GatewayException extends RuntimeException {
    public GatewayException(String message) {
        super(message);
    }
}
