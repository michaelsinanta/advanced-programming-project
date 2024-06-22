package id.ac.ui.cs.advprog.warnetservice.exceptions;

public class FoodSoldOutException extends RuntimeException{
    public FoodSoldOutException() {
        super("Stok makanan kosong");
    }
}
