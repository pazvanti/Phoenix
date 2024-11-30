package tech.petrepopescu.phoenix.format;

import org.springframework.http.MediaType;
import tech.petrepopescu.phoenix.special.ContentGroup;
import tech.petrepopescu.phoenix.special.PhoenixSpecialElementsUtil;

public abstract class HtmlFormat extends Format {
    protected ContentGroup contentGroup = new ContentGroup();

    public abstract String getContentForSection(String sectionName, PhoenixSpecialElementsUtil specialElementsUtil);
    @Override
    public MediaType getMediaType() {
        return MediaType.TEXT_HTML;
    }

    public void setContentGroup(ContentGroup contentGroup) {
        this.contentGroup = contentGroup;
    }
}
