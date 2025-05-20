package test;

import java.util.Collection;

import Factory.PersistenceFactory;
import dao.AcademiaDAO;
import dao.AcademiaDAOImplJDBC;
import entidades.Alumno;
import entidades.Curso;
import entidades.Matricula;

public class BorrarHelper {
    
    private AcademiaDAO dao = null;

    // Constructor
    public BorrarHelper() {
        System.out.println("Creando el DAO...");
        dao = PersistenceFactory.createDAO();
    }

    private void borrarMatriculas() {
        System.out.println("\nBorrando cualquier matricula previa...");
        
        Collection<Matricula> matriculas = dao.cargarMatriculas();
        
        for (Matricula matricula : matriculas) {
            if (dao.borrarMatricula(matricula.getIdMatricula()) == 1) {
                System.out.println("Se ha borrado la matricula con ID: " + matricula.getIdMatricula());
            } else {
                System.out.println("Error al borrar la matricula con ID: " + matricula.getIdMatricula());
            }
        }
    }

    private void borrarAlumnos() {
        System.out.println("\nBorrando cualquier alumno previo...");
        
        Collection<Alumno> alumnos = dao.cargarAlumnos();
        for (Alumno alumno : alumnos) {
            if (dao.borrarAlumno(alumno.getIdAlumno()) == 1) {
                System.out.println("Se ha borrado el alumno con ID: " + alumno.getIdAlumno());
            } else {
                System.out.println("Error al borrar el alumno con ID: " + alumno.getIdAlumno());
            }
        }
    }

    private void borrarCursos() {
        System.out.println("\nBorrando cualquier curso previo...");
        
        Collection<Curso> cursos = dao.cargarCursos();
        for (Curso curso : cursos) {
            if (dao.borrarCurso(curso.getIdCurso()) == 1) {
                System.out.println("Se ha borrado el curso con ID: " + curso.getIdCurso());
            } else {
                System.out.println("Error al borrar el curso con ID: " + curso.getIdCurso());
            }
        }
    }

    public static void main(String[] args) {
        BorrarHelper programa = new BorrarHelper();

        programa.borrarMatriculas();
        programa.borrarAlumnos();
        programa.borrarCursos();

        System.out.println("\nFin del programa.");
    }
}
