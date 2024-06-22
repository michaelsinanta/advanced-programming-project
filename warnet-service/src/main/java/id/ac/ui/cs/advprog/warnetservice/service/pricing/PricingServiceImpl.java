package id.ac.ui.cs.advprog.warnetservice.service.pricing;

import id.ac.ui.cs.advprog.warnetservice.dto.pricing.CreateSessionRequest;
import id.ac.ui.cs.advprog.warnetservice.dto.pricing.CreateSessionResponse;
import id.ac.ui.cs.advprog.warnetservice.dto.pricing.PricingByPCResponse;
import id.ac.ui.cs.advprog.warnetservice.dto.rest.*;
import id.ac.ui.cs.advprog.warnetservice.exceptions.*;
import id.ac.ui.cs.advprog.warnetservice.model.PC;
import id.ac.ui.cs.advprog.warnetservice.model.Pricing;
import id.ac.ui.cs.advprog.warnetservice.model.Session;
import id.ac.ui.cs.advprog.warnetservice.model.SessionPricing;
import id.ac.ui.cs.advprog.warnetservice.repository.PCRepository;
import id.ac.ui.cs.advprog.warnetservice.repository.PricingRepository;
import id.ac.ui.cs.advprog.warnetservice.repository.SessionPricingRepository;
import id.ac.ui.cs.advprog.warnetservice.repository.SessionRepository;
import id.ac.ui.cs.advprog.warnetservice.rest.BayarService;
import id.ac.ui.cs.advprog.warnetservice.rest.CafeService;
import id.ac.ui.cs.advprog.warnetservice.service.kelolawarnet.KelolaWarnetServiceImpl;
import id.ac.ui.cs.advprog.warnetservice.service.pricing.validitycheckhandler.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.*;

@Service
@RequiredArgsConstructor
public class PricingServiceImpl implements PricingService {
    private final SessionRepository userSessionRepository;
    private final PCRepository pcRepository;
    private final KelolaWarnetServiceImpl kelolaWarnetService;
    private final PricingRepository pricingRepository;
    private final SessionPricingRepository sessionPricingRepository;
    private final BayarService bayarService;
    private final CafeService cafeService;
    private final PCExistenceValidationHandler pcExistenceValidationHandler;
    private final PricingExistenceValidationHandler pricingExistenceValidationHandler;
    private final PCPricingPairValidationHandler pcPricingPairValidationHandler;
    private final PCUsageValidationHandler pcUsageValidationHandler;
    private final QuantityValidationHandler quantityValidationHandler;
    private final FoodAvailabilityValidationHandler foodAvailabilityValidationHandler;

    public synchronized CreateSessionResponse createSession(CreateSessionRequest createSessionRequest) {

        validateInput(createSessionRequest);

        LocalDateTime initialTime = LocalDateTime.now();
        Optional<PC> optionalPc = pcRepository.findById(createSessionRequest.getPcId());

        PC pc = optionalPc.orElseGet(PC::new);

        Optional<Pricing> optionalPricing = pricingRepository.findById(createSessionRequest.getPricingId());

        Pricing pricing = optionalPricing.orElseGet(Pricing::new);

        Integer quantity = createSessionRequest.getQuantity();

        Integer hours = pricing.getDuration() * quantity;
        Session session = Session.builder()
                .pc(pc)
                .datetimeStart(initialTime)
                .datetimeEnd(initialTime.plusHours(hours))
                .build();

        session = userSessionRepository.save(session);

        addOrderToCafe(pricing.getMakananId(), session.getId());
        createInvoice(session.getId().toString());
        addBillToBayarSquad(pricing, createSessionRequest.getQuantity(), session.getId().toString());

        SessionPricing sessionPricing = SessionPricing.builder()
                .session(session)
                .pricing(pricing)
                .waktuPembelian(LocalDateTime.now())
                .quantity(quantity)
                .build();

        sessionPricingRepository.save(sessionPricing);

        return CreateSessionResponse.builder()
                .id(session.getId())
                .pcId(pc.getId())
                .noPC(pc.getNoPC())
                .noRuangan(pc.getNoRuangan())
                .pricingId(pricing.getId())
                .namaPricing(pricing.getName())
                .totalHarga(pricing.getPrice() * quantity)
                .totalDurasi(hours)
                .build();
    }

    private void validateInput(CreateSessionRequest createSessionRequest) {

        pcExistenceValidationHandler.setNextHandler(pricingExistenceValidationHandler)
                .setNextHandler(pcPricingPairValidationHandler)
                .setNextHandler(pcUsageValidationHandler)
                .setNextHandler(quantityValidationHandler)
                .setNextHandler(foodAvailabilityValidationHandler);

        pcExistenceValidationHandler.handleRequest(createSessionRequest);
    }

    public Boolean isFoodStockAvailable(String makananId) {
        if (makananId != null) {
            MenuItemDTO menuItemDTO = cafeService.getMenuItemByIdFromMicroservice(makananId);
            if (menuItemDTO.getStock() <= 0) {
                return false;
            }
        }
        return true;
    }

    public void addOrderToCafe(String makananId, UUID sessionId) {
        if (makananId != null) {
            OrderDetailsDTO orderDetailsDTO = OrderDetailsDTO.builder()
                    .menuItemId(makananId)
                    .quantity(1)
                    .status("Menunggu Konfirmasi")
                    .build();

            ArrayList<OrderDetailsDTO> orderDetails = new ArrayList<>();
            orderDetails.add(orderDetailsDTO);

            cafeService.createOrderFromMicroservice(
                    OrderRequest.builder()
                            .session(sessionId)
                            .orderDetailsData(orderDetails)
                            .build());
        }
    }

    public void createInvoice(String sessionId) {
        bayarService.createInvoiceFromMicroservice(
                InvoiceRequest.builder()
                        .sessionId(sessionId)
                        .build());
    }

    public void addBillToBayarSquad(Pricing pricing, Integer quantity, String sessionId) {

        Long subTotal = (long) pricing.getPrice() * quantity;
        bayarService.addToBillFromMicroservice(
                BillRequest.builder()
                        .name(pricing.getName())
                        .price(pricing.getPrice())
                        .quantity(quantity)
                        .subTotal(subTotal)
                        .sessionId(sessionId)
                        .build());
    }

    public Session getSession(UUID id) {
        Optional<Session> session = userSessionRepository.findById(id);
        if (session.isEmpty()) {
            throw new SessionDoesNotExistException(id.toString());
        }
        return session.get();
    }

    @Override
    public List<PricingByPCResponse> getAllPricingbyPC(Integer pcId, Boolean reqIsPaket) {
        try {
            PC pc = kelolaWarnetService.getPCById(pcId);

            List<Pricing> listPricing = pc.getPricingList();
            List<CompletableFuture<PricingByPCResponse>> futures = new ArrayList<>();

            ExecutorService executorService = Executors.newFixedThreadPool(10);

            listPricing.forEach(pricing -> {
                CompletableFuture<PricingByPCResponse> future = CompletableFuture.supplyAsync(() -> {
                    if (((reqIsPaket && pricing.getIsPaket()) || (!reqIsPaket && !pricing.getIsPaket()))
                            && Boolean.TRUE.equals(isFoodStockAvailable(pricing.getMakananId()))) {

                        return PricingByPCResponse.builder()
                                .pricingId(pricing.getId())
                                .namaPricing(pricing.getName())
                                .harga(pricing.getPrice())
                                .durasi(pricing.getDuration())
                                .makananId(pricing.getMakananId())
                                .build();
                    } else {
                        return null;
                    }
                }, executorService);

                futures.add(future);
            });

            CompletableFuture<Void> allFutures = CompletableFuture.allOf(futures.toArray(new CompletableFuture[0]));
            CompletableFuture<List<PricingByPCResponse>> combinedFuture = allFutures.thenApply(v -> futures.stream()
                    .map(CompletableFuture::join)
                    .filter(Objects::nonNull)
                    .toList());

            List<PricingByPCResponse> results = combinedFuture.get();

            executorService.shutdown();

            return results;
        } catch (InterruptedException | ExecutionException e) {
            Thread.currentThread().interrupt();
            throw new PCInterruptedException("Interrupted while processing PC pricing: " + e.getMessage());
        }
    }

    public Session endSession(UUID id) {
        Session session = getSession(id);

        if (session.getDatetimeEnd().isBefore(LocalDateTime.now())) {
            throw new SessionExpiredException(session.getId().toString());
        }
        session.setDatetimeEnd(LocalDateTime.now());
        return userSessionRepository.save(session);
    }

}
