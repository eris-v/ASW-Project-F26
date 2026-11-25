import java.time.LocalDate;
import java.util.Arrays;

public class Book extends Item {
    private Integer isbn;
    private Integer pageCount;
    private String edition;
    private String[] chapters;

    public Book(Integer itemID, String title, String[] authors, Publisher publisher, LocalDate publicationDate, Genre genre, String language, String fileURL, FileFormat fileFormat, Double fileSizeMB, Integer availableLicenses, Integer isbn, Integer pageCount, String edition, String[] chapters) {
        super(itemID, title, authors, publisher, publicationDate, genre, language, fileURL, fileFormat, fileSizeMB, availableLicenses);
        this.isbn = isbn;
        this.pageCount = pageCount;
        this.edition = edition;
        this.chapters = chapters;
    }

    public String getCitation() {
        String pubName = (getPublisher() != null) ? getPublisher().getCompanyName() : "Unknown Publisher";
        int year = (getPublicationDate() != null) ? getPublicationDate().getYear() : 0;
        return String.format("%s (%d). %s (%s ed.). %s. ISBN: %d.",
                Arrays.toString(getAuthors()), year, getTitle(), edition, pubName, isbn);
    }

    public Integer getIsbn() {
        return isbn;
    }

    public void setIsbn(Integer isbn) {
        this.isbn = isbn;
    }

    public Integer getPageCount() {
        return pageCount;
    }

    public void setPageCount(Integer pageCount) {
        this.pageCount = pageCount;
    }

    public String getEdition() {
        return edition;
    }

    public void setEdition(String edition) {
        this.edition = edition;
    }

    public String[] getChapters() {
        return chapters;
    }

    public void setChapters(String[] chapters) {
        this.chapters = chapters;
    }
}