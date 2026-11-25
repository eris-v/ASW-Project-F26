import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Publisher extends User {
    private String companyName;
    private List<Item> publishedItems;
    private String license;
    private Double royaltyRate;

    public Publisher(Integer userID, String name, String email, String password, String username, LocalDate birthDate, boolean isActive, String companyName, Item[] publishedItems, String license, Double royaltyRate) {
        super(userID, name, email, password, username, birthDate, isActive);
        this.companyName = companyName;
        this.publishedItems = List.of(publishedItems);
        this.license = license;
        this.royaltyRate = royaltyRate;
    }

    public boolean publishItem(Item item) {
            if (item == null || publishedItems.contains(item)) {
                return false;
            }
            return publishedItems.add(item);
        }

    public void updateMetadata(Item updatedItem) {
        if (updatedItem == null) return;
        for (int i = 0; i < publishedItems.size(); i++) {
            if (publishedItems.get(i).getItemID().equals(updatedItem.getItemID())) {
                publishedItems.set(i, updatedItem);
                return;
            }
        }
    }
    public boolean removeItem(Integer itemID) {
        if (itemID == null) return false;
        return publishedItems.removeIf(item -> item.getItemID().equals(itemID));
    }

    public void updateLicenseCount(Integer itemID, Integer count) {
        if (itemID == null || count == null || count < 0) return;
        for (Item item : publishedItems) {
            if (item.getItemID().equals(itemID)) {
                item.setAvailableLicenses(count);
                return;
            }
        }
    }

    public Double calculateRoyalties() {
        return publishedItems.size() * this.royaltyRate;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public List<Item> getPublishedItems() {
        return publishedItems;
    }

    public void setPublishedItems(List<Item> publishedItems) {
        this.publishedItems = publishedItems;
    }

    public String getLicense() {
        return license;
    }

    public void setLicense(String license) {
        this.license = license;
    }

    public Double getRoyaltyRate() {
        return royaltyRate;
    }

    public void setRoyaltyRate(Double royaltyRate) {
        this.royaltyRate = royaltyRate;
    }
}
