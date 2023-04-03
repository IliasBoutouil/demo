package me.ilias.domains;

import lombok.*;
import me.ilias.validation.SafeContent;
import net.minidev.json.annotate.JsonIgnore;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Past;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
@Table(uniqueConstraints = @UniqueConstraint(name = "author_book",columnNames = {"title","author_id"}))
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotBlank
    private String title;
    @Lob
    @NotBlank
    @SafeContent
    private String summary;
    @Min(10)
    private int pages;
    @Past
    private LocalDate publishDate;
    @CreationTimestamp
    private LocalDateTime createdAt;
    @ManyToOne(optional = false)
    @JoinColumn(name = "author_id")
    private Author author;
    @Lob
    @JsonIgnore
    private byte[] cover;
    @Override
    public String toString() {
        return "Book " + this.title;
    }
}
