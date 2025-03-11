package project.canteen.repository.canteen;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import project.canteen.entity.canteen.action;
import project.canteen.entity.canteen.cart;
import project.canteen.entity.canteen.cartItem;

@Repository
public interface actionRepository extends JpaRepository<action, Long> {
    @Query(value = "SELECT * from `action` where `name` = :name", nativeQuery = true)
    action findActionByName(@Param("name") String name);
}
