package es.batoi.bfg;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Scanner;

public class HttpDelete {
    public static HttpResponse<String> ejecutarDelete(HttpClient cliente, String nombreTabla) throws IOException, InterruptedException {
        Scanner scanner = new Scanner(System.in);
        String url = "";
        String json = "";
        int id = 0;

        switch (nombreTabla) {
            case "artistas":
                url = Main.URL_ARTISTAS;

                System.out.print("\nIntroduce el ID del artista que deseas eliminar: ");
                id = Utilidades.pedirNumeroSeleccionUsuario(scanner);

                json = "{\n" +
                        "  \"id\": \"" + id + "\"\n" +
                        "}";

                break;

            // TODO: Error 500 (si se introduce un id de artista que no existe salta el error)
            case "canciones":
                url = Main.URL_CANCIONES;

                System.out.print("\nIntroduce el ID de la canción que deseas eliminar: ");
                id = Utilidades.pedirNumeroSeleccionUsuario(scanner);

                json = "{\n" +
                        "  \"id\": \"" + id + "\"\n" +
                        "}";

                break;

            default:
                System.err.println("\n¡Error no contemplado (HttpDelete)!");
        }

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url + "/" + id))
                .header("accept", "application/json")
                .header("Content-Type", "application/json")
                .method("DELETE", HttpRequest.BodyPublishers.ofString(json))
                .build();

        return cliente.send(request, HttpResponse.BodyHandlers.ofString());
    }
}
