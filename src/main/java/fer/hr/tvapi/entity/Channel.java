package fer.hr.tvapi.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "channel")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Channel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="name", unique = true, nullable = false)
    private String name;

    @Column(name = "description", nullable = false)
    private String description;

    @Column(name = "logo")
    private String logoBase64;

    @ManyToOne
    @JsonIgnore
    @ToString.Exclude
    @JoinColumn(name = "id_user")
    private Users user;

    @OnDelete(action = OnDeleteAction.CASCADE)
    @OneToMany(mappedBy = "channel")
    @LazyCollection(LazyCollectionOption.FALSE)
    private List<Content> contentList;

}
