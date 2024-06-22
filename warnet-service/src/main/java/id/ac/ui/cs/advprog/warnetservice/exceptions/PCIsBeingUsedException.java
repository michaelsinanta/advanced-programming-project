package id.ac.ui.cs.advprog.warnetservice.exceptions;

public class PCIsBeingUsedException extends RuntimeException{

    public PCIsBeingUsedException(Integer noPC, Integer noRuangan) {
        super("PC " + noPC + " Ruangan " + noRuangan + " sedang digunakan");
    }
}
