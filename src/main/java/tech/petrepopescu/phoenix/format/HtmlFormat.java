package tech.petrepopescu.phoenix.format;

import org.springframework.http.MediaType;
import tech.petrepopescu.phoenix.special.PhoenixSpecialElementsUtil;

public abstract class HtmlFormat extends Format {
    public abstract String getContentForSection(String sectionName, PhoenixSpecialElementsUtil specialElementsUtil);
    @Override
    public MediaType getMediaType() {
        return MediaType.TEXT_HTML;
    }
}
