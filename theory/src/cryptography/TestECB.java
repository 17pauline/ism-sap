package cryptography;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;
import java.io.*;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;

public class TestECB {

    public static void encrypt(String inputFile, String encryptedFile, byte[] key, String algorithm) throws IOException, NoSuchPaddingException, NoSuchAlgorithmException, NoSuchProviderException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
        // Manage the files
        File inputF = new File(inputFile);
        if (!inputF.exists()) {
            throw new UnsupportedOperationException("Input file is missing");
        }
        File outputF = new File(encryptedFile);
        if (!outputF.exists()) {
            outputF.createNewFile();
        }

        FileInputStream fis = new FileInputStream(inputF);
        BufferedInputStream bis = new BufferedInputStream(fis);
        FileOutputStream fos = new FileOutputStream(outputF);
        BufferedOutputStream bos = new BufferedOutputStream(fos);

        // ECB
        // each block is processed in parallel (plaintext -> ciphertext)
        // write the ciphertext blocks in the right order
        // check properties for ciphertext
        // * output is always a multiple of block size
        // * key size needs to be appropriate for the algorithm
        Cipher cipher = Cipher.getInstance(algorithm + "/ECB/PKCS5Padding");
        SecretKeySpec keySpec = new SecretKeySpec(key, algorithm);
        cipher.init(Cipher.ENCRYPT_MODE, keySpec);

        byte[] buffer = new byte[cipher.getBlockSize()];

        while (true) {
            int noBytes = bis.read(buffer);
            if (noBytes == -1) {
                break;
            }
            // process the input
            // start from the first byte and stop at the last one you've got
            // do not read the residuals; so the decryption can work
            // each block is processed (read, encrypted - update())
            // for hashes you just feed the hash and at the end you tell it at the end the size of what you read
            // the cipher takes each block one by one, reads it, encrypts it, preserves in its memory the previous block
            byte[] output = cipher.update(buffer, 0, noBytes);
            bos.write(output); // output, not buffer !!!
        }
        // the last block is tricky because it might not have the full size
        // doFinal() = apply the padding + final cleanup
        // but if your block was the right size and it didn't need padding, you need to tell the destination it didn't need it
        // -> in this situation, doFinal() will create a final block that is full padding
        byte[] output = cipher.doFinal(); // give me the last block
        bos.write(output);

        fis.close();
        bos.close(); // fos.close() resulted in 0 bytes
    }

    public static void decrypt(String inputFile, String outputFile, byte[] key, String algorithm) throws IOException, NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
        File inputF = new File(inputFile);
        if (!inputF.exists()) {
            throw new UnsupportedOperationException("Input file is missing");
        }
        File outputF = new File(outputFile);
        if (!outputF.exists()) {
            outputF.createNewFile();
        }

        FileInputStream fis = new FileInputStream(inputF);
        BufferedInputStream bis = new BufferedInputStream(fis);
        FileOutputStream fos = new FileOutputStream(outputF);
        BufferedOutputStream bos = new BufferedOutputStream(fos);

        Cipher cipher = Cipher.getInstance(algorithm + "/ECB/PKCS5Padding");
        SecretKeySpec keySpec = new SecretKeySpec(key, algorithm);
        cipher.init(Cipher.DECRYPT_MODE, keySpec);

        byte[] buffer = new byte[cipher.getBlockSize()];

        while (true) {
            int noBytes = bis.read(buffer);
            if (noBytes == -1) {
                break;
            }
            byte[] output = cipher.update(buffer, 0, noBytes);
            bos.write(output);
        }
        byte[] output = cipher.doFinal();
        bos.write(output);

        fis.close();
        bos.close();
    }

    public static void main(String[] args) throws NoSuchPaddingException, IllegalBlockSizeException, IOException, NoSuchAlgorithmException, BadPaddingException, NoSuchProviderException, InvalidKeyException {
        encrypt("MyMessage.txt", "MyMessage.enc", "ism12345password".getBytes(), "AES");
        System.out.println("Encryption succcessful");

        decrypt("MyMessage.enc", "MyMessageCopy.txt", "ism12345password".getBytes(), "AES");
        System.out.println("Decryption successful");
    }
}
