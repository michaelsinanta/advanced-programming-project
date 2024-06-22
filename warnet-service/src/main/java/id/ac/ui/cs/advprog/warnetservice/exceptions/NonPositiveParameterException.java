package id.ac.ui.cs.advprog.warnetservice.exceptions;
public class NonPositiveParameterException extends RuntimeException{
    public NonPositiveParameterException(String paramName) {
        super("Parameter " + paramName + " harus positif");
    }
}
