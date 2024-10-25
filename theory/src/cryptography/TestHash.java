package cryptography;

import java.io.*;
import java.security.*;

import org.bouncycastle.jce.provider.BouncyCastleProvider;

public class TestHash {

    public static void printHex(byte[] values) {
        System.out.println("\nHEX: ");
        for (byte b : values) {
            System.out.printf("%02x ", b);
        }
    }

    public static void main(String[] args) throws NoSuchAlgorithmException, NoSuchProviderException, IOException {

        // Checking and using different providers
        // Bouncy Castle
        String BouncyCastleProvider = "BC";

        // Check if provider is available
        Provider provider = Security.getProvider(BouncyCastleProvider);
        if (provider == null) {
            System.out.println("Bouncy Castle is not available"); // <-
        } else {
            System.out.println("Bouncy Castle is available");
        }

        // even if we included the library, the instance of the library is not available until we load the provider
        // pay attention to whoich package the provider comes from
        // it should be "org.bouncycastle.jce.provider.BouncyCastleProvider"

        // Load BC provider
        Security.addProvider(new BouncyCastleProvider());
        // Check if provider is available
        provider = Security.getProvider(BouncyCastleProvider);
        if (provider == null) {
            System.out.println("Bouncy Castle is not available");
        } else {
            System.out.println("Bouncy Castle is available"); // <-
        }

        // Check if the SUN provider is available
        provider = Security.getProvider("SUN");
        if (provider == null) {
            System.out.println("SUN is not available");
        } else {
            System.out.println("SUN is available"); // <-
        }

        String message = "ISM";

        // HASHING a string
        MessageDigest md = MessageDigest.getInstance("SHA-1", "BC");

        // Compute the hash in one step
        // the input is small enough
        byte[] hashValue = md.digest(message.getBytes());
        printHex(hashValue); // db 8b 44 fe 7e a6 31 1e c9 2b 32 75 a7 01 27 cd d3 db a0 e6

        // with the default provider
        md = MessageDigest.getInstance("SHA-1");
        // Compute the hash in one step
        // the input is small enough
        hashValue = md.digest(message.getBytes());
        printHex(hashValue); // db 8b 44 fe 7e a6 31 1e c9 2b 32 75 a7 01 27 cd d3 db a0 e6

        // Compute the hash of a file
        // we read all file types as binary
        File file = new File("src/cryptography/Message.txt");
        if (!file.exists()) {
            System.out.println("\n*********** The file is not there");
        }
        FileInputStream fis = new FileInputStream(file);
        BufferedInputStream bis = new BufferedInputStream(fis);

        md = MessageDigest.getInstance("MD5", "BC");
        byte[] buffer = new byte[8];


        // if we are at the EOF, bis.read(buffer) will return how many bytes we were able to read
        // md.update(buffer) -> will produce a wrong hash, because we didn't clean the buffer after reading and before the next iteration, on the last iteration
        // if the buffer is not filled by the content of the last block, we will have residual content from the last iteration
        // for example, if the buffer had "message", and needs to be filled with ".", it will result in ".essage" !!!
        do {
            int noBytes = bis.read(buffer); // we try to read 8 bytes
            if (noBytes != -1) {
                // we have data
                md.update(buffer, 0, noBytes);
            } else {
                break;
            }
        } while (true);

        // Get final hash
        hashValue = md.digest();

        bis.close();

        printHex(hashValue); // 83 74 80 29 db 13 ed d1 1d 1c 1f a4 02 73 ac 30
    }
}
