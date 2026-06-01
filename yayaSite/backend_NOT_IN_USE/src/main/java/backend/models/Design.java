package backend.models;
 
import jakarta.persistence.*;
 
@Entity
@Table(name = "designs")
public class Design {
 
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
 
    @Column(nullable = false)
    private String title;
 
    @Column(nullable = false)
    private String category;
 
    @Column(nullable = false)
    private String imageUrl;
 
    @Column(columnDefinition = "TEXT")
    private String description;
 
    private int displayOrder = 0;
 
    // Getters
    public Long getId()             { return id; }
    public String getTitle()        { return title; }
    public String getCategory()     { return category; }
    public String getImageUrl()     { return imageUrl; }
    public String getDescription()  { return description; }
    public int getDisplayOrder()    { return displayOrder; }
 
    // Setters
    public void setId(Long id)                  { this.id = id; }
    public void setTitle(String title)          { this.title = title; }
    public void setCategory(String category)    { this.category = category; }
    public void setImageUrl(String imageUrl)    { this.imageUrl = imageUrl; }
    public void setDescription(String description) { this.description = description; }
    public void setDisplayOrder(int displayOrder)  { this.displayOrder = displayOrder; }
}