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
public class Pricing {
    @Id
    @GeneratedValue
    private Integer id;
    private String name;
    private Integer price;
    private Integer duration;
    private Boolean isPaket;
    @JsonIgnore
    @ManyToMany
    @JoinTable(name = "pricing_pc",
        joinColumns = @JoinColumn(name = "pricing_id"),
        inverseJoinColumns = @JoinColumn(name = "pc_id"))
    private List<PC> pcList;
    private String makananId;
}
