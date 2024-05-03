package tech.petrepopescu.phoenix.spring;

import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import tech.petrepopescu.phoenix.format.Result;
import tech.petrepopescu.phoenix.spring.config.PhoenixConfiguration;
import tech.petrepopescu.phoenix.views.View;
import tech.petrepopescu.phoenix.views.predefined.View404;
import tech.petrepopescu.phoenix.views.predefined.View500;

@EnableWebMvc
@ControllerAdvice
public class PhoenixErrorHandler {
    private final View404 view404 = new View404();
    private final View500 view500 = new View500();
    private final PhoenixConfiguration configuration;

    public PhoenixErrorHandler(PhoenixConfiguration configuration) {
        this.configuration = configuration;
    }

    @ExceptionHandler(NoHandlerFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Result handleNoHandlerFound(NoHandlerFoundException e, WebRequest request) {
        if (configuration.getErrorPages() == null || StringUtils.isBlank(configuration.getErrorPages().getCode404())) {
            return new Result(view404, HttpStatus.NOT_FOUND);
        }

        return new Result(View.of(configuration.getErrorPages().getCode404()), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Result handlerAnyException(NoHandlerFoundException e, WebRequest request) {
        if (configuration.getErrorPages() == null || StringUtils.isBlank(configuration.getErrorPages().getCode500())) {
            return new Result(view500, HttpStatus.NOT_FOUND);
        }

        return new Result(View.of(configuration.getErrorPages().getCode500()), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
