import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Ultimate extends Membership {
    private String billingCycle;
    private LocalDate renewalDate;
    private String paymentMethod;
    final Integer maxDevices = 5;
    private Integer familySharingSlots = 5;
    private boolean priorityWaitList;
    private List<Member> familyMembers;

    public Ultimate(String billingCycle, LocalDate renewalDate, String paymentMethod, boolean priorityWaitList) {
        super("Ultimate", 19.99, 25, 30, 5, true, false);
        this.billingCycle = billingCycle;
        this.renewalDate = renewalDate;
        this.paymentMethod = paymentMethod;
        this.priorityWaitList = priorityWaitList;
        this.familyMembers = new ArrayList<>();
    }

    public boolean canAddDevice(Integer currentDevices) {
        return currentDevices != null && currentDevices < this.maxDevices;
    }

    public boolean addFamilyMember(Member member) {
        if (member == null || familyMembers.size() >= familySharingSlots || familyMembers.contains(member)) {
            return false;
        }
        return familyMembers.add(member);
    }

    public boolean removeFamilyMember(Member member) {
        return familyMembers.remove(member);
    }

    public boolean joinPriorityWaitList(Item item) {
        return this.priorityWaitList && item != null;
    }

    public boolean joinPriorityWaitList(Item item, Member member) {
        return joinPriorityWaitList(item, member, Database.getInstance());
    }

    public boolean joinPriorityWaitList(Item item, Member member, Database db) {
        if (!this.priorityWaitList || item == null || member == null) {
            return false;
        }
        Database targetDb = (db != null) ? db : Database.getInstance();
        return targetDb.addToWaitList(item, member);
    }

    public boolean processSubscriptionRenewal() {
        if (this.paymentMethod == null || this.paymentMethod.isBlank()) {
            return false;
        }
        LocalDate baseDate = (this.renewalDate != null) ? this.renewalDate : LocalDate.now();
        if ("ANNUAL".equalsIgnoreCase(this.billingCycle)) {
            this.renewalDate = baseDate.plusYears(1);
        } else {
            this.renewalDate = baseDate.plusMonths(1);
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

    public Integer getMaxDevices() {
        return maxDevices;
    }

    public Integer getFamilySharingSlots() {
        return familySharingSlots;
    }

    public void setFamilySharingSlots(Integer familySharingSlots) {
        this.familySharingSlots = familySharingSlots;
    }

    public boolean isPriorityWaitList() {
        return priorityWaitList;
    }

    public void setPriorityWaitList(boolean priorityWaitList) {
        this.priorityWaitList = priorityWaitList;
    }

    public List<Member> getFamilyMembers() {
        return familyMembers;
    }

    public void setFamilyMembers(List<Member> familyMembers) {
        this.familyMembers = familyMembers;
    }
}
