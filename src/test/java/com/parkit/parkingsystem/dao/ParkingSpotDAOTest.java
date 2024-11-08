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
import com.parkit.parkingsystem.constants.ParkingType;
import com.parkit.parkingsystem.model.ParkingSpot;



import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

public class ParkingSpotDAOTest {

    private ParkingSpotDAO parkingSpotDAO;
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

        parkingSpotDAO = new ParkingSpotDAO();
        parkingSpotDAO.dataBaseConfig = dataBaseConfig;

        when(dataBaseConfig.getConnection()).thenReturn(connection);
    }

    @Test
    public void testGetNextAvailableSlotSuccess() throws Exception {
        // Simuler la préparation de la requête et l'exécution du ResultSet
        when(connection.prepareStatement(DBConstants.GET_NEXT_PARKING_SPOT)).thenReturn(preparedStatement);
        when(preparedStatement.executeQuery()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(true);
        when(resultSet.getInt(1)).thenReturn(1);

        // Appel de la méthode
        int result = parkingSpotDAO.getNextAvailableSlot(ParkingType.CAR);

        // Vérifications
        assertEquals(1, result);
        verify(preparedStatement, times(1)).setString(1, ParkingType.CAR.toString());
        verify(resultSet, times(1)).getInt(1);
        verify(dataBaseConfig, times(1)).closeResultSet(resultSet);
        verify(dataBaseConfig, times(1)).closePreparedStatement(preparedStatement);
        verify(dataBaseConfig, times(1)).closeConnection(connection);
    }

    @Test
    public void testGetNextAvailableSlotExceptionHandling() throws Exception {
        // Simuler une exception lors de la connexion
        when(dataBaseConfig.getConnection()).thenThrow(new SQLException("Connection failed"));

        // Appel de la méthode
        int result = parkingSpotDAO.getNextAvailableSlot(ParkingType.CAR);

        // Vérification du résultat
        assertEquals(-1, result);
        verify(dataBaseConfig, times(1)).closeConnection(null);
    }

    @Test
    public void testUpdateParkingSuccess() throws Exception {
        // Création d'un ParkingSpot de test
        ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR, true);

        // Simuler la préparation de la requête et l'exécution
        when(connection.prepareStatement(DBConstants.UPDATE_PARKING_SPOT)).thenReturn(preparedStatement);
        when(preparedStatement.executeUpdate()).thenReturn(1);

        // Appel de la méthode
        boolean result = parkingSpotDAO.updateParking(parkingSpot);

        // Vérifications
        assertTrue(result);
        verify(preparedStatement, times(1)).setBoolean(1, parkingSpot.isAvailable());
        verify(preparedStatement, times(1)).setInt(2, parkingSpot.getId());
        verify(preparedStatement, times(1)).executeUpdate();
        verify(dataBaseConfig, times(1)).closePreparedStatement(preparedStatement);
        verify(dataBaseConfig, times(1)).closeConnection(connection);
    }

    @Test
    public void testUpdateParkingFailure() throws Exception {
        // Création d'un ParkingSpot de test
        ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR, false);

        // Simuler une mise à jour échouée
        when(connection.prepareStatement(DBConstants.UPDATE_PARKING_SPOT)).thenReturn(preparedStatement);
        when(preparedStatement.executeUpdate()).thenReturn(0);

        // Appel de la méthode
        boolean result = parkingSpotDAO.updateParking(parkingSpot);

        // Vérifications
        assertFalse(result);
        verify(preparedStatement, times(1)).setBoolean(1, parkingSpot.isAvailable());
        verify(preparedStatement, times(1)).setInt(2, parkingSpot.getId());
        verify(preparedStatement, times(1)).executeUpdate();
        verify(dataBaseConfig, times(1)).closePreparedStatement(preparedStatement);
        verify(dataBaseConfig, times(1)).closeConnection(connection);
    }

}
