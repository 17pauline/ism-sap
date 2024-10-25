package cryptography;

import java.util.Base64;

public class TestEncoding {

    public static void printHex(byte[] values) {
        System.out.println("\nHEX: ");
        for (byte b : values) {
            System.out.printf("%02x ", b);
        }
    }

    public static void main(String[] args) {
        // When we want to send binary data to a destination, we need to manage it as strings
        byte[] values = new byte[] {(byte)0x00, (byte)0x01, (byte)0x30, (byte)0x62}; // the first 2 do not have a correspondent in ASCII, 0x30 = 0
        byte[] values2 = new byte[] {(byte)0x00, (byte)0x02, (byte)0x30, (byte)0x62};

        // Converting to String
        // WRONG, this will give an error result
        String stringValues = new String(values);
        System.out.println("Values: " + stringValues);
        String stringValues2 = new String(values2);
        System.out.println("Values: " + stringValues2);
        // they look the same:  0b
        // BUT we know that they are NOT the same
        // string tries to approximate the values that do not have a correspondent in ASCII

        // Encoding using Base64
        // RIGHT way
        String values1Base64 = Base64.getEncoder().encodeToString(values);
        System.out.println("Encoded values: " + values1Base64); // AAEwYg==
        String values2Base64 = Base64.getEncoder().encodeToString(values2);
        System.out.println("Encoded values: " + values2Base64); // AAIwYg==

        // Decoding
        byte[] initialValues = Base64.getDecoder().decode(values1Base64);
        System.out.println("\nDecoded values (original array): ");
        printHex(initialValues); // 00 01 30 62
    }
}
