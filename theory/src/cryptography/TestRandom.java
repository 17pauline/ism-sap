package cryptography;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

public class TestRandom {

    public static void printHex(byte[] values) {
        System.out.println("\nHEX: ");
        for (byte b : values) {
            System.out.printf("%02x ", b);
        }
    }

    public static void main(String[] args) throws NoSuchAlgorithmException {

        // Using crypto safe PRNGs
        // Do NOT use the Random class !
        SecureRandom secureRandom = SecureRandom.getInstance("SHA1PRNG");
        byte[] desKey = new byte[8];

//        secureRandom.nextBytes(desKey);
//        printHex(desKey); // 27 0a 32 c4 e1 33 36 d3
        // if this is a KEY, we do NOT use a seed, because we do not need to compute this exact value again

        // using a seed
        // if we call the nextBytes method *before* setting the seed, we will NOT get the same hex
        secureRandom.setSeed(new byte[]{(byte)0xFF, (byte)0xA8});
        secureRandom.nextBytes(desKey);
        printHex(desKey); // d9 9d c6 ae f3 71 ff 80

        // this will produce the same key as before -- the destination
        SecureRandom secureRandom2 = SecureRandom.getInstance("SHA1PRNG");
        byte[] desKey2 = new byte[8];
        secureRandom2.setSeed(new byte[]{(byte)0xFF, (byte)0xA8});
        secureRandom2.nextBytes(desKey2);
        printHex(desKey2); // d9 9d c6 ae f3 71 ff 80

    }
}
