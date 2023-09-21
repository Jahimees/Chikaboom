package net.chikaboom.model.database;

import jakarta.persistence.*;
import lombok.Data;

import java.sql.Time;

import static net.chikaboom.util.constant.DbNamesConstant.*;

@Entity
@Data
@Table(name = ACCOUNT_SETTINGS)
public class AccountSettings {

    @Id
    @Column(name = ID_ACCOUNT_SETTINGS)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idAccountSettings;

    @Column(name = IS_PHONE_VISIBLE)
    private boolean isPhoneVisible;

    @Column(name = DEFAULT_WORKING_DAY_START)
    private Time defaultWorkingDayStart;

    @Column(name = DEFAULT_WORKING_DAY_END)
    private Time defaultWorkingDayEnd;
}
