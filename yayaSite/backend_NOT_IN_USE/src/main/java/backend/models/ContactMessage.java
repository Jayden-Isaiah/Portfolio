package backend.models;
 
import jakarta.persistence.*;
import java.time.LocalDateTime;
 
@Entity
@Table(name = "contact_messages")
public class ContactMessage {
 
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
 
    @Column(nullable = false)
    private String name;
 
    @Column(nullable = false)
    private String email;
 
    @Column(nullable = false)
    private String subject;
 
    @Column(nullable = false, columnDefinition = "TEXT")
    private String message;
 
    @Column(nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now();
 
    private boolean read = false;
 
    // Getters
    public Long getId()             { return id; }
    public String getName()         { return name; }
    public String getEmail()        { return email; }
    public String getSubject()      { return subject; }
    public String getMessage()      { return message; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public boolean isRead()         { return read; }
 
    // Setters
    public void setId(Long id)          { this.id = id; }
    public void setName(String name)    { this.name = name; }
    public void setEmail(String email)  { this.email = email; }
    public void setSubject(String subject) { this.subject = subject; }
    public void setMessage(String message) { this.message = message; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public void setRead(boolean read)   { this.read = read; }
}