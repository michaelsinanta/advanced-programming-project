package id.ac.ui.cs.advprog.bayarservice.service.coupon;

import id.ac.ui.cs.advprog.bayarservice.dto.coupon.CouponRequest;
import id.ac.ui.cs.advprog.bayarservice.dto.coupon.UseCouponRequest;
import id.ac.ui.cs.advprog.bayarservice.exception.coupon.CouponAlreadyExistException;
import id.ac.ui.cs.advprog.bayarservice.exception.coupon.CouponAlreadyUsedException;
import id.ac.ui.cs.advprog.bayarservice.exception.coupon.CouponDoesNotExistException;
import id.ac.ui.cs.advprog.bayarservice.exception.invoice.InvoiceDoesNotExistException;
import id.ac.ui.cs.advprog.bayarservice.model.coupon.Coupon;
import id.ac.ui.cs.advprog.bayarservice.model.invoice.Invoice;
import id.ac.ui.cs.advprog.bayarservice.repository.CouponRepository;
import id.ac.ui.cs.advprog.bayarservice.repository.InvoiceRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

@Service
public class CouponServiceImpl implements  CouponService {

    private final CouponRepository couponRepository;

    private final InvoiceRepository invoiceRepository;

    public CouponServiceImpl(CouponRepository couponRepository, InvoiceRepository invoiceRepository) {
        this.couponRepository = couponRepository;
        this.invoiceRepository = invoiceRepository;
    }

    @Override
    public Coupon findById(Integer id) {
        return this.couponRepository.findById(id)
                .orElseThrow(() -> new CouponDoesNotExistException(id));
    }

    @Override
    public Coupon update(Integer id, CouponRequest request) {
        Optional<Coupon> coupon = this.couponRepository.findByName(request.getName());
        if (coupon.isPresent() && !Objects.equals(coupon.get().getId(), id)) {
            throw new CouponAlreadyExistException(request.getName());
        }

        Coupon newCoupon = this.findById(id);
        newCoupon.setName(request.getName());
        newCoupon.setDiscount(request.getDiscount());

        return this.couponRepository.save(newCoupon);
    }

    @Override
    public synchronized void useCoupon(UUID sessionId, UseCouponRequest request) {
        Invoice invoice = this.invoiceRepository.findBySessionId(sessionId)
                .orElseThrow(() -> new InvoiceDoesNotExistException(sessionId));
        Coupon coupon = this.couponRepository.findByName(request.getName())
                .orElseThrow(() -> new CouponDoesNotExistException(request.getName()));

        if (coupon.isUsed()) {
            throw new CouponAlreadyUsedException(request.getName());
        }
        coupon.setUsed(true);
        invoice.setDiscount(invoice.getDiscount() + coupon.getDiscount());

        this.couponRepository.save(coupon);
        this.invoiceRepository.save(invoice);
    }

    @Override
    public Coupon createCoupon(CouponRequest request) {
        if (isCouponAlreadyExist(request.getName())) {
            throw new CouponAlreadyExistException(request.getName());
        }

        Coupon coupon = Coupon.builder()
                .name(request.getName())
                .discount(request.getDiscount())
                .build();

        return this.couponRepository.save(coupon);
    }

    @Override
    public void deleteCoupon(Integer id) {
        if (this.couponRepository.findById(id).isEmpty()) {
            throw new CouponDoesNotExistException(id);
        }
        this.couponRepository.deleteById(id);
    }


    @Override
    public List<Coupon> getAllCoupon() {
        return this.couponRepository.findAll();
    }

    private boolean isCouponAlreadyExist(String name) {
        return this.couponRepository.findByName(name).isPresent();
    }
}
