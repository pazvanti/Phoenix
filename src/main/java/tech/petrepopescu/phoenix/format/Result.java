package tech.petrepopescu.phoenix.format;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;

public class Result extends ResponseEntity<Format> {
    public Result(Format format, HttpStatus status) {
        super(format, HttpStatusCode.valueOf(status.value()));
    }
}
