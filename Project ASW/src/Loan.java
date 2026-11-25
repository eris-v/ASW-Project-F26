import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class Loan {
    private Integer loanID;
    private Checkout checkout;
    private Item item;
    private LocalDate dueDate;
    private LocalDate returnDate;
    private Integer renewCount;
    private String status;

    public Loan(Integer loanID, Checkout checkout, Item item, LocalDate dueDate) {
        this.loanID = loanID;
        this.checkout = checkout;
        this.item = item;
        this.dueDate = dueDate;
        this.returnDate = null;
        this.renewCount = 0;
        this.status = "ACTIVE";
    }

    public boolean renewLoan() {
        return renewLoan(Database.getInstance());
    }

    public boolean renewLoan(Database db) {
        if (!"ACTIVE".equals(this.status) || checkout == null || checkout.getMemberID() == null) {
            return false;
        }
        Membership membership = checkout.getMemberID().getMembership();
        if (membership != null && membership.canRenew(this.renewCount)) {
            this.dueDate = membership.calculateDueDate(this.dueDate);
            this.renewCount++;
            if (db != null) {
                db.updateLoan(this);
            }
            return true;
        }
        return false;
    }

    public void markAsReturned() {
        markAsReturned(Database.getInstance());
    }

    public void markAsReturned(Database db) {
        if ("ACTIVE".equals(this.status)) {
            this.returnDate = LocalDate.now();
            this.status = "RETURNED";
            if (this.item != null) {
                this.item.incrementLicense(db);
            }
            if (db != null) {
                db.updateLoan(this);
            }
        }
    }

    public boolean isOverdue() {
        return "ACTIVE".equals(this.status) && this.dueDate != null && LocalDate.now().isAfter(this.dueDate);
    }

    public Integer calculateDaysRemaining() {
        if (!"ACTIVE".equals(this.status) || this.dueDate == null) {
            return 0;
        }
        long days = ChronoUnit.DAYS.between(LocalDate.now(), this.dueDate);
        return (int) Math.max(0, days);
    }

    public void revokeAccess() {
        revokeAccess(Database.getInstance());
    }

    public void revokeAccess(Database db) {
        if ("ACTIVE".equals(this.status)) {
            this.status = "REVOKED";
            if (this.item != null) {
                this.item.incrementLicense(db);
            }
            if (this.checkout != null && this.checkout.getMemberID() != null) {
                this.checkout.getMemberID().getActiveLoans().remove(this);
            }
            if (db != null) {
                db.updateLoan(this);
            }
        }
    }

    public Integer getLoanID() {
        return loanID;
    }

    public void setLoanID(Integer loanID) {
        this.loanID = loanID;
    }

    public Checkout getCheckout() {
        return checkout;
    }

    public void setCheckout(Checkout checkout) {
        this.checkout = checkout;
    }

    public Item getItem() {
        return item;
    }

    public void setItem(Item item) {
        this.item = item;
    }

    public LocalDate getDueDate() {
        return dueDate;
    }

    public void setDueDate(LocalDate dueDate) {
        this.dueDate = dueDate;
    }

    public LocalDate getReturnDate() {
        return returnDate;
    }

    public void setReturnDate(LocalDate returnDate) {
        this.returnDate = returnDate;
    }

    public Integer getRenewCount() {
        return renewCount;
    }

    public void setRenewCount(Integer renewCount) {
        this.renewCount = renewCount;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
