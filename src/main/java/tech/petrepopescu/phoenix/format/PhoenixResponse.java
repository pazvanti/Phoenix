package tech.petrepopescu.phoenix.format;

import org.springframework.http.HttpStatus;

public class PhoenixResponse {
    private PhoenixResponse() {
        // utility class
    }

    public static Result ok(Object object) {
        return withHttpStatus(object, HttpStatus.OK);
    }

    public static Result ok() {
        return withHttpStatus(new JsonFormat(), HttpStatus.OK);
    }

    public static Result ok(Format format) {
        return withHttpStatus(format, HttpStatus.OK);
    }

    public static Result notFound() {
        return withHttpStatus(new JsonFormat(), HttpStatus.NOT_FOUND);
    }

    public static Result notFound(Object object) {
        return withHttpStatus(object, HttpStatus.NOT_FOUND);
    }

    public static Result notFound(Format format) {
        return withHttpStatus(format, HttpStatus.NOT_FOUND);
    }

    public static Result withHttpStatus(Object object, HttpStatus status) {
        return withHttpStatus(new JsonFormat(object), status);
    }

    public static Result withHttpStatus(Format format, HttpStatus status) {
        return new Result(format, status);
    }
}
