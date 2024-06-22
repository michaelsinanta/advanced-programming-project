package id.ac.ui.cs.advprog.bayarservice.repository;

import id.ac.ui.cs.advprog.bayarservice.model.coupon.Coupon;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

@Repository
public interface CouponRepository extends JpaRepository<Coupon, Integer> {
    @NonNull
    Optional<Coupon> findById(@NonNull Integer id);

    @NonNull
    Optional<Coupon> findByName(@NonNull String name);
}
