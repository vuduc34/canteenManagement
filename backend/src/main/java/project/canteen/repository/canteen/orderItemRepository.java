package project.canteen.repository.canteen;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import project.canteen.entity.canteen.orderItem;

@Repository
public interface orderItemRepository extends JpaRepository<orderItem, Long> {
}
