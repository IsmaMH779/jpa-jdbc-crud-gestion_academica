package dao;

import java.util.Collection;
import java.util.List;

import entidades.Alumno;
import entidades.Curso;
import entidades.Matricula;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.Persistence;

public class AcademiaDAOImplJPA implements AcademiaDAO {

    private static final String PERSISTENCE_UNIT = "Academia";
    private EntityManager em;

    public AcademiaDAOImplJPA() {
        em = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT).createEntityManager();
    }

    // ALUMNO

    @Override
    public Collection<Alumno> cargarAlumnos() {
        TypedQuery<Alumno> q = em.createQuery(
            "SELECT a FROM Alumno a", Alumno.class
        );
        return q.getResultList();
    }

    @Override
    public Alumno getAlumno(int idAlumno) {
        return em.find(Alumno.class, idAlumno);
    }

    @Override
    public int grabarAlumno(Alumno alumno) {
        try {
            em.getTransaction().begin();
            em.persist(alumno);
            em.getTransaction().commit();
            return 1;
        } catch (Exception e) {
            if (em.getTransaction().isActive()) em.getTransaction().rollback();
            return 0;
        }
    }

    @Override
    public int actualizarAlumno(Alumno alumno) {
        try {
            em.getTransaction().begin();
            em.merge(alumno);
            em.getTransaction().commit();
            return 1;
        } catch (Exception e) {
            if (em.getTransaction().isActive()) em.getTransaction().rollback();
            return 0;
        }
    }

    @Override
    public int borrarAlumno(int idAlumno) {
        try {
            em.getTransaction().begin();
            Alumno a = em.find(Alumno.class, idAlumno);
            if (a != null) em.remove(a);
            em.getTransaction().commit();
            return (a != null ? 1 : 0);
        } catch (Exception e) {
            if (em.getTransaction().isActive()) em.getTransaction().rollback();
            return 0;
        }
    }

    // CURSO

    @Override
    public Collection<Curso> cargarCursos() {
        TypedQuery<Curso> q = em.createQuery(
            "SELECT c FROM Curso c", Curso.class
        );
        return q.getResultList();
    }

    @Override
    public Curso getCurso(int idCurso) {
        return em.find(Curso.class, idCurso);
    }

    @Override
    public int grabarCurso(Curso curso) {
        try {
            em.getTransaction().begin();
            em.persist(curso);
            em.getTransaction().commit();
            return 1;
        } catch (Exception e) {
            if (em.getTransaction().isActive()) em.getTransaction().rollback();
            return 0;
        }
    }

    @Override
    public int actualizarCurso(Curso curso) {
        try {
            em.getTransaction().begin();
            em.merge(curso);
            em.getTransaction().commit();
            return 1;
        } catch (Exception e) {
            if (em.getTransaction().isActive()) em.getTransaction().rollback();
            return 0;
        }
    }

    @Override
    public int borrarCurso(int idCurso) {
        try {
            em.getTransaction().begin();
            Curso c = em.find(Curso.class, idCurso);
            if (c != null) em.remove(c);
            em.getTransaction().commit();
            return (c != null ? 1 : 0);
        } catch (Exception e) {
            if (em.getTransaction().isActive()) em.getTransaction().rollback();
            return 0;
        }
    }

    // MATRICULA

    @Override
    public Collection<Matricula> cargarMatriculas() {
        TypedQuery<Matricula> q = em.createQuery(
            "SELECT m FROM Matricula m", Matricula.class
        );
        return q.getResultList();
    }

    @Override
    public long getIdMatricula(int idAlumno, int idCurso) {
        TypedQuery<Number> q = em.createQuery(
            "SELECT m.idMatricula FROM Matricula m WHERE m.alumnoID = :aid AND m.cursoID = :cid",
            Number.class
        );
        q.setParameter("aid", idAlumno);
        q.setParameter("cid", idCurso);
        List<Number> list = q.getResultList();
        return list.isEmpty() 
             ? 0L 
             : list.get(0).longValue();
    }


    @Override
    public Matricula getMatricula(long idMatricula) {
    	int id = (int) idMatricula;
        return em.find(Matricula.class, id);
    }

    @Override
    public int grabarMatricula(Matricula matricula) {
        try {
            em.getTransaction().begin();
            em.persist(matricula);
            em.getTransaction().commit();
            return 1;
        } catch (Exception e) {
            if (em.getTransaction().isActive()) em.getTransaction().rollback();
            return 0;
        }
    }

    @Override
    public int actualizarMatricula(Matricula matricula) {
        try {
            em.getTransaction().begin();
            em.merge(matricula);
            em.getTransaction().commit();
            return 1;
        } catch (Exception e) {
            if (em.getTransaction().isActive()) em.getTransaction().rollback();
            return 0;
        }
    }

    @Override
    public int borrarMatricula(long idMatricula) {
        try {
            em.getTransaction().begin();
            Matricula m = em.find(Matricula.class, idMatricula);
            if (m != null) em.remove(m);
            em.getTransaction().commit();
            return (m != null ? 1 : 0);
        } catch (Exception e) {
            if (em.getTransaction().isActive()) em.getTransaction().rollback();
            return 0;
        }
    }
}
