package id.ac.ui.cs.advprog.warnetservice.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
@Table(name = "pc")
public class PC {
    @Id
    @GeneratedValue
    @Column(name = "id")
    private Integer id;
    @Column(name = "nopc")
    private Integer noPC;
    @Column(name = "no_ruangan")
    private Integer noRuangan;
    @JsonIgnore
    @OneToMany(mappedBy = "pc", cascade = CascadeType.ALL)
    private List<Session> sessionList;
    @JsonIgnore
    @ManyToMany(mappedBy = "pcList")
    private List<Pricing> pricingList;
}

