import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class Database {
    private static Database instance = new Database();

    private List<User> users;
    private List<Item> items;
    private List<Checkout> checkouts;
    private List<Loan> loans;
    private Map<Integer, List<Member>> waitLists;

    private int nextUserId = 1;
    private int nextItemId = 1;
    private int nextCheckoutId = 1;
    private int nextLoanId = 1;

    public Database() {
        this.users = new ArrayList<>();
        this.items = new ArrayList<>();
        this.checkouts = new ArrayList<>();
        this.loans = new ArrayList<>();
        this.waitLists = new HashMap<>();
    }

    public Database(List<User> users, List<Item> items, List<Checkout> checkouts, List<Loan> loans) {
        this.users = (users != null) ? new ArrayList<>(users) : new ArrayList<>();
        this.items = (items != null) ? new ArrayList<>(items) : new ArrayList<>();
        this.checkouts = (checkouts != null) ? new ArrayList<>(checkouts) : new ArrayList<>();
        this.loans = (loans != null) ? new ArrayList<>(loans) : new ArrayList<>();
        this.waitLists = new HashMap<>();
        syncSequenceCounters();
    }

    private void syncSequenceCounters() {
        for (User u : users) {
            if (u.getUserID() != null && u.getUserID() >= nextUserId) {
                nextUserId = u.getUserID() + 1;
            }
        }
        for (Item i : items) {
            if (i.getItemID() != null && i.getItemID() >= nextItemId) {
                nextItemId = i.getItemID() + 1;
            }
        }
        for (Checkout c : checkouts) {
            if (c.getCheckoutID() != null && c.getCheckoutID() >= nextCheckoutId) {
                nextCheckoutId = c.getCheckoutID() + 1;
            }
        }
        for (Loan l : loans) {
            if (l.getLoanID() != null && l.getLoanID() >= nextLoanId) {
                nextLoanId = l.getLoanID() + 1;
            }
        }
    }

    public static Database getInstance() {
        if (instance == null) {
            instance = new Database();
        }
        return instance;
    }

    public static void setInstance(Database database) {
        instance = (database != null) ? database : new Database();
    }

    // User operations

    public boolean addUser(User user) {
        if (user == null) {
            return false;
        }
        if (user.getUserID() == null) {
            user.setUserID(getNextUserId());
        } else if (findUserById(user.getUserID()) != null) {
            return false;
        }
        if (user.getUsername() != null && findUserByUsername(user.getUsername()) != null) {
            return false;
        }
        if (user.getUserID() >= nextUserId) {
            nextUserId = user.getUserID() + 1;
        }
        return users.add(user);
    }

    public boolean updateUser(User updatedUser) {
        if (updatedUser == null || updatedUser.getUserID() == null) {
            return false;
        }
        for (int i = 0; i < users.size(); i++) {
            if (Objects.equals(users.get(i).getUserID(), updatedUser.getUserID())) {
                users.set(i, updatedUser);
                return true;
            }
        }
        return false;
    }

    public boolean removeUser(Integer userID) {
        if (userID == null) {
            return false;
        }
        return users.removeIf(user -> Objects.equals(user.getUserID(), userID));
    }

    public User findUserById(Integer userID) {
        if (userID == null) {
            return null;
        }
        for (User user : users) {
            if (Objects.equals(user.getUserID(), userID)) {
                return user;
            }
        }
        return null;
    }

    public User findUserByUsername(String username) {
        if (username == null || username.isBlank()) {
            return null;
        }
        for (User user : users) {
            if (user.getUsername() != null && user.getUsername().equalsIgnoreCase(username)) {
                return user;
            }
        }
        return null;
    }

    public User findUserByEmail(String email) {
        if (email == null || email.isBlank()) {
            return null;
        }
        for (User user : users) {
            if (user.getEmail() != null && user.getEmail().equalsIgnoreCase(email)) {
                return user;
            }
        }
        return null;
    }

    public List<Member> getAllMembers() {
        List<Member> members = new ArrayList<>();
        for (User user : users) {
            if (user instanceof Member member) {
                members.add(member);
            }
        }
        return members;
    }

    public List<Publisher> getAllPublishers() {
        List<Publisher> publishers = new ArrayList<>();
        for (User user : users) {
            if (user instanceof Publisher publisher) {
                publishers.add(publisher);
            }
        }
        return publishers;
    }

    public List<Admin> getAllAdmins() {
        List<Admin> admins = new ArrayList<>();
        for (User user : users) {
            if (user instanceof Admin admin) {
                admins.add(admin);
            }
        }
        return admins;
    }

    public int getNextUserId() {
        return nextUserId++;
    }

    // Item operations

    public boolean addItem(Item item) {
        if (item == null) {
            return false;
        }
        if (item.getItemID() == null) {
            item.setItemID(getNextItemId());
        } else if (findItemById(item.getItemID()) != null) {
            return false;
        }
        if (item.getItemID() >= nextItemId) {
            nextItemId = item.getItemID() + 1;
        }
        return items.add(item);
    }

    public boolean updateItem(Item updatedItem) {
        if (updatedItem == null || updatedItem.getItemID() == null) {
            return false;
        }
        for (int i = 0; i < items.size(); i++) {
            if (Objects.equals(items.get(i).getItemID(), updatedItem.getItemID())) {
                items.set(i, updatedItem);
                return true;
            }
        }
        return false;
    }

    public boolean removeItem(Integer itemID) {
        if (itemID == null) {
            return false;
        }
        waitLists.remove(itemID);
        return items.removeIf(item -> Objects.equals(item.getItemID(), itemID));
    }

    public Item findItemById(Integer itemID) {
        if (itemID == null) {
            return null;
        }
        for (Item item : items) {
            if (Objects.equals(item.getItemID(), itemID)) {
                return item;
            }
        }
        return null;
    }

    public List<Item> findItemsByTitle(String title) {
        List<Item> results = new ArrayList<>();
        if (title == null || title.isBlank()) {
            return results;
        }
        String lowerTitle = title.toLowerCase();
        for (Item item : items) {
            if (item.getTitle() != null && item.getTitle().toLowerCase().contains(lowerTitle)) {
                results.add(item);
            }
        }
        return results;
    }

    public List<Item> findItemsByGenre(Genre genre) {
        List<Item> results = new ArrayList<>();
        if (genre == null) {
            return results;
        }
        for (Item item : items) {
            if (item.getGenre() == genre) {
                results.add(item);
            }
        }
        return results;
    }

    public List<Item> findItemsByAuthor(String author) {
        List<Item> results = new ArrayList<>();
        if (author == null || author.isBlank()) {
            return results;
        }
        String lowerAuthor = author.toLowerCase();
        for (Item item : items) {
            if (item.getAuthors() != null) {
                for (String a : item.getAuthors()) {
                    if (a != null && a.toLowerCase().contains(lowerAuthor)) {
                        results.add(item);
                        break;
                    }
                }
            }
        }
        return results;
    }

    public List<Item> findItemsByPublisher(Publisher publisher) {
        List<Item> results = new ArrayList<>();
        if (publisher == null) {
            return results;
        }
        for (Item item : items) {
            if (item.getPublisher() != null
                    && Objects.equals(item.getPublisher().getUserID(), publisher.getUserID())) {
                results.add(item);
            }
        }
        return results;
    }

    public List<Item> getAvailableItems() {
        List<Item> available = new ArrayList<>();
        for (Item item : items) {
            if (item.isAvailable()) {
                available.add(item);
            }
        }
        return available;
    }

    public int getNextItemId() {
        return nextItemId++;
    }

    // Checkout operations

    public boolean addCheckout(Checkout checkout) {
        if (checkout == null) {
            return false;
        }
        if (checkout.getCheckoutID() == null) {
            checkout.setCheckoutID(getNextCheckoutId());
        } else if (findCheckoutById(checkout.getCheckoutID()) != null) {
            return false;
        }
        if (checkout.getCheckoutID() >= nextCheckoutId) {
            nextCheckoutId = checkout.getCheckoutID() + 1;
        }
        return checkouts.add(checkout);
    }

    public boolean updateCheckout(Checkout updatedCheckout) {
        if (updatedCheckout == null || updatedCheckout.getCheckoutID() == null) {
            return false;
        }
        for (int i = 0; i < checkouts.size(); i++) {
            if (Objects.equals(checkouts.get(i).getCheckoutID(), updatedCheckout.getCheckoutID())) {
                checkouts.set(i, updatedCheckout);
                return true;
            }
        }
        return false;
    }

    public boolean removeCheckout(Integer checkoutID) {
        if (checkoutID == null) {
            return false;
        }
        return checkouts.removeIf(checkout -> Objects.equals(checkout.getCheckoutID(), checkoutID));
    }

    public Checkout findCheckoutById(Integer checkoutID) {
        if (checkoutID == null) {
            return null;
        }
        for (Checkout checkout : checkouts) {
            if (Objects.equals(checkout.getCheckoutID(), checkoutID)) {
                return checkout;
            }
        }
        return null;
    }

    public List<Checkout> findCheckoutsByMember(Integer memberID) {
        List<Checkout> results = new ArrayList<>();
        if (memberID == null) {
            return results;
        }
        for (Checkout checkout : checkouts) {
            if (checkout.getMemberID() != null && Objects.equals(checkout.getMemberID().getUserID(), memberID)) {
                results.add(checkout);
            }
        }
        return results;
    }

    public int getNextCheckoutId() {
        return nextCheckoutId++;
    }

    // Loan operations

    public boolean addLoan(Loan loan) {
        if (loan == null) {
            return false;
        }
        if (loan.getLoanID() == null) {
            loan.setLoanID(getNextLoanId());
        } else if (findLoanById(loan.getLoanID()) != null) {
            return false;
        }
        if (loan.getLoanID() >= nextLoanId) {
            nextLoanId = loan.getLoanID() + 1;
        }
        return loans.add(loan);
    }

    public boolean updateLoan(Loan updatedLoan) {
        if (updatedLoan == null || updatedLoan.getLoanID() == null) {
            return false;
        }
        for (int i = 0; i < loans.size(); i++) {
            if (Objects.equals(loans.get(i).getLoanID(), updatedLoan.getLoanID())) {
                loans.set(i, updatedLoan);
                return true;
            }
        }
        return false;
    }

    public boolean removeLoan(Integer loanID) {
        if (loanID == null) {
            return false;
        }
        return loans.removeIf(loan -> Objects.equals(loan.getLoanID(), loanID));
    }

    public Loan findLoanById(Integer loanID) {
        if (loanID == null) {
            return null;
        }
        for (Loan loan : loans) {
            if (Objects.equals(loan.getLoanID(), loanID)) {
                return loan;
            }
        }
        return null;
    }

    public List<Loan> getActiveLoans() {
        List<Loan> active = new ArrayList<>();
        for (Loan loan : loans) {
            if ("ACTIVE".equals(loan.getStatus())) {
                active.add(loan);
            }
        }
        return active;
    }

    public List<Loan> getOverdueLoans() {
        List<Loan> overdue = new ArrayList<>();
        for (Loan loan : loans) {
            if (loan.isOverdue()) {
                overdue.add(loan);
            }
        }
        return overdue;
    }

    public List<Loan> findLoansByMember(Integer memberID) {
        List<Loan> results = new ArrayList<>();
        if (memberID == null) {
            return results;
        }
        for (Loan loan : loans) {
            if (loan.getCheckout() != null
                    && loan.getCheckout().getMemberID() != null
                    && Objects.equals(loan.getCheckout().getMemberID().getUserID(), memberID)) {
                results.add(loan);
            }
        }
        return results;
    }

    public int getNextLoanId() {
        return nextLoanId++;
    }

    // Priority Waitlist operations

    public boolean addToWaitList(Item item, Member member) {
        if (item == null || item.getItemID() == null || member == null) {
            return false;
        }
        List<Member> queue = waitLists.computeIfAbsent(item.getItemID(), k -> new ArrayList<>());
        if (queue.contains(member)) {
            return false;
        }
        return queue.add(member);
    }

    public boolean removeFromWaitList(Item item, Member member) {
        if (item == null || item.getItemID() == null || member == null) {
            return false;
        }
        List<Member> queue = waitLists.get(item.getItemID());
        return queue != null && queue.remove(member);
    }

    public List<Member> getWaitList(Item item) {
        if (item == null || item.getItemID() == null) {
            return new ArrayList<>();
        }
        List<Member> queue = waitLists.get(item.getItemID());
        return (queue != null) ? new ArrayList<>(queue) : new ArrayList<>();
    }

    public void clearAll() {
        users.clear();
        items.clear();
        checkouts.clear();
        loans.clear();
        waitLists.clear();
        nextUserId = 1;
        nextItemId = 1;
        nextCheckoutId = 1;
        nextLoanId = 1;
    }

    // Getters and Setters

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = (users != null) ? new ArrayList<>(users) : new ArrayList<>();
        syncSequenceCounters();
    }

    public List<Item> getItems() {
        return items;
    }

    public void setItems(List<Item> items) {
        this.items = (items != null) ? new ArrayList<>(items) : new ArrayList<>();
        syncSequenceCounters();
    }

    public List<Checkout> getCheckouts() {
        return checkouts;
    }

    public void setCheckouts(List<Checkout> checkouts) {
        this.checkouts = (checkouts != null) ? new ArrayList<>(checkouts) : new ArrayList<>();
        syncSequenceCounters();
    }

    public List<Loan> getLoans() {
        return loans;
    }

    public void setLoans(List<Loan> loans) {
        this.loans = (loans != null) ? new ArrayList<>(loans) : new ArrayList<>();
        syncSequenceCounters();
    }

    public Map<Integer, List<Member>> getWaitLists() {
        return waitLists;
    }

    public void setWaitLists(Map<Integer, List<Member>> waitLists) {
        this.waitLists = (waitLists != null) ? new HashMap<>(waitLists) : new HashMap<>();
    }
}
