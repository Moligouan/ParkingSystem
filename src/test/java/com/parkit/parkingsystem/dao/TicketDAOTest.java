package com.parkit.parkingsystem.dao;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Timestamp;
import java.sql.SQLException;
import java.sql.ResultSet;
import java.util.Date;
import com.parkit.parkingsystem.config.DataBaseConfig;
import com.parkit.parkingsystem.constants.DBConstants;
import com.parkit.parkingsystem.model.Ticket;
import com.parkit.parkingsystem.model.ParkingSpot;
import com.parkit.parkingsystem.constants.ParkingType;


public class TicketDAOTest {

    private TicketDAO ticketDAO;
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

        ticketDAO = new TicketDAO();
        ticketDAO.setDataBaseConfig(dataBaseConfig);

        when(dataBaseConfig.getConnection()).thenReturn(connection);
        when(connection.prepareStatement(DBConstants.SAVE_TICKET)).thenReturn(preparedStatement);
        when(connection.prepareStatement(DBConstants.GET_TICKET)).thenReturn(preparedStatement);
        when(connection.prepareStatement(DBConstants.UPDATE_TICKET)).thenReturn(preparedStatement);
        when(preparedStatement.executeQuery()).thenReturn(resultSet);
    }

    @Test
    public void testSaveTicket() throws Exception {
        // Mock du Ticket
        Ticket ticket = new Ticket();
        ticket.setParkingSpot(new ParkingSpot(1, ParkingType.CAR, true));
        ticket.setVehicleRegNumber("ABC123");
        ticket.setPrice(10.0);
        ticket.setInTime(new java.util.Date());
        ticket.setOutTime(null);

        // Mock du comportement de l'exécution de la requête
        when(preparedStatement.execute()).thenReturn(true);

        // Appel de la méthode à tester
        boolean result = ticketDAO.saveTicket(ticket);

        // Vérification
        assertTrue(result);
        verify(preparedStatement, times(1)).setInt(1, ticket.getParkingSpot().getId());
        verify(preparedStatement, times(1)).setString(2, ticket.getVehicleRegNumber());
        verify(preparedStatement, times(1)).setDouble(3, ticket.getPrice());
        verify(preparedStatement, times(1)).setTimestamp(4, new Timestamp(ticket.getInTime().getTime()));
        verify(preparedStatement, times(1)).setTimestamp(5, null);

        // Vérifie que la connexion est fermée
        verify(dataBaseConfig, times(1)).closeConnection(connection);
    }

    @Test
    public void testSaveTicketException() throws Exception {
        // Simuler une exception lors de l'obtention de la connexion
        when(dataBaseConfig.getConnection()).thenThrow(new RuntimeException("Connection error"));

        // Appel de la méthode à tester
        boolean result = ticketDAO.saveTicket(new Ticket());

        // Vérification que le résultat est `false` en cas d'exception
        assertFalse(result);
        verify(dataBaseConfig, times(1)).closeConnection(connection);
    }

    @Test
    public void testGetTicket() throws Exception {
        // Simuler le comportement du ResultSet
        when(resultSet.next()).thenReturn(true);
        when(resultSet.getInt(1)).thenReturn(1);
        when(resultSet.getInt(2)).thenReturn(123);
        when(resultSet.getDouble(3)).thenReturn(10.0);
        when(resultSet.getTimestamp(4)).thenReturn(new Timestamp(System.currentTimeMillis()));
        when(resultSet.getTimestamp(5)).thenReturn(null);
        when(resultSet.getString(6)).thenReturn("CAR");

        // Appel de la méthode à tester
        Ticket result = ticketDAO.getTicket("ABC123");

        // Vérifications
        assertNotNull(result);
        assertEquals("ABC123", result.getVehicleRegNumber());
        assertEquals(1, result.getParkingSpot().getId());
        assertEquals(10.0, result.getPrice());
        assertEquals(ParkingType.CAR, result.getParkingSpot().getParkingType());

        // Vérifie que les ressources sont fermées
        verify(dataBaseConfig, times(1)).closeResultSet(resultSet);
        verify(dataBaseConfig, times(1)).closePreparedStatement(preparedStatement);
        verify(dataBaseConfig, times(1)).closeConnection(connection);
    }

    @Test
    public void testGetTicketNotFound() throws Exception {
        // Simuler un ResultSet vide
        when(resultSet.next()).thenReturn(false);

        // Appel de la méthode à tester
        Ticket result = ticketDAO.getTicket("XYZ789");

        // Vérification que le ticket est null lorsque le véhicule n'existe pas
        assertNull(result);

        // Vérifie que les ressources sont fermées
        verify(dataBaseConfig, times(1)).closeResultSet(resultSet);
        verify(dataBaseConfig, times(1)).closePreparedStatement(preparedStatement);
        verify(dataBaseConfig, times(1)).closeConnection(connection);
    }

    @Test
    public void testGetTicketExceptionHandling() throws Exception {
        // Simuler une exception lors de l'obtention de la connexion
        when(dataBaseConfig.getConnection()).thenThrow(new SQLException("Connection failed"));

        // Appel de la méthode à tester
        Ticket result = ticketDAO.getTicket("ABC123");

        // Vérification que le ticket est null en cas d'exception
        assertNull(result);

        // Vérification que le logger.error est appelé
        // Remarque : pour vérifier le logger, il peut être utile d'utiliser un framework comme LogCaptor ou Mockito pour capturer les logs.
        verify(dataBaseConfig, times(1)).closeConnection(null);
    }

    @Test
    public void testSimpleUpdateTicket() throws Exception {
        // Crée un ticket de test
        Ticket ticket = new Ticket();
        ticket.setId(123);
        ticket.setPrice(20.0);
        ticket.setOutTime(new java.util.Date());

        // Simule une exécution simple
        when(dataBaseConfig.getConnection()).thenReturn(connection);
        when(connection.prepareStatement(DBConstants.UPDATE_TICKET)).thenReturn(preparedStatement);
        doNothing().when(preparedStatement).execute();

        // Appel de la méthode
        ticketDAO.updateTicket(ticket);

        // Ajoute un point de vérification pour savoir si la connexion a été utilisée
        verify(connection, times(1)).prepareStatement(DBConstants.UPDATE_TICKET);
        verify(preparedStatement, times(1)).execute();
    }

    @Test
    public void testUpdateTicketExceptionHandling() throws Exception {
        // Simuler une exception lors de l'obtention de la connexion
        when(dataBaseConfig.getConnection()).thenThrow(new SQLException("Connection failed"));

        // Appel de la méthode à tester
        boolean result = ticketDAO.updateTicket(new Ticket());

        // Vérification que le résultat est `false` en cas d'exception
        assertFalse(result);

        // Vérifie que la connexion est fermée
        verify(dataBaseConfig, times(1)).closeConnection(null);
    }
}