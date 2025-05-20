package Factory;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import dao.AcademiaDAO;
import dao.AcademiaDAOImplJDBC;
import dao.AcademiaDAOImplJPA;

public class PersistenceFactory {
    public static AcademiaDAO createDAO() {
        String persistenceType = null;

        // Cargar el properties
        try (InputStream input = PersistenceFactory.class.getClassLoader().getResourceAsStream("application.properties")) {
            if (input == null) {
                throw new RuntimeException("No se encontró el archivo application.properties");
            }
            Properties props = new Properties();
            
            props.load(input);
            persistenceType = props.getProperty("persistence.type");
        } catch (IOException e) {
            throw new RuntimeException("Error al cargar application.properties", e);
        }

        // Seleccionar el metodo de persitencia
        if ("jdbc".equalsIgnoreCase(persistenceType)) {
        	System.out.println("Metodo de persistencia: " + persistenceType);
            return new AcademiaDAOImplJDBC();
        } else if ("jpa".equalsIgnoreCase(persistenceType)) {
        	System.out.println("Metodo de persistencia: " + persistenceType);
            return new AcademiaDAOImplJPA();
        } else {
            throw new IllegalArgumentException("Tipo de persistencia no soportado: " + persistenceType);
        }
    }
}
