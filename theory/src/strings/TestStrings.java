package strings;

public class TestStrings {

    public static void main(String[] args) {
        // Comparing Strings
        // String Constant Pool
        String file1 = "Keys.txt";
        String file2 = "Keys.txt";
        if (file1 == file2) {
            System.out.println("The files are the same"); // <-
        } else {
            System.out.println("The files are different");
        }

        file2 = new String("Keys.txt");
        if (file1 == file2) {
            System.out.println("The files are the same");
        } else {
            System.out.println("The files are different"); // <-
        }
        if (file1.equals(file2)) {
            System.out.println("The files are the same"); // <-
        } else {
            System.out.println("The files are different");
        }

        System.out.println("\n");
        // Small numbers from 0 to 127 are managed by an Int Constant Pool
        int vb = 10;

        Integer intVb = 10;
        Integer intVb2 = 10;
        if (intVb == intVb2) {
            System.out.println("The numbers are the same"); // <-
        } else {
            System.out.println("The numbers are different");
        }
        if (intVb.equals(intVb2)) {
            System.out.println("The numbers are the same"); // <-
        } else {
            System.out.println("The numbers are different");
        }

        Integer intVb3 = 128; // over 127
        Integer intVb4 = 128;
        if (intVb3 == intVb4) {
            System.out.println("The numbers are the same");
        } else {
            System.out.println("The numbers are different"); // <-
        }
        if (intVb3.equals(intVb4)) {
            System.out.println("The numbers are the same"); // <- so use equals() !!!
        } else {
            System.out.println("The numbers are different");
        }

        // Converting numbers to strings
        int value = 33;
        String binaryRep = Integer.toBinaryString(value);
        String hexRep = Integer.toHexString(value);
        System.out.println("\nBinary string: " + binaryRep); // 100001
        System.out.println("Hex string: " + hexRep); // 21

        byte smallValue = 23;
        System.out.println("\nBinary string: " + Integer.toBinaryString(smallValue)); // 10111
        System.out.println("Hex string: " + Integer.toHexString(smallValue)); // 17

        // Converting strings to numbers
        Integer initialValue = Integer.parseInt(hexRep, 16);
        System.out.println("\nInitial value is (from hexRep): " + initialValue); // 33
        initialValue = Integer.parseInt(binaryRep, 2);
        System.out.println("Initial value is (from binaryRep): " + initialValue); // 33

        byte smallValue2 = -23;
        System.out.println("\nBinary string: " + Integer.toBinaryString(smallValue2)); // 11111111111111111111111111101001
        System.out.println("Hex string: " + Integer.toHexString(smallValue2)); // ffffffe9

        System.out.println(Byte.toUnsignedInt(smallValue2)); // 233

        System.out.println("Binary string: " + Integer.toBinaryString(Byte.toUnsignedInt(smallValue2))); // 11101001
        System.out.println("Hex string: " + Integer.toHexString(Byte.toUnsignedInt(smallValue2))); // e9

        byte[] hash = {(byte)23, (byte)-23, (byte)10, (byte)5};
        // WRONG way
        String hashHexString = "";
        for (int i = 0; i < hash.length; i++) {
            hashHexString += Integer.toHexString(hash[i]);
        }
        System.out.println("\nThe hash is " + hashHexString); // 17ffffffe9a5

        // another WRONG way - because small byte values are converted to a single hex symbol
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < hash.length; i++) {
            sb.append(Integer.toHexString(Byte.toUnsignedInt(hash[i])));
        }
        System.out.println("The hash is " + sb.toString().toUpperCase()); // 17E9A5

        // RIGHT way
        sb = new StringBuilder();
        for (int i = 0; i < hash.length; i++) {
            sb.append(getByteUnsignedHexRepresentation(hash[i]));
        }
        System.out.println("The hash is " + sb.toString().toUpperCase()); // 17E90A05
        System.out.println("The hash is " + getHexFromByteArray(hash)); // 17e90a05
    }

    static String getByteUnsignedHexRepresentation(byte value) {
        String hex = Integer.toHexString(Byte.toUnsignedInt(value));
        if (hex.length() == 1) {
            hex = "0" + hex;
        }
        return hex;
    }

    static String getHexFromByteArray(byte[] values) {
        StringBuilder sb = new StringBuilder();
        for (byte value : values) {
            sb.append(String.format("%02x", value));
        }
        return sb.toString();
    }
}
