package id.ac.ui.cs.advprog.bayarservice.service.bank;

import id.ac.ui.cs.advprog.bayarservice.exception.bank.BankAlreadyExistsException;
import id.ac.ui.cs.advprog.bayarservice.exception.bank.BankDoesNotExistException;
import id.ac.ui.cs.advprog.bayarservice.dto.bank.BankRequest;
import id.ac.ui.cs.advprog.bayarservice.model.bank.Bank;
import id.ac.ui.cs.advprog.bayarservice.repository.BankRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BankServiceImpl implements BankService {
    private final BankRepository bankRepository;

    @Override
    public List<Bank> getAll() {
        return this.bankRepository.findAll();
    }

    @Override
    public Bank create(BankRequest request) {
        if (isBankAlreadyExist(request.getName())) {
            throw new BankAlreadyExistsException(request.getName());
        }
        Bank newBank = Bank.builder()
                .name(request.getName())
                .adminFee(request.getAdminFee())
                .build();
        return bankRepository.save(newBank);
    }

    @Override
    public void deleteById(Integer id) {
        if (isBankDoesNotExist(id)) {
            throw new BankDoesNotExistException(id);
        } else {
            bankRepository.deleteById(id);
        }
    }

    @Override
    public Bank findById(Integer id) {
        return this.bankRepository.findById(id)
                .orElseThrow(() -> new BankDoesNotExistException(id));
    }

    @Override
    public Bank update(Integer id, BankRequest request) {
        Bank bank = this.bankRepository.findById(id)
                .orElseThrow(() -> new BankDoesNotExistException(id));
        Optional<Bank> bankByName = this.bankRepository.findByName(request.getName());

        if (bankByName.isPresent() && !Objects.equals(bankByName.get().getId(), id)) {
            throw new BankAlreadyExistsException(request.getName());
        }

        bank.setName(request.getName());
        bank.setAdminFee(request.getAdminFee());
        return bankRepository.save(bank);
    }

    private boolean isBankAlreadyExist(String name) {
        return bankRepository.findByName(name).isPresent();
    }

    private boolean isBankDoesNotExist(Integer id) {
        return bankRepository.findById(id).isEmpty();
    }
}
