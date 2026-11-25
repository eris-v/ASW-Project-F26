import java.time.LocalDate;
import java.util.Arrays;

public abstract class Item {
    private Integer itemID;
    private String title;
    private String[] authors;
    private Publisher publisher;
    private LocalDate publicationDate;
    private Genre genre;
    private String language;
    private String fileURL;
    private FileFormat fileFormat;
    private Double fileSizeMB;
    private Integer availableLicenses;

    public Item(Integer itemID, String title, String[] authors, Publisher publisher, LocalDate publicationDate, Genre genre, String language,
                String fileURL, FileFormat fileFormat, Double fileSizeMB, Integer availableLicenses) {
        this.itemID = itemID;
        this.title = title;
        this.authors = authors;
        this.publisher = publisher;
        this.publicationDate = publicationDate;
        this.genre = genre;
        this.language = language;
        this.fileURL = fileURL;
        this.fileFormat = fileFormat;
        this.fileSizeMB = fileSizeMB;
        this.availableLicenses = availableLicenses;
    }

    public boolean isAvailable() {
        return this.availableLicenses != null && this.availableLicenses > 0;
    }

    public boolean decrementLicenses() {
        return decrementLicenses(Database.getInstance());
    }

    public boolean decrementLicenses(Database db) {
        if (isAvailable()) {
            this.availableLicenses--;
            if (db != null) {
                db.updateItem(this);
            }
            return true;
        }
        return false;
    }

    public void incrementLicense() {
        incrementLicense(Database.getInstance());
    }

    public void incrementLicense(Database db) {
        if (this.availableLicenses == null) {
            this.availableLicenses = 1;
        } else {
            this.availableLicenses++;
        }
        if (db != null) {
            db.updateItem(this);
        }
    }

    public void updateDetails(String title, String language, String fileURL) {
        updateDetails(title, language, fileURL, Database.getInstance());
    }

    public void updateDetails(String title, String language, String fileURL, Database db) {
        if (title != null && !title.isBlank()) this.title = title;
        if (language != null && !language.isBlank()) this.language = language;
        if (fileURL != null && !fileURL.isBlank()) this.fileURL = fileURL;
        if (db != null) {
            db.updateItem(this);
        }
    }

    public String getMetadata() {
        return String.format("[%d] %s by %s (%s) | Genre: %s | Format: %s | Available: %d",
                itemID, title, Arrays.toString(authors), publicationDate, genre, fileFormat, availableLicenses);
    }

    public Integer getItemID() {
        return itemID;
    }

    public void setItemID(Integer itemID) {
        this.itemID = itemID;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String[] getAuthors() {
        return authors;
    }

    public void setAuthors(String[] authors) {
        this.authors = authors;
    }

    public Publisher getPublisher() {
        return publisher;
    }

    public void setPublisher(Publisher publisher) {
        this.publisher = publisher;
    }

    public LocalDate getPublicationDate() {
        return publicationDate;
    }

    public void setPublicationDate(LocalDate publicationDate) {
        this.publicationDate = publicationDate;
    }

    public Genre getGenre() {
        return genre;
    }

    public void setGenre(Genre genre) {
        this.genre = genre;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getFileURL() {
        return fileURL;
    }

    public void setFileURL(String fileURL) {
        this.fileURL = fileURL;
    }

    public FileFormat getFileFormat() {
        return fileFormat;
    }

    public void setFileFormat(FileFormat fileFormat) {
        this.fileFormat = fileFormat;
    }

    public Double getFileSizeMB() {
        return fileSizeMB;
    }

    public void setFileSizeMB(Double fileSizeMB) {
        this.fileSizeMB = fileSizeMB;
    }

    public Integer getAvailableLicenses() {
        return availableLicenses;
    }

    public void setAvailableLicenses(Integer availableLicenses) {
        this.availableLicenses = availableLicenses;
    }
}
