package project.canteen.entity.canteen;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import project.canteen.entity.auth.account;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
public class orderItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column
    private Long price;
    @Column
    private int quantity;

    @ManyToOne
    @JoinColumn(name = "order_id")
    @JsonIgnore
    private order order;

    @ManyToOne
    @JoinColumn(name = "item_id")
    @JsonIgnore
    private foodItem foodItem;

}
