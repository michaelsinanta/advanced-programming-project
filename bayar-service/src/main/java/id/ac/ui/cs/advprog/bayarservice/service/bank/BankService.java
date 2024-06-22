package id.ac.ui.cs.advprog.bayarservice.service.bank;

import id.ac.ui.cs.advprog.bayarservice.model.bank.Bank;
import org.springframework.stereotype.Service;
import id.ac.ui.cs.advprog.bayarservice.dto.bank.BankRequest;

import java.util.List;

@Service
public interface BankService {
    List<Bank> getAll();
    Bank create(BankRequest request);
    Bank findById(Integer id);
    void deleteById(Integer id);
    Bank update(Integer id, BankRequest request);
}
