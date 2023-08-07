package net.chikaboom.model.database;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.sql.Timestamp;
import java.util.Collection;
import java.util.Set;

import static net.chikaboom.util.constant.DbNamesConstant.*;

/**
 * Определяет модель таблицы Account в базе данных
 */
@Data
@Entity
@Table(name = ACCOUNT)
@JsonIgnoreProperties(ignoreUnknown = true)
public class Account implements BaseEntity, UserDetails {

    /**
     * id аккаунта
     */
    @Id
    @Column(name = ID_ACCOUNT)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idAccount;

    /**
     * Пароль от аккаунта
     */
    @Column(name = PASSWORD)
    private String password;

    /**
     * Соль для пароля
     */
    @Column(name = SALT)
    private String salt;

    /**
     * Имя пользователя
     */
    @Column(name = NICKNAME)
    private String nickname;

    /**
     * Дата регистрации аккаунта
     */
    @Column(name = REGISTRATION_DATE)
    private Timestamp registrationDate;

    /**
     * Электронная почта пользователя
     */
    @Column(name = EMAIL)
    private String email;

    @ManyToMany(fetch = FetchType.EAGER)
    //    @JoinColumn(name = ID_ROLE)
    private Set<Role> roles;

    @Column(name = ADDRESS)
    private String address;

    @OneToOne
    @JoinColumn(name = ID_ABOUT)
    private About about;

    @OneToOne
    @JoinColumn(name = ID_SOCIAL_NETWORK)
    private SocialNetwork socialNetwork;

    @OneToOne
    @JoinColumn(name = ID_WORKING_DAYS)
    private WorkingDays workingDays;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = ID_PHONE_CODE)
    private PhoneCode phoneCode;

    /**
     * Номер телефона владельца аккаунта
     */
    @Column(name = PHONE)
    private String phone;

    @JsonIgnore
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return getRoles();
    }

    @JsonIgnore
    @Override
    public String getUsername() {
        return getNickname();
    }

    @JsonIgnore
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @JsonIgnore
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @JsonIgnore
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @JsonIgnore
    @Override
    public boolean isEnabled() {
        return true;
    }
}
