package com.enlace.lattemilkcollection;

import java.util.UUID;

public class UUIDGenerator {

    public static void main(String[] args) {
        String uuid = generate12CharUUID();
        System.out.println(uuid);
    }

    public static String generate12CharUUID() {
        // Generate a UUID
        UUID uuid = UUID.randomUUID();

        // Convert the UUID to a string without hyphens
        String uuidStr = uuid.toString().replace("-", "");

        // Ensure the string is at least 30 characters long
        if (uuidStr.length() >= 30) {
            return uuidStr.substring(0, 30);
        } else {
            throw new IllegalArgumentException("Generated UUID is less than 12 characters");
        }
    }
}
