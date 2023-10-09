package net.chikaboom.model.database;

import jakarta.persistence.*;
import lombok.Data;

import static net.chikaboom.util.constant.DbNamesConstant.*;

/**
 * Определяет модель таблицы About в базе данных
 */
@Data
@Entity
@Table(name = ABOUT)
public class About implements BaseEntity, Cloneable {

    /**
     * id сущности about
     */
    @Id
    @Column(name = ID_ABOUT)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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
     * Краткое отражение вида деятельности мастера
     */
    @Column(name = PROFESSION)
    private String profession;

    @Override
    public About clone() {
        try {
            About clone = (About) super.clone();
            // TODO: copy mutable state here, so the clone can't change the internals of the original
            return clone;
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }
}
