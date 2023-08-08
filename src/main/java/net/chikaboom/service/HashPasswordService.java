package net.chikaboom.service;

import com.google.common.hash.Hashing;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.util.HashMap;
import java.util.Map;


/**
 * Данный сервис предназначен для хэширования пароля с целью повышения безопасности.
 * Помимо обычного хэширования пароли "засаливаются"
 *
 * @deprecated Более не используется из-за использования {@link org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder}.
 * Однако может пригодится в будущих проектах
 */
@Deprecated
@Service
public class HashPasswordService {

    @Value("${salted_password}")
    private String SALTED_PASSWORD;
    @Value("${converted_password}")
    private String CONVERTED_PASSWORD;
    @Value("${salt}")
    private String SALT;

    /**
     * Ковертирует пароль для его дальнейшего хранения в базе данных.
     * <p>
     * Отличие от функции конвертирования для сравнения с паролем из базы заключается в генерации случайной соли,
     * которая будет использоваться для конвертации
     *
     * @param password "чистый" пароль
     * @return параметры 'salt', 'convertedPassword'
     */
    public Map<String, Object> convertPasswordForStorage(String password) {
        Map<String, Object> complexPasswordEO = saltPassword(password);
        convertPassword(complexPasswordEO);

        return complexPasswordEO;
    }

    /**
     * Ковертирует пароль для его дальнейшего сравнения с паролем из базы данных.
     * <p>
     * Отличие от функции конвертирования для хранения заключается в передаче "соли",
     * которая была использована при первоначальной конвертации
     *
     * @param password введенный пользователем пароль
     * @param salt     соль, которая была использована при конвертации для хранения
     * @return хэшированный пароль
     */
    public String convertPasswordForComparing(String password, String salt) {
        Map<String, Object> complexPasswordEO = saltPassword(password, salt);
        convertPassword(complexPasswordEO);

        return complexPasswordEO.get(CONVERTED_PASSWORD).toString();
    }

    /**
     * Производит хэширование пароля и устанавливает передаваемому объекту соответствующее поле
     */
    private void convertPassword(Map<String, Object> complexPassword) {
        String convertedPassword = hashPassword(complexPassword.get(SALTED_PASSWORD).toString());
        complexPassword.put(CONVERTED_PASSWORD, convertedPassword);
        complexPassword.remove(SALTED_PASSWORD);
    }

    /**
     * Засаливает пароль путем добавления случайной комбинации символов
     *
     * @param password "чистый" пароль
     * @return "засоленный" пароль
     */
    private Map<String, Object> saltPassword(String password) {
        String salt = generateSalt();

        return saltPassword(password, salt);
    }

    /**
     * Засаливает пароль заранее известной солью
     *
     * @param password "чистый" пароль
     * @param salt     соль, случайная комбинация символов
     * @return "засоленный" пароль
     */
    private Map<String, Object> saltPassword(String password, String salt) {
        String saltedPassword = password + salt;

        Map<String, Object> complexPasswordEO = new HashMap<>();
        complexPasswordEO.put(SALTED_PASSWORD, saltedPassword);
        complexPasswordEO.put(SALT, salt);

        return complexPasswordEO;
    }

    /**
     * Хэширует пароль алгоритмом SHA256
     *
     * @param saltedPassword "засоленный" пароль
     * @return хэш пароля
     */
    private String hashPassword(String saltedPassword) {
        return Hashing.sha256().hashString(saltedPassword).toString();
    }

    /**
     * Генерирует случайную последовательность символов - соль
     *
     * @return случайная последовательность символов (соль)
     */
    private String generateSalt() {
        SecureRandom random = new SecureRandom();
        byte[] bytes = new byte[10];
        random.nextBytes(bytes);

        return new String(bytes, StandardCharsets.UTF_8);
    }
}
