package es.batoi.bfg.exceptions;

public class ExceptionError500 extends RuntimeException {
    public ExceptionError500(String message) {
        super("(500) ¡Error! " + message);
    }
}
