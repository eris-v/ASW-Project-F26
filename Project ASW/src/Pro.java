import java.time.LocalDate;

public class Pro extends Membership {
    private String billingCycle;
    private LocalDate renewalDate;
    private String paymentMethod;
    boolean accessToAcademicContent = true;
    final Integer maxDevices = 2;

    public Pro(String billingCycle, LocalDate renewalDate, String paymentMethod) {
        super("Pro", 9.99, 10, 14, 2, true, false);
        this.billingCycle = billingCycle;
        this.renewalDate = renewalDate;
        this.paymentMethod = paymentMethod;
    }

    public boolean canAddDevice(Integer currentDevices) {
        return currentDevices != null && currentDevices < this.maxDevices;
    }

    public boolean canAccessAcademicPaper() {
        return this.accessToAcademicContent;
    }

    public boolean processSubscriptionRenewal() {
        if (this.paymentMethod == null || this.paymentMethod.isBlank()) {
            return false;
        }
        if ("ANNUAL".equalsIgnoreCase(this.billingCycle)) {
            this.renewalDate = this.renewalDate.plusYears(1);
        } else {
            this.renewalDate = this.renewalDate.plusMonths(1);
        }
        return true;
    }

    public String getBillingCycle() {
        return billingCycle;
    }

    public void setBillingCycle(String billingCycle) {
        this.billingCycle = billingCycle;
    }

    public LocalDate getRenewalDate() {
        return renewalDate;
    }

    public void setRenewalDate(LocalDate renewalDate) {
        this.renewalDate = renewalDate;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public boolean isAccessToAcademicContent() {
        return accessToAcademicContent;
    }

    public void setAccessToAcademicContent(boolean accessToAcademicContent) {
        this.accessToAcademicContent = accessToAcademicContent;
    }

    public Integer getMaxDevices() {
        return maxDevices;
    }
}
