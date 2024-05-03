package tech.petrepopescu.phoenix.format;

import org.springframework.http.MediaType;
import tech.petrepopescu.phoenix.special.PhoenixSpecialElementsUtil;

public abstract class Format {
    public abstract String getContent(PhoenixSpecialElementsUtil specialElementsUtil);

    public abstract MediaType getMediaType();
}
