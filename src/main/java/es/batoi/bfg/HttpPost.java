package es.batoi.bfg;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Scanner;

public class HttpPost {
    public static HttpResponse<String> ejecutarPost(HttpClient cliente, String nombreTabla) throws IOException, InterruptedException {
        Scanner scanner = new Scanner(System.in);
        String url = "";
        String json = "";

        String nombre;

        switch (nombreTabla) {
            case "artistas":
                url = Main.URL_ARTISTAS;

                System.out.print("\nIntroduce el nombre del artista: ");
                nombre = scanner.nextLine();
                System.out.print("Introduce el año en el que se 'creó': ");
                int anyo_creacion = Utilidades.pedirAnyoCreacionGrupo(scanner);

                json = "{\n" +
                        "  \"nombre\": \"" + nombre + "\",\n" +
                        "  \"anyo_creacion\": " + anyo_creacion + "\n" +
                        "}";

                break;

            case "canciones":
                url = Main.URL_CANCIONES;

                System.out.print("\nIntroduce el nombre de la canción: ");
                nombre = scanner.nextLine();
                System.out.print("Introduce el nombre álbum al cuál pertenece: ");
                String nombreAlbum = scanner.nextLine();
                System.out.print("Introduce el ID del artista al cuál pertenece: ");
                int idArtistaCancion = Utilidades.pedirNumeroSeleccionUsuario(scanner);

                json = "{\n" +
                        "  \"nombre\": \"" + nombre + "\",\n" +
                        "  \"album\": \"" + nombreAlbum + "\",\n" +
                        "  \"idArtista\": " + idArtistaCancion + "\n" +
                        "}";

                break;

            default:
                System.err.println("\n¡Error no contemplado (HttpPost)!");
        }

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .build();

        return cliente.send(request, HttpResponse.BodyHandlers.ofString());
    }
}
