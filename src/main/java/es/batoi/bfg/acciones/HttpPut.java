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

public class HttpPut {
    public static HttpResponse<String> ejecutarPut(HttpClient cliente, String nombreTabla) throws IOException, InterruptedException {
        Scanner scanner = new Scanner(System.in);
        String url = "";
        String json = "";

        int id = 0;

        switch (nombreTabla) {
            case "artistas":
                url = Main.URL_ARTISTAS;

                Artista artista = devolverArtistaConId(scanner);
                id = artista.getId();

                json = "{\n" +
                        "  \"id\": \"" + artista.getId() + "\",\n" +
                        "  \"nombre\": \"" + artista.getNombre() + "\",\n" +
                        "  \"anyo_creacion\": " + artista.getAnyo_creacion() + "\n" +
                        "}";

                break;

            // TODO: Error 500 (si se introduce un id de artista que no existe salta el error)
            case "canciones":
                url = Main.URL_CANCIONES;

                Cancion cancion = devolverCancionConId(scanner);
                id = cancion.getId();

                json = "{\n" +
                        "  \"id\": \"" + cancion.getId() + "\",\n" +
                        "  \"nombre\": \"" + cancion.getNombre() + "\",\n" +
                        "  \"album\": \"" + cancion.getAlbum() + "\",\n" +
                        "  \"idArtista\": " + cancion.getIdArtista() + "\n" +
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

    private static Artista devolverArtistaConId(Scanner scanner) {
        System.out.print("\nIntroduce el ID del artista que deseas actualizar: ");
        int id = Utilidades.pedirNumeroSeleccionUsuario(scanner);
        scanner.nextLine();

        System.out.print("Introduce el nombre del artista: ");
        String nombre = scanner.nextLine();

        System.out.print("Introduce el año en el que se 'creó': ");
        int anyo_creacion = Utilidades.pedirAnyoCreacionGrupo(scanner);

        Artista artista = new Artista();
        artista.setId(id);
        artista.setNombre(nombre);
        artista.setAnyo_creacion(anyo_creacion);

        return artista;
    }

    private static Cancion devolverCancionConId(Scanner scanner) {
        System.out.print("\nIntroduce el ID de la canción que deseas actualizar: ");
        int id = Utilidades.pedirNumeroSeleccionUsuario(scanner);
        scanner.nextLine();

        System.out.print("Introduce el nombre de la canción: ");
        String nombre = scanner.nextLine();

        System.out.print("Introduce el nombre álbum al cuál pertenece: ");
        String nombreAlbum = scanner.nextLine();

        System.out.print("Introduce el ID del artista al cuál pertenece: ");
        int idArtistaCancion = Utilidades.pedirNumeroSeleccionUsuario(scanner);

        Cancion cancion = new Cancion();
        cancion.setId(id);
        cancion.setNombre(nombre);
        cancion.setAlbum(nombreAlbum);
        cancion.setIdArtista(idArtistaCancion);

        return cancion;
    }
}
