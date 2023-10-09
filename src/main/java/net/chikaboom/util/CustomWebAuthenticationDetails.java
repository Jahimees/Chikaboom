package net.chikaboom.util;

import jakarta.servlet.http.HttpServletRequest;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.web.authentication.WebAuthenticationDetails;

/**
 * Кастомное определение деталей в объекте {@link org.springframework.security.core.Authentication}
 * Добавляет одно новое поле, определяющее, пришёл запрос с UI или нет
 */
@Getter
@Setter
public class CustomWebAuthenticationDetails extends WebAuthenticationDetails {

    private boolean isRequestFromUI;

    public CustomWebAuthenticationDetails(HttpServletRequest request) {
        super(request);
        this.isRequestFromUI = Boolean.parseBoolean(request.getParameter("isRequestFromUI"));
    }

}
