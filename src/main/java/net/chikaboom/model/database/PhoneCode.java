package net.chikaboom.model.database;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

import static net.chikaboom.util.constant.DbNamesConstant.*;

/**
 * Определяет модель таблицы PhoneCode в базе данных
 */
@Data
@Entity
@Table(name = PHONE_CODE)
public class PhoneCode implements BaseEntity {

    private static final long serialVersionUID = 1;

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
