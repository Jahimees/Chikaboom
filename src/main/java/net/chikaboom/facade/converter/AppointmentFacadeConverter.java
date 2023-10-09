package net.chikaboom.facade.converter;

import lombok.RequiredArgsConstructor;
import net.chikaboom.facade.dto.AppointmentFacade;
import net.chikaboom.model.database.Appointment;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;

@Component
@RequiredArgsConstructor
public class AppointmentFacadeConverter implements FacadeConverter<AppointmentFacade, Appointment> {

    private final ServiceFacadeConverter serviceFacadeConverter;
    private final AccountFacadeConverter accountFacadeConverter;
    private final UserDetailsFacadeConverter userDetailsFacadeConverter;

    @Override
    public AppointmentFacade convertToDto(Appointment model) {
        AppointmentFacade appointmentFacade = new AppointmentFacade();

        appointmentFacade.setIdAppointment(model.getIdAppointment());
        appointmentFacade.setAppointmentDateTime((Timestamp) model.getAppointmentDateTime().clone());
        appointmentFacade.setServiceFacade(serviceFacadeConverter.convertToDto(model.getService()));
        appointmentFacade.setMasterAccountFacade(accountFacadeConverter.convertToDto(model.getMasterAccount()));
        appointmentFacade.setUserDetailsFacadeClient(userDetailsFacadeConverter.convertToDto(model.getUserDetailsClient()));

        return appointmentFacade;
    }

    @Override
    public Appointment convertToModel(AppointmentFacade facade) {
        Appointment appointment = new Appointment();

        appointment.setIdAppointment(facade.getIdAppointment());
        appointment.setAppointmentDateTime((Timestamp) facade.getAppointmentDateTime().clone());
        appointment.setService(serviceFacadeConverter.convertToModel(facade.getServiceFacade()));
        appointment.setMasterAccount(accountFacadeConverter.convertToModel(facade.getMasterAccountFacade()));
        appointment.setUserDetailsClient(userDetailsFacadeConverter.convertToModel(facade.getUserDetailsFacadeClient()));

        return appointment;
    }
}
