package tr.softtech.patika.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@Entity
@Table(name = "USER")
@Getter
@Setter
@NoArgsConstructor
@SuperBuilder
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class User extends BaseEntity {

    @Id
    @Column(name = "user_id")
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    private String userId;

    @Column(name = "username",unique = true,nullable = false)
    private String username;

    @Column(name = "password",unique = true,nullable = false)
    private String password;

    @Column(name = "name",length = 50)
    private String name;

    @Column(name = "surname",length = 50)
    private String surname;

//    @OneToMany(mappedBy = "ownerUser",fetch = FetchType.LAZY, cascade = CascadeType.ALL)
//    private Set<Product> productSet = new HashSet<>();

}
