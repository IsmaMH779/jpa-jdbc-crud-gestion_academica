package dao;

import java.util.Collection;
import entidades.Alumno;
import entidades.Curso;
import entidades.Matricula;

public interface AcademiaDAO {
	
	public Collection<Alumno> cargarAlumnos();
	public Alumno getAlumno(int idAlumno);	
	public int grabarAlumno(Alumno alumno);
	public int actualizarAlumno(Alumno alumno);
	public int borrarAlumno(int idAlumno);
	
	public Collection<Curso> cargarCursos();
	public Curso getCurso(int idCurso);
	public int grabarCurso(Curso curso);
	public int actualizarCurso(Curso curso);
	public int borrarCurso(int idCurso);
	
	public Collection<Matricula> cargarMatriculas();
	public long getIdMatricula(int idAlumno, int idCurso);
	public Matricula getMatricula(long idMatricula);
	public int grabarMatricula(Matricula matricula);
	public int actualizarMatricula(Matricula matricula);
	public int borrarMatricula(long idMatricula);
		
}
