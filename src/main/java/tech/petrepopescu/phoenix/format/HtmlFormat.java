package tech.petrepopescu.phoenix.format;

import org.springframework.http.MediaType;

public abstract class HtmlFormat extends Format {
    @Override
    public MediaType getMediaType() {
        return MediaType.TEXT_HTML;
    }
}
