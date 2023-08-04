package net.chikaboom.model.database;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
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
        switch (idRole) {
            case 1: {
                this.idRole = idRole;
                this.name = "ROLE_MASTER";
                break;
            }
            case 2: {
                this.idRole = idRole;
                this.name = "ROLE_CLIENT";
                break;
            }
            default:
                this.idRole = 2;
                this.name = "ROLE_CLIENT";
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

    @Transient
    @ManyToMany(mappedBy = ROLES)
    private Set<Account> accounts;

    @JsonIgnore
    @Override
    public String getAuthority() {
        return getName();
    }
}
