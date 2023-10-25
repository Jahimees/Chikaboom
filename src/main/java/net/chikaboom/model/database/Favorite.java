package net.chikaboom.model.database;

import jakarta.persistence.*;
import lombok.Data;

import static net.chikaboom.util.constant.DbNamesConstant.*;

/**
 * Определяет модель таблицы Favorite в базе данных
 */
@Data
@Entity
@Table(name = FAVORITE)
public class Favorite implements BaseEntity {

    /**
     * Идентификатор сущности в таблице
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = ID_FAVORITE)
    private int idFavorite;

    /**
     * Аккаунт (субъект), который владеет избранной сущностью
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = ID_FAVORITE_OWNER)
    private Account favoriteOwner;

    /**
     * Аккаунт (объект), кто является объектом избранной сущности
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = ID_FAVORITE_MASTER)
    private Account favoriteMaster;
}
