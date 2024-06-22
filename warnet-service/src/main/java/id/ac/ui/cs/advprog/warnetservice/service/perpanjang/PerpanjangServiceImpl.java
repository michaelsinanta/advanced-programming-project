package id.ac.ui.cs.advprog.warnetservice.service.perpanjang;

import id.ac.ui.cs.advprog.warnetservice.dto.perpanjang.*;
import id.ac.ui.cs.advprog.warnetservice.exceptions.*;
import id.ac.ui.cs.advprog.warnetservice.model.Pricing;
import id.ac.ui.cs.advprog.warnetservice.model.Session;
import id.ac.ui.cs.advprog.warnetservice.model.SessionPricing;
import id.ac.ui.cs.advprog.warnetservice.repository.PricingRepository;
import id.ac.ui.cs.advprog.warnetservice.repository.SessionPricingRepository;
import id.ac.ui.cs.advprog.warnetservice.repository.SessionRepository;
import id.ac.ui.cs.advprog.warnetservice.service.pricing.PricingServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PerpanjangServiceImpl implements PerpanjangService {
    private final PricingRepository pricingRepository;
    private final SessionRepository sessionRepository;
    private final SessionPricingRepository sessionPricingRepository;
    private final PricingServiceImpl pricingServiceImpl;

    @Override
    public Session extendSession(ExtendSessionRequest extendSessionRequest) {
        Optional<Session> optionalSession = sessionRepository.findById(extendSessionRequest.getId());
        if (optionalSession.isEmpty()) {
            throw new SessionDoesNotExistException(extendSessionRequest.getId().toString());
        }
        Session session = optionalSession.get();

        if (LocalDateTime.now().isAfter(session.getDatetimeEnd())) {
            throw new SessionExpiredException(session.getId().toString());
        }

        Optional<Pricing> optionalPricing = pricingRepository.findById(extendSessionRequest.getPricingId());
        if (optionalPricing.isEmpty()) {
            throw new PricingDoesNotExistException(extendSessionRequest.getPricingId());
        }
        Pricing pricing = optionalPricing.get();

        if (!session.getPc().getPricingList().contains(pricing)) {
            throw new PCPricingPairDoesNotExistException(session.getPc().getId(), pricing.getId());
        }

        if (extendSessionRequest.getQuantity() <= 0) {
            throw new NonPositiveParameterException("quantity");
        }

        if (Boolean.FALSE.equals(pricingServiceImpl.isFoodStockAvailable(pricing.getMakananId()))) {
            throw new FoodSoldOutException();
        }

        Integer quantity = extendSessionRequest.getQuantity();

        SessionPricing sessionPricing = SessionPricing.builder()
                .session(session)
                .pricing(pricing)
                .waktuPembelian(LocalDateTime.now())
                .quantity(quantity)
                .build();

        sessionPricingRepository.save(sessionPricing);

        pricingServiceImpl.addOrderToCafe(pricing.getMakananId(), session.getId());

        pricingServiceImpl.addBillToBayarSquad(pricing, quantity, session.getId().toString());

        Integer hours = pricing.getDuration() * quantity;
        session.setDatetimeEnd(session.getDatetimeEnd().plusHours(hours));

        return sessionRepository.save(session);
    }
}
