package net.chikaboom.model.database;

import jakarta.persistence.*;
import lombok.Data;

import java.sql.Timestamp;

import static net.chikaboom.util.constant.DbNamesConstant.*;

@Entity(name = COMMENT)
@Data
public class Comment implements BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = ID_COMMENT)
    private int idComment;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = ID_ACCOUNT_CLIENT)
    private Account accountClient;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = ID_ACCOUNT_MASTER)
    private Account accountMaster;

    @Column(name = IS_GOOD)
    private boolean isGood;

    @Column(name = DATE)
    private Timestamp date;

    @Column(name = TEXT)
    private String text;
}
