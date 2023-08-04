package net.chikaboom.model.database;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.Data;

import static net.chikaboom.util.constant.DbNamesConstant.*;

@Data
@Entity
@Table(name = ACCOUNT_ROLES)
@JsonIgnoreProperties(ignoreUnknown = true)
public class AccountRoles {

    @Id
    @Column(name = ID_ACCOUNT_ROLES)
    private int idAccountRoles;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = ID_ACCOUNT)
    private Account account;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = ID_ROLE)
    private Role role;
}
