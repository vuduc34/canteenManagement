package project.canteen.repository.canteen;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import project.canteen.entity.canteen.category;
import project.canteen.entity.canteen.foodItem;

import java.util.List;

@Repository
public interface foodRepository extends JpaRepository<foodItem, Long> {
    @Query( value = "SELECT  * FROM food_item where category_id = :category_id  and status =:status ", nativeQuery = true)
    List<foodItem> findByCategory(@Param("category_id") Long category_id,@Param("status") String status);

    @Query(value = "SELECT * from food_item where id = :id", nativeQuery = true)
    foodItem findFoodById(@Param("id") Long id);

    @Query(value = "SELECT * from food_item where status = 'available'", nativeQuery = true)
    Page<foodItem> findAll(Pageable pageable);

    @Query(value = "SELECT * from food_item where status = 'available'", nativeQuery = true)
    List<foodItem> findAllAvailable();

    @Query(value = "SELECT * from food_item where status = 'available' and `name` like %:text% order by price ", nativeQuery = true)
    List<foodItem> findFilter(@Param("text") String text);
    @Query(value = "SELECT * from food_item where status != 'deleted'", nativeQuery = true)
    List<foodItem> findAllFood();
}
