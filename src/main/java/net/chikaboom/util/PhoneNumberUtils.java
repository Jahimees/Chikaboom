package net.chikaboom.util;

import com.google.i18n.phonenumbers.NumberParseException;
import com.google.i18n.phonenumbers.PhoneNumberUtil;

/**
 * Конвертирует номер телефона для удобного хранения
 */
public final class PhoneNumberUtils {

    private static final PhoneNumberUtil phoneUtil = PhoneNumberUtil.getInstance();

    private PhoneNumberUtils() {
    }

    /**
     * Приводит номер телефона в формат интернационального отображения:
     * 80291232323 -> +375 29 123-23-23
     *
     * @param phoneNumber номер телефона
     * @param countryCut  сокращенное буквенное обозначение кода страны (напр. 'by', 'ru')
     * @return отформатированный номер телефона
     * @throws NumberParseException возникает, если невозможно отформатировать номер телефона
     */
    public static String formatNumberInternational(String phoneNumber, String countryCut) throws NumberParseException {
        if (phoneNumber == null || countryCut == null) {
            throw new NumberParseException(NumberParseException.ErrorType.NOT_A_NUMBER, "phone number and|or country code are|is empty");
        }

        return phoneUtil.format(
                phoneUtil.parse(phoneNumber, countryCut.toUpperCase()),
                PhoneNumberUtil.PhoneNumberFormat.INTERNATIONAL);
    }
}
