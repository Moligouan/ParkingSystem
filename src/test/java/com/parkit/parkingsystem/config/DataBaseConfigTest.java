package com.parkit.parkingsystem.config;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*; // Pour assertDoesNotThrow, assertNotNull, etc.
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
public class DataBaseConfigTest {
    @Mock
    private Connection mockConnection;
    @Mock
    private PreparedStatement mockPreparedStatement;
    @Mock
    private ResultSet mockResultSet;

    private DataBaseConfig dataBaseConfig;

    @BeforeEach
    public void setUp() {
        dataBaseConfig = new DataBaseConfig();
    }

    @Test
    public void testGetConnection() throws ClassNotFoundException, SQLException {
        // Comme getConnection() utilise une connexion réelle, ce test vérifiera uniquement que la méthode ne lance pas d'exception
        assertDoesNotThrow(() -> {
            Connection connection = dataBaseConfig.getConnection();
            assertNotNull(connection); // La connexion ne doit pas être null si elle est créée avec succès
            connection.close(); // Fermer la connexion après test
        });
    }

    @Test
    public void testCloseConnection() throws SQLException {
        doNothing().when(mockConnection).close();
        dataBaseConfig.closeConnection(mockConnection);
        verify(mockConnection, times(1)).close();
    }

    @Test
    public void testCloseConnectionWithNull() {
        // Appel de la méthode avec une connexion null pour vérifier qu'aucune exception n'est lancée
        assertDoesNotThrow(() -> dataBaseConfig.closeConnection(null));
    }

    @Test
    public void testClosePreparedStatement() throws SQLException {
        // Simuler un PreparedStatement qui peut être fermé
        doNothing().when(mockPreparedStatement).close();

        // Appel de la méthode
        dataBaseConfig.closePreparedStatement(mockPreparedStatement);

        // Vérification que la méthode close() est appelée une fois
        verify(mockPreparedStatement, times(1)).close();
    }

    @Test
    public void testClosePreparedStatementWithNull() {
        // Appel de la méthode avec un PreparedStatement null pour vérifier qu'aucune exception n'est lancée
        assertDoesNotThrow(() -> dataBaseConfig.closePreparedStatement(null));
    }

    @Test
    public void testCloseResultSet() throws SQLException {
        // Simuler un ResultSet qui peut être fermé
        doNothing().when(mockResultSet).close();

        // Appel de la méthode
        dataBaseConfig.closeResultSet(mockResultSet);

        // Vérification que la méthode close() est appelée une fois
        verify(mockResultSet, times(1)).close();
    }

    @Test
    public void testCloseResultSetWithNull() {
        // Appel de la méthode avec un ResultSet null pour vérifier qu'aucune exception n'est lancée
        assertDoesNotThrow(() -> dataBaseConfig.closeResultSet(null));
    }

    @Test
    public void testCloseConnectionWithException() throws SQLException {
        // Simuler une exception lorsque close() est appelé
        doThrow(new SQLException("Error while closing connection")).when(mockConnection).close();

        // Appeler la méthode
        dataBaseConfig.closeConnection(mockConnection);

        // Vérifier que la méthode close() a été appelée malgré l'exception
        verify(mockConnection, times(1)).close();
    }

    @Test
    public void testClosePreparedStatementWithException() throws SQLException {
        // Simuler une exception lorsque close() est appelé
        doThrow(new SQLException("Error while closing prepared statement")).when(mockPreparedStatement).close();

        // Appeler la méthode
        dataBaseConfig.closePreparedStatement(mockPreparedStatement);

        // Vérifier que la méthode close() a été appelée malgré l'exception
        verify(mockPreparedStatement, times(1)).close();
    }

    @Test
    public void testCloseResultSetWithException() throws SQLException {
        // Simuler une exception lorsque close() est appelé
        doThrow(new SQLException("Error while closing result set")).when(mockResultSet).close();

        // Appeler la méthode
        dataBaseConfig.closeResultSet(mockResultSet);

        // Vérifier que la méthode close() a été appelée malgré l'exception
        verify(mockResultSet, times(1)).close();
    }

}
