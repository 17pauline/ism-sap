package cryptography;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;

public class TestOTP {

    public static byte[] generateRandomKey(int keySizeInBytes) throws NoSuchAlgorithmException {
        SecureRandom secureRandom = SecureRandom.getInstance("SHA1PRNG");
        byte[] random = new byte[keySizeInBytes];
        secureRandom.nextBytes(random);
        return random;
    }

    public static byte[] otpEncryptDecrypt(byte[] plaintext, byte[] key) {
        if (plaintext.length != key.length) {
            throw new UnsupportedOperationException("Key and message must be same sized");
        }
        byte[] cipher = new byte[plaintext.length];

        for (int i = 0; i < plaintext.length; i++) {
            cipher[i] = (byte) ((byte) plaintext[i] ^ (byte) key[i]); // java casts to byte so i want to make sure it's processing bits at byte level so we add (byte) plaintext[i] ^ (byte) key[i]
        }
        return cipher;
    }

    public static void main(String[] args) throws NoSuchAlgorithmException {
        String message = "The requirements for tomorrow are...";
        byte[] randomKey = generateRandomKey(message.length());
        System.out.println("Random key: " + Utils.getHex(randomKey));

        byte[] encryptedMessage = otpEncryptDecrypt(message.getBytes(), randomKey);

        // NO
        // String randomKeyString = new String(randomKey);
        // YES -> Remember if you want to store the key somewhere you need to convert it to Base64 to conserve the binary format
        String randomKeyString = Base64.getEncoder().encodeToString(randomKey);
        System.out.println("Random key: " + randomKeyString);

        System.out.println("Ciphertext: " + Utils.getHex(encryptedMessage));

        // decryption
        byte[] initialMessage = otpEncryptDecrypt(encryptedMessage, randomKey);
        String initialMsg = new String(initialMessage);
        System.out.println("Initial message: " + initialMsg);
    }
}
