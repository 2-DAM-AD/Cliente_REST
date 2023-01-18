package es.batoi.bfg;

import es.batoi.bfg.modelos.Helper;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Scanner;

public class HttpGet {
    private static boolean isList = false;

    public static Helper ejecutarGet(HttpClient cliente, String nombreTabla) throws IOException, InterruptedException {
        Scanner scanner = new Scanner(System.in);

        String url = obtenerUrlCorrectaGet(nombreTabla, scanner);

        HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .uri(URI.create(url))
                .build();

        return new Helper(cliente.send(request, HttpResponse.BodyHandlers.ofString()), isList);
    }

    private static String obtenerUrlCorrectaGet(String nombreTabla, Scanner scanner) {
        String nombreDatoSingular = "";
        boolean isOpcionObtenerValida;
        int idDato;
        String url;

        do {
            url = "";

            System.out.println("\n¿Qué deseas obtener?");
            switch (nombreTabla) {
                case "artistas":
                    System.out.println("1. Todos los artistas\n2. Artista identificado por ID\n");
                    nombreDatoSingular = "del artista";
                    url = Main.URL_ARTISTAS;
                    break;

                case "canciones":
                    System.out.println("1. Todos las canciones\n2. Canción identificado por ID\n");
                    nombreDatoSingular = "de la canción";
                    url = Main.URL_CANCIONES;
                    break;

                default:
                    System.err.println("\n¡Error no contemplado (HttpGet)!");
            }

            System.out.print("Selecciona una opción: ");
            int opcionObtenerDatos = Utilidades.pedirNumeroSeleccionUsuario(scanner);

            switch (opcionObtenerDatos) {
                case 1:
                    isOpcionObtenerValida = true;
                    isList = true;
                    break;

                case 2:
                    isOpcionObtenerValida = true;
                    isList = false;

                    System.out.print("\nIntroduce el ID " + nombreDatoSingular + ": ");
                    idDato = Utilidades.pedirNumeroSeleccionUsuario(scanner);

                    url += "/" + idDato;

                    break;

                default:
                    isOpcionObtenerValida = false;
                    System.err.println("¡Error! No has introducido una opción válida. Vuelve a intentarlo\n");
            }

        } while(!isOpcionObtenerValida);

        return url;
    }
}
