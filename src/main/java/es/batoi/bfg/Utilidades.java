package es.batoi.bfg;

import java.util.Scanner;

public class Utilidades {
    public static int pedirNumeroSeleccionUsuario(Scanner scanner) {

        int valor;

        do {
            while (!scanner.hasNextInt()) {
                System.err.print("\n¡Error! No has introducido un número. Vuelve a intentarlo: ");
                scanner.next();
            }

            valor = scanner.nextInt();

            if (valor > 0) {
                break;
            } else {
                System.err.print("\n¡Error! Debes introducir un número mayor que 0. Vuelve a intentarlo: ");
            }

        } while (true);

        return valor;
    }
}
