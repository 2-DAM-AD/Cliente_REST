package es.batoi.bfg.exceptions;

public class ExceptionError404 extends RuntimeException {
    public ExceptionError404() {
        super("(404) ¡Error! No puedes obtener un objeto cuyo ID no existe.");
    }
}
