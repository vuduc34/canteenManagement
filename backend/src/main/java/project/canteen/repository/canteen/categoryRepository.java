package project.canteen.repository.canteen;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import project.canteen.entity.auth.account;
import project.canteen.entity.canteen.category;

@Repository
public interface categoryRepository extends JpaRepository<category, Long> {
    @Query(value = "SELECT * from category where id = :id", nativeQuery = true)
    category findCategoryById(@Param("id") Long id);
}
