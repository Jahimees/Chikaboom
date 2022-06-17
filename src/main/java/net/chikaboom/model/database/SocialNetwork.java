package net.chikaboom.model.database;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import static net.chikaboom.util.constant.DbNamesConstant.*;

/**
 * Определяет модель таблицы social_network в базе данных
 */
@Data
@Entity
@Table(name = SOCIAL_NETWORK)
public class SocialNetwork {
    /**
     * id сущности в таблице social_network
     */
    @Id
    @Column(name = ID_SOCIAL_NETWORK)
    private int idSocialNetwork;

    /**
     * Ссылка на социальную сеть
     */
    @Column(name = LINK)
    private String link;

    /**
     * Внешний ключ к таблице account. Кому принадлежит ссылка на соц. сеть
     */
    @Column(name = ID_ACCOUNT)
    private int idAccount;
}
