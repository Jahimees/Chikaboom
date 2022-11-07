package net.chikaboom.model.client;

import lombok.Data;

import java.sql.Timestamp;

@Data
public class AccountInformation {

    public AccountInformation() {

    }

    public AccountInformation(int idAccount, int phoneCode, String phone, String nickname, Timestamp registrationDate,
                              String role, String email, String address, String aboutTags, String aboutText, String profession) {
            this.idAccount = idAccount;
            this.phoneCode = phoneCode;
            this.phone = phone;
            this.nickname = nickname;
            this.registrationDate = registrationDate;
            this.role = role;
            this.email = email;
            this.address = address;
            this.aboutTags = aboutTags;
            this.aboutText = aboutText;
            this.profession = profession;
    }

    /**
     * id аккаунта
     */
    private int idAccount;

    /**
     * Код телефона страны
     */
    private int phoneCode;

    /**
     * Номер телефона владельца аккаунта
     */
    private String phone;

    /**
     * Имя пользователя
     */
    private String nickname;

    /**
     * Дата регистрации аккаунта
     */
    private Timestamp registrationDate;

    /**
     * Роль пользователя
     */
    private String role;

    /**
     * Электронная почта пользователя
     */
    private String email;

    /**
     * Адрес пользователя в строковом представлении
     */
    private String address;

    private String aboutTags;

    private String aboutText;

    private String profession;

}
