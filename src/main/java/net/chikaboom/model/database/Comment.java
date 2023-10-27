package net.chikaboom.model.database;

import jakarta.persistence.*;
import lombok.Data;

import java.sql.Timestamp;

import static net.chikaboom.util.constant.DbNamesConstant.*;

/**
 * Определяет модель таблицы Comment в базе данных
 */
@Entity(name = COMMENT)
@Data
public class Comment implements BaseEntity {

    /**
     * Идентификатор комментария
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = ID_COMMENT)
    private int idComment;

    /**
     * Аккаунт клиента, оставившего комментарий
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = ID_ACCOUNT_CLIENT)
    private Account accountClient;

    /**
     * Аккаунт мастера, получившего комментарий
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = ID_ACCOUNT_MASTER)
    private Account accountMaster;

    /**
     * Отражает, положительный комментарий или нет
     */
    @Column(name = IS_GOOD)
    private boolean isGood;

    /**
     * Дата комментария
     */
    @Column(name = DATE)
    private Timestamp date;

    /**
     * Текст комментария
     */
    @Column(name = TEXT)
    private String text;
}
