package net.chikaboom.model.database;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import static net.chikaboom.util.constant.DbNamesConstant.ID_ROLE;
import static net.chikaboom.util.constant.DbNamesConstant.ROLE;

/**
 * Определяет модель таблицы Role в базе данных
 */
@Data
@Entity
@Table(name = ROLE)
public class Role implements BaseEntity {

    public Role() {

    }

    public Role(int idRole) {
        switch (idRole) {
            case 1: {
                this.idRole = idRole;
                this.role = "master";
                break;
            }
            case 2: {
                this.idRole = idRole;
                this.role = "client";
                break;
            }
            default:
                this.idRole = 2;
                role = "client";
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
    @Column(name = ROLE)
    private String role;
}
