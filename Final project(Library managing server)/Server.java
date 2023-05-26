package finalproject;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Server {
    public static final String BOOKS_FILE_PATH = "books.txt"; // 책 정보를 저장할 파일 경로
    private List<Book> bookList = new ArrayList<>(); // 책 리스트

    public void process(int port) {
        readBooks(); // 시작 시 책 파일로부터 정보 읽기
        listenClient(port); // 입력된 port로 서버 기동
    }

    private void readBooks() {
        try {
            File file = new File(BOOKS_FILE_PATH);
            BufferedReader br = new BufferedReader(new FileReader(file));
            String line;
            while ((line = br.readLine()) != null) {
                String[] lineSplit = line.split("\t"); // 각 필드는 탭 문자로 구분되어 있음
                String title = lineSplit[0];
                String author = lineSplit[1];
                String borrowId = lineSplit[2];
                bookList.add(new Book(title, author, borrowId)); // 책 정보 리스트에 추가
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void listenClient(int port) { // 포트 번호로 클라이언트 기다리
        try {
            ServerSocket serverSocket = new ServerSocket(port);
            while (true) {
                Socket socket = serverSocket.accept(); // 클라이언트 접속 대기
                new ServerThread(socket, bookList).start(); // 클라이언트 명령어 처리할 쓰레드 객체 생성 후 시작
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        if (args.length != 1) { // 인자가 1개가 아닌 경우 에러 메시지 출력 후 종료
            System.out.println("Please give the port number as an argument.");
            return;
        }
        int port = Integer.parseInt(args[0]);
        new Server().process(port);
    }
}

class ServerThread extends Thread { // 서버에서 클라이언트 연결 시 명령어 받아줄 쓰레드 클래스
    private List<Book> bookList; // 서버 책 리스트
    private InputStream inputStream; // 클라이언트에서 입력 받을 스트림
    private ObjectInputStream objectInputStream; // 클라이언트에서 객체 입력 받을 스트림
    private OutputStream outputStream; // 클라이언트로 출력할 스트림
    private ObjectOutputStream objectOutputStream; // 클라이언트로 객체 출력할 스트림

    ServerThread(Socket socket, List<Book> bookList) throws IOException {
        this.bookList = bookList;
        inputStream = socket.getInputStream();
        objectInputStream = new ObjectInputStream(inputStream);
        outputStream = socket.getOutputStream();
        objectOutputStream = new ObjectOutputStream(outputStream);
    }

    @Override
    public void run() { // 클라이언트 접속 시 실행되는 메소드
        System.out.println("Login!!");
        while (true) {
            try {
                CommandMessage commandMessage = (CommandMessage) objectInputStream.readObject();
                switch (commandMessage.getType()) { // 클라이언트로 전달받은 명령어 타입에 따라 처리
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

    private boolean addBook(CommandMessage commandMessage) throws IOException { // 책 추가 메소드
        String title = commandMessage.getTitle(); // 추가할 책 제목
        String author = commandMessage.getAuthor(); // 추가할 책 저자
        for (Book book : bookList) {
            if (title.equalsIgnoreCase(book.getTitle())) { // 만약 같은 책 제목이 있다면 불가 처리
                outputStream.write(0);
                return false;
            }
        }
        if(author != null) { // 저자까지 입력되어 있다면 책 리스트에 저장
            bookList.add(new Book(title, author, "-")); // 책 추가 시 기본값으로 대여 ID는 "-" 입력
            Collections.sort(bookList); // 책이 새로 추가됐으므로 제목 알파벳순으로 정렬
            saveBooks(); // 책 정보 저장
        }
        outputStream.write(1);
        return true;
    }


    private boolean borrowBook(CommandMessage commandMessage) throws IOException { // 책 대여 명령어 처리
        String title = commandMessage.getTitle(); // 빌릴 책 제목
        String userId = commandMessage.getUserId(); // 명령어 입력한 사용자 ID
        for (Book book : bookList) {
            if (title.equalsIgnoreCase(book.getTitle())) { // 빌릴 책을 찾았고
                if (book.getBorrowId().equals("-")) {  // 아무도 빌려가지 않은 상태라면 책 대여
                    book.setBorrowId(userId); // 빌린 사람 ID를 명령어를 입력한 사용자 ID로 변경
                    saveBooks(); // 책 정보 저장
                    objectOutputStream.writeObject(book); // 클라이언트로 빌린 책 전달
                    return true;
                }
                objectOutputStream.writeObject(null); // 책은 찾았지만 빌려간 사람이 있다면 null 전달
                return false;
            }
        }
        objectOutputStream.writeObject(null); // 동일한 책 제목을 찾지 못했다면 null 전달
        return false;
    }

    private boolean returnBook(CommandMessage commandMessage) throws IOException { // 책 반납 명령어 처리
        String title = commandMessage.getTitle(); // 반납할 책 제목
        String userId = commandMessage.getUserId(); // 명령어 입력한 사용자 ID
        for (Book book : bookList) {
            if (title.equalsIgnoreCase(book.getTitle())) { // 반납할 책을 찾았고
                if (book.getBorrowId().equals(userId)) {  // 책을 대여해간 사용자가 명령어를 입력한 사용자와 같다면 반납 처리
                    book.setBorrowId("-"); // 빌린 사람 ID를 "-"으로 변
                    saveBooks(); // 책 정보 저장
                    objectOutputStream.writeObject(book); // 클라이언트로 반납한 책 전달
                    return true; // 책 반납을 성공했으므로 true 반환
                }
                objectOutputStream.writeObject(null); // 책은 찾았지만 빌려간 사람이 다르다면 null 전달
                return false; // 책은 찾았지만 빌려간 사람이 다르다면 false 반환
            }
        }
        objectOutputStream.writeObject(null); // 동일한 책 제목을 찾지 못했다면 null 전달
        return false; // 동일한 책 제목을 찾지 못했다면 false 반환
    }

    private void info(CommandMessage commandMessage) throws IOException { // 사용자가 대출중인 도서 목록 조회 명령어 처리
        String userId = commandMessage.getUserId();
        List<Book> borrowedBookList = new ArrayList<>();
        for (Book book : bookList) {
            if (book.getBorrowId().equals(userId)) {  // 책을 대여해간 사용자가 명령어를 입력한 사용자와 같다면 리스트 추가
                borrowedBookList.add(book);
            }
        }
        objectOutputStream.writeObject(borrowedBookList); // 클라이언트로 빌린 책 목록 전달
    }

    private void searchBook(CommandMessage commandMessage) throws IOException { // 책 검색 명령어 처리
        String title = commandMessage.getTitle();
        List<Book> searchedBookList = new ArrayList<>();
        for (Book book : bookList) {
            // 책 제목이나 저자에서 대소문자를 구분하지 않고 검색 단어가 포함되어 있다면 검색 책 리스트에 추가
            if (book.getTitle().toLowerCase().contains(title.toLowerCase()) || book.getAuthor().toLowerCase().contains(title.toLowerCase())) {
                searchedBookList.add(book);
            }
        }
        objectOutputStream.writeObject(searchedBookList); // 클라이언트로 검색된 책 목록 전달
    }

    private void saveBooks() { // 책 저장 메소드
        try {
            BufferedWriter bw = new BufferedWriter(new FileWriter(new File(Server.BOOKS_FILE_PATH)));
            for (Book book : bookList) { // 모든 책 정보를 순차적으로 저장
                bw.write(book.toString() + "\n");
            }
            bw.close();
            System.out.println("Books is saved");
        } catch (IOException ie) {
            ie.printStackTrace();
        }
    }
}

class Book implements Comparable<Book>, Serializable { // 책 정보를 저장하는 클래스로 정렬 가능해야 하기 때문에 Comparable 구현, 소켓으로 전달할 수 있도록 Serializable 구현
    private final String title; // 책 제목
    private final String author; // 책 저자
    private String borrowId; // 책을 빌린 사용자 ID, 없을 경우 "-"

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
    public int compareTo(Book o) { // 책 제목을 대소문자를 구분하지 않으며 알파벳 순서로 정렬
        return title.compareToIgnoreCase(o.getTitle());
    }

    @Override
    public String toString() {
        return title + "\t" + author + "\t" + borrowId;
    }
}

class CommandMessage implements Serializable { // 클라이언트가 서버로 전달할 명령 정보를 담은 클래스
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

