import java.time.LocalDate;

public class Loan {
    private Integer loanID;
    private Checkout checkoutID;
    private Item item;
    private LocalDate dueDate;
    private LocalDate returnDate;
    private Integer renewalCount;
    private String status;
}
