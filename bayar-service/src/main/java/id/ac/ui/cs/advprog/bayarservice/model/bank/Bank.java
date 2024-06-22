package id.ac.ui.cs.advprog.bayarservice.model.bank;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import id.ac.ui.cs.advprog.bayarservice.model.invoice.Invoice;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "banks")
public class Bank {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private Integer adminFee;
    @JsonManagedReference
    @JsonIgnore
    @OneToMany(mappedBy = "bank", cascade = CascadeType.ALL)
    private List<Invoice> invoices;
}
