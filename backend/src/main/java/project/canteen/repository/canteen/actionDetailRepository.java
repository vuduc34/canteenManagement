package project.canteen.repository.canteen;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import project.canteen.entity.canteen.action;
import project.canteen.entity.canteen.actionDetail;
import project.canteen.entity.canteen.cartItem;

import java.util.List;

@Repository
public interface actionDetailRepository extends JpaRepository<actionDetail, Long> {
    @Query(value = "SELECT * from `action_detail` where `account_id` = :account_id", nativeQuery = true)
    List<actionDetail> findActionDetailByAccountId(@Param("account_id") Long account_id);
}
