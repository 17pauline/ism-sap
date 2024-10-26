package cryptography;

public class Utils {

    // watch out when comparing hashes for
    // - spaces between bytes
    // - upper case / lower case
    // this function is just for visualizing
    public static String getHex(byte[] values) {
        StringBuffer sb = new StringBuffer();
        for (byte b : values) {
            sb.append(String.format(" %02x", b));
        }
        return sb.toString();
    }


}
