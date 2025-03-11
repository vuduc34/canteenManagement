package project.canteen.entity.canteen;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
public class actionDetail {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column
    private Long account_id;
    @Column
    private Long order_id;
    @Column( name = "timeCreate")
    private LocalDateTime timeCreate;
    @ManyToOne
    @JoinColumn(name = "action_id")
    @JsonIgnore
    private action action;

    @PrePersist
    protected void onCreate() {
        timeCreate = LocalDateTime.now();
    }
}
