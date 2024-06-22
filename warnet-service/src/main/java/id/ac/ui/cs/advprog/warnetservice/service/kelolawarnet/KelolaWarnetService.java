package id.ac.ui.cs.advprog.warnetservice.service.kelolawarnet;
import java.util.List;

import org.springframework.stereotype.Service;

import id.ac.ui.cs.advprog.warnetservice.dto.kelolawarnet.AllPCResponse;
import id.ac.ui.cs.advprog.warnetservice.model.PC;

@Service
public interface KelolaWarnetService {
    List<AllPCResponse> getAllPC(boolean filtered);
    PC getPCById(Integer id);
    PC deletePC(Integer id);
}
