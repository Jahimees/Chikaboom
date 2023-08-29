package net.chikaboom.model.database;

import jakarta.persistence.*;
import lombok.Data;

import static net.chikaboom.util.constant.DbNamesConstant.*;

@Data
@Table(name = "user_details")
@Entity
public class UserDetails implements BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "iduser_details")
    private int idUserDetails;

    @OneToOne
    @JoinColumn(name = "idaccount_owner")
    private Account masterOwner;

    /**
     * Общедоступная информация о пользователе
     */
    @OneToOne
    @JoinColumn(name = ID_ABOUT)
    private About about;

    /**
     * Ссылки на социальные сети пользователя
     */
    @OneToOne
    @JoinColumn(name = ID_SOCIAL_NETWORK)
    private SocialNetwork socialNetwork;

    /**
     * Код телефона пользователя
     */
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = ID_PHONE_CODE)
    private PhoneCode phoneCode;

    /**
     * Номер телефона владельца аккаунта
     */
    @Column(name = PHONE)
    private String phone;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

}
