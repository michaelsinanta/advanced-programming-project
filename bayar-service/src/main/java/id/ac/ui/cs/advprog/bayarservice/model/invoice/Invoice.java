package id.ac.ui.cs.advprog.bayarservice.model.invoice;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import id.ac.ui.cs.advprog.bayarservice.model.bank.Bank;
import id.ac.ui.cs.advprog.bayarservice.model.bill.Bill;
import id.ac.ui.cs.advprog.bayarservice.model.payment.PaymentLog;
import jakarta.persistence.*;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.DynamicInsert;

import java.sql.Date;
import java.util.List;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@DynamicInsert
@Table(name = "invoices", indexes = {@Index(name = "invoice_session_id_idx", columnList = "sessionId")})
public class Invoice {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @Column(columnDefinition = "VARCHAR(5)")
    @Enumerated(EnumType.STRING)
    private PaymentMethod paymentMethod;

    @Column(columnDefinition = "VARCHAR(10) default 'UNPAID'")
    @Enumerated(EnumType.STRING)
    private PaymentStatus paymentStatus = PaymentStatus.UNPAID;

    @CreationTimestamp
    @Column(nullable = false, updatable = false, columnDefinition = "TIMESTAMP WITH TIME ZONE default CURRENT_TIMESTAMP")
    private Date createdAt;

    @Column(nullable = false, columnDefinition = "BIGINT DEFAULT 0")
    private Long totalAmount = 0L;

    @Column(nullable = false, columnDefinition = "BIGINT DEFAULT 0")
    private Long discount = 0L;

    @JsonManagedReference
    @OneToMany(mappedBy = "invoice", cascade = CascadeType.ALL)
    private List<Bill> bills;

    @JsonManagedReference
    @JsonIgnore
    @OneToMany(mappedBy = "invoice", cascade = CascadeType.ALL)
    private List<PaymentLog> paymentHistories;

    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(nullable = false)
    private UUID sessionId;

    @JsonBackReference
    @ManyToOne
    @JoinColumn(name = "bank_id")
    private Bank bank;
}
