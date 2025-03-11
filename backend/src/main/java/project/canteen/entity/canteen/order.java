package project.canteen.entity.canteen;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import project.canteen.entity.auth.account;
import project.canteen.model.canteen.orderResponse;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Set;

@Entity
@Getter
@Setter
public class order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column
    private Long totalPrice;
    @Column
    private String status;
    @Column
    private String code;
    @Column( name = "orderTime")
    private LocalDateTime orderTime;
    @ManyToOne
    @JoinColumn(name = "account_id")
    @JsonIgnore
    private account account;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    @JsonIgnore
    private Set<orderItem> orderItems;


    @PrePersist
    protected void onCreate() {
        orderTime = LocalDateTime.now();
    }

    public orderResponse toResponse() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
//        ZonedDateTime vietnamTime = this.orderTime.atZone(ZoneId.of("Asia/Ho_Chi_Minh"));
        orderResponse orderResponse = new orderResponse();
        orderResponse.setId(this.id);
        orderResponse.setCode(this.code);
        orderResponse.setStatus(this.status);
        orderResponse.setTotalPrice(this.totalPrice);
        orderResponse.setOrderTime(this.orderTime.plusHours(7).format(formatter));
        return  orderResponse;
    }
}
