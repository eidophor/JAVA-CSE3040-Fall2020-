package finalproject;


import java.io.*;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.List;
import java.util.Scanner;

public class Client {
    private Scanner scanner = new Scanner(System.in);
    private String userId;
    private InputStream inputStream;
    private ObjectInputStream objectInputStream;
    private OutputStream outputStream;
    private ObjectOutputStream objectOutputStream;

    public void process(String ip, int port) {
        Socket socket = new Socket();
        try {
            socket.connect(new InetSocketAddress(ip, port));
            outputStream = socket.getOutputStream();
            objectOutputStream = new ObjectOutputStream(outputStream);
            inputStream = socket.getInputStream();
            objectInputStream = new ObjectInputStream(inputStream);
            userId = inputUserId();
            while (true) {
                System.out.print(userId + ">> ");
                String commandType = scanner.nextLine().trim();
                switch (commandType) {
                    case "add":// �߰� ��ɾ� �Է� �� ó��
                        addBook();
                        break;
                    case "borrow":// �뿩 ��ɾ� �Է� �� ó��
                        borrowBook();
                        break;
                    case "return":// �ݳ� ��ɾ� �Է� �� ó��
                        returnBook();
                        break;
                    case "info": // ���� å ��ɾ� �Է� �� ó��
                        info();
                        break;
                    case "search": // �˻� ��ɾ� �Է� �� ó��
                        searchBook();
                        break;
                    default: // �߸��� ��ɾ� �Է� �� ó��
                        System.out.println("[available commands]");
                        System.out.println("add: add a new book to the list of books.");
                        System.out.println("borrow: borrow a book from the library.");
                        System.out.println("return: return a book to the library.");
                        System.out.println("info: show list of books I am currently borrowing.");
                        System.out.println("search: search for books.");
                        break;
                }
            }
        } catch (Exception e) {
            System.out.println("Connection establishment failed.");
        }
    }

    private String inputUserId() { // ����� ID �Է� �޼ҵ�
        do {
            System.out.print("Enter userID>> ");
            String userId = scanner.nextLine().trim(); // ����� ID �Է�
            if (!isValidUserId(userId)) { // �Էµ� ID�� ��ȿ���� �ʴٸ� ���� �޽��� ��� �� �� �Է�
                System.out.println("UserID must be a single word with lowercase alphabets and numbers.");
            } else { // ��ȿ�ϴٸ� ��ο� �޽��� ���
                System.out.println("Hello " + userId + "!");
                return userId;
            }
        } while (true);
    }

    private boolean isValidUserId(String userId) { // ����� �Է� ID�� ��ȿ���� üũ�ϴ� �޼ҵ�
        if (userId.split(" ").length > 1) { // ������ ���ԵǾ� �ִٸ� ��ȿ
            return false;
        }
        String specialChars = "~`!@#$%^&*()-_=+\\|[{]};:'\",<.>/?"; // Ư�� ���ڵ�
        boolean numberPresent = false;
        boolean upperCasePresent = false;
        boolean lowerCasePresent = false;
        boolean specialCharacterPresent = false;

        for (int i = 0; i < userId.length(); i++) {
            char currentCharacter = userId.charAt(i); // �˻��� ���� �ε��� ����
            if (Character.isDigit(currentCharacter)) { // ���� üũ
                numberPresent = true;
            } else if (Character.isUpperCase(currentCharacter)) { // �빮�� üũ
                upperCasePresent = true;
            } else if (Character.isLowerCase(currentCharacter)) { // �ҹ��� üũ
                lowerCasePresent = true;
            } else if (specialChars.contains(String.valueOf(currentCharacter))) { // Ư������ üũ
                specialCharacterPresent = true;
            }
        }
        if (upperCasePresent || specialCharacterPresent) { // �빮��, Ư������ �� �ϳ��� �ִٸ� ��ȿ
            return false;
        }
        return lowerCasePresent || numberPresent; // �ҹ��ڿ� ���ڸ� �ִٸ� ��ȿ
    }

    private void addBook() throws IOException { // å �߰� �޼ҵ�
        System.out.print("add-title> ");
        String title = scanner.nextLine().trim(); // �߰��� å ���� �Է�
        if (title.equals("") || title.isEmpty()) { // �ƹ��͵� �Է����� ���� ��� �ڷ� ���ư���
            return;
        }
        System.out.print("add-author> ");
        String author = scanner.nextLine().trim(); // �߰��� å ���� �Է�
        if (author.equals("") || author.isEmpty()) { // �ƹ��͵� �Է����� ���� ��� �ڷ� ���ư���
            return;
        }
        CommandMessage commandMessage = new CommandMessage("add", title, author, userId);
        objectOutputStream.writeObject(commandMessage); // ������ ��ɾ� ����
        boolean result = inputStream.read() == 1; // �����κ��� ��� ���� �ޱ�. 1�̶�� ���� 0�̸� ����
        if (result) {
            System.out.println("A new book added to the list.");
        } else {
            System.out.println("The book already exists in the list.");
        }
    }

    private void borrowBook() throws IOException, ClassNotFoundException { // å �뿩 �޼ҵ�
        System.out.print("borrow-title> ");
        String title = scanner.nextLine().trim(); // �뿩�� å ���� �Է�
        if (title.equals("") || title.isEmpty()) { // �ƹ��͵� �Է����� ���� ��� �ڷ� ���ư���
            return;
        }
        CommandMessage commandMessage = new CommandMessage("borrow", title, null, userId);
        objectOutputStream.writeObject(commandMessage); // ������ ��ɾ� ����
        Book borrowedBook = (Book) objectInputStream.readObject(); // �����κ��� ��� ���� �ޱ�.
        if (borrowedBook != null) {
            System.out.println("You borrowed a book. - " + borrowedBook.getTitle());
        } else {
            System.out.println("The book is not available.");
        }
    }

    private void returnBook() throws IOException, ClassNotFoundException { // å �ݳ� �޼ҵ�
        System.out.print("return-title> ");
        String title = scanner.nextLine().trim(); // �ݳ��� å ���� �Է�
        if (title.equals("")) { // �ƹ��͵� �Է����� ���� ��� �ڷ� ���ư���
            return;
        }
        CommandMessage commandMessage = new CommandMessage("return", title, null, userId);
        objectOutputStream.writeObject(commandMessage); // ������ ��ɾ� ����
        Book returnedBook = (Book) objectInputStream.readObject(); // �����κ��� ��� ���� �ޱ�.
        if (returnedBook != null) {
            System.out.println("You returned a book. - " + returnedBook.getTitle());
        } else {
            System.out.println("You did not borrow the book.");
        }
    }

    private void info() throws IOException, ClassNotFoundException { // �뿩�� å ��� ��� �޼ҵ�
        CommandMessage commandMessage = new CommandMessage("info", null, null, userId);
        objectOutputStream.writeObject(commandMessage); // ������ ��ɾ� ����
        List<Book> borrowedBookList = (List<Book>) objectInputStream.readObject(); // �����κ��� �뿩�� å ��� ���� �ޱ�
        System.out.println("You are currently borrowing " + borrowedBookList.size() + " books: "); // ���� å ���� ���
        for (int i = 0; i < borrowedBookList.size(); i++) { // ���� å ��� ���
            Book book = borrowedBookList.get(i);
            System.out.println((i + 1) + ". " + book.getTitle() + ", " + book.getAuthor());
        }
    }

    private void searchBook() throws IOException, ClassNotFoundException { // å �˻� �޼ҵ�
        String searchString = null; // �˻��� ���ڿ�
        do {
            System.out.print("search-string> ");
            searchString = scanner.nextLine(); // �˻��� ���ڿ� �Է�
            if (searchString.equals("") || searchString.isEmpty()) { // �ƹ��͵� �Է����� ���� ��� �ڷ� ���ư���
                return;
            }
            if (searchString.length() < 3) { // ���ڿ� ���̰� 3 �̸��̶�� ���Է�
                System.out.println("Search string must be longer than 2 characters.");
            } else {
                break;
            }
        } while (true);
        CommandMessage commandMessage = new CommandMessage("search", searchString, null, userId);
        objectOutputStream.writeObject(commandMessage); // ������ ��ɾ� ����
        List<Book> searchedBookList = (List<Book>) objectInputStream.readObject(); // �����κ��� �˻��� å ��� ���� �ޱ�
        System.out.println("Your search matched " + searchedBookList.size() + " results."); // �˻��� å ���� ���
        for (int i = 0; i < searchedBookList.size(); i++) { // �˻��� å ��� ���
            Book book = searchedBookList.get(i);
            System.out.println((i + 1) + ". " + book.getTitle() + ", " + book.getAuthor());
        }
    }

    public static void main(String[] args) {
        if (args.length != 2) { // ���ڰ� 2���� �ƴ϶�� ���� �޽��� ��� �� ����
            System.out.println("Please give the IP address and port number as arguments.");
            return;
        }
        String serverIp = args[0];
        int port = Integer.parseInt(args[1]);
        new Client().process(serverIp, port);
    }
}



