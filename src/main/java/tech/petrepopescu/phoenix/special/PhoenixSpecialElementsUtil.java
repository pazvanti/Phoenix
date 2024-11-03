package tech.petrepopescu.phoenix.special;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Component
public class PhoenixSpecialElementsUtil {
    public String getCsrfTokenName() {
        CsrfToken csrfToken = getCsrfToken();
        if (csrfToken != null) {
            return csrfToken.getParameterName();
        }

        return "";
    }

    public String getCsrfHeaderName() {
        CsrfToken csrfToken = getCsrfToken();
        if (csrfToken != null) {
            return csrfToken.getHeaderName();
        }

        return "";
    }

    public String getCsrfTokenValue() {
        CsrfToken csrfToken = getCsrfToken();
        if (csrfToken != null) {
            return csrfToken.getToken();
        }

        return "";
    }

    private CsrfToken getCsrfToken() {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attributes != null) {
            // Access the HttpServletRequest
            HttpServletRequest request = attributes.getRequest();
            CsrfToken token = (CsrfToken) request.getAttribute(CsrfToken.class.getName());
            request.setAttribute(CsrfToken.class.getName(), token);
            request.setAttribute(token.getParameterName(), token);
            return token;
        }

        return null;
    }
}
