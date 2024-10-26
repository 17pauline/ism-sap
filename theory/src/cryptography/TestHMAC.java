package cryptography;

import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.*;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

public class TestHMAC {

    // HMAC requires a key
    public static byte[] getHMAC(String fileName, String algorithm, String password) throws NoSuchAlgorithmException, InvalidKeyException, IOException {
        Mac hmac = Mac.getInstance(algorithm);
        SecretKeySpec key = new SecretKeySpec(password.getBytes(), algorithm);
        hmac.init(key);

        // Read the file and process it
        File inputFile = new File(fileName);
        if (!inputFile.exists()) {
            throw new UnsupportedOperationException("File is missing");
        }
        FileInputStream fis = new FileInputStream(inputFile); // open it at byte level
        BufferedInputStream bis = new BufferedInputStream(fis);

        // Define a buffer
        // reading bigger buffers (1KB, 100MB, etc.) is faster than reading smaller ones
        // here we use very small ones
        byte[] buffer = new byte[8];
        while (true) {
            // !!!
            // generaly we don't have files which are multiple of 8s
            // hash algorithms don't need padding
            // but if you read the last block (let's say it has 6 bytes) and it reaches the EOF, you will have 2 bytes left to read
            // and you will get a wrong hash because it will try to read them even if they are residual values
            int noBytes = bis.read(buffer); // they will try to read as much as the buffer
            if (noBytes == -1) { // reached EOF
                break;
            }
            // process buffer
            // start with index 0
            // process the exact number of bytes that you read
            hmac.update(buffer, 0, noBytes);
        }

        fis.close();

        byte[] result = hmac.doFinal();
        return result;
    }

    // PBKDF - store user passwords in databases; Password-Based Encryption
    // to prevent rainbow attacks (attacker sees the same hash and deduces the input) -> use salts
    // number of iterations -> on my server, this will take half a second to compute the hash PER PASS
    // output size in bits
    public static byte[] getPBKDF(String userPass, String algorithm, String salt, int noIterations, int outputSize) throws NoSuchAlgorithmException, InvalidKeySpecException {
        PBEKeySpec pbeKeySpec = new PBEKeySpec(userPass.toCharArray(), salt.getBytes(), noIterations, outputSize); // we need the password as a char[] and salt as a byte[]
        SecretKeyFactory pbkdf = SecretKeyFactory.getInstance(algorithm);
        SecretKey key = pbkdf.generateSecret(pbeKeySpec);

        return key.getEncoded(); // return the byte array without any processing
    }

    public static void main(String[] args) throws NoSuchAlgorithmException, IOException, InvalidKeyException, InvalidKeySpecException {

        // Test HMAC
        // Creating a file and recomputing the hmac for it
        byte[] hmac = getHMAC("Message-HMAC.txt", "HmacSHA1", "ism1234");
        System.out.println("HMAC: " + Utils.getHex(hmac));
        // same input (file content), same algorithm, same key -> same output, always
        byte[] hmac2 = getHMAC("Message-HMAC.txt", "HmacSHA1", "ism12345");
        System.out.println("HMAC: " + Utils.getHex(hmac2));

        // Test PBKDF
        byte[] key = getPBKDF("ism1234", "PBKDF2WithHmacSHA1", "rd@h1", 1000, 160); // if you wanna store this byte array you do it in Base64 since it's binary
        System.out.println("Value stored in database: " + Utils.getHex(key));
        byte[] key2 = getPBKDF("ism1234", "PBKDF2WithHmacSHA1", "rd@h2", 1000, 160);
        System.out.println("Value stored in database: " + Utils.getHex(key2));

        // benchmark PBKDF performance
        int noIterations = 100000; // different number of iterations also changes the hash
        double tstart = System.currentTimeMillis();
        key = getPBKDF("ism1234", "PBKDF2WithHmacSHA1", "rd@h1", noIterations, 160);
        double tfinal = System.currentTimeMillis();
        System.out.printf("PBKDF with %d iterations computed in %f seconds\n", noIterations, (tfinal - tstart)/1000);
        System.out.println("Value stored in database: " + Utils.getHex(key));

        noIterations = (int) 1e6; // 1 million
        tstart = System.currentTimeMillis();
        key = getPBKDF("ism1234", "PBKDF2WithHmacSHA1", "rd@h1", noIterations, 160);
        tfinal = System.currentTimeMillis();
        System.out.printf("PBKDF with %d iterations computed in %f seconds\n", noIterations, (tfinal - tstart)/1000);
        System.out.println("Value stored in database: " + Utils.getHex(key));
    }
}
