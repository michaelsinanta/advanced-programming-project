package id.ac.ui.cs.advprog.warnetservice.exceptions;
public class PCDoesNotExistException extends RuntimeException{
    public PCDoesNotExistException(Integer id) {
        super("PC dengan id " + id + " tidak ada");
    }
}
