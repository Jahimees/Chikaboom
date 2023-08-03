package net.chikaboom.util;

/**
 * Конвертирует номер телефона для удобного хранения
 */
public final class PhoneNumberConverter {

    private PhoneNumberConverter() {
    }

    public static String clearPhoneNumber(String dirtyPhoneNumber) {
        return dirtyPhoneNumber.replaceAll("\\D", "").replaceAll("\\s", "");
    }
}
