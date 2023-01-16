package es.batoi.bfg;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Scanner;

public class HttpGet {
    public static HttpResponse<String> ejecutarGet(HttpClient cliente, String nombreTabla) throws IOException, InterruptedException {
        Scanner scanner = new Scanner(System.in);
        boolean isOpcionObtenerValida = false;
        String nombreDatoSingular = "";
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
                    break;
                case 2:
                    isOpcionObtenerValida = true;

                    System.out.print("\nIntroduce el ID " + nombreDatoSingular + ": ");
                    idDato = Utilidades.pedirNumeroSeleccionUsuario(scanner);

                    url += "/" + idDato;

                    break;
                default:
                    isOpcionObtenerValida = false;
                    System.err.println("¡Error! No has introducido una opción válida. Vuelve a intentarlo\n");
            }

        } while(!isOpcionObtenerValida);

        HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .header("accept", "application/json")
                .uri(URI.create(url))
                .build();

        HttpResponse<String> response = cliente.send(request, HttpResponse.BodyHandlers.ofString());

        return response;
    }
}
