package net.chikaboom.facade.converter;

import net.chikaboom.facade.dto.AppointmentFacade;
import net.chikaboom.model.database.Appointment;

import java.sql.Timestamp;

/**
 * DOCS {@link FacadeConverter}
 */
public final class AppointmentFacadeConverter implements FacadeConverter {

    private AppointmentFacadeConverter() {
    }

    /**
     * Поля:
     * id, appointmentDateTime, service(все, кроме serviceSubtypeFacade)
     */
    public static AppointmentFacade toDtoForDataTableInModal(Appointment model) {
        AppointmentFacade appointmentFacade = new AppointmentFacade();

        appointmentFacade.setIdAppointment(model.getIdAppointment());
        if (model.getAppointmentDateTime() != null) {
            appointmentFacade.setAppointmentDateTime((Timestamp) model.getAppointmentDateTime().clone());
        }
        if (model.getService() != null) {
            appointmentFacade.setServiceFacade(ServiceFacadeConverter.toDtoForAppointmentDataTable(model.getService()));
            appointmentFacade.getServiceFacade().setServiceSubtypeFacade(null);
        }

        return appointmentFacade;
    }

    /**
     * Поля:
     * id, appointmentDateTime, service, masterAccount, userDetailsClient
     */
    public static AppointmentFacade toDtoForAppointmentDataTable(Appointment model) {
        AppointmentFacade appointmentFacade = new AppointmentFacade();

        appointmentFacade.setIdAppointment(model.getIdAppointment());
        if (model.getAppointmentDateTime() != null) {
            appointmentFacade.setAppointmentDateTime((Timestamp) model.getAppointmentDateTime().clone());
        }
        if (model.getService() != null) {
            appointmentFacade.setServiceFacade(ServiceFacadeConverter.toDtoForAppointmentDataTable(model.getService()));
        }
        if (model.getMasterAccount() != null) {
            appointmentFacade.setMasterAccountFacade(AccountFacadeConverter.toDtoAppointmentDataTable(model.getMasterAccount()));
        }
        if (model.getUserDetailsClient() != null) {
            appointmentFacade.setUserDetailsFacadeClient(UserDetailsFacadeConverter.toDtoAppointmentDataTable(model.getUserDetailsClient()));
        }

        return appointmentFacade;
    }

    /**
     * Конвертирует объект базы данных в объект фасада - DTO
     *
     * @param model объект модели
     * @return объект фасада - DTO
     */
    public static AppointmentFacade convertToDto(Appointment model) {
        AppointmentFacade appointmentFacade = new AppointmentFacade();

        appointmentFacade.setIdAppointment(model.getIdAppointment());
        if (model.getAppointmentDateTime() != null) {
            appointmentFacade.setAppointmentDateTime((Timestamp) model.getAppointmentDateTime().clone());
        }
        if (model.getService() != null) {
            appointmentFacade.setServiceFacade(ServiceFacadeConverter.convertToDto(model.getService()));
        }
//        TODO warning place 4 really warning place. for master, client? only id? What endpoint
        if (model.getMasterAccount() != null) {
            appointmentFacade.setMasterAccountFacade(AccountFacadeConverter.toDtoForNotAccountUser(model.getMasterAccount()));
        }
        if (model.getUserDetailsClient() != null) {
            appointmentFacade.setUserDetailsFacadeClient(UserDetailsFacadeConverter.convertToDto(model.getUserDetailsClient()));
        }

        return appointmentFacade;
    }

    /**
     * Конвертирует объект фасада в объект модели
     *
     * @param facade объект фасада - DTO
     * @return объект модели
     */
    public static Appointment convertToModel(AppointmentFacade facade) {
        Appointment appointment = new Appointment();

        appointment.setIdAppointment(facade.getIdAppointment());
        if (facade.getAppointmentDateTime() != null) {
            appointment.setAppointmentDateTime((Timestamp) facade.getAppointmentDateTime().clone());
        }
        if (facade.getServiceFacade() != null) {
            appointment.setService(ServiceFacadeConverter.convertToModel(facade.getServiceFacade()));
        }
        if (facade.getMasterAccountFacade() != null) {
            appointment.setMasterAccount(AccountFacadeConverter.convertToModel(facade.getMasterAccountFacade()));
        }
        if (facade.getUserDetailsFacadeClient() != null) {
            appointment.setUserDetailsClient(UserDetailsFacadeConverter.convertToModel(facade.getUserDetailsFacadeClient()));
        }

        return appointment;
    }
}
