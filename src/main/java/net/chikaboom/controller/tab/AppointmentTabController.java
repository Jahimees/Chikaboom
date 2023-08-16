package net.chikaboom.controller.tab;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Отвечает за отрисовку вкладкок записей
 * <p>
 * На самой вкладке существуют две подвкладки записей: "Мои записи" и "Записи ко мне".
 * Первая отвечает за те записи, на которые записался сам клиент. Вторая, соответственно, за записи к конкретному мастеру.
 */
@Controller
@RequestMapping("/chikaboom/personality/{idAccount}")
public class AppointmentTabController {

    @Value("${tab.appointment}")
    private String APPOINTMENT_TAB;
    @Value("${tab.outcome_appointment}")
    private String OUTCOME_APPOINTMENT_TAB;
    @Value("${tab.income_appointment}")
    private String INCOME_APPOINTMENT_TAB;

    private final Logger logger = Logger.getLogger(this.getClass());

    /**
     * Открывает главную вкладку записей
     *
     * @param idAccount идентификатор аккаунта, чьи записи необходимо открыть
     * @return путь к вкладке записей
     */
    @PreAuthorize("#idAccount == authentication.principal.idAccount")
    @GetMapping(value = "/appointment")
    public String openAppointmentTab(@PathVariable int idAccount) {
        logger.info("Opening appointment tab.");

        return APPOINTMENT_TAB;
    }

    /**
     * Открывает подвкладку исходящих записей. Т.е. те записи, на которые записался клиент
     *
     * @param idAccount идентификатор аккаунта
     * @return путь к подвкладке исходящих записей
     */
    @PreAuthorize("#idAccount == authentication.principal.idAccount")
    @GetMapping("/appointment/outcome")
    public String openOutcomeAppointmentTab(@PathVariable int idAccount) {
        logger.info("Opening outcome appointment tab");

        return OUTCOME_APPOINTMENT_TAB;
    }

    /**
     * Открывает подвкладку входящих записей. Т.е. те записи, на которые записались к мастеру
     *
     * @param idAccount идентификатор аккаунта
     * @return путь к подвкладке входящих записей
     */
    @PreAuthorize("#idAccount == authentication.principal.idAccount")
    @GetMapping("/appointment/income")
    public String openIncomeAppointmentTab(@PathVariable int idAccount) {
        logger.info("Opening income appointment tab");

        return INCOME_APPOINTMENT_TAB;
    }
}
