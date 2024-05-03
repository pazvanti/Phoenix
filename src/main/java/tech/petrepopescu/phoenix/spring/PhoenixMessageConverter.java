package tech.petrepopescu.phoenix.spring;

import org.springframework.http.HttpInputMessage;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.MediaType;
import org.springframework.http.converter.AbstractHttpMessageConverter;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.stereotype.Component;
import tech.petrepopescu.phoenix.format.Format;
import tech.petrepopescu.phoenix.special.PhoenixSpecialElementsUtil;

import java.io.IOException;
import java.io.OutputStream;

@Component
public class PhoenixMessageConverter extends AbstractHttpMessageConverter<Format> {
    private final PhoenixSpecialElementsUtil phoenixSpecialElementsUtil;

    public PhoenixMessageConverter(PhoenixSpecialElementsUtil phoenixSpecialElementsUtil) {
        super(MediaType.TEXT_HTML, MediaType.APPLICATION_JSON);
        this.phoenixSpecialElementsUtil = phoenixSpecialElementsUtil;
    }

    @Override
    protected boolean supports(Class<?> clazz) {
        return Format.class.isAssignableFrom(clazz);
    }

    @Override
    protected Format readInternal(Class<? extends Format> clazz, HttpInputMessage inputMessage) throws IOException, HttpMessageNotReadableException {
        return null;
    }

    @Override
    protected void writeInternal(Format phoenixFormat, HttpOutputMessage outputMessage) throws IOException, HttpMessageNotWritableException {
        String data = phoenixFormat.getContent(phoenixSpecialElementsUtil);

        outputMessage.getHeaders().setContentType(phoenixFormat.getMediaType());

        // write the response body
        OutputStream outputStream = outputMessage.getBody();
        outputStream.write(data.getBytes());
        outputStream.flush();
    }
}
