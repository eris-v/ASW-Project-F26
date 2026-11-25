import java.util.ArrayList;
import java.util.List;

public class Library {
    private String name;
    private Database database;
    private List<Loan> loans;
    private String contactEmail;

    public Library(String name, String contactEmail) {
        this(name, Database.getInstance(), contactEmail);
    }

    public Library(String name, Database database, String contactEmail) {
        this.name = name;
        this.database = (database != null) ? database : Database.getInstance();
        this.loans = this.database.getLoans();
        this.contactEmail = contactEmail;
    }

    public Library(String name, Database database, List<Loan> loans, String contactEmail) {
        this.name = name;
        this.database = (database != null) ? database : Database.getInstance();
        if (loans != null) {
            for (Loan loan : loans) {
                this.database.addLoan(loan);
            }
        }
        this.loans = this.database.getLoans();
        this.contactEmail = contactEmail;
    }

    // User management

    public boolean registerUser(User user) {
        return database.addUser(user);
    }

    public boolean removeUser(Integer userID) {
        return database.removeUser(userID);
    }

    public User findUserById(Integer userID) {
        return database.findUserById(userID);
    }

    public User findUserByUsername(String username) {
        return database.findUserByUsername(username);
    }

    public User authenticateUser(String username, String password) {
        if (username == null || password == null) {
            return null;
        }
        User user = database.findUserByUsername(username);
        if (user != null && user.login(username, password)) {
            return user;
        }
        return null;
    }

    public boolean suspendUser(Admin admin, User user) {
        if (admin == null || !admin.isActive() || user == null) {
            return false;
        }
        admin.suspendUser(user, database);
        return true;
    }

    public boolean approvePublisher(Admin admin, Publisher publisher) {
        if (admin == null || !admin.isActive() || publisher == null) {
            return false;
        }
        return admin.approvePublisher(publisher, database);
    }

    // Catalog management

    public boolean addItem(Item item) {
        return database.addItem(item);
    }

    public boolean publishItem(Publisher publisher, Item item) {
        if (publisher == null || !publisher.isActive() || item == null) {
            return false;
        }
        return publisher.publishItem(item, database);
    }

    public boolean removeItem(Integer itemID) {
        return database.removeItem(itemID);
    }

    public boolean removeItem(Admin admin, Integer itemID) {
        if (admin == null || !admin.isActive() || itemID == null) {
            return false;
        }
        return admin.removeCatalogItem(itemID, database);
    }

    public Item findItemById(Integer itemID) {
        return database.findItemById(itemID);
    }

    public List<Item> searchItems(String query) {
        List<Item> results = new ArrayList<>();
        if (query == null || query.isBlank()) {
            return results;
        }
        String lowerQuery = query.toLowerCase();
        for (Item item : database.getItems()) {
            boolean matchesTitle = item.getTitle() != null && item.getTitle().toLowerCase().contains(lowerQuery);
            boolean matchesGenre = item.getGenre() != null && item.getGenre().name().toLowerCase().contains(lowerQuery);
            boolean matchesAuthor = false;
            if (item.getAuthors() != null) {
                for (String author : item.getAuthors()) {
                    if (author != null && author.toLowerCase().contains(lowerQuery)) {
                        matchesAuthor = true;
                        break;
                    }
                }
            }
            boolean matchesKeyword = (item instanceof Paper paper) && paper.hasKeyword(query);

            if (matchesTitle || matchesGenre || matchesAuthor || matchesKeyword) {
                results.add(item);
            }
        }
        return results;
    }

    public List<Item> searchByGenre(Genre genre) {
        return database.findItemsByGenre(genre);
    }

    public List<Item> searchByAuthor(String author) {
        return database.findItemsByAuthor(author);
    }

    public List<Item> getAvailableItems() {
        return database.getAvailableItems();
    }

    // Loan and checkout operations

    public Loan borrowItem(Member member, Item item) {
        if (member == null || !member.isActive() || item == null) {
            return null;
        }
        return member.borrowItem(item, database);
    }

    public boolean returnItem(Member member, Loan loan) {
        if (member == null || loan == null) {
            return false;
        }
        return member.returnItem(loan, database);
    }

    public boolean renewLoan(Loan loan) {
        if (loan == null) {
            return false;
        }
        return loan.renewLoan(database);
    }

    public List<Loan> getActiveLoans() {
        return database.getActiveLoans();
    }

    public List<Loan> getOverdueLoans() {
        return database.getOverdueLoans();
    }

    public List<Loan> getLoansByMember(Member member) {
        if (member == null) {
            return new ArrayList<>();
        }
        return database.findLoansByMember(member.getUserID());
    }

    public int revokeOverdueLoans() {
        int revokedCount = 0;
        for (Loan loan : database.getOverdueLoans()) {
            loan.revokeAccess(database);
            revokedCount++;
        }
        return revokedCount;
    }

    public boolean joinPriorityWaitList(Member member, Item item) {
        if (member == null || !member.isActive() || item == null) {
            return false;
        }
        if (member.getMembership() instanceof Ultimate ultimate) {
            return ultimate.joinPriorityWaitList(item, member, database);
        }
        return false;
    }

    // Reporting

    public String generateLibraryReport() {
        return String.format(
                "Library: %s (%s) | Users: %d | Items: %d (%d available) | Checkouts: %d | Active Loans: %d | Overdue Loans: %d",
                name,
                contactEmail,
                database.getUsers().size(),
                database.getItems().size(),
                database.getAvailableItems().size(),
                database.getCheckouts().size(),
                database.getActiveLoans().size(),
                database.getOverdueLoans().size()
        );
    }

    // Getters and Setters

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Database getDatabase() {
        return database;
    }

    public void setDatabase(Database database) {
        this.database = (database != null) ? database : Database.getInstance();
        this.loans = this.database.getLoans();
    }

    public List<Loan> getLoans() {
        return loans;
    }

    public void setLoans(List<Loan> loans) {
        this.database.setLoans(loans);
        this.loans = this.database.getLoans();
    }

    public String getContactEmail() {
        return contactEmail;
    }

    public void setContactEmail(String contactEmail) {
        this.contactEmail = contactEmail;
    }
}
