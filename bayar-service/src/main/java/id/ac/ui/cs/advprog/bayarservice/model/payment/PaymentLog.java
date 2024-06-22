package id.ac.ui.cs.advprog.bayarservice.model.payment;

import com.fasterxml.jackson.annotation.JsonBackReference;
import id.ac.ui.cs.advprog.bayarservice.model.invoice.Invoice;
import id.ac.ui.cs.advprog.bayarservice.model.invoice.PaymentMethod;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.sql.Date;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "payment_logs", indexes = {@Index(name = "payment_log_session_id_idx", columnList = "sessionId")})
public class PaymentLog {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @Column(nullable = false)
    private Long totalAmount;

    @Column(columnDefinition = "VARCHAR(5)")
    @Enumerated(EnumType.STRING)
    private PaymentMethod paymentMethod;

    @CreationTimestamp
    @Column(nullable = false, updatable = false, columnDefinition = "TIMESTAMP WITH TIME ZONE default CURRENT_TIMESTAMP")
    private Date createdAt;

    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(nullable = false)
    private UUID sessionId;

    @JsonBackReference
    @ManyToOne
    @JoinColumn(name = "invoice_id", nullable = false)
    private Invoice invoice;

    @Column(nullable = false)
    private Integer noPC;
}
