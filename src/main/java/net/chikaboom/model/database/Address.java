package net.chikaboom.model.database;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import static net.chikaboom.util.constant.DbNamesConstant.*;

/**
 * Определяет модель таблицы Address в базе данных
 */
@Data
@Entity
@Table(name = ADDRESS)
public class Address implements BaseEntity {

    public Address() {

    }

    public Address(int idAccount) {
        this.idAccount = idAccount;
    }

    /**
     * id сущности в таблице Address
     */
    @Id
    @Column(name = ID_ADDRESS)
    private int idAddress;

    /**
     * Адрес, записанный в текстовом формате
     */
    @Column(name = ADDRESS)
    private String address;

    /**
     * Внешний ключ на account. Отражает какому аккаунту, принадлежит этот адрес
     */
    @Column(name = ID_ACCOUNT)
    private int idAccount;
}
