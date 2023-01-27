package net.chikaboom.controller.tab.appointment_tab;

import net.chikaboom.model.database.Appointment;
import net.chikaboom.service.ClientDataStorageService;
import net.chikaboom.service.action.AppointmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

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

    @Autowired
    public AppointmentTabController(ClientDataStorageService clientDataStorageService, AppointmentService appointmentService) {
        this.clientDataStorageService = clientDataStorageService;
        this.appointmentService = appointmentService;
    }

    @GetMapping(value = "/appointment")
    public ModelAndView openAppointmentTab(@PathVariable int idAccount) {
        ModelAndView modelAndView = new ModelAndView(APPOINTMENT_TAB);

        clientDataStorageService.setData(ID_ACCOUNT_MASTER, idAccount);

        List<Appointment> appointmentList = appointmentService.executeAndGetList();

        modelAndView.addObject(APPOINTMENT_LIST, appointmentList);

        return modelAndView;
    }

    @GetMapping(value = "/myappointment")
    public ModelAndView openMyAppointmentTab(@PathVariable int idAccount) {
        ModelAndView modelAndView = new ModelAndView(MY_APPOINTMENT_TAB);

        clientDataStorageService.setData(ID_ACCOUNT_CLIENT, idAccount);

        List<Appointment> appointmentList = appointmentService.executeAndGetClientList();

        modelAndView.addObject(APPOINTMENT_LIST, appointmentList);

        return modelAndView;
    }
}
