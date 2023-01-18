package es.batoi.bfg.modelos;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.Objects;

/**
 * {@code @JsonInclude} 'Include.NOT_NULL' Indica que no tenga en cuenta valores nulos a la hora de hacer la conversión desde el format JSON
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Artista {
    private Integer id;
    private String nombre;
    private Integer anyo_creacion;


    public Artista() {
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

    public Integer getAnyo_creacion() {
        return anyo_creacion;
    }

    public void setAnyo_creacion(Integer anyo_creacion) {
        this.anyo_creacion = anyo_creacion;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Artista artista = (Artista) o;
        return Objects.equals(id, artista.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }


    @Override
    public String toString() {
        return "Artista {" +
                "id=" + id +
                ", nombre='" + nombre + '\'' +
                ", anyo_creacion=" + anyo_creacion +
                '}';
    }
}
