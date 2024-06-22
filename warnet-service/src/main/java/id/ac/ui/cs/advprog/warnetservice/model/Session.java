package id.ac.ui.cs.advprog.warnetservice.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "session", indexes = {
        @Index(name = "DatetimeStartIndex", columnList = "datetime_start")
})
public class Session {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id")
    private UUID id;
    @ManyToOne
    @JoinColumn(name = "pc_id")
    private PC pc;
    @Column(name = "datetime_start")
    private LocalDateTime datetimeStart;
    @Column(name = "datetime_end")
    private LocalDateTime datetimeEnd;
    @JsonIgnore
    @OneToMany(mappedBy = "session", cascade = CascadeType.ALL)
    private List<SessionPricing> sessionPricingList;
}
