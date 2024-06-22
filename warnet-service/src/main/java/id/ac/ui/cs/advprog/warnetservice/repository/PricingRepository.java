package id.ac.ui.cs.advprog.warnetservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import id.ac.ui.cs.advprog.warnetservice.model.Pricing;

import org.springframework.stereotype.Repository;

@Repository
public interface PricingRepository extends JpaRepository<Pricing, Integer> {
}
