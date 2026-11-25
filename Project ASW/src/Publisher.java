import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class Publisher extends User {
    private String companyName;
    private List<Item> publishedItems;
    private String license;
    private Double royaltyRate;

    public Publisher(Integer userID, String name, String email, String password, String username, LocalDate birthDate, boolean isActive, String companyName, String license, Double royaltyRate) {
        super(userID, name, email, password, username, birthDate, isActive);
        this.companyName = companyName;
        this.publishedItems = new ArrayList<>();
        this.license = license;
        this.royaltyRate = royaltyRate;
    }

    public Publisher(Integer userID, String name, String email, String password, String username, LocalDate birthDate, boolean isActive, String companyName, Item[] publishedItems, String license, Double royaltyRate) {
        super(userID, name, email, password, username, birthDate, isActive);
        this.companyName = companyName;
        this.publishedItems = (publishedItems != null) ? new ArrayList<>(Arrays.asList(publishedItems)) : new ArrayList<>();
        this.license = license;
        this.royaltyRate = royaltyRate;
    }

    public boolean publishItem(Item item) {
        return publishItem(item, Database.getInstance());
    }

    public boolean publishItem(Item item, Database db) {
        if (!isActive() || item == null || publishedItems.contains(item)) {
            return false;
        }
        Database targetDb = (db != null) ? db : Database.getInstance();
        if (item.getItemID() != null && targetDb.findItemById(item.getItemID()) != null) {
            return false;
        }
        if (item.getPublisher() == null) {
            item.setPublisher(this);
        }
        if (targetDb.addItem(item)) {
            publishedItems.add(item);
            targetDb.updateUser(this);
            return true;
        }
        return false;
    }

    public void updateMetadata(Item updatedItem) {
        updateMetadata(updatedItem, Database.getInstance());
    }

    public void updateMetadata(Item updatedItem, Database db) {
        if (updatedItem == null || updatedItem.getItemID() == null) {
            return;
        }
        for (int i = 0; i < publishedItems.size(); i++) {
            if (Objects.equals(publishedItems.get(i).getItemID(), updatedItem.getItemID())) {
                publishedItems.set(i, updatedItem);
                if (db != null) {
                    db.updateItem(updatedItem);
                }
                return;
            }
        }
    }

    public boolean removeItem(Integer itemID) {
        return removeItem(itemID, Database.getInstance());
    }

    public boolean removeItem(Integer itemID, Database db) {
        if (itemID == null) {
            return false;
        }
        boolean removedLocal = publishedItems.removeIf(item -> Objects.equals(item.getItemID(), itemID));
        boolean removedDb = (db != null) && db.removeItem(itemID);
        if (removedLocal && db != null) {
            db.updateUser(this);
        }
        return removedLocal || removedDb;
    }

    public void updateLicenseCount(Integer itemID, Integer count) {
        updateLicenseCount(itemID, count, Database.getInstance());
    }

    public void updateLicenseCount(Integer itemID, Integer count, Database db) {
        if (itemID == null || count == null || count < 0) {
            return;
        }
        for (Item item : publishedItems) {
            if (Objects.equals(item.getItemID(), itemID)) {
                item.setAvailableLicenses(count);
                if (db != null) {
                    db.updateItem(item);
                }
                return;
            }
        }
    }

    public Double calculateRoyalties() {
        if (this.royaltyRate == null) {
            return 0.0;
        }
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
        this.publishedItems = (publishedItems != null) ? new ArrayList<>(publishedItems) : new ArrayList<>();
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
