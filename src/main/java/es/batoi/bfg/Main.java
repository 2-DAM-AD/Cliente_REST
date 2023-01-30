package es.batoi.bfg;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonParser;
import es.batoi.bfg.acciones.HttpDelete;
import es.batoi.bfg.acciones.HttpGet;
import es.batoi.bfg.acciones.HttpPost;
import es.batoi.bfg.acciones.HttpPut;
import es.batoi.bfg.exceptions.ExceptionError404;
import es.batoi.bfg.exceptions.ExceptionError412;
import es.batoi.bfg.exceptions.ExceptionError500;
import es.batoi.bfg.modelos.Artista;
import es.batoi.bfg.modelos.Cancion;
import es.batoi.bfg.utiles.Helper;
import es.batoi.bfg.utiles.Utilidades;

import java.io.IOException;
import java.net.ConnectException;
import java.net.http.HttpClient;
import java.net.http.HttpResponse;
import java.util.List;
import java.util.Scanner;

public class Main {
    public static final String URL_ARTISTAS = "http://127.0.0.1:1818/bfg/artistas";
    public static final String URL_CANCIONES = "http://127.0.0.1:1818/bfg/canciones";
    public static final String TABLA_ARTISTAS = "artistas";
    public static final String TABLA_CANCIONES = "canciones";
    public static final String METODO_GET = "GET";
    public static final String METODO_POST = "POST";
    public static final String METODO_PUT = "PUT";
    public static final String METODO_DELETE = "DELETE";

    private static String tablaSeleccionada = "";

    public static void main(String[] args) {
        HttpClient httpCliente = HttpClient.newHttpClient();

        Scanner scanner = new Scanner(System.in);
        HttpResponse<String> respuesta = null;
        boolean isAccionCorrecta;
        boolean isList;

        String metodoEjecutado = "";

        boolean continuar = false;

        do {
            isList = false;

            // * Pedir tabla sobre la que actuar
            obtenerTabla(scanner);

            // * Pedir y realizar opción a ejecutar
            try {
                do {
                    System.out.println("\nOperaciones disponibles:\n1. Obtener datos (GET)\n2. Insertar datos (POST)\n3. Actualizar datos (PUT)\n4. Eliminar datos (DELETE)\n5. Salir");
                    System.out.print("\nSelecciona una operación: ");

                    int idOperacionSeleccionada = Utilidades.pedirNumeroSeleccionUsuario(scanner);

                    switch (idOperacionSeleccionada) {
                        // ! Código 404 (al intentar obtener un registro inexistente (not found))
                        // * Código 200 (se ha encontrado el registro (ok))
                        case 1:
                            isAccionCorrecta = true;
                            metodoEjecutado = METODO_GET;
                            Helper helper = HttpGet.ejecutarGet(httpCliente, tablaSeleccionada);
                            isList = helper.isList();
                            respuesta = helper.getHttpResponse();

                            break;

                        // ! Código 500 (no se puede añadir una canción con un artista inexistente)
                        // * Código 201 (se ha insertado correctamente (created))
                        case 2:
                            isAccionCorrecta = true;
                            metodoEjecutado = METODO_POST;
                            respuesta = HttpPost.ejecutarPost(httpCliente, tablaSeleccionada);

                            break;

                        // ! Código 412 (la url es distinta al id insertado (poco probable))
                        // * Código 204 (al actualizar no devuelve cuerpo, solo el código 204 (si el id no existe igualmente se insertará))
                        case 3:
                            isAccionCorrecta = true;
                            metodoEjecutado = METODO_PUT;
                            respuesta = HttpPut.ejecutarPut(httpCliente, tablaSeleccionada);

                            break;

                        // ! Código 412 (la url es distinta al id insertado (poco probable))
                        // ! Código 500 (no se puede eliminar un registro con claves ajenas 'activas')
                        // * Código 204 (al eliminar no devuelve cuerpo, solo el código 204 (si no existe no falla))
                        case 4:
                            isAccionCorrecta = true;
                            metodoEjecutado = METODO_DELETE;
                            respuesta = HttpDelete.ejecutarDelete(httpCliente, tablaSeleccionada);
                            break;

                        case 5:
                            System.out.println("\nAdiós");
                            return;

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
            } catch (ConnectException e) {
                System.err.println("\n¡Error! El cliente no se ha podido conectar. Por favor, contacta con el 'soporte técnico'");
                return;
            } catch (IOException | InterruptedException e) {
                System.err.println("\n" + e.getMessage());
                return;
            }

            // * Obtener código y cuerpo de respuesta
            int codigoRespuesta = respuesta.statusCode();
            String cuerpoRespuesta = respuesta.body();

            try {
                // * Gestionar código de respuesta
                gestionarCodigoRespuesta(metodoEjecutado, codigoRespuesta);

                // * Convertir cuerpo a objeto y mostrarlo
                convertirBodyToObject(cuerpoRespuesta, isList);

            } catch (ExceptionError404 | ExceptionError412 | ExceptionError500 e) {
                System.err.println("\n" + e.getMessage());
            } catch (Exception e) {
                System.err.println("\n" + e.getMessage());
            }

            try {
                Thread.sleep(50);
            } catch (InterruptedException e) {
                System.err.println("¡Error! Espera en selección de continuar fallida");
            }

            // * Pide al usuario si desea continuar
            continuar = pedirConfirmacionSalir(scanner);

        } while (continuar);
    }


    private static void obtenerTabla(Scanner scanner) {
        boolean isTablaCorrecta;
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
    }

    private static boolean pedirConfirmacionSalir(Scanner scanner) {
        System.out.println("\n¿Quieres continuar?:\n1. Sí\n2. No\n");
        System.out.print("Selecciona tu opción: ");

        boolean continuar;
        int opcionSalir = Utilidades.pedirNumeroSeleccionUsuario(scanner);

        switch (opcionSalir) {
            case 1:
                continuar = true;
                System.out.println();
                break;

            case 2:
                continuar = false;
                System.out.println("\nAdiós");
                break;

            default:
                continuar = false;
                System.err.println("\n¡Error! Opción seleccionada incorrecta.\nSaliendo del programa...");

        }
        return continuar;
    }

    /**
     * * 200 -> 1 | 201 -> 1 | 204 -> 2 (distintos mensajes)
     * ? 404 -> 1 | 412 -> 2 (mismo mensaje)
     * ! 500 -> 2 (distintos mensajes)
     * ! ConnectException (si está desconectado)
     */
    private static void gestionarCodigoRespuesta(String metodoEjecutado, int codigoRespuesta) throws Exception {
        switch (codigoRespuesta) {
            case 200:
                System.out.println("\nCódigo 200: Ok");
                break;
            case 201:
                System.out.println("\nCódigo 201: Objeto insertado correctamente");
                break;
            case 204:
                System.out.print("\nCódigo 204:");

                if (metodoEjecutado.equals(METODO_PUT)) {
                    System.out.println(" Objeto actualizado correctamente");
                }

                if (metodoEjecutado.equals(METODO_DELETE)) {
                    System.out.println(" Objeto eliminado correctamente");
                }

                break;

            // Excepciones de error
            case 404:
                throw new ExceptionError404();
            case 412:
                throw new ExceptionError412();
            case 500:
                if (metodoEjecutado.equals(METODO_POST)) {
                    throw new ExceptionError500("El ID del artista de la canción no existe.");
                }

                if (metodoEjecutado.equals(METODO_DELETE)) {
                    throw new ExceptionError500("No puedes eliminar un artista que tenga canciones registradas.");
                }

                break;

            default:
                System.err.println("¡Error no contemplado!");
                throw new Exception("Código " + codigoRespuesta + ": ¡Error no contemplado!");

        }
    }

    // TODO: Hacer método que controle los códigos de la respuesta y sus posibles excepciones

    // TODO: Recibir body de HttpResponse y convertirlo en el objeto correspondiente
    private static void convertirBodyToObject(String cuerpoRespuesta, boolean isList) {
        if (!cuerpoRespuesta.equals("")) {
            Gson gson = new GsonBuilder().create();
            String json = gson.toJson(JsonParser.parseString(cuerpoRespuesta));

            ObjectMapper objectMapper = new ObjectMapper();
            try {
                if (isList) {
                    switch (tablaSeleccionada) {
                        case TABLA_ARTISTAS:
                            List<Artista> participantJsonList = objectMapper.readValue(json, new TypeReference<List<Artista>>() {});
                            for (Artista artista : participantJsonList) {
                                System.out.println(artista);
                            }
                            break;

                        case TABLA_CANCIONES:
                            List<Cancion> participantJsonList1 = objectMapper.readValue(json, new TypeReference<List<Cancion>>() {});
                            for (Cancion artista : participantJsonList1) {
                                System.out.println(artista);
                            }
                            break;

                    }

                } else {
                    switch (tablaSeleccionada) {
                        case TABLA_ARTISTAS:
                            System.out.println(objectMapper.readValue(json, Artista.class));
                            break;

                        case TABLA_CANCIONES:
                            System.out.println(objectMapper.readValue(json, Cancion.class));
                            break;
                    }
                }

            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
        }
    }
}
