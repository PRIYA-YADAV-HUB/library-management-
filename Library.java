import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;

public class LibraryManagementSystem {
    public static void main(String[] args) {
        Library library = new Library();
        Scanner scanner = new Scanner(System.in);
        
        // Pre-populate with sample data
        library.addBook(new Book("B001", "Effective Java", "Joshua Bloch", "978-0134686097", 2018, 5));
        library.addBook(new Book("B002", "Clean Code", "Robert Martin", "978-0136083238", 2008, 3));
        library.addMember(new Member("M001", "John Smith", "john@email.com", "1234567890"));
        
        while (true) {
            System.out.println("\n=== LIBRARY MANAGEMENT SYSTEM ===");
            System.out.println("1. Add Book");
            System.out.println("2. Add Member");
            System.out.println("3. Issue Book");
            System.out.println("4. Return Book");
            System.out.println("5. Search Books");
            System.out.println("6. View Member Details");
            System.out.println("7. Display Available Books");
            System.out.println("8. Exit");
            System.out.print("Enter your choice: ");
            
            try {
                int choice = scanner.nextInt();
                scanner.nextLine(); // Consume newline
                
                switch (choice) {
                    case 1 -> library.addBook(scanner);
                    case 2 -> library.addMember(scanner);
                    case 3 -> library.issueBook(scanner);
                    case 4 -> library.returnBook(scanner);
                    case 5 -> library.searchBooks(scanner);
                    case 6 -> library.viewMemberDetails(scanner);
                    case 7 -> library.displayAvailableBooks();
                    case 8 -> {
                        System.out.println("Exiting...");
                        scanner.close();
                        return;
                    }
                    default -> System.out.println("Invalid choice!");
                }
            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
                scanner.nextLine(); // Clear buffer
            }
        }
    }
}

class Book {
    private String bookId;
    private String title;
    private String author;
    private String isbn;
    private int publicationYear;
    private int availableCopies;
    private final int totalCopies;

    public Book(String bookId, String title, String author, String isbn, int publicationYear, int totalCopies) {
        this.bookId = bookId;
        this.title = title;
        this.author = author;
        this.isbn = isbn;
        this.publicationYear = publicationYear;
        this.totalCopies = totalCopies;
        this.availableCopies = totalCopies;
    }

    public void reduceAvailableCopies() {
        if (availableCopies > 0) {
            availableCopies--;
        }
    }

    public void increaseAvailableCopies() {
        if (availableCopies < totalCopies) {
            availableCopies++;
        }
    }

    // Getters
    public String getBookId() { return bookId; }
    public String getTitle() { return title; }
    public String getAuthor() { return author; }
    public String getIsbn() { return isbn; }
    public int getPublicationYear() { return publicationYear; }
    public int getAvailableCopies() { return availableCopies; }
    
    @Override
    public String toString() {
        return String.format("%s - '%s' by %s (%d) [%d/%d]", 
            bookId, title, author, publicationYear, availableCopies, totalCopies);
    }
}

class Member {
    private String memberId;
    private String name;
    private String email;
    private String phoneNumber;
    private final ArrayList<Book> borrowedBooks;
    private static final int MAX_BOOKS = 3;

    public Member(String memberId, String name, String email, String phoneNumber) {
        this.memberId = memberId;
        this.name = name;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.borrowedBooks = new ArrayList<>();
    }

    public boolean canBorrow() {
        return borrowedBooks.size() < MAX_BOOKS;
    }

    public void borrowBook(Book book) {
        if (canBorrow()) {
            borrowedBooks.add(book);
        }
    }

    public void returnBook(Book book) {
        borrowedBooks.remove(book);
    }

    // Getters
    public String getMemberId() { return memberId; }
    public String getName() { return name; }
    public ArrayList<Book> getBorrowedBooks() { return borrowedBooks; }
    
    @Override
    public String toString() {
        return String.format("%s - %s (%s)", memberId, name, email);
    }
}

class Library {
    private final ArrayList<Book> books = new ArrayList<>();
    private final ArrayList<Member> members = new ArrayList<>();
    private final HashMap<String, Book> bookMap = new HashMap<>();
    private final HashMap<String, Member> memberMap = new HashMap<>();

    public void addBook(Book book) {
        books.add(book);
        bookMap.put(book.getBookId(), book);
    }

    public void addMember(Member member) {
        members.add(member);
        memberMap.put(member.getMemberId(), member);
    }

    public void addBook(Scanner scanner) {
        System.out.print("Enter Book ID: ");
        String bookId = scanner.nextLine();
        System.out.print("Enter Title: ");
        String title = scanner.nextLine();
        System.out.print("Enter Author: ");
        String author = scanner.nextLine();
        System.out.print("Enter ISBN: ");
        String isbn = scanner.nextLine();
        System.out.print("Enter Publication Year: ");
        int year = scanner.nextInt();
        System.out.print("Enter Total Copies: ");
        int copies = scanner.nextInt();
        scanner.nextLine();
        
        addBook(new Book(bookId, title, author, isbn, year, copies));
        System.out.println("Book added successfully!");
    }

    public void addMember(Scanner scanner) {
        System.out.print("Enter Member ID: ");
        String memberId = scanner.nextLine();
        System.out.print("Enter Name: ");
        String name = scanner.nextLine();
        System.out.print("Enter Email: ");
        String email = scanner.nextLine();
        System.out.print("Enter Phone: ");
        String phone = scanner.nextLine();
        
        addMember(new Member(memberId, name, email, phone));
        System.out.println("Member added successfully!");
    }

    public void issueBook(Scanner scanner) {
        System.out.print("Enter Member ID: ");
        String memberId = scanner.nextLine();
        System.out.print("Enter Book ID: ");
        String bookId = scanner.nextLine();
        
        Member member = memberMap.get(memberId);
        Book book = bookMap.get(bookId);
        
        if (member == null || book == null) {
            System.out.println("Member or book not found");
            return;
        }
        
        if (!member.canBorrow()) {
            System.out.println("Member has reached maximum borrowing limit");
            return;
        }
        
        if (book.getAvailableCopies() <= 0) {
            System.out.println("No copies available");
            return;
        }
        
        member.borrowBook(book);
        book.reduceAvailableCopies();
        System.out.println("Book issued successfully!");
    }

    public void returnBook(Scanner scanner) {
        System.out.print("Enter Member ID: ");
        String memberId = scanner.nextLine();
        System.out.print("Enter Book ID: ");
        String bookId = scanner.nextLine();
        
        Member member = memberMap.get(memberId);
        Book book = bookMap.get(bookId);
        
        if (member == null || book == null) {
            System.out.println("Member or book not found");
            return;
        }
        
        if (!member.getBorrowedBooks().contains(book)) {
            System.out.println("This member didn't borrow this book");
            return;
        }
        
        member.returnBook(book);
        book.increaseAvailableCopies();
        System.out.println("Book returned successfully!");
    }

    public void searchBooks(Scanner scanner) {
        System.out.print("Search by title/author/ISBN: ");
        String query = scanner.nextLine().toLowerCase();
        
        List<Book> results = books.stream()
            .filter(b -> b.getTitle().toLowerCase().contains(query) || 
                        b.getAuthor().toLowerCase().contains(query) ||
                        b.getIsbn().contains(query))
            .toList();
        
        if (results.isEmpty()) {
            System.out.println("No books found");
        } else {
            System.out.println("Search results:");
            results.forEach(System.out::println);
        }
    }

    public void viewMemberDetails(Scanner scanner) {
        System.out.print("Enter Member ID: ");
        String memberId = scanner.nextLine();
        Member member = memberMap.get(memberId);
        
        if (member == null) {
            System.out.println("Member not found");
            return;
        }
        
        System.out.println("\nMember Details:");
        System.out.println(member);
        System.out.println("Borrowed Books:");
        member.getBorrowedBooks().forEach(System.out::println);
    }

    public void displayAvailableBooks() {
        System.out.println("Available Books:");
        books.stream()
            .filter(b -> b.getAvailableCopies() > 0)
            .forEach(System.out::println);
    }
}