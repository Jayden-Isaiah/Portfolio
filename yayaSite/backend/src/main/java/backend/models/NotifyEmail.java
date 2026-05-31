package backend.models;
 
import jakarta.persistence.*;
import java.time.LocalDateTime;
 
@Entity
@Table(name = "notify_emails")
public class NotifyEmail {
 
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
 
    @Column(nullable = false, unique = true)
    private String email;
 
    @Column(nullable = false)
    private LocalDateTime subscribedAt = LocalDateTime.now();
 
    // Getters
    public Long getId()                     { return id; }
    public String getEmail()                { return email; }
    public LocalDateTime getSubscribedAt()  { return subscribedAt; }
 
    // Setters
    public void setId(Long id)                          { this.id = id; }
    public void setEmail(String email)                  { this.email = email; }
    public void setSubscribedAt(LocalDateTime subscribedAt) { this.subscribedAt = subscribedAt; }
}