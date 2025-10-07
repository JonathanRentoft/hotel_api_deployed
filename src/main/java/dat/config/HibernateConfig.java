package dat.config;

import dat.entities.Hotel;
import dat.entities.Room;
import dat.security.entities.Role;
import dat.security.entities.User;
import jakarta.persistence.EntityManagerFactory;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;

import java.util.Properties;

public class HibernateConfig {

    private static EntityManagerFactory emf;
    private static boolean isTest = false; // Simpel boolean, da den kun er true/false

    public static EntityManagerFactory getEntityManagerFactory() {
        if (emf == null) {
            emf = createEMF();
        }
        return emf;
    }

    public static EntityManagerFactory getEntityManagerFactoryForTest() {
        isTest = true;
        if (emf == null) {
            emf = createEMF();
        }
        return emf;
    }

    private static EntityManagerFactory createEMF() {
        try {
            Configuration configuration = new Configuration();
            Properties props = new Properties();

            // Fælles properties
            props.put("hibernate.dialect", "org.hibernate.dialect.PostgreSQLDialect");
            props.put("hibernate.connection.driver_class", "org.postgresql.Driver");
            props.put("hibernate.current_session_context_class", "thread");
            props.put("hibernate.show_sql", "true");
            props.put("hibernate.format_sql", "true");
            props.put("hibernate.use_sql_comments", "true");

            // Håndtering af forskellige miljøer
            if (isTest) {
                // Test-database opsætning
                props.put("hibernate.connection.driver_class", "org.testcontainers.jdbc.ContainerDatabaseDriver");
                props.put("hibernate.connection.url", "jdbc:tc:postgresql:15.3-alpine3.18:///test_db");
                props.put("hibernate.hbm2ddl.auto", "create-drop");
            } else if (System.getenv("DEPLOYED") != null) {
                // Server (DigitalOcean) opsætning
                props.setProperty("hibernate.connection.url", System.getenv("CONNECTION_STR"));
                props.setProperty("hibernate.connection.username", System.getenv("DB_USERNAME"));
                props.setProperty("hibernate.connection.password", System.getenv("DB_PASSWORD"));
                props.put("hibernate.hbm2ddl.auto", "update");
            } else {
                // Lokal udviklings-opsætning
                props.put("hibernate.connection.url", "jdbc:postgresql://localhost:5432/hotel");
                props.put("hibernate.connection.username", "postgres");
                props.put("hibernate.connection.password", "postgres");
                props.put("hibernate.hbm2ddl.auto", "update");
            }

            configuration.setProperties(props);

            // Tilføj dine entity-klasser
            configuration.addAnnotatedClass(Hotel.class);
            configuration.addAnnotatedClass(Room.class);
            configuration.addAnnotatedClass(User.class);
            configuration.addAnnotatedClass(Role.class);

            ServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder()
                    .applySettings(configuration.getProperties())
                    .build();
            SessionFactory sf = configuration.buildSessionFactory(serviceRegistry);
            return sf.unwrap(EntityManagerFactory.class);
        } catch (Throwable ex) {
            System.err.println("Initial SessionFactory creation failed." + ex);
            throw new ExceptionInInitializerError(ex);
        }
    }
}