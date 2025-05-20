package entidades;

import java.io.Serializable;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name="cursos")
public class Curso implements Serializable{

	private static final long serialVersionUID = 1L;
	
	@Id
	@Column(name="id_curso")
	private int idCurso;
	
	@Column(name="nombre_curso")
	private String nombreCurso;

	public Curso() {
	}
	
	public Curso(int idCurso, String nombreCurso) {
		this.idCurso = idCurso;
		this.nombreCurso = nombreCurso;
	}
	
	public void setIdCurso(int idCurso) {
		this.idCurso = idCurso;
	}
	
	public int getIdCurso() {
		return idCurso;
	}
	
	public void setNombreCurso(String nombreCurso) {
		this.nombreCurso = nombreCurso;
	}
	
	public String getNombreCurso() {
		return nombreCurso;
	}
	
	@Override
	public String toString() {
		return this.idCurso + " - " + this.nombreCurso;
	}

}
