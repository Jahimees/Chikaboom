package net.chikaboom.model.database;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

import static net.chikaboom.util.constant.DbNamesConstant.*;

/**
 * Определяет модель таблицы social_network в базе данных
 */
@Data
@Entity
@Table(name = SOCIAL_NETWORK)
public class SocialNetwork implements BaseEntity {

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
}
