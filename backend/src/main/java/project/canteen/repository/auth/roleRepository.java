package project.canteen.repository.auth;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import project.canteen.entity.auth.role;

@Repository
public interface roleRepository extends JpaRepository<role, Long> {
    role findByName(String name);
}
