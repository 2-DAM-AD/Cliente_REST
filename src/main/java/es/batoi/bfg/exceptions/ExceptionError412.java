package es.batoi.bfg.exceptions;

public class ExceptionError412 extends RuntimeException {
    public ExceptionError412() {
        super("(412) ¡Error! La precondición (idUrl != idInsertado)");
    }
}
