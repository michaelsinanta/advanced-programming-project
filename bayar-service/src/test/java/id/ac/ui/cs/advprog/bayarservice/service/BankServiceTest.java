package id.ac.ui.cs.advprog.bayarservice.service;

import id.ac.ui.cs.advprog.bayarservice.dto.bank.BankRequest;
import id.ac.ui.cs.advprog.bayarservice.exception.bank.BankAlreadyExistsException;
import id.ac.ui.cs.advprog.bayarservice.exception.bank.BankDoesNotExistException;
import id.ac.ui.cs.advprog.bayarservice.model.bank.Bank;
import id.ac.ui.cs.advprog.bayarservice.repository.BankRepository;
import id.ac.ui.cs.advprog.bayarservice.service.bank.BankServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BankServiceTest {

    @InjectMocks
    private BankServiceImpl bankService;

    @Mock
    private BankRepository bankRepository;

    Bank bank;

    private final String bankName = "Bank BCA";

    @Test
    void whenGetAllBanksShouldReturnListOfBanks() {
        Bank newBank = Bank.builder()
                .id(1)
                .name("BNI")
                .adminFee(5000)
                .build();

        List<Bank> bankList = List.of(newBank);

        when(bankRepository.findAll()).thenReturn(bankList);

        List<Bank> result = bankService.getAll();
        verify(bankRepository, atLeastOnce()).findAll();
        Assertions.assertEquals(bankList, result);
    }

    @Test
    void whenFindByIdAndFoundShouldReturnBank() {
        bank = Bank.builder()
                .id(0)
                .name(bankName)
                .build();

        when(bankRepository.findById(any(Integer.class))).thenReturn(Optional.of(bank));

        Bank result = bankService.findById(bank.getId());

        verify(bankRepository, atLeastOnce()).findById(any(Integer.class));
        Assertions.assertEquals(bank, result);
    }

    @Test
    void whenFindByIdAndNotFoundShouldThrowException() {
        when(bankRepository.findById(any(Integer.class))).thenReturn(Optional.empty());

        Assertions.assertThrows(BankDoesNotExistException.class, () -> bankService.findById(1));
    }

    @Test
    void whenCreateBankShouldReturnBank() {
        BankRequest request = BankRequest.builder()
                .name(bankName)
                .build();

        bank = Bank.builder()
                .name(request.getName())
                .build();
        when(bankRepository.save(any(Bank.class))).thenReturn(bank);
        Bank result = bankService.create(request);
        Assertions.assertEquals(bank, result);
    }

    @Test
    void whenCreateBankAlreadyExistShouldThrowBankAlreadyExistException() {
        BankRequest request = BankRequest.builder()
                .name(bankName)
                .build();

        bank = Bank.builder()
                .name(request.getName())
                .build();
        when(bankRepository.findByName(any(String.class))).thenReturn(Optional.of(bank));
        Assertions.assertThrows(BankAlreadyExistsException.class, () -> bankService.create(request));
    }

    @Test
    void whenDeleteAndFoundByIdShouldDeleteBank() {
        bank = Bank.builder()
                .name(bankName)
                .adminFee(3000)
                .build();
        bankRepository.save(bank);
        when(bankRepository.findById(any(Integer.class))).thenReturn(Optional.of(bank));

        bankService.deleteById(1);

        verify(bankRepository, atLeastOnce()).findById(any(Integer.class));
        verify(bankRepository, atLeastOnce()).deleteById(any(Integer.class));
    }

    @Test
    void whenDeleteAndNotFoundByIdShouldThrowException() {
        when(bankRepository.findById(any(Integer.class))).thenReturn(Optional.empty());

        Assertions.assertThrows(BankDoesNotExistException.class, () -> bankService.deleteById(1));
    }

    @Test
    void whenUpdateAndFoundByIdShouldUpdateBank() {
        BankRequest request = BankRequest.builder()
                .name(bankName)
                .build();

        bank = Bank.builder()
                .name(request.getName())
                .build();
        when(bankRepository.findById(any(Integer.class))).thenReturn(Optional.of(bank));
        when(bankRepository.save(any(Bank.class))).thenReturn(bank);
        Bank result = bankService.update(1, request);
        Assertions.assertEquals(bank, result);
    }

    @Test
    void whenUpdateAndNotFoundByIdShouldThrowException() {
        BankRequest request = BankRequest.builder()
                .name(bankName)
                .build();

        when(bankRepository.findById(any(Integer.class))).thenReturn(Optional.empty());
        Assertions.assertThrows(BankDoesNotExistException.class, () -> bankService.update(1, request));
    }

    @Test
    void whenUpdateAndFoundByIdButNameAlreadyExistShouldThrowException() {
        BankRequest request = BankRequest.builder()
                .name(bankName)
                .build();

        bank = Bank.builder()
                .name(request.getName())
                .build();
        when(bankRepository.findById(any(Integer.class))).thenReturn(Optional.of(bank));
        when(bankRepository.findByName(any(String.class))).thenReturn(Optional.of(bank));
        Assertions.assertThrows(BankAlreadyExistsException.class, () -> bankService.update(1, request));
    }

    @Test
    void whenUpdateAndFoundByIdAndNameDoesNotExist() {
        BankRequest request = BankRequest.builder()
                .name(bankName)
                .build();

        bank = Bank.builder()
                .id(1)
                .name(request.getName())
                .build();

        Bank bank2 = Bank.builder()
                .id(2)
                .name(request.getName())
                .build();
        when(bankRepository.findById(any(Integer.class))).thenReturn(Optional.of(bank));
        when(bankRepository.findByName(any(String.class))).thenReturn(Optional.of(bank2));
        when(bankRepository.save(any(Bank.class))).thenReturn(bank);
        Bank result = bankService.update(2, request);
        Assertions.assertEquals(bank, result);
    }
}
