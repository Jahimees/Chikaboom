package net.chikaboom.model.database;

import jakarta.persistence.*;
import lombok.Data;

import static net.chikaboom.util.constant.DbNamesConstant.*;

@Entity
@Table(name = USER_FILE)
@Data
public class UserFile implements BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = ID_USER_FILE)
    private int idUserFile;

    @JoinColumn(name = ID_ACCOUNT)
    @ManyToOne(fetch = FetchType.LAZY)
    private Account account;

    @Column(name = FILE_PATH)
    private String filePath;
}
