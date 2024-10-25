package collections;

import java.util.List;

public class Certificate {
    String owner;
    List<Byte> publicKey;
    int keySize;
    String domain;

    public Certificate(String owner, List<Byte> publicKey, int keySize, String domain) {
        this.owner = owner;
//        this.publicKey = publicKey;
        this.publicKey = List.copyOf(publicKey);
        this.keySize = keySize;
        this.domain = domain;
    }

    void print() {
        System.out.printf("\nOwner: " + this.owner +
                "\nDomain: " + this.domain +
                "\nKey size: " + this.keySize +
                "\nPublic key: ");
        for (byte b : this.publicKey) {
            System.out.printf("%02x", b);
        }
    }

    @Override
    protected Object clone() throws CloneNotSupportedException {
        return new Certificate(this.owner, this.publicKey, this.keySize, this.domain);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null || !(obj instanceof Certificate)) {
            return false;
        }
        return this.domain.equals(((Certificate) obj).domain);
    }
}
