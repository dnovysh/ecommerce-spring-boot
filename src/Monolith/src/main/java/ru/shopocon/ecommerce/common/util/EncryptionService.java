package ru.shopocon.ecommerce.common.util;

public interface EncryptionService {

    String encrypt(String value);

    String decrypt(String value);
}
