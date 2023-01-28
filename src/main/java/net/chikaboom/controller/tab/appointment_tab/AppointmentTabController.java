package net.chikaboom.controller.tab.appointment_tab;

import net.chikaboom.model.database.Appointment;
import net.chikaboom.service.ClientDataStorageService;
import net.chikaboom.service.action.AppointmentService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

/**
 * Обрабатывает запросы, связанные с вкладкой записей.
 * <p>
 * Существует две вкладки записей: "Мои записи" и "Записи ко мне".
 * Первая отвечает за те записи, на которые записался сам клиент. Вторая, соответственно, за записи к конкретному мастеру.
 */
@Controller
@RequestMapping("/chikaboom/personality/{idAccount}")
public class AppointmentTabController {

    @Value("${tab.appointment}")
    private String APPOINTMENT_TAB;
    @Value("${tab.my_appointment}")
    private String MY_APPOINTMENT_TAB;
    @Value("${attr.idAccountMaster}")
    private String ID_ACCOUNT_MASTER;
    @Value("${attr.idAccountClient}")
    private String ID_ACCOUNT_CLIENT;
    @Value("${attr.appointmentList}")
    private String APPOINTMENT_LIST;

    private final ClientDataStorageService clientDataStorageService;
    private final AppointmentService appointmentService;
    private final Logger logger = Logger.getLogger(this.getClass());

    @Autowired
    public AppointmentTabController(ClientDataStorageService clientDataStorageService, AppointmentService appointmentService) {
        this.clientDataStorageService = clientDataStorageService;
        this.appointmentService = appointmentService;
    }

    /**
     * Загружает вкладку записей к мастеру и передает на неё данные обо всех записях к мастеру.
     * Сохраняет параметры с клиента и передает управление сервису {@link  AppointmentService} для загрузки данных.
     *
     * @param idAccount идентификатор аккаунта
     * @return модель (содержит данные записей к мастеру) и представление (содержит путь к вкладке записей)
     */
    @GetMapping(value = "/appointment")
    public ModelAndView openAppointmentTab(@PathVariable int idAccount) {
        logger.info("Opening appointment tab.");
        ModelAndView modelAndView = new ModelAndView(APPOINTMENT_TAB);

        clientDataStorageService.setData(ID_ACCOUNT_MASTER, idAccount);

        List<Appointment> appointmentList = appointmentService.executeAndGetList();

        modelAndView.addObject(APPOINTMENT_LIST, appointmentList);

        return modelAndView;
    }

    /**
     * Загружает вкладку записей клиента и передает на неё данные обо всех записях клиента к мастерам/салонам.
     * Сохраняет параметры с клиента и передает управление сервису {@link  AppointmentService} для загрузки данных
     *
     * @param idAccount идентификатор аккаунта
     * @return модель (содержит данные записей клиента) и представление (содержит путь к вкладке моих записей)
     */
    @GetMapping(value = "/myappointment")
    public ModelAndView openMyAppointmentTab(@PathVariable int idAccount) {
        logger.info("Opening myappointment tab.");
        ModelAndView modelAndView = new ModelAndView(MY_APPOINTMENT_TAB);

        clientDataStorageService.setData(ID_ACCOUNT_CLIENT, idAccount);

        List<Appointment> appointmentList = appointmentService.executeAndGetClientList();

        modelAndView.addObject(APPOINTMENT_LIST, appointmentList);

        return modelAndView;
    }
}
