package ro.ase.ism.sap.paulatintareanu.passwords;

import org.bouncycastle.jce.provider.BouncyCastleProvider;

import java.io.*;
import java.security.*;

public class TestLostPassword {

    public static void main(String[] args) throws IOException, NoSuchAlgorithmException, NoSuchProviderException {

        final String HASH_VALUE = "5810504ecc963188d15c39ed514f901c4dc522b725c85ee3a54f9a6d2c1e9582";
        final String PREFIX = "ismsap";

        String BouncyCastleProvider = "BC";
        Security.addProvider(new BouncyCastleProvider());
        MessageDigest md5 = MessageDigest.getInstance("MD5", "BC");
        MessageDigest sha256 = MessageDigest.getInstance("SHA-256", "BC");

        File file = new File("ignis-10M.txt");
        if (!file.exists()) { System.out.println("\nFile not found"); }
        FileReader fileReader = new FileReader(file);
        BufferedReader bufferedReader = new BufferedReader(fileReader);
        String line = null;

        long tstart = System.currentTimeMillis();
        System.out.println("Loading...");
        // brute force
        do {
            line = bufferedReader.readLine();
            if (line != null) {
                String prefixed = PREFIX + line;
                byte[] value1 = md5.digest(prefixed.getBytes());
                byte[] value2 = sha256.digest(value1);

                StringBuilder sb = new StringBuilder();
                for (byte b : value2) {
                    sb.append(String.format("%02x", b));
                }

                if (sb.toString().equals(HASH_VALUE)) {
                    System.out.println("Found password: " + line);
                    break;
                }
            }
        } while (line != null);

        long tfinal = System.currentTimeMillis();
        System.out.println("Duration is: " + (tfinal - tstart));

        fileReader.close();
    }
}