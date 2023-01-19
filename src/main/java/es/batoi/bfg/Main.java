package es.batoi.bfg;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import es.batoi.bfg.modelos.Helper;

import java.io.IOException;
import java.net.http.HttpClient;
import java.net.http.HttpResponse;
import java.util.Scanner;

public class Main {
    public static final String URL_ARTISTAS = "http://127.0.0.1:1818/bfg/artistas";
    public static final String URL_CANCIONES = "http://127.0.0.1:1818/bfg/canciones";
    public static final String TABLA_ARTISTAS = "artistas";
    public static final String TABLA_CANCIONES = "canciones";

    public static void main(String[] args) throws IOException, InterruptedException {
        HttpClient httpCliente = HttpClient.newHttpClient();

        Scanner scanner = new Scanner(System.in);
        HttpResponse<String> respuesta = null;
        boolean isTablaCorrecta;
        boolean isAccionCorrecta;

        String tablaSeleccionada = "";

        do {
            System.out.println("Tablas disponibles:\n1. Artistas\n2. Canciones\n");
            System.out.print("Selecciona una tabla: ");

            int idTablaSeleccionada = Utilidades.pedirNumeroSeleccionUsuario(scanner);

            switch (idTablaSeleccionada) {
                case 1:
                    tablaSeleccionada = TABLA_ARTISTAS;
                    isTablaCorrecta = true;
                    break;

                case 2:
                    tablaSeleccionada = TABLA_CANCIONES;
                    isTablaCorrecta = true;
                    break;

                default:
                    isTablaCorrecta = false;
                    System.err.println("\n¡Error! No has introducido una tabla válida. Vuelve a intentarlo\n");

                    try {
                        Thread.sleep(50);
                    } catch (InterruptedException e) {
                        System.err.println("¡Error! Espera en selección de tabla fallida");
                    }
            }
        } while (!isTablaCorrecta);

        do {
            System.out.println("\nOperaciones disponibles:\n1. Obtener datos (GET)\n2. Insertar datos (POST)\n3. Actualizar datos (PUT)\n4. Eliminar datos (DELETE)");
            System.out.print("\nSelecciona una operación: ");

            int idOperacionSeleccionada = Utilidades.pedirNumeroSeleccionUsuario(scanner);

            switch (idOperacionSeleccionada) {
                case 1:
                    isAccionCorrecta = true;
                    Helper helper = HttpGet.ejecutarGet(httpCliente, tablaSeleccionada);
                    respuesta = helper.getHttpResponse();

                    break;

                case 2:
                    isAccionCorrecta = true;
                    respuesta = HttpPost.ejecutarPost(httpCliente, tablaSeleccionada);

                    break;

                // ! Código 204 (al actualizar no devuelve cuerpo, sólo el código 204)
                case 3:
                    isAccionCorrecta = true;
                    respuesta = HttpPut.ejecutarPut(httpCliente, tablaSeleccionada);

                    break;

                // ! Código 500 (no se puede eliminar un registro con claves ajenas 'activas'
                case 4:
                    isAccionCorrecta = true;
                    respuesta = HttpDelete.ejecutarDelete(httpCliente, tablaSeleccionada);
                    break;

                default:
                    isAccionCorrecta = false;
                    respuesta = null;

                    System.err.println("\n¡Error! No has introducido una operación válida. Vuelve a intentarlo");

                    try {
                        Thread.sleep(50);
                    } catch (InterruptedException e) {
                        System.err.println("¡Error! Espera en selección de tabla fallida");
                    }
            }
        } while (!isAccionCorrecta || respuesta == null);

        System.out.println(convertirBodyToObject(respuesta));
    }

    private static String convertirBodyToObject(HttpResponse<String> respuesta) {
        String json = respuesta.body();

        JsonParser parser  = new JsonParser();
        JsonElement element = parser.parse(json);

        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        json = gson.toJson(element);

        /*try {
            ObjectMapper objectMapper = new ObjectMapper();

            switch (tablaSeleccionada) {
                case TABLA_ARTISTAS:
                    return objectMapper.readValue(json, Artista.class);
                case TABLA_CANCIONES:
                    return objectMapper.readValue(json, Cancion.class);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }*/

        return json;
    }
}
