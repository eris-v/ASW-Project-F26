import java.time.LocalDate;

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
}
