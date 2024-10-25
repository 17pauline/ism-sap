package bitwise;

public class TestBitwise {

    public static void main(String[] args) {
        System.out.println("Working with byte:");
        byte value = 15;
        System.out.println("Value (byte value = 15) is " + value); // 15
        value = 0b00001111;
        System.out.println("Value (binary 0b00001111) is " + value); // 15
        value = 0x0F;
        System.out.println("Value (hex 0x0F) is " + value); // 15

        // concattenation
        value = 1 << 3 | 1 << 2 | 1 << 1 | 1;
        System.out.println("\nValue is " + value); // 15

        value = 8;
        value = (byte) (value << 1); // multiply (left shift) by 2
        System.out.println("\nValue is " + value); // 16
        value = (byte) (value >> 1); // divide (right shift) by 2
        System.out.println("Value is " + value); // 8

        value = 65;
        value = (byte) (value << 1);
        System.out.println("\nValue is " + value); // -126

        value = -1;
        System.out.println("\n-1 in hex:");
        System.out.println(String.format("%02x", value)); // ff

        value = (byte) (value >> 1);
        System.out.println(value); // -1
        value = (byte) (value >>> 1); // >>> does not work on bytes, just Integers
        System.out.println(value); // -1

        System.out.println("\nWorking with int:");
        int value2 = -1;
        value2 = value2 >> 1; // shifts the bit sign AND preserves the value sign
        System.out.println("After >>");
        System.out.println(value2); // -1

        int value3 = -1;
        value3 = value3 >>> 1; // shifts the bit sign BUT does not preserve the value sign
        System.out.println("After >>>");
        System.out.println(value3); // 2147483647

        // Checking for bits
        System.out.println("\nChecking for bits with Bit Masks:");
        // check if a byte has the 3rd bit 1 (left to right, 1st bit is 1st)
        byte anyValue = 39;
        // using a Bit Mask
        byte bitMask = 1 << 5; // 0b00100000;
        byte result = (byte)(anyValue & bitMask); // possible values: 0 or !0
        if (result == 0) {
            System.out.println("We checked for the 3rd bit, and it is 0");
        } else {
            System.out.println("We checked for the 3rd bit, and it is 1"); // <-
        }
    }
}
