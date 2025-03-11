package project.canteen.entity.canteen;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Entity
@Getter
@Setter
public class foodItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column
    private Long price;
    @Column
    private String name;
    @Column
    private String description;
    @Column
    private String imageUrl;
    @Column
    private String status;

    @ManyToOne
    @JoinColumn(name = "category_id")
    @JsonIgnore
    private category category;

    @OneToMany(mappedBy = "foodItem", cascade = CascadeType.ALL)
    @JsonIgnore
    private Set<orderItem> orderItems;

    @OneToMany(mappedBy = "foodItem", cascade = CascadeType.ALL)
    @JsonIgnore
    private Set<cartItem> cartItems;

}
