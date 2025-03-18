package project.canteen.entity.canteen;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
public class canteenInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column
    private String name;
    @Column
    private String adress;
    @Column
    private String email;
    @Column
    private  String phone;
    @Column
    private  String description;
    @Column
    private String openTime;
    @Column
    private String imageUrl;


}
