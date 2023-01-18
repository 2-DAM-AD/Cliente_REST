package es.batoi.bfg.modelos;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.Objects;

/**
 * {@code @JsonInclude} 'Include.NOT_NULL' Indica que no tenga en cuenta valores nulos a la hora de hacer la conversión desde el format JSON
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Cancion {
    private Integer id;
    private String nombre;
    private String album;
    private Integer idArtista;


    public Cancion() {
    }


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getAlbum() {
        return album;
    }

    public void setAlbum(String album) {
        this.album = album;
    }

    public Integer getIdArtista() {
        return idArtista;
    }

    public void setIdArtista(Integer idArtista) {
        this.idArtista = idArtista;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Cancion cancion = (Cancion) o;
        return Objects.equals(id, cancion.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }


    @Override
    public String toString() {
        return "Cancion {" +
                "id=" + id +
                ", nombre='" + nombre + '\'' +
                ", album='" + album + '\'' +
                ", idArtista=" + idArtista +
                '}';
    }
}
