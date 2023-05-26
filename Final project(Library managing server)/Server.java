package finalproject;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Server {
    public static final String BOOKS_FILE_PATH = "books.txt"; // å ������ ������ ���� ���
    private List<Book> bookList = new ArrayList<>(); // å ����Ʈ

    public void process(int port) {
        readBooks(); // ���� �� å ���Ϸκ��� ���� �б�
        listenClient(port); // �Էµ� port�� ���� �⵿
    }

    private void readBooks() {
        try {
            File file = new File(BOOKS_FILE_PATH);
            BufferedReader br = new BufferedReader(new FileReader(file));
            String line;
            while ((line = br.readLine()) != null) {
                String[] lineSplit = line.split("\t"); // �� �ʵ�� �� ���ڷ� ���еǾ� ����
                String title = lineSplit[0];
                String author = lineSplit[1];
                String borrowId = lineSplit[2];
                bookList.add(new Book(title, author, borrowId)); // å ���� ����Ʈ�� �߰�
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void listenClient(int port) { // ��Ʈ ��ȣ�� Ŭ���̾�Ʈ ��ٸ�
        try {
            ServerSocket serverSocket = new ServerSocket(port);
            while (true) {
                Socket socket = serverSocket.accept(); // Ŭ���̾�Ʈ ���� ���
                new ServerThread(socket, bookList).start(); // Ŭ���̾�Ʈ ��ɾ� ó���� ������ ��ü ���� �� ����
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        if (args.length != 1) { // ���ڰ� 1���� �ƴ� ��� ���� �޽��� ��� �� ����
            System.out.println("Please give the port number as an argument.");
            return;
        }
        int port = Integer.parseInt(args[0]);
        new Server().process(port);
    }
}

class ServerThread extends Thread { // �������� Ŭ���̾�Ʈ ���� �� ��ɾ� �޾��� ������ Ŭ����
    private List<Book> bookList; // ���� å ����Ʈ
    private InputStream inputStream; // Ŭ���̾�Ʈ���� �Է� ���� ��Ʈ��
    private ObjectInputStream objectInputStream; // Ŭ���̾�Ʈ���� ��ü �Է� ���� ��Ʈ��
    private OutputStream outputStream; // Ŭ���̾�Ʈ�� ����� ��Ʈ��
    private ObjectOutputStream objectOutputStream; // Ŭ���̾�Ʈ�� ��ü ����� ��Ʈ��

    ServerThread(Socket socket, List<Book> bookList) throws IOException {
        this.bookList = bookList;
        inputStream = socket.getInputStream();
        objectInputStream = new ObjectInputStream(inputStream);
        outputStream = socket.getOutputStream();
        objectOutputStream = new ObjectOutputStream(outputStream);
    }

    @Override
    public void run() { // Ŭ���̾�Ʈ ���� �� ����Ǵ� �޼ҵ�
        System.out.println("Login!!");
        while (true) {
            try {
                CommandMessage commandMessage = (CommandMessage) objectInputStream.readObject();
                switch (commandMessage.getType()) { // Ŭ���̾�Ʈ�� ���޹��� ��ɾ� Ÿ�Կ� ���� ó��
                    case "add":
                        addBook(commandMessage);
                        break;
                    case "borrow":
                        borrowBook(commandMessage);
                        break;
                    case "return":
                        returnBook(commandMessage);
                        break;
                    case "info":
                        info(commandMessage);
                        break;
                    case "search":
                        searchBook(commandMessage);
                        break;
                    default:
                        break;
                }
            } catch (IOException | ClassNotFoundException ie) {
                break;
            }
        }
    }

    private boolean addBook(CommandMessage commandMessage) throws IOException { // å �߰� �޼ҵ�
        String title = commandMessage.getTitle(); // �߰��� å ����
        String author = commandMessage.getAuthor(); // �߰��� å ����
        for (Book book : bookList) {
            if (title.equalsIgnoreCase(book.getTitle())) { // ���� ���� å ������ �ִٸ� �Ұ� ó��
                outputStream.write(0);
                return false;
            }
        }
        if(author != null) { // ���ڱ��� �ԷµǾ� �ִٸ� å ����Ʈ�� ����
            bookList.add(new Book(title, author, "-")); // å �߰� �� �⺻������ �뿩 ID�� "-" �Է�
            Collections.sort(bookList); // å�� ���� �߰������Ƿ� ���� ���ĺ������� ����
            saveBooks(); // å ���� ����
        }
        outputStream.write(1);
        return true;
    }


    private boolean borrowBook(CommandMessage commandMessage) throws IOException { // å �뿩 ��ɾ� ó��
        String title = commandMessage.getTitle(); // ���� å ����
        String userId = commandMessage.getUserId(); // ��ɾ� �Է��� ����� ID
        for (Book book : bookList) {
            if (title.equalsIgnoreCase(book.getTitle())) { // ���� å�� ã�Ұ�
                if (book.getBorrowId().equals("-")) {  // �ƹ��� �������� ���� ���¶�� å �뿩
                    book.setBorrowId(userId); // ���� ��� ID�� ��ɾ �Է��� ����� ID�� ����
                    saveBooks(); // å ���� ����
                    objectOutputStream.writeObject(book); // Ŭ���̾�Ʈ�� ���� å ����
                    return true;
                }
                objectOutputStream.writeObject(null); // å�� ã������ ������ ����� �ִٸ� null ����
                return false;
            }
        }
        objectOutputStream.writeObject(null); // ������ å ������ ã�� ���ߴٸ� null ����
        return false;
    }

    private boolean returnBook(CommandMessage commandMessage) throws IOException { // å �ݳ� ��ɾ� ó��
        String title = commandMessage.getTitle(); // �ݳ��� å ����
        String userId = commandMessage.getUserId(); // ��ɾ� �Է��� ����� ID
        for (Book book : bookList) {
            if (title.equalsIgnoreCase(book.getTitle())) { // �ݳ��� å�� ã�Ұ�
                if (book.getBorrowId().equals(userId)) {  // å�� �뿩�ذ� ����ڰ� ��ɾ �Է��� ����ڿ� ���ٸ� �ݳ� ó��
                    book.setBorrowId("-"); // ���� ��� ID�� "-"���� ��
                    saveBooks(); // å ���� ����
                    objectOutputStream.writeObject(book); // Ŭ���̾�Ʈ�� �ݳ��� å ����
                    return true; // å �ݳ��� ���������Ƿ� true ��ȯ
                }
                objectOutputStream.writeObject(null); // å�� ã������ ������ ����� �ٸ��ٸ� null ����
                return false; // å�� ã������ ������ ����� �ٸ��ٸ� false ��ȯ
            }
        }
        objectOutputStream.writeObject(null); // ������ å ������ ã�� ���ߴٸ� null ����
        return false; // ������ å ������ ã�� ���ߴٸ� false ��ȯ
    }

    private void info(CommandMessage commandMessage) throws IOException { // ����ڰ� �������� ���� ��� ��ȸ ��ɾ� ó��
        String userId = commandMessage.getUserId();
        List<Book> borrowedBookList = new ArrayList<>();
        for (Book book : bookList) {
            if (book.getBorrowId().equals(userId)) {  // å�� �뿩�ذ� ����ڰ� ��ɾ �Է��� ����ڿ� ���ٸ� ����Ʈ �߰�
                borrowedBookList.add(book);
            }
        }
        objectOutputStream.writeObject(borrowedBookList); // Ŭ���̾�Ʈ�� ���� å ��� ����
    }

    private void searchBook(CommandMessage commandMessage) throws IOException { // å �˻� ��ɾ� ó��
        String title = commandMessage.getTitle();
        List<Book> searchedBookList = new ArrayList<>();
        for (Book book : bookList) {
            // å �����̳� ���ڿ��� ��ҹ��ڸ� �������� �ʰ� �˻� �ܾ ���ԵǾ� �ִٸ� �˻� å ����Ʈ�� �߰�
            if (book.getTitle().toLowerCase().contains(title.toLowerCase()) || book.getAuthor().toLowerCase().contains(title.toLowerCase())) {
                searchedBookList.add(book);
            }
        }
        objectOutputStream.writeObject(searchedBookList); // Ŭ���̾�Ʈ�� �˻��� å ��� ����
    }

    private void saveBooks() { // å ���� �޼ҵ�
        try {
            BufferedWriter bw = new BufferedWriter(new FileWriter(new File(Server.BOOKS_FILE_PATH)));
            for (Book book : bookList) { // ��� å ������ ���������� ����
                bw.write(book.toString() + "\n");
            }
            bw.close();
            System.out.println("Books is saved");
        } catch (IOException ie) {
            ie.printStackTrace();
        }
    }
}

class Book implements Comparable<Book>, Serializable { // å ������ �����ϴ� Ŭ������ ���� �����ؾ� �ϱ� ������ Comparable ����, �������� ������ �� �ֵ��� Serializable ����
    private final String title; // å ����
    private final String author; // å ����
    private String borrowId; // å�� ���� ����� ID, ���� ��� "-"

    public Book(String title, String author, String borrowId) {
        this.title = title;
        this.author = author;
        this.borrowId = borrowId;
    }

    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    public String getBorrowId() {
        return borrowId;
    }

    public void setBorrowId(String borrowId) {
        this.borrowId = borrowId;
    }


    @Override
    public int compareTo(Book o) { // å ������ ��ҹ��ڸ� �������� ������ ���ĺ� ������ ����
        return title.compareToIgnoreCase(o.getTitle());
    }

    @Override
    public String toString() {
        return title + "\t" + author + "\t" + borrowId;
    }
}

class CommandMessage implements Serializable { // Ŭ���̾�Ʈ�� ������ ������ ��� ������ ���� Ŭ����
    private final String type;
    private final String title;
    private final String author;
    private final String userId;

    public CommandMessage(String type, String title, String author, String userId) {
        this.type = type;
        this.title = title;
        this.author = author;
        this.userId = userId;
    }

    public String getType() {
        return type;
    }

    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    public String getUserId() {
        return userId;
    }
}

