import java.time.LocalDate;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        Database db = Database.getInstance();
        db.clearAll();

        Library library = new Library("ASW Digital Library", db, "contact@aswlibrary.org");

        // 1. Create and register users (Admin, Publisher, Members)
        Admin admin = new Admin(
                db.getNextUserId(), "Sara Admin", "sara@aswlibrary.org", "admin123", "sara_admin",
                LocalDate.of(1990, 5, 12), true, AdminRole.SUPER_ADMIN,
                new String[]{"MANAGE_USERS", "MANAGE_CATALOG"}, "Operations"
        );
        library.registerUser(admin);

        Publisher publisher = new Publisher(
                db.getNextUserId(), "Omar Publisher", "omar@techpress.com", "pub123", "techpress",
                LocalDate.of(1985, 3, 20), false, "TechPress Media", "LIC-2026-001", 2.5
        );
        library.approvePublisher(admin, publisher);

        Member freeMember = new Member(
                db.getNextUserId(), "Ali Hassan", "ali@mail.com", "pass123", "ali_h",
                LocalDate.of(2002, 8, 15), true, new Free()
        );
        Member proMember = new Member(
                db.getNextUserId(), "Nour Adel", "nour@mail.com", "pass456", "nour_a",
                LocalDate.of(1999, 11, 3), true,
                new Pro("MONTHLY", LocalDate.now().plusMonths(1), "VISA-4242")
        );
        Member ultimateMember = new Member(
                db.getNextUserId(), "Youssef Karim", "youssef@mail.com", "pass789", "youssef_k",
                LocalDate.of(1997, 1, 25), true,
                new Ultimate("ANNUAL", LocalDate.now().plusYears(1), "MASTERCARD-1111", true)
        );
        library.registerUser(freeMember);
        library.registerUser(proMember);
        library.registerUser(ultimateMember);

        // 2. Publish items (Book, Journal, Paper) into the Database via Publisher & Library
        Book book = new Book(
                db.getNextItemId(), "Clean Architecture", new String[]{"Robert C. Martin"},
                publisher, LocalDate.of(2017, 9, 20), Genre.COMPUTER_SCIENCE, "English",
                "https://aswlibrary.org/files/clean-arch.pdf", FileFormat.PDF, 12.5, 2,
                978013449, 432, "1st", new String[]{"Introduction", "Design Principles", "Components"}
        );
        library.publishItem(publisher, book);

        Journal journal = new Journal(
                db.getNextItemId(), "IEEE Software Engineering", new String[]{"Various Authors"},
                publisher, LocalDate.of(2026, 1, 10), Genre.ENGINEERING, "English",
                "https://aswlibrary.org/files/ieee-se.epub", FileFormat.EPUB, 8.2, 1,
                12345678, 43, 2, "Dr. Jane Smith", "Quarterly"
        );
        library.publishItem(publisher, journal);

        Paper paper = new Paper(
                db.getNextItemId(), "Scalable Distributed Databases", new String[]{"Dr. Ahmed Tarek"},
                publisher, LocalDate.of(2025, 6, 1), Genre.COMPUTER_SCIENCE, "English",
                "https://aswlibrary.org/files/dist-db.pdf", FileFormat.PDF, 3.1, 1,
                10114599, "A study on consensus protocols in distributed databases.",
                new String[]{"distributed", "databases", "consensus"}, "Cairo University"
        );
        library.publishItem(publisher, paper);

        // 3. Authentication & Search
        User loggedIn = library.authenticateUser("nour_a", "pass456");
        System.out.println("Authenticated: " + (loggedIn != null ? loggedIn.getName() : "Failed"));

        List<Item> searchResults = library.searchItems("databases");
        System.out.println("Search 'databases' found: " + searchResults.size() + " item(s)");

        // 4. Borrowing, Receipt, Renewal, Priority Waitlist, Returning
        Loan loan1 = library.borrowItem(proMember, book);
        System.out.println("\n--- Checkout Receipt ---");
        System.out.println(loan1.getCheckout().generateReceipt());

        boolean renewed = library.renewLoan(loan1);
        System.out.println("Loan renewed: " + renewed + " | New due date: " + loan1.getDueDate());

        Loan loan2 = library.borrowItem(freeMember, journal);
        System.out.println("Journal available after borrow: " + journal.isAvailable());

        boolean waitlisted = library.joinPriorityWaitList(ultimateMember, journal);
        System.out.println("Ultimate member joined priority waitlist: " + waitlisted
                + " | Waitlist size: " + db.getWaitList(journal).size());

        boolean returned = library.returnItem(freeMember, loan2);
        System.out.println("Journal returned: " + returned + " | Available again: " + journal.isAvailable());

        // 5. Overdue loan revocation check
        Loan loan3 = library.borrowItem(ultimateMember, paper);
        loan3.setDueDate(LocalDate.now().minusDays(2));
        int revoked = library.revokeOverdueLoans();
        System.out.println("Overdue loans revoked: " + revoked + " | Paper available: " + paper.isAvailable());

        // 6. Reports from Library & Admin
        System.out.println("\n--- Reports ---");
        System.out.println(library.generateLibraryReport());
        System.out.println(admin.generateSystemReport());
    }
}
