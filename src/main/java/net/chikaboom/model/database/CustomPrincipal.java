package net.chikaboom.model.database;

import lombok.Data;

import java.io.Serializable;

/**
 * Не хранится в базе. Используется для идентификации пользователя. Данные о пользователе сохраняются при авторизации
 * см. {@link net.chikaboom.service.CustomAuthenticationProvider}
 */
@Data
public class CustomPrincipal implements Serializable {

    private static final long serialVersionUID = 1l;

    private int idAccount;
    private int idUserDetails;

    public CustomPrincipal(int idAccount, int idUserDetails) {
        this.idAccount = idAccount;
        this.idUserDetails = idUserDetails;
    }
}
