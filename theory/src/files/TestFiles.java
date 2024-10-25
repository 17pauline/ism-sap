package files;

import java.io.*;

public class TestFiles {

    public static void listFolder(File repository) {
        if (repository.exists() && repository.isDirectory()) {
            // print location content
            File[] items = repository.listFiles();
            for (File item : items) {
                System.out.println(item.getName() + " - " +
                        (item.isFile() ? "FILE" : "FOLDER"));
                System.out.println(item.getAbsolutePath());
                if (item.isDirectory()) {
                    listFolder(item);
                }
            }
        }
    }

    public static void main(String[] args) throws IOException {
        // In Java, the File class is a pointer, an entry in the file system
        // Java stores chars on 2 bytes
        // => Java has 2 frameworks for files
        // * text files
        // * binary files

        // File System Management
        File repository = new File("C:\\Users\\tznpa\\source\\repos\\ism-sap\\theory");
        listFolder(repository);

        System.out.println("\n\n");

        // Text Files
        // reading & writing
        File msgFile = new File("src/files/Message.txt");
        if (!msgFile.exists()) {
            msgFile.createNewFile();
        }

        // write into a .txt file, append mode = true
        // append = true -> each println() appends content to previous content from earlier runs
        // append = false -> file is overwritten after every run
        FileWriter fileWriter = new FileWriter(msgFile, true);
        PrintWriter printWriter = new PrintWriter(fileWriter);
        printWriter.println("This is a secret message");
        printWriter.println("Don't tell anyone");
        printWriter.close();

        // read from a .txt file
        FileReader fileReader = new FileReader(msgFile);
        BufferedReader bufferedReader = new BufferedReader(fileReader);
        System.out.println("\nFile content: ");
        String line = null;
        do {
            line = bufferedReader.readLine();
            if (line != null) {
                System.out.println(line);
            }
        } while (line != null);

        //		while((line = bufferReader.readLine())!= null) {
        //
        //	}
        //        This is a secret message
        //        Don't tell anyone     x however many times we ran it, since append = true
        fileReader.close();


        // Binary Files
        // reading & writing
        File binaryFile = new File("src/files/myData.bin");
        if (!binaryFile.exists()) {
            binaryFile.createNewFile();
        }

        float floatValue = 23.5f;
        double doubleValue = 10;
        int intValue = 10;
        boolean flag = true;
        String text = "Hello";
        byte[] values = new byte[]{(byte)0xff, (byte)0x0A};

        // write into a .bin file
        FileOutputStream fos = new FileOutputStream(binaryFile);
        DataOutputStream dos = new DataOutputStream(fos);

        dos.writeFloat(floatValue);
        dos.writeDouble(doubleValue);
        dos.writeInt(intValue);
        dos.writeBoolean(flag);
        dos.writeUTF(text);
        dos.write(values); // we forgot to put the size of the array before it

        dos.close();

        // read from a .bin file
        FileInputStream fis = new FileInputStream(binaryFile);
        BufferedInputStream bis = new BufferedInputStream(fis);
        DataInputStream dis = new DataInputStream(bis);

        floatValue = dis.readFloat();
        doubleValue = dis.readDouble();
        intValue = dis.readInt();
        flag = dis.readBoolean();
        text = dis.readUTF();
        values = dis.readAllBytes(); // we know the array is the last one

        dis.close();

        System.out.println(intValue); // 10
        System.out.println(text); // Hello

        RandomAccessFile raf = new RandomAccessFile(binaryFile, "r");
        raf.seek(12); // jump the float and the double
        int vb = raf.readInt();
        raf.close();

        System.out.println(vb); // 10
    }
}
