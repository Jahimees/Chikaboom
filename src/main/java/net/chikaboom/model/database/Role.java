package net.chikaboom.model.database;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import net.chikaboom.util.constant.ApplicationRole;
import org.springframework.security.core.GrantedAuthority;

import java.util.Set;

import static net.chikaboom.util.constant.DbNamesConstant.*;

/**
 * Определяет модель таблицы Role в базе данных
 */
@Data
@Entity
@Table(name = ROLE)
public class Role implements BaseEntity, GrantedAuthority {

    public Role() {

    }

    public Role(int idRole) {
        this.idRole = idRole;
        switch (idRole) {
            case 1: {
                this.name = ApplicationRole.ROLE_MASTER.name();
                break;
            }
            case 2: {
                this.name = ApplicationRole.ROLE_CLIENT.name();
                break;
            }
            default:
                this.idRole = 2;
                this.name = ApplicationRole.ROLE_CLIENT.name();
        }
    }

    /**
     * id сущности в таблице role
     */
    @Id
    @Column(name = ID_ROLE)
    private int idRole;

    /**
     * Имя роли пользователя
     */
    @Column(name = NAME)
    private String name;

    /**
     * Ссылки на аккаунты, у которых есть данная роль
     */
    @Transient
    @ManyToMany(mappedBy = ROLES)
    private Set<Account> accounts;

    @JsonIgnore
    @Override
    public String getAuthority() {
        return getName();
    }
}
