package net.chikaboom.service;

import com.google.common.hash.Hashing;
import net.chikaboom.model.ExpandableObject;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;

/**
 * Данный сервис предназначен для хэширования пароля с целью повышения безопасности.
 * Помимо обычного хэширования пароли "засаливаются"
 */
@Service
public class HashPasswordService {

    /**
     * Хэширует пароль
     *
     * @param password пароль ("засоленный")
     * @return хэш пароля
     */
    public String hashPassword(String password) {
        String convertedPassword = Hashing.sha256().hashString(password).toString();

        return convertedPassword;
    }

    /**
     * Засаливает пароль путем добавления случайной комбинации символов
     *
     * @param password "чистый" пароль
     * @return "засоленный" пароль
     */
    public ExpandableObject saltPassword(String password) {
        String salt = generateSalt();
        String saltedPassword = password + salt;

        ExpandableObject expandableObject = new ExpandableObject();
        expandableObject.setField("saltedPassword", saltedPassword);
        expandableObject.setField("salt", salt);

        return expandableObject;
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
