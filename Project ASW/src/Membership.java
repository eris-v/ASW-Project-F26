import java.time.LocalDate;

public abstract class Membership {
    private String tierName;
    private Double monthlyPrice;
    private Integer maxActiveLoans;
    private Integer loanDurationDays;
    private Integer maxRenewals;
    private boolean allowOfflineDownloads;
    private boolean hasAds;

    public Membership(String tierName, Double monthlyPrice, Integer maxActiveLoans, Integer loanDurationDays, Integer maxRenewals, boolean allowOfflineDownloads, boolean hasAds) {
        this.tierName = tierName;
        this.monthlyPrice = monthlyPrice;
        this.maxActiveLoans = maxActiveLoans;
        this.loanDurationDays = loanDurationDays;
        this.maxRenewals = maxRenewals;
        this.allowOfflineDownloads = allowOfflineDownloads;
        this.hasAds = hasAds;
    }

    public boolean canBorrowMore(Integer currentLoans) {
        return currentLoans != null && currentLoans < maxActiveLoans;
    }

    public boolean canRenew(Integer currentRenewals) {
        return currentRenewals != null && currentRenewals < maxRenewals;
    }
    public  LocalDate calculateDueDate(LocalDate today){
        return today.plusDays(this.loanDurationDays);
    }
    public String getTierBenefits() {
        return String.format("%s Tier: $%.2f/mo | Max Loans: %d | Duration: %d days | Renewals: %d | Offline: %b | Ads: %b",
                tierName, monthlyPrice, maxActiveLoans, loanDurationDays, maxRenewals, allowOfflineDownloads, hasAds);
    }

    public String getTierName() {
        return tierName;
    }

    public void setTierName(String tierName) {
        this.tierName = tierName;
    }

    public Double getMonthlyPrice() {
        return monthlyPrice;
    }

    public void setMonthlyPrice(Double monthlyPrice) {
        this.monthlyPrice = monthlyPrice;
    }

    public Integer getMaxActiveLoans() {
        return maxActiveLoans;
    }

    public void setMaxActiveLoans(Integer maxActiveLoans) {
        this.maxActiveLoans = maxActiveLoans;
    }

    public Integer getLoanDurationDays() {
        return loanDurationDays;
    }

    public void setLoanDurationDays(Integer loanDurationDays) {
        this.loanDurationDays = loanDurationDays;
    }

    public Integer getMaxRenewals() {
        return maxRenewals;
    }

    public void setMaxRenewals(Integer maxRenewals) {
        this.maxRenewals = maxRenewals;
    }

    public boolean isAllowOfflineDownloads() {
        return allowOfflineDownloads;
    }

    public void setAllowOfflineDownloads(boolean allowOfflineDownloads) {
        this.allowOfflineDownloads = allowOfflineDownloads;
    }

    public boolean isHasAds() {
        return hasAds;
    }

    public void setHasAds(boolean hasAds) {
        this.hasAds = hasAds;
    }
}
