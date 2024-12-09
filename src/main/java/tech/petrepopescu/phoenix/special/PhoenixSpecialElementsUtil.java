package tech.petrepopescu.phoenix.special;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Component
public class PhoenixSpecialElementsUtil {
    public String getCsrfTokenName(CsrfToken csrfToken) {
        if (csrfToken != null) {
            return csrfToken.getParameterName();
        }

        return "";
    }

    public String getCsrfHeaderName(CsrfToken csrfToken) {
        if (csrfToken != null) {
            return csrfToken.getHeaderName();
        }

        return "";
    }

    public String getCsrfTokenValue(CsrfToken csrfToken) {
        if (csrfToken != null) {
            return csrfToken.getToken();
        }

        return "";
    }

    public CsrfToken getCsrfToken() {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        return (CsrfToken) request.getAttribute(CsrfToken.class.getName());
    }
}
