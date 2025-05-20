package entidades;

import java.io.Serializable;
import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name="matriculas")

public class Matricula implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	@Id
	@Column(name="id_matricula")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int idMatricula;
	
	@Column(name="id_alumno")
	private int alumnoID;
	
	@Column(name="id_curso")
	private int cursoID;
	
	@Column(name="fecha_inicio")
	private LocalDate fecha;
	
	public Matricula() {
	}
	
	public Matricula(int idMatricula, int alumnoID, int cursoID, LocalDate fecha) {
		this.idMatricula = idMatricula;
		this.alumnoID = alumnoID;
		this.cursoID = cursoID;
		this.fecha = fecha;
	}
	
	public Matricula(int alumnoID, int cursoID, LocalDate fecha) {
		this.alumnoID = alumnoID;
		this.cursoID = cursoID;
		this.fecha = fecha;
	}

	public int getIdMatricula() {
		return idMatricula;
	}

	public void setIdMatricula(int idMatricula) {
		this.idMatricula = idMatricula;
	}

	public int getAlumnoID() {
		return alumnoID;
	}

	public void setAlumnoID(int alumnoID) {
		this.alumnoID = alumnoID;
	}

	public int getCursoID() {
		return cursoID;
	}

	public void setCursoID(int cursoID) {
		this.cursoID = cursoID;
	}

	public LocalDate getFecha() {
		return fecha;
	}

	public void setFecha(LocalDate fecha) {
		this.fecha = fecha;
	}

	@Override
	public String toString() {
		return this.idMatricula + " - " + this.alumnoID + " - " + this.cursoID + " - " + this.fecha;
	}
		
}
