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

    private static final long serialVersionUID = 6815923411592154041l;

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

    @Transient
    private String oldPassword;

    /**
     * Имя пользователя
     */
    @Column(name = USERNAME)
    private String username;

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

    /**
     * Содержит все роли пользователя
     */
    @ManyToMany(fetch = FetchType.EAGER)
    private Set<Role> roles;

    /**
     * Адрес пользователя
     */
    @Column(name = ADDRESS)
    private String address;

    /**
     * Содержит подробную информацию об аккаунте
     */
    @OneToOne
    @JoinColumn(name = ID_USER_DETAILS)
    private net.chikaboom.model.database.UserDetails userDetails;

    /**
     * Содержит настройки аккаунта
     */
    @OneToOne
    @JoinColumn(name = ID_ACCOUNT_SETTINGS)
    private AccountSettings accountSettings;

    @JsonIgnore
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return getRoles();
    }

    @Override
    public String getUsername() {
        return username;
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

//    TODO Переделать
    public void clearPersonalFields() {
        password = null;
        oldPassword = null;
        registrationDate = null;
        email = null;
        accountSettings = null;
    }
}
