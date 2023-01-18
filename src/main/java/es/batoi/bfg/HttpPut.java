package es.batoi.bfg;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Scanner;

public class HttpPut {
    public static HttpResponse<String> ejecutarPut(HttpClient cliente, String nombreTabla) throws IOException, InterruptedException {
        Scanner scanner = new Scanner(System.in);
        String url = "";
        String json = "";
        int id = 0;

        String nombre;

        switch (nombreTabla) {
            case "artistas":
                url = Main.URL_ARTISTAS;

                System.out.print("\nIntroduce el ID del artista que deseas actualizar: ");
                id = Utilidades.pedirNumeroSeleccionUsuario(scanner);
                scanner.nextLine();
                System.out.print("Introduce el nombre del artista: ");
                nombre = scanner.nextLine();
                System.out.print("Introduce el año en el que se 'creó': ");
                int anyo_creacion = Utilidades.pedirAnyoCreacionGrupo(scanner);

                json = "{\n" +
                        "  \"id\": \"" + id + "\",\n" +
                        "  \"nombre\": \"" + nombre + "\",\n" +
                        "  \"anyo_creacion\": " + anyo_creacion + "\n" +
                        "}";

                break;

            // TODO: Error 500 (si se introduce un id de artista que no existe salta el error)
            case "canciones":
                url = Main.URL_CANCIONES;

                System.out.print("\nIntroduce el ID de la canción que deseas actualizar: ");
                id = Utilidades.pedirNumeroSeleccionUsuario(scanner);
                scanner.nextLine();
                System.out.print("Introduce el nombre de la canción: ");
                nombre = scanner.nextLine();
                System.out.print("Introduce el nombre álbum al cuál pertenece: ");
                String nombreAlbum = scanner.nextLine();
                System.out.print("Introduce el ID del artista al cuál pertenece: ");
                int idArtistaCancion = Utilidades.pedirNumeroSeleccionUsuario(scanner);

                json = "{\n" +
                        "  \"id\": \"" + id + "\",\n" +
                        "  \"nombre\": \"" + nombre + "\",\n" +
                        "  \"album\": \"" + nombreAlbum + "\",\n" +
                        "  \"idArtista\": " + idArtistaCancion + "\n" +
                        "}";

                break;

            default:
                System.err.println("\n¡Error no contemplado (HttpPut)!");
        }

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url + "/" + id))
                .header("Content-Type", "application/json")
                .PUT(HttpRequest.BodyPublishers.ofString(json))
                .build();


        return cliente.send(request, HttpResponse.BodyHandlers.ofString());
    }
}
