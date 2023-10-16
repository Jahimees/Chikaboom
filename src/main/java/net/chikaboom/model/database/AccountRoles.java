package net.chikaboom.model.database;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.Data;

import static net.chikaboom.util.constant.DbNamesConstant.*;

/**
 * Промежуточная сущность между {@link Account} и {@link Role}, чтобы избежать связи "многие-ко-многим"
 */
@Data
@Entity
@Table(name = ACCOUNT_ROLES)
@JsonIgnoreProperties(ignoreUnknown = true)
public class AccountRoles implements BaseEntity {

    /**
     * Идентификатор сущности
     */
    @Id
    @Column(name = ID_ACCOUNT_ROLES)
    private int idAccountRoles;

    /**
     * Ссылка на аккаунт
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = ID_ACCOUNT)
    private Account account;

    /**
     * Ссылка на роль
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = ID_ROLE)
    private Role role;
}
