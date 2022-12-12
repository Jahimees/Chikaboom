package net.chikaboom.model.database;

import lombok.Data;

import javax.persistence.*;

import static net.chikaboom.util.constant.DbNamesConstant.*;

@Data
@Entity
@Table(name = APPOINTMENT)
public class Appointment implements BaseEntity {

    @Id
    @Column(name = ID_APPOINTMENT)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idAppointment;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = ID_ACCOUNT_MASTER)
    private Account masterAccount;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = ID_ACCOUNT_CLIENT)
    private Account clientAccount;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = ID_USER_SERVICE)
    private UserService userService;

    @Column(name = APPOINTMENT_DATE)
    private String appointmentDate;

    @Column(name = APPOINTMENT_TIME)
    private String appointmentTime;

}
