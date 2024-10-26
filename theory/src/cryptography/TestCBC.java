package cryptography;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.*;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

public class TestCBC {

    public static void desEncrypt(String inputFile, String outputFile, byte[] key) throws IOException, NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException {
        File inputF = new File(inputFile);
        if (!inputF.exists()) {
            throw new UnsupportedOperationException("Input file is missing");
        }
        File outputF = new File(outputFile);
        if (!outputF.exists()) {
            outputF.createNewFile();
        }

        FileInputStream fis = new FileInputStream(inputF);
        FileOutputStream fos = new FileOutputStream(outputF);

        // CBC
        // the first block is special because it needs an IV
        // the next blocks use the previous blocks to be computed
        Cipher cipher = Cipher.getInstance("DES/CBC/PKCS5Padding");
        SecretKeySpec keySpec = new SecretKeySpec(key, "DES");
        cipher.init(Cipher.ENCRYPT_MODE, keySpec);

        byte[] buffer = new byte[cipher.getBlockSize()];

        // the destination needs to know the IV
        // recommended values for IV: all 0s or all 1s (standard value)
        // IV is not secret. You have the following options:
        // 1. hardcoded known value
        // 2. known value or any value stored in the ciphertext at the beginning (first block in the ciphertext)

        // 2.
        // IV has the 3rd byte with all bits 1
        byte[] IV = new byte[cipher.getBlockSize()];
        IV[2]= (byte) 0xFF;

        // write IV into file
        fos.write(IV);

        IvParameterSpec ivSpec = new IvParameterSpec(IV);
        cipher.init(Cipher.ENCRYPT_MODE, keySpec, ivSpec);

        while (true) {
            int noBytes = fis.read(buffer);
            if (noBytes == -1) {
                break;
            }
            byte[] output = cipher.update(buffer, 0, noBytes);
            fos.write(output);
        }
        byte[] output = cipher.doFinal();
        fos.write(output);

        fis.close();
        fos.close();
    }

    public static void desDecrypt(String inputFile, String outputFile, byte[] key) throws IOException, NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException {
        File inputF = new File(inputFile);
        if (!inputF.exists()) {
            throw new UnsupportedOperationException("File is missing");
        }
        File outputF = new File(outputFile);
        if (!outputF.exists()) {
            outputF.createNewFile();
        }

        FileInputStream fis = new FileInputStream(inputF);
        FileOutputStream fos = new FileOutputStream(outputF);

        Cipher cipher = Cipher.getInstance("DES/CBC/PKCS5Padding");
        // read IV
        byte[] IV = new byte[cipher.getBlockSize()];
        fis.read(IV);

        SecretKeySpec keySpec = new SecretKeySpec(key, "DES");
        IvParameterSpec ivSpec = new IvParameterSpec(IV);
        cipher.init(Cipher.DECRYPT_MODE, keySpec, ivSpec);

        byte[] buffer = new byte[cipher.getBlockSize()];

        while (true) {
            int noBytes = fis.read(buffer);
            if (noBytes == -1) {
                break;
            }
            byte[] output = cipher.update(buffer, 0, noBytes);
            fos.write(output);
        }
        byte[] output = cipher.doFinal();
        fos.write(output);

        fis.close();
        fos.close();
    }

    // if your file has 15 bytes
    // the .enc file will have padding + IV block (8 bytes)

    public static void main(String[] args) throws InvalidAlgorithmParameterException, NoSuchPaddingException, IllegalBlockSizeException, IOException, NoSuchAlgorithmException, BadPaddingException, InvalidKeyException {
        desEncrypt("MyMessage.txt", "desCipher.enc", "ism12345".getBytes());
        System.out.println("Encryption successful");

        desDecrypt("desCipher.enc", "desCipherDecrypted.txt", "ism12345".getBytes());
        System.out.println("Decryption successful");
    }
}
