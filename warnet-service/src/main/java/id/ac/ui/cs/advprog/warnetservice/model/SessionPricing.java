package id.ac.ui.cs.advprog.warnetservice.model;

import java.time.LocalDateTime;

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
public class SessionPricing {
    @Id
    @GeneratedValue
    private Integer id;
    @ManyToOne
    private Session session;
    @ManyToOne
    private Pricing pricing;
    private LocalDateTime waktuPembelian;
    private Integer quantity;
}
