package com.parkit.parkingsystem.dao;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import com.parkit.parkingsystem.config.DataBaseConfig;
import com.parkit.parkingsystem.constants.DBConstants;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

public class FideliteDAOTest {

    private FideliteDAO fideliteDAO;
    private DataBaseConfig dataBaseConfig;
    private Connection connection;
    private PreparedStatement preparedStatement;
    private ResultSet resultSet;

    @BeforeEach
    public void setUp() throws Exception {
        dataBaseConfig = mock(DataBaseConfig.class);
        connection = mock(Connection.class);
        preparedStatement = mock(PreparedStatement.class);
        resultSet = mock(ResultSet.class);

        fideliteDAO = new FideliteDAO();
        fideliteDAO.dataBaseConfig = dataBaseConfig;

        when(dataBaseConfig.getConnection()).thenReturn(connection);
    }

    @Test
    public void testGetFideliteSuccess() throws Exception {
        // Simuler la préparation de la requête et l'exécution du ResultSet
        when(connection.prepareStatement(DBConstants.GET_REGULAR)).thenReturn(preparedStatement);
        when(preparedStatement.executeQuery()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(true);
        when(resultSet.getInt(1)).thenReturn(5);

        // Appel de la méthode
        int result = fideliteDAO.getFidelite("ABC123");

        // Vérifications
        assertEquals(5, result);
        verify(preparedStatement, times(1)).setString(1, "ABC123");
        verify(resultSet, times(1)).getInt(1);
        verify(dataBaseConfig, times(1)).closeResultSet(resultSet);
        verify(dataBaseConfig, times(1)).closePreparedStatement(preparedStatement);
        verify(dataBaseConfig, times(1)).closeConnection(connection);
    }

    @Test
    public void testGetFideliteNoResult() throws Exception {
        // Simuler un ResultSet vide
        when(connection.prepareStatement(DBConstants.GET_REGULAR)).thenReturn(preparedStatement);
        when(preparedStatement.executeQuery()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(false);

        // Appel de la méthode
        int result = fideliteDAO.getFidelite("XYZ789");

        // Vérification du résultat lorsqu'il n'y a pas de données
        assertEquals(-1, result);
        verify(preparedStatement, times(1)).setString(1, "XYZ789");
        verify(resultSet, times(1)).next();
        verify(dataBaseConfig, times(1)).closeResultSet(resultSet);
        verify(dataBaseConfig, times(1)).closePreparedStatement(preparedStatement);
        verify(dataBaseConfig, times(1)).closeConnection(connection);
    }

    @Test
    public void testGetFideliteExceptionHandling() throws Exception {
        // Simuler une exception lors de la connexion
        when(dataBaseConfig.getConnection()).thenThrow(new SQLException("Connection failed"));

        // Appel de la méthode
        int result = fideliteDAO.getFidelite("ABC123");

        // Vérification que le résultat est -1 en cas d'exception
        assertEquals(-1, result);
        verify(dataBaseConfig, times(1)).closeConnection(null);
    }
}
