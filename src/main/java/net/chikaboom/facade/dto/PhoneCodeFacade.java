package net.chikaboom.facade.dto;

import lombok.Data;

@Data
public class PhoneCodeFacade implements Facade {

    /**
     * id сущности в таблице phone_code
     */
    private int idPhoneCode;

    /**
     * Имя страны, к которой относится данный код страны
     */
    private String countryName;

    /**
     * Код телефона страны
     */
    private int phoneCode;

    /**
     * Сокращенное название страны
     */
    private String countryCut;
}
