package net.chikaboom.model.database;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import static net.chikaboom.util.constant.DbNamesConstant.*;

/**
 * Определяет модель таблицы PhoneCode в базе данных
 */
@Data
@Entity
@Table(name = PHONE_CODE)
public class PhoneCode implements BaseEntity {

    /**
     * id сущности в таблице phone_code
     */
    @Id
    @Column(name = ID_PHONE_CODE)
    private int idPhoneCode;

    /**
     * Имя страны, к которой относится данный код страны
     */
    @Column(name = COUNTRY_NAME)
    private String countryName;

    /**
     * Код телефона страны
     */
    @Column(name = PHONE_CODE)
    private int phoneCode;

    /**
     * Сокращенное название страны
     */
    @Column(name = COUNTRY_CUT)
    private String countryCut;
}
