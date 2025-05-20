package dao;

import java.io.ByteArrayInputStream;
import java.sql.Blob;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;

import entidades.Alumno;
import entidades.Curso;
import entidades.Matricula;

public class AcademiaDAOImplJDBC implements AcademiaDAO {
	// Cadena de conexión predeterminada
		private String URLConexion = new String("jdbc:mysql://172.16.0.11:3306/dbformacion");
		private String username = "dam2a";
		private String password = "secreto";
		/*
		 * SQL
		 */
		
		private static final String FIND_ALL_ALUMNOS_SQL = "SELECT id_alumno, nombre_alumno, foto FROM alumnos";
	    private static final String ADD_ALUMNO_SQL = "INSERT INTO alumnos (id_alumno, nombre_alumno, foto) VALUES (?,?,?)";
	    private static final String UPDATE_ALUMNO_SQL = "UPDATE alumnos SET nombre_alumno=?, foto=? WHERE id_alumno=?";
	    private static final String GET_ALUMNO_SQL = "SELECT id_alumno, nombre_alumno, foto FROM alumnos WHERE id_alumno = ?";
	    private static final String DELETE_ALUMNO_SQL = "DELETE FROM alumnos WHERE id_alumno = ?";
	    
	    private static final String FIND_ALL_CURSOS_SQL = "SELECT * FROM cursos";
	    private static final String GET_CURSO_SQL = "SELECT * FROM cursos WHERE id_curso = ?";
	    private static final String ADD_CURSO_SQL = "INSERT INTO cursos (id_curso, nombre_curso) VALUES (?,?)";
	    private static final String UPDATE_CURSO_SQL = "UPDATE cursos SET nombre_curso = ? WHERE id_curso = ?";
	    private static final String DELETE_CURSO_SQL = "DELETE FROM cursos WHERE id_curso = ?";
	    
	    private static final String FIND_ALL_MATRICULAS_SQL = "SELECT * FROM matriculas";
	    private static final String GET_ID_MATRICULA_SQL = "SELECT id_matricula FROM matriculas WHERE id_alumno = ? AND id_curso = ?";
	    private static final String GET_MATRICULA_SQL = "SELECT * FROM matriculas WHERE id_matricula = ?";
	    private static final String ADD_MATRICULA_SQL = "INSERT INTO matriculas (id_alumno, id_curso, fecha_inicio) VALUES (?,?,?)";
	    private static final String UPDATE_MATRICULA_SQL = "UPDATE matriculas SET id_alumno = ?, id_curso = ?, fecha_inicio = ? WHERE id_matricula = ?";
	    private static final String DELETE_MATRICULA_SQL = "DELETE FROM matriculas WHERE id_matricula = ?";
		
	/*
		 * CONSTRUCTORES
		 */
		public AcademiaDAOImplJDBC() { }
		
		// Sobrecargamos el método por si queremos 
		// machacar la cadena de conexión 
		public AcademiaDAOImplJDBC(String URLConexion) {	
			this.URLConexion=URLConexion;
		}
		
		
		/*
		 * OPERACIONES GENERALES
		 */
		
		// Obtener la conexión
		private Connection getConnection() throws SQLException {
			return DriverManager.getConnection(URLConexion, username, password);
		}

		// Liberar la conexión
		private void releaseConnection(Connection con) {
			if (con != null) {
				try {
					con.close();
					con = null;
				} catch (SQLException e) {
					for (Throwable t : e) {
						System.err.println("Error: " + t);
					}
				}
			}
		}
		
		/*
		 * OPERACIONES ALUMNO
		 */
		@Override
		public Collection<Alumno> cargarAlumnos() {
			Collection<Alumno> alumnos = new ArrayList<Alumno>();
			Connection con = null;
			try {
				con = getConnection();
				PreparedStatement ps = con.prepareStatement(FIND_ALL_ALUMNOS_SQL);
				ResultSet rs = ps.executeQuery();			
				while (rs.next()) {
					int id= rs.getInt(1);				
					String nombre = (rs.getString(2)!=null?rs.getString(2):"sin nombre");
					
					Blob foto = rs.getBlob(3);
					
					Alumno alumno = new Alumno(id,nombre);
					
					if (foto!=null) {
						alumno.setFoto(foto.getBytes(1L, (int) foto.length()));
					}	
					else {
						alumno.setFoto(null);
					}
					
					alumnos.add(alumno);
				}
				rs.close();
				ps.close();
			} catch (SQLException e) {
				for (Throwable t : e) {
					System.err.println("Error: " + t);
				}
			} finally {
				releaseConnection(con);
			}
			return alumnos;
		}

		@Override
		public Alumno getAlumno(int idAlumno) {
			Alumno alumno = null;

			try (PreparedStatement ps = getConnection().prepareStatement(GET_ALUMNO_SQL)){
				
				ps.setInt(1, idAlumno);
				
				ResultSet res = ps.executeQuery();
				
				if (res.next()) {
					alumno = new Alumno(res.getInt("id_alumno"), res.getString("nombre_alumno"));
					Blob foto =res.getBlob(3);
					
					if (foto!=null)
						alumno.setFoto(foto.getBytes(1L, (int)foto.length()));
					else
						alumno.setFoto(null);
				} 
					
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} finally {
				try {
					releaseConnection(getConnection());
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			
			return alumno;
		}

		@Override
		public int grabarAlumno(Alumno alumno) {
			int changed = 0;
			try (PreparedStatement ps = getConnection().prepareStatement(ADD_ALUMNO_SQL)){
				
				ps.setInt(1, alumno.getIdAlumno());
				ps.setString(2, alumno.getNombreAlumno());
				
				
				if (alumno.getFoto()!=null) {
					ps.setBinaryStream(3,new ByteArrayInputStream(alumno.getFoto()));
				} else {
					ps.setBinaryStream(3, null);
				}
				
				changed = ps.executeUpdate();
				
			} catch (SQLException e) {
				e.printStackTrace();
			} finally {
				try {
					releaseConnection(getConnection());
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			
			return changed;
		}

		@Override
		public int actualizarAlumno(Alumno alumno) {
			int changed = 0;

			try (PreparedStatement ps = getConnection().prepareStatement(UPDATE_ALUMNO_SQL)){
				
				ps.setString(1, alumno.getNombreAlumno());
				
				if (alumno.getFoto() != null) {
					ps.setBinaryStream(2, new ByteArrayInputStream(alumno.getFoto()));
				} else {
					ps.setBinaryStream(2, null);
				}
				
				ps.setInt(3, alumno.getIdAlumno());
				
				changed = ps.executeUpdate();
			
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} finally {
				try {
					releaseConnection(getConnection());
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			
			return changed;
		}

		@Override
		public int borrarAlumno(int idAlumno) {
			int changed = 0;

			try (PreparedStatement ps = getConnection().prepareStatement(DELETE_ALUMNO_SQL)){
				
				ps.setInt(1, idAlumno);
				
				changed = ps.executeUpdate();
				
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} finally {
				try {
					releaseConnection(getConnection());
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			
			return changed;
		}
		
		@Override
		public Collection<Curso> cargarCursos() {
			Collection<Curso> cursos = new ArrayList<Curso>();
			
			try (PreparedStatement ps = getConnection().prepareStatement(FIND_ALL_CURSOS_SQL);) {

				ResultSet rs = ps.executeQuery();	
				
				while (rs.next()) {
					int id= rs.getInt(1);				
					String nombre = 
						(rs.getString(2)!=null?rs.getString(2):"sin nombre");
					
					cursos.add(new Curso(id,nombre));
				}
			} catch (SQLException e) {
				for (Throwable t : e) {
					System.err.println("Error: " + t);
				}
			} finally {
				try {
					releaseConnection(getConnection());
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			
			return cursos;
		}

		@Override
		public Curso getCurso(int idCurso) {
			Curso curso = null;

			try (PreparedStatement ps = getConnection().prepareStatement(GET_CURSO_SQL)){
				
				ps.setInt(1, idCurso);
				
				ResultSet res = ps.executeQuery();
				
				if (res.next()) {
					curso = new Curso(res.getInt("id_curso"), res.getString("nombre_curso"));	
				}
				
				
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} finally {
				try {
					releaseConnection(getConnection());
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			
			return curso;
		}

		@Override
		public int grabarCurso(Curso curso) {
			int changed = 0;

			try (PreparedStatement ps = getConnection().prepareStatement(ADD_CURSO_SQL)){
				
				ps.setInt(1, curso.getIdCurso());
				ps.setString(2, curso.getNombreCurso());
				
				changed = ps.executeUpdate();
				
			} catch (SQLException e) {
				e.printStackTrace();
			} finally {
				try {
					releaseConnection(getConnection());
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			
			return changed;
		}

		@Override
		public int actualizarCurso(Curso curso) {
			int changed = 0;

			try (PreparedStatement ps = getConnection().prepareStatement(UPDATE_CURSO_SQL)){
				
				ps.setString(1, curso.getNombreCurso());
				ps.setInt(2, curso.getIdCurso());
				
				changed = ps.executeUpdate();
				
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} finally {
				try {
					releaseConnection(getConnection());
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			
			return changed;
		}

		@Override
		public int borrarCurso(int idCurso) {
			int changed = 0;

			try (PreparedStatement ps = getConnection().prepareStatement(DELETE_CURSO_SQL)){
				
				ps.setInt(1, idCurso);
				
				changed = ps.executeUpdate();
				
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} finally {
				try {
					releaseConnection(getConnection());
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			
			return changed;
		}
		
		@Override
		public Collection<Matricula> cargarMatriculas() {
			Collection<Matricula> matriculas = new ArrayList<Matricula>();
			
			try (PreparedStatement ps = getConnection().prepareStatement(FIND_ALL_MATRICULAS_SQL);) {

				ResultSet rs = ps.executeQuery();	
				
				while (rs.next()) {
					int id= rs.getInt(1);				
					int alumnoId = rs.getInt(2);
					int cursoId = rs.getInt(3);
					LocalDate fecha = rs.getDate(4).toLocalDate();
					
					matriculas.add(new Matricula(id,alumnoId, cursoId, fecha));
				}
			} catch (SQLException e) {
				for (Throwable t : e) {
					System.err.println("Error: " + t);
				}
			} finally {
				try {
					releaseConnection(getConnection());
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			
			return matriculas;
		}

		@Override
		public long getIdMatricula(int idAlumno, int idCurso) {
			long matricula = 0;

			try (PreparedStatement ps = getConnection().prepareStatement(GET_ID_MATRICULA_SQL)){
				
				ps.setInt(1, idAlumno);
				ps.setInt(2, idCurso);
				
				ResultSet res = ps.executeQuery();
				
				if (res.next()) {
					matricula = res.getLong("id_matricula");
				}
				
				
				
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} finally {
				try {
					releaseConnection(getConnection());
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			
			return matricula;
		}

		@Override
		public Matricula getMatricula(long idMatricula) {
			Matricula matricula = null;

			try (PreparedStatement ps = getConnection().prepareStatement(GET_MATRICULA_SQL)){
				
				ps.setLong(1, idMatricula);
				
				ResultSet res = ps.executeQuery();
				
				if (res.next()) {
					matricula = new Matricula(res.getInt("id_matricula"), res.getInt("id_alumno"), res.getInt("id_curso"), res.getDate("fecha_inicio").toLocalDate());
				}
				
				
			} catch (SQLException e) {
				e.printStackTrace();
			} finally {
				try {
					releaseConnection(getConnection());
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			
			return matricula;
		}

		@Override
		public int grabarMatricula(Matricula matricula) {
			int changed = 0;

			try (PreparedStatement ps = getConnection().prepareStatement(ADD_MATRICULA_SQL)){
				
				ps.setInt(1, matricula.getAlumnoID());
				ps.setInt(2, matricula.getCursoID());
				ps.setDate(3, java.sql.Date.valueOf(matricula.getFecha()));
				
				changed = ps.executeUpdate();
				
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} finally {
				try {
					releaseConnection(getConnection());
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			
			return changed;
		}

		@Override
		public int actualizarMatricula(Matricula matricula) {
			int changed = 0;

			try (PreparedStatement ps = getConnection().prepareStatement(UPDATE_MATRICULA_SQL)){
				
				ps.setInt(1, matricula.getAlumnoID());
				ps.setInt(2, matricula.getCursoID());
				ps.setDate(3, java.sql.Date.valueOf(matricula.getFecha()));
				ps.setInt(4, matricula.getIdMatricula());
				
				changed = ps.executeUpdate();
				
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} finally {
				try {
					releaseConnection(getConnection());
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			
			return changed;
		}

		@Override
		public int borrarMatricula(long idMatricula) {
			int changed = 0;

			try (PreparedStatement ps = getConnection().prepareStatement(DELETE_MATRICULA_SQL)){
				
				ps.setLong(1, idMatricula);
				
				changed = ps.executeUpdate();
				
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} finally {
				try {
					releaseConnection(getConnection());
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			
			return changed;
		}
		

}
