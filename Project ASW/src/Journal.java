import java.time.LocalDate;

public class Journal extends Item {
    private Integer issn;
    private Integer volume;
    private Integer issueNumber;
    private String editor;
    private String frequency;

    public Journal(Integer itemID, String title, String[] authors, Publisher publisher, LocalDate publicationDate, Genre genre, String language, String fileURL, FileFormat fileFormat, Double fileSizeMB, Integer availableLicenses, Integer issn, Integer volume, Integer issueNumber, String editor, String frequency) {
        super(itemID, title, authors, publisher, publicationDate, genre, language, fileURL, fileFormat, fileSizeMB, availableLicenses);
        this.issn = issn;
        this.volume = volume;
        this.issueNumber = issueNumber;
        this.editor = editor;
        this.frequency = frequency;
    }

    public String getTableOfContents() {
        return String.format("Table of Contents - %s (Vol. %d, Issue %d) | Editor: %s",
                getTitle(), volume, issueNumber, editor);
    }

    public String getIssueCitation() {
        int year = (getPublicationDate() != null) ? getPublicationDate().getYear() : 0;
        return String.format("%s, Vol. %d, No. %d (%d). Ed. %s. ISSN: %d.",
                getTitle(), volume, issueNumber, year, editor, issn);
    }

    public Integer getIssn() {
        return issn;
    }

    public void setIssn(Integer issn) {
        this.issn = issn;
    }

    public Integer getVolume() {
        return volume;
    }

    public void setVolume(Integer volume) {
        this.volume = volume;
    }

    public Integer getIssueNumber() {
        return issueNumber;
    }

    public void setIssueNumber(Integer issueNumber) {
        this.issueNumber = issueNumber;
    }

    public String getEditor() {
        return editor;
    }

    public void setEditor(String editor) {
        this.editor = editor;
    }

    public String getFrequency() {
        return frequency;
    }

    public void setFrequency(String frequency) {
        this.frequency = frequency;
    }
}
