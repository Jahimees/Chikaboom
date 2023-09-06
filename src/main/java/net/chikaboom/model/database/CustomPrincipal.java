package net.chikaboom.model.database;

import lombok.Data;

import java.io.Serializable;

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
