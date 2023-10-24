package net.chikaboom.model.database;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.Fetch;

import static net.chikaboom.util.constant.DbNamesConstant.*;

@Data
@Entity
@Table(name = FAVORITE)
public class Favorite implements BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = ID_FAVORITE)
    private int idFavorite;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = ID_FAVORITE_OWNER)
    private Account favoriteOwner;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = ID_FAVORITE_MASTER)
    private Account favoriteMaster;
}
