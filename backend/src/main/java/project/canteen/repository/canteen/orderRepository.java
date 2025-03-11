package project.canteen.repository.canteen;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Component;
import project.canteen.entity.canteen.foodItem;
import project.canteen.entity.canteen.order;

import java.util.List;

@Component
public interface orderRepository extends JpaRepository<order, Long> {
    @Query(value = "SELECT * from `order` where id = :id", nativeQuery = true)
    order findOrderById(@Param("id") Long id);
    @Query(value = "SELECT * from `order` where code = :code", nativeQuery = true)
    order findOrderByCode(@Param("code") String code);

    @Query(value = "SELECT * from `order` where account_id = :account_id order by id desc", nativeQuery = true)
    List<order> findOrderByAccountId(@Param("account_id") Long account_id);

    @Query(value = "SELECT * from `order` where status = 'unconfirmed' or status = 'preparing' ORDER BY `order_time` ASC", nativeQuery = true)
    List<order> findOrderPreparingOrUnconfirmed();

    @Query(value = "SELECT DATE(o.order_time) AS orderDate, SUM(o.total_price) AS totalRevenue " +
            "FROM `order` o " +
            "WHERE o.order_time >= CURRENT_DATE - INTERVAL :days DAY  and o.status = 'done' " +
            "GROUP BY DATE(o.order_time) " +
            "ORDER BY orderDate ASC",nativeQuery = true)
    List<Object[]> getTotalRevenueLastDays(@Param("days") int days);


    @Query(value = "SELECT DATE_FORMAT(o.order_time, '%Y-%m') AS orderMonth, SUM(o.total_price) AS totalRevenue " +
            "FROM `order` o " +
            "WHERE o.order_time >= DATE_SUB(CURDATE(), INTERVAL :months MONTH) and o.status = 'done' " +
            "GROUP BY DATE_FORMAT(o.order_time, '%Y-%m') " +
            "ORDER BY orderMonth ASC", nativeQuery = true)
    List<Object[]> getTotalRevenueLastMonths(@Param("months") int months);


    @Query(value = "SELECT SUM(o.total_price) FROM `order` o " +
            "WHERE DATE(o.order_time) = DATE(CONVERT_TZ(NOW(), 'UTC', 'Asia/Ho_Chi_Minh')) and o.status = 'done'", nativeQuery = true)
    Long getTotalRevenueToday();

    @Query(value = "SELECT COUNT(*) FROM `order` o " +
            "WHERE DATE(o.order_time) = DATE(CONVERT_TZ(NOW(), 'UTC', 'Asia/Ho_Chi_Minh'))" +
            " AND o.status = :status", nativeQuery = true)
    int getTotalOrdersTodayByStatus(@Param("status") String status);



}
