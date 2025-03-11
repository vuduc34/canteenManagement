package project.canteen.repository.canteen;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import project.canteen.entity.canteen.cart;
import project.canteen.entity.canteen.category;

@Repository
public interface cartRepository extends JpaRepository<cart, Long> {
    @Query(value = "SELECT * from cart where account_id = :account_id", nativeQuery = true)
    cart findCartByAccountId(@Param("account_id") Long account_id);
    @Query(value = "SELECT * from cart where id = :id", nativeQuery = true)
    cart findCartById(@Param("id") Long id);
}
