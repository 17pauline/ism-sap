package lfsr;

public class TestLFSR {
    // implements a simple LFSR
    // with a 32 byte register
    // and a TAP sequence: x^31 + x^7 + x^5 + x^3 + x^2 + x + 1

    // possible implementations - integer vs byte array of 4 values

    // Integer implementation
    public static void printRegister(int register) {
        System.out.println("Register: " + Integer.toBinaryString(register));
        System.out.println("Register: " + Integer.toHexString(register));
    }

    public static int initRegister(byte[] initialValues) {
        if (initialValues.length != 4) {
            System.out.println("Wrong initial value. 4 bytes needed!");
        }
        int result = 0;
        for (int i = 3; i >= 0; i--) {
            result = result | (((int)initialValues[3-i] & 0xFF) << (i * 8));
        }
        return result;
    }

    public static byte applyTapSequence(int register) {
        byte result = 0;
        byte[] index = {31, 7, 5, 3, 2, 1, 0};
        for (int i = 0; i <  index.length; i++) {
            byte bitValue = (byte)(((1 << index[i]) & register) >>> index[i]);
            result = (byte) (result ^ bitValue);
        }
        return result;
    }

    public static byte getLeastSignificantBit(int register) {
        return (byte) (register & 1);
    }

    public static int shiftAndInsertTapBit(int register, byte tapBit) {
        register = register >>> 1;
        register = register | (tapBit << 31);
        return register;
    }

    public static void main(String[] args) {
        // the register
        int register = 0;
        // the initial seed
        byte[] seed = {
                (byte)0b10101010,
                (byte)0b11110000,
                (byte)0b00001111,
                (byte)0b01010101
        };
        register = initRegister(seed);
        printRegister(register);

        System.out.println(getLeastSignificantBit(register));
        register = shiftAndInsertTapBit(register, (byte) 0);
        printRegister(register);
        register = shiftAndInsertTapBit(register, (byte) 1);
        printRegister(register);
        register = shiftAndInsertTapBit(register, (byte) 1);
        printRegister(register);

        /*
        Register: 10101010111100000000111101010101
        Register: aaf00f55
        1
        Register: 1010101011110000000011110101010
        Register: 557807aa
        Register: 10101010101111000000001111010101
        Register: aabc03d5
        Register: 11010101010111100000000111101010
        Register: d55e01ea
         */
    }
}
