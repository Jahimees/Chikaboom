package net.chikaboom.model.database;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import static net.chikaboom.util.constant.DbNamesConstant.*;

/**
 * Определяет модель таблицы About в базе данных
 */
@Data
@Entity
@Table(name = ABOUT)
public class About implements BaseEntity {

    public About() {

    }

    public About(int idAccount) {
        this.idAccount = idAccount;
    }

    /**
     * id сущности about
     */
    @Id
    @Column(name = ID_ABOUT)
    private int idAbout;

    /**
     * Текст, содержащийся в разделе "о себе"
     */
    @Column(name = TEXT)
    private String text;

    /**
     * Набор тегов, определенных пользователем. Разделитель - ','
     */
    @Column(name = TAGS)
    private String tags;

    /**
     * Внешний ключ к таблице Account
     */
    @Column(name = ID_ACCOUNT)
    private int idAccount;

    /**
     * Краткое отражение вида деятельности мастера
     */
    @Column(name = PROFESSION)
    private String profession;
}
