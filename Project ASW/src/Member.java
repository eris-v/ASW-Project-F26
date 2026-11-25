import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Member extends User {
    private Membership membership;
    private List<Loan> activeLoans;
    private List<Checkout> checkoutHistory;
    private List<Item> bookmarks;

    public Member(Integer userID, String name, String email, String password, String username, LocalDate birthDate, boolean isActive, Membership membership) {
        super(userID, name, email, password, username, birthDate, isActive);
        this.membership = membership;
        this.activeLoans = new ArrayList<>();
        this.checkoutHistory = new ArrayList<>();
        this.bookmarks = new ArrayList<>();
    }

    public Member(Integer userID, String name, String email, String password, String username, LocalDate birthDate, boolean isActive, List<Item> bookmarks, List<Checkout> checkoutHistory, Loan[] activeLoans, Membership membership) {
        super(userID, name, email, password, username, birthDate, isActive);
        this.bookmarks = (bookmarks != null) ? new ArrayList<>(bookmarks) : new ArrayList<>();
        this.checkoutHistory = (checkoutHistory != null) ? new ArrayList<>(checkoutHistory) : new ArrayList<>();
        this.activeLoans = (activeLoans != null) ? new ArrayList<>(Arrays.asList(activeLoans)) : new ArrayList<>();
        this.membership = membership;
    }

    public Loan borrowItem(Item item) {
        return borrowItem(item, Database.getInstance());
    }

    public Loan borrowItem(Item item, Database db) {
        if (!isActive() || item == null || membership == null || !item.isAvailable() || !membership.canBorrowMore(activeLoans.size())) {
            return null;
        }
        Database targetDb = (db != null) ? db : Database.getInstance();
        if (item.decrementLicenses(targetDb)) {
            LocalDate today = LocalDate.now();
            Checkout checkout = new Checkout(targetDb.getNextCheckoutId(), this, today);
            LocalDate dueDate = membership.calculateDueDate(today);
            Loan loan = new Loan(targetDb.getNextLoanId(), checkout, item, dueDate);

            checkout.addLoan(loan);
            checkoutHistory.add(checkout);
            activeLoans.add(loan);

            targetDb.addCheckout(checkout);
            targetDb.addLoan(loan);
            targetDb.updateUser(this);
            return loan;
        }
        return null;
    }

    public boolean returnItem(Loan loan) {
        return returnItem(loan, Database.getInstance());
    }

    public boolean returnItem(Loan loan, Database db) {
        if (loan != null && activeLoans.contains(loan)) {
            Database targetDb = (db != null) ? db : Database.getInstance();
            loan.markAsReturned(targetDb);
            activeLoans.remove(loan);
            targetDb.updateUser(this);
            return true;
        }
        return false;
    }

    public void addBookmark(Item item) {
        if (item != null && !bookmarks.contains(item)) {
            bookmarks.add(item);
            Database.getInstance().updateUser(this);
        }
    }

    public void removeBookmark(Item item) {
        if (bookmarks.remove(item)) {
            Database.getInstance().updateUser(this);
        }
    }

    public boolean upgradeMembership(Membership newTier) {
        if (newTier == null) {
            return false;
        }
        this.membership = newTier;
        Database.getInstance().updateUser(this);
        return true;
    }

    public boolean downloadForOffline(Item item) {
        return item != null && membership != null && membership.isAllowOfflineDownloads();
    }

    public Membership getMembership() {
        return membership;
    }

    public void setMembership(Membership membership) {
        this.membership = membership;
    }

    public List<Loan> getActiveLoans() {
        return activeLoans;
    }

    public void setActiveLoans(List<Loan> activeLoans) {
        this.activeLoans = (activeLoans != null) ? new ArrayList<>(activeLoans) : new ArrayList<>();
    }

    public List<Checkout> getCheckoutHistory() {
        return checkoutHistory;
    }

    public void setCheckoutHistory(List<Checkout> checkoutHistory) {
        this.checkoutHistory = (checkoutHistory != null) ? new ArrayList<>(checkoutHistory) : new ArrayList<>();
    }

    public List<Item> getBookmarks() {
        return bookmarks;
    }

    public void setBookmarks(List<Item> bookmarks) {
        this.bookmarks = (bookmarks != null) ? new ArrayList<>(bookmarks) : new ArrayList<>();
    }
}
