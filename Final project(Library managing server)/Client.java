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
                    case "add":// 추가 명령어 입력 시 처리
                        addBook();
                        break;
                    case "borrow":// 대여 명령어 입력 시 처리
                        borrowBook();
                        break;
                    case "return":// 반납 명령어 입력 시 처리
                        returnBook();
                        break;
                    case "info": // 빌린 책 명령어 입력 시 처리
                        info();
                        break;
                    case "search": // 검색 명령어 입력 시 처리
                        searchBook();
                        break;
                    default: // 잘못된 명령어 입력 시 처리
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

    private String inputUserId() { // 사용자 ID 입력 메소드
        do {
            System.out.print("Enter userID>> ");
            String userId = scanner.nextLine().trim(); // 사용자 ID 입력
            if (!isValidUserId(userId)) { // 입력된 ID가 유효하지 않다면 에러 메시지 출력 후 재 입력
                System.out.println("UserID must be a single word with lowercase alphabets and numbers.");
            } else { // 유효하다면 헬로우 메시지 출력
                System.out.println("Hello " + userId + "!");
                return userId;
            }
        } while (true);
    }

    private boolean isValidUserId(String userId) { // 사용자 입력 ID가 유효한지 체크하는 메소드
        if (userId.split(" ").length > 1) { // 공백이 포함되어 있다면 무효
            return false;
        }
        String specialChars = "~`!@#$%^&*()-_=+\\|[{]};:'\",<.>/?"; // 특수 문자들
        boolean numberPresent = false;
        boolean upperCasePresent = false;
        boolean lowerCasePresent = false;
        boolean specialCharacterPresent = false;

        for (int i = 0; i < userId.length(); i++) {
            char currentCharacter = userId.charAt(i); // 검사할 현재 인덱스 문자
            if (Character.isDigit(currentCharacter)) { // 숫자 체크
                numberPresent = true;
            } else if (Character.isUpperCase(currentCharacter)) { // 대문자 체크
                upperCasePresent = true;
            } else if (Character.isLowerCase(currentCharacter)) { // 소문자 체크
                lowerCasePresent = true;
            } else if (specialChars.contains(String.valueOf(currentCharacter))) { // 특수문자 체크
                specialCharacterPresent = true;
            }
        }
        if (upperCasePresent || specialCharacterPresent) { // 대문자, 특수문자 중 하나라도 있다면 무효
            return false;
        }
        return lowerCasePresent || numberPresent; // 소문자와 숫자만 있다면 유효
    }

    private void addBook() throws IOException { // 책 추가 메소드
        System.out.print("add-title> ");
        String title = scanner.nextLine().trim(); // 추가할 책 제목 입력
        if (title.equals("") || title.isEmpty()) { // 아무것도 입력하지 않은 경우 뒤로 돌아가기
            return;
        }
        System.out.print("add-author> ");
        String author = scanner.nextLine().trim(); // 추가할 책 저자 입력
        if (author.equals("") || author.isEmpty()) { // 아무것도 입력하지 않은 경우 뒤로 돌아가기
            return;
        }
        CommandMessage commandMessage = new CommandMessage("add", title, author, userId);
        objectOutputStream.writeObject(commandMessage); // 서버로 명령어 전달
        boolean result = inputStream.read() == 1; // 서버로부터 결과 전달 받기. 1이라면 정상 0이면 에러
        if (result) {
            System.out.println("A new book added to the list.");
        } else {
            System.out.println("The book already exists in the list.");
        }
    }

    private void borrowBook() throws IOException, ClassNotFoundException { // 책 대여 메소드
        System.out.print("borrow-title> ");
        String title = scanner.nextLine().trim(); // 대여할 책 제목 입력
        if (title.equals("") || title.isEmpty()) { // 아무것도 입력하지 않은 경우 뒤로 돌아가기
            return;
        }
        CommandMessage commandMessage = new CommandMessage("borrow", title, null, userId);
        objectOutputStream.writeObject(commandMessage); // 서버로 명령어 전달
        Book borrowedBook = (Book) objectInputStream.readObject(); // 서버로부터 결과 전달 받기.
        if (borrowedBook != null) {
            System.out.println("You borrowed a book. - " + borrowedBook.getTitle());
        } else {
            System.out.println("The book is not available.");
        }
    }

    private void returnBook() throws IOException, ClassNotFoundException { // 책 반납 메소드
        System.out.print("return-title> ");
        String title = scanner.nextLine().trim(); // 반납할 책 제목 입력
        if (title.equals("")) { // 아무것도 입력하지 않은 경우 뒤로 돌아가기
            return;
        }
        CommandMessage commandMessage = new CommandMessage("return", title, null, userId);
        objectOutputStream.writeObject(commandMessage); // 서버로 명령어 전달
        Book returnedBook = (Book) objectInputStream.readObject(); // 서버로부터 결과 전달 받기.
        if (returnedBook != null) {
            System.out.println("You returned a book. - " + returnedBook.getTitle());
        } else {
            System.out.println("You did not borrow the book.");
        }
    }

    private void info() throws IOException, ClassNotFoundException { // 대여한 책 목록 출력 메소드
        CommandMessage commandMessage = new CommandMessage("info", null, null, userId);
        objectOutputStream.writeObject(commandMessage); // 서버로 명령어 전달
        List<Book> borrowedBookList = (List<Book>) objectInputStream.readObject(); // 서버로부터 대여한 책 목록 전달 받기
        System.out.println("You are currently borrowing " + borrowedBookList.size() + " books: "); // 빌린 책 개수 출력
        for (int i = 0; i < borrowedBookList.size(); i++) { // 빌린 책 목록 출력
            Book book = borrowedBookList.get(i);
            System.out.println((i + 1) + ". " + book.getTitle() + ", " + book.getAuthor());
        }
    }

    private void searchBook() throws IOException, ClassNotFoundException { // 책 검색 메소드
        String searchString = null; // 검색할 문자열
        do {
            System.out.print("search-string> ");
            searchString = scanner.nextLine(); // 검색할 문자열 입력
            if (searchString.equals("") || searchString.isEmpty()) { // 아무것도 입력하지 않은 경우 뒤로 돌아가기
                return;
            }
            if (searchString.length() < 3) { // 문자열 길이가 3 미만이라면 재입력
                System.out.println("Search string must be longer than 2 characters.");
            } else {
                break;
            }
        } while (true);
        CommandMessage commandMessage = new CommandMessage("search", searchString, null, userId);
        objectOutputStream.writeObject(commandMessage); // 서버로 명령어 전달
        List<Book> searchedBookList = (List<Book>) objectInputStream.readObject(); // 서버로부터 검색된 책 목록 전달 받기
        System.out.println("Your search matched " + searchedBookList.size() + " results."); // 검색된 책 개수 출력
        for (int i = 0; i < searchedBookList.size(); i++) { // 검색된 책 목록 출력
            Book book = searchedBookList.get(i);
            System.out.println((i + 1) + ". " + book.getTitle() + ", " + book.getAuthor());
        }
    }

    public static void main(String[] args) {
        if (args.length != 2) { // 인자가 2개가 아니라면 에러 메시지 출력 후 종료
            System.out.println("Please give the IP address and port number as arguments.");
            return;
        }
        String serverIp = args[0];
        int port = Integer.parseInt(args[1]);
        new Client().process(serverIp, port);
    }
}



