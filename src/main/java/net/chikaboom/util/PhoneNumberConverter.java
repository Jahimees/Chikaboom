package net.chikaboom.util;

public final class PhoneNumberConverter {

    private PhoneNumberConverter() {

    }

    public static String clearPhoneNumber(String dirtyPhoneNumber) {
        return dirtyPhoneNumber.replaceAll("\\D", "").replaceAll("\\s", "");
    }
}
