import java.time.LocalDate;
import java.util.Arrays;

public class Paper extends Item {
    private Integer doi;
    private String paperabstract;
    private String[] keywords;
    private String institution;

    public Paper(Integer itemID, String title, String[] authors, Publisher publisher, LocalDate publicationDate, Genre genre, String language, String fileURL, FileFormat fileFormat, Double fileSizeMB, Integer availableLicenses, Integer doi, String paperabstract, String[] keywords, String institution) {
        super(itemID, title, authors, publisher, publicationDate, genre, language, fileURL, fileFormat, fileSizeMB, availableLicenses);
        this.doi = doi;
        this.paperabstract = paperabstract;
        this.keywords = keywords;
        this.institution = institution;
    }
    public String getAbstract() {
        return paperabstract;
    }

    public boolean hasKeyword(String keyword) {
        if (keyword == null || keywords == null) return false;
        for (String kw : keywords) {
            if (kw != null && kw.equalsIgnoreCase(keyword)) {
                return true;
            }
        }
        return false;
    }

    public String getAcademicCitation() {
        int year = (getPublicationDate() != null) ? getPublicationDate().getYear() : 0;
        return String.format("%s (%d). \"%s.\" %s. DOI: %d.",
                Arrays.toString(getAuthors()), year, getTitle(), institution, doi);
    }

    public Integer getDoi() {
        return doi;
    }

    public void setDoi(Integer doi) {
        this.doi = doi;
    }

    public String getPaperabstract() {
        return paperabstract;
    }

    public void setPaperabstract(String paperabstract) {
        this.paperabstract = paperabstract;
    }

    public String[] getKeywords() {
        return keywords;
    }

    public void setKeywords(String[] keywords) {
        this.keywords = keywords;
    }

    public String getInstitution() {
        return institution;
    }

    public void setInstitution(String institution) {
        this.institution = institution;
    }
}
