package project.canteen.repository.canteen;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import project.canteen.entity.canteen.cartItem;

import java.util.List;

@Repository
public interface cartItemRepository extends JpaRepository<cartItem, Long> {
    @Query(value = "SELECT * from cart_item where id = :id", nativeQuery = true)
    cartItem findCartItemById(@Param("id") Long id);

    @Modifying
    @Transactional
    @Query(value = "DELETE from cart_item where item_id = :item_id", nativeQuery = true)
    void deleteCartItemByItemId(@Param("item_id") Long item_id);

    @Modifying
    @Transactional
    @Query(value = "DELETE from cart_item where id = :id", nativeQuery = true)
    void deleteCartItemById(@Param("id") Long id);
}
