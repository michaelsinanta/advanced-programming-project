package id.ac.ui.cs.advprog.bayarservice.repository;

import id.ac.ui.cs.advprog.bayarservice.model.coupon.Coupon;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class CouponRepositoryTest {
    @Autowired
    private CouponRepository couponRepository;

    Coupon coupon;

    @BeforeEach
    void setUp() {
        coupon = Coupon.builder()
                .name("SEPTEMBERCERIA")
                .discount(50000L)
                .build();
        couponRepository.save(coupon);
    }

    @AfterEach
    void tearDown() {
        couponRepository.deleteAll();
    }

    @Test
    void testFindById() {
        Optional<Coupon> optionalCoupon = couponRepository.findById(coupon.getId());

        Assertions.assertTrue(optionalCoupon.isPresent());
    }

    @Test
    void testFindByIdNotFound() {
        Optional<Coupon> optionalCoupon = couponRepository.findById(100);

        Assertions.assertFalse(optionalCoupon.isPresent());
    }
}
