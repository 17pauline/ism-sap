package collections;

import java.util.*;

public class TestCollections {
    public static void main(String[] args) {
        // Collection types

        // * List
        // variable size array, ordered by insertion
        // O(n)
        List<String> files = new ArrayList<>();
        files.add("Keys.txt");
        files.add("Passwords.txt");
        files.add("Users.txt");
        files.add("Passwords.txt");

        for (String filename : files) {
            System.out.println(filename);
        }

        // * Set
        // variable size array with unique values
        // complexity depends on type
        Set<String> usernames = new HashSet<String>();
        usernames.add("Alice");
        usernames.add("Alice");
        usernames.add("John");
        usernames.add("Bob");

        for (String username : usernames) {
            System.out.println(username);
        }

        // * Map
        // dictionary of Key-Value pairs
        // the Key is unique
        // O(1)
        HashMap<Integer, String> users = new HashMap<>();
        users.put(10, "Alice");
        users.put(20, "John");
        users.put(30, "Bob");
        users.put(10, "Vader");

        String user = users.get(10);
        if (user != null) {
            System.out.println("User with id=10 is " + user);
        } else {
            System.out.println("There is no user with id=10");
        }

        for (Integer id : users.keySet()) {
            System.out.printf("\nid=%d username=%s", id, users.get(id));
        }

        // Test - SHALLOW COPY
        List<Byte> key = Arrays.asList((byte)0xA4, (byte)0x10, (byte)0x2F, (byte)0x22);
        Certificate certISM = new Certificate("ISM", key, 1024, "ism.ase.ro");
        System.out.println("\n");
        certISM.print();
        System.out.println("\n");
        Certificate certPortalISM = new Certificate("PortalISM", key, 1024, "portal.ism.ase.ro");
        certPortalISM.print();
        System.out.println("\n");

        key.set(1, (byte)0xFF);
        certISM.print();
        System.out.println("\n");

        // * Bitset
        BitSet bitSet = new BitSet(32); // 32 = the initial size
        bitSet.set(0);
        bitSet.set(30);

        BitSet register = BitSet.valueOf(new byte[]{(byte)0xA4, ( byte)0x10, (byte)0x2F, (byte)0x22});

        System.out.println("\nRegister: ");
        for (int i = 0; i < register.size(); i++) {
            System.out.printf("%d", register.get(i) ? 1 : 0); // 0010010100001000111101000100010000000000000000000000000000000000
        }
        System.out.println("\nRegister: ");
        for (byte b : register.toByteArray()) {
            System.out.printf("%02x", b); // a4102f22
        }
    }
}
