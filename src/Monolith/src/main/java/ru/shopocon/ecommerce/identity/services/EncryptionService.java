package ru.shopocon.ecommerce.identity.services;

public interface EncryptionService {

    String encrypt(String value);

    String decrypt(String value);
}
