package project.canteen.entity.auth;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import project.canteen.entity.canteen.cart;
import project.canteen.entity.canteen.order;
import project.canteen.model.auth.AccountDTO;

import java.time.Instant;
import java.util.Set;

@Entity
@Getter
@Setter
public class account {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String username;
    @Column(nullable = false)
    private String password;
    @Column
    private Long status;
    @Column
    private String tokenForgotPassword;
    @Column
    private Instant timeCreatioToken;
    @Column
    private String email;
    @Column
    private String code;
    @Column
    private String fullname;
    @Column
    private String phoneNumber;

    @JsonIgnore
    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.MERGE)
    @JoinTable(name = "account_role", joinColumns = {
            @JoinColumn(name = "account_id", referencedColumnName = "id", nullable = false) }, inverseJoinColumns = {
            @JoinColumn(name = "role_id", referencedColumnName = "id", nullable = false) })
    private Set<role> roles;

    @OneToMany(mappedBy = "account", cascade = CascadeType.ALL)
    private Set<order> orders;

    @OneToOne(mappedBy = "account")
    private cart cart;


    public AccountDTO toDTO() {
        AccountDTO account = new AccountDTO();
        account.setId(id);
        account.setUsername(username);
//		account.setPassword(password);
        account.setStatus(status);
        account.setTokenForgotPassword(tokenForgotPassword);
        account.setEmail(email);
        account.setCode(code);
        account.setFullname(fullname);
        account.setPhoneNumber(phoneNumber);
        return  account;
    }
}
