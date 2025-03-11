package project.canteen.repository.canteen;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import project.canteen.entity.canteen.canteenInfo;
import project.canteen.entity.canteen.cartItem;

@Repository
public interface canteenInfoRepository extends JpaRepository<canteenInfo, Long> {

}
