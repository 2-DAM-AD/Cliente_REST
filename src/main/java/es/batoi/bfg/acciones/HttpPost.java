package es.batoi.bfg.acciones;

import es.batoi.bfg.Main;
import es.batoi.bfg.modelos.Artista;
import es.batoi.bfg.modelos.Cancion;
import es.batoi.bfg.utiles.Utilidades;

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

        switch (nombreTabla) {
            case "artistas":
                url = Main.URL_ARTISTAS;

                Artista artista = devolverArtista(scanner);

                json = "{\n" +
                        "  \"nombre\": \"" + artista.getNombre() + "\",\n" +
                        "  \"anyo_creacion\": " + artista.getAnyo_creacion() + "\n" +
                        "}";

                break;

            case "canciones":
                url = Main.URL_CANCIONES;

                Cancion cancion = devolverCancion(scanner);

                json = "{\n" +
                        "  \"nombre\": \"" + cancion.getNombre() + "\",\n" +
                        "  \"album\": \"" + cancion.getAlbum() + "\",\n" +
                        "  \"idArtista\": " + cancion.getIdArtista() + "\n" +
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

    private static Artista devolverArtista(Scanner scanner) {
        System.out.print("Introduce el nombre del artista: ");
        String nombre = scanner.nextLine();

        System.out.print("Introduce el año en el que se 'creó': ");
        int anyo_creacion = Utilidades.pedirAnyoCreacionGrupo(scanner);

        Artista artista = new Artista();
        artista.setNombre(nombre);
        artista.setAnyo_creacion(anyo_creacion);

        return artista;
    }

    private static Cancion devolverCancion(Scanner scanner) {
        System.out.print("Introduce el nombre de la canción: ");
        String nombre = scanner.nextLine();

        System.out.print("Introduce el nombre álbum al cuál pertenece: ");
        String nombreAlbum = scanner.nextLine();

        System.out.print("Introduce el ID del artista al cuál pertenece: ");
        int idArtistaCancion = Utilidades.pedirNumeroSeleccionUsuario(scanner);

        Cancion cancion = new Cancion();
        cancion.setNombre(nombre);
        cancion.setAlbum(nombreAlbum);
        cancion.setIdArtista(idArtistaCancion);

        return cancion;
    }
}
