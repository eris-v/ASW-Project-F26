import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Checkout {
    private Integer checkoutID;
    private Member memberID;
    private LocalDate checkoutDate;
    private List<Loan> loans;

    public Checkout(Integer checkoutID, Member memberID, LocalDate checkoutDate) {
        this.checkoutID = checkoutID;
        this.memberID = memberID;
        this.checkoutDate = checkoutDate;
        this.loans = new ArrayList<>();
    }

    public void addLoan(Loan loan) {
        addLoan(loan, Database.getInstance());
    }

    public void addLoan(Loan loan, Database db) {
        if (loan != null && !loans.contains(loan)) {
            loans.add(loan);
            if (db != null) {
                db.updateCheckout(this);
            }
        }
    }

    public void removeLoan(Integer loanID) {
        removeLoan(loanID, Database.getInstance());
    }

    public void removeLoan(Integer loanID, Database db) {
        if (loanID != null) {
            boolean removed = loans.removeIf(loan -> Objects.equals(loan.getLoanID(), loanID));
            if (removed && db != null) {
                db.removeLoan(loanID);
                db.updateCheckout(this);
            }
        }
    }

    public Integer getTotalItems() {
        return loans.size();
    }

    public String generateReceipt() {
        String memberName = (memberID != null) ? memberID.getName() : "Unknown Member";
        StringBuilder receipt = new StringBuilder();
        receipt.append(String.format("Checkout #%d | Member: %s | Date: %s%n",
                checkoutID, memberName, checkoutDate));
        for (Loan loan : loans) {
            String title = (loan.getItem() != null) ? loan.getItem().getTitle() : "Unknown Item";
            receipt.append(String.format(" - %s (Due: %s)%n", title, loan.getDueDate()));
        }
        receipt.append("Total Items: ").append(getTotalItems());
        return receipt.toString();
    }

    public Integer getCheckoutID() {
        return checkoutID;
    }

    public void setCheckoutID(Integer checkoutID) {
        this.checkoutID = checkoutID;
    }

    public Member getMemberID() {
        return memberID;
    }

    public void setMemberID(Member memberID) {
        this.memberID = memberID;
    }

    public LocalDate getCheckoutDate() {
        return checkoutDate;
    }

    public void setCheckoutDate(LocalDate checkoutDate) {
        this.checkoutDate = checkoutDate;
    }

    public List<Loan> getLoans() {
        return loans;
    }

    public void setLoans(List<Loan> loans) {
        this.loans = (loans != null) ? new ArrayList<>(loans) : new ArrayList<>();
    }
}
