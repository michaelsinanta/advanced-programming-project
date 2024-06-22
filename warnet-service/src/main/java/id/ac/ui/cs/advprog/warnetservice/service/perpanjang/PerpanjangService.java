package id.ac.ui.cs.advprog.warnetservice.service.perpanjang;


import id.ac.ui.cs.advprog.warnetservice.dto.perpanjang.*;
import id.ac.ui.cs.advprog.warnetservice.model.Session;


public interface PerpanjangService {
    public Session extendSession(ExtendSessionRequest extendSessionRequest);
}
