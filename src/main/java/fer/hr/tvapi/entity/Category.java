package fer.hr.tvapi.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "category")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="name", unique = true, nullable = false)
    private String name;

    @Column(name="description")
    private String description;

    @OneToMany(mappedBy = "channel")
    @JsonIgnore
    @LazyCollection(LazyCollectionOption.FALSE)
    @ToString.Exclude
    private List<Content> contentList;

}
