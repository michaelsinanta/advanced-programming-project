package id.ac.ui.cs.advprog.bayarservice.model.bill;

import com.fasterxml.jackson.annotation.JsonBackReference;
import id.ac.ui.cs.advprog.bayarservice.model.invoice.Invoice;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "bills", indexes = {@Index(name = "bills_invoice_id_idx", columnList = "invoice_id")})
public class Bill {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;
    @Column(nullable = false)
    private String name;
    @Column(nullable = false, columnDefinition = "INT CHECK (price > 0)")
    private Integer price;
    @Column(nullable = false, columnDefinition = "INT CHECK (quantity > 0)")
    private Integer quantity;
    @Column(nullable = false, columnDefinition = "BIGINT CHECK (price > 0)")
    private Long subTotal;
    @JsonBackReference
    @ManyToOne
    @JoinColumn(name = "invoice_id", nullable = false)
    private Invoice invoice;
}
