package cryptography;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

public class TestCTS {
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

        Cipher cipher = Cipher.getInstance("DES/CTS/NoPadding");
        SecretKeySpec keySpec = new SecretKeySpec(key, "DES");
        cipher.init(Cipher.ENCRYPT_MODE, keySpec);

        byte[] buffer = new byte[cipher.getBlockSize()];

        byte[] IV = new byte[cipher.getBlockSize()];
        IV[2]= (byte) 0xFF;

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

        Cipher cipher = Cipher.getInstance("DES/CTS/NoPadding");

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


    public static void main(String[] args) throws InvalidAlgorithmParameterException, NoSuchPaddingException, IllegalBlockSizeException, IOException, NoSuchAlgorithmException, BadPaddingException, InvalidKeyException {
        desEncrypt("MyMessage.txt", "desCipher.enc", "ism12345".getBytes());
        System.out.println("Encryption successful");

        desDecrypt("desCipher.enc", "desCipherDecrypted.txt", "ism12345".getBytes());
        System.out.println("Decryption successful");
    }
}
