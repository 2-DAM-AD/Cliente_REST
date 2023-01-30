package es.batoi.bfg.utiles;

import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

    public static int pedirAnyoCreacionGrupo(Scanner scanner) {

        int valor;

        do {
            while (!scanner.hasNextInt()) {
                System.err.print("\n¡Error! No has introducido un número. Vuelve a intentarlo: ");
                scanner.next();
            }

            valor = scanner.nextInt();

            Matcher matcher = Pattern.compile("\\d{4}").matcher(String.valueOf(valor));

            if (valor > 0 && matcher.matches()) {
                break;
            } else {
                System.err.print("\n¡Error! Debes introducir un número mayor que 0 y de 4 dígitos. Vuelve a intentarlo: ");
            }

        } while (true);

        return valor;
    }
}
