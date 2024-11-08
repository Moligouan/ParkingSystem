package com.parkit.parkingsystem.service;

import com.parkit.parkingsystem.constants.ParkingType;
import com.parkit.parkingsystem.dao.ParkingSpotDAO;
import com.parkit.parkingsystem.dao.TicketDAO;
import com.parkit.parkingsystem.model.ParkingSpot;
import com.parkit.parkingsystem.model.Ticket;
import com.parkit.parkingsystem.util.InputReaderUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.io.InputStream;
import java.io.ByteArrayInputStream;

import java.util.Date;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class ParkingServiceTest {

    private static ParkingService parkingService;

    @Mock
    private static InputReaderUtil inputReaderUtil;
    @Mock
    private static ParkingSpotDAO parkingSpotDAO;
    @Mock
    private static TicketDAO ticketDAO;

    @BeforeEach
    public void setUpPerTest() {
        try {
            when(inputReaderUtil.readVehicleRegistrationNumber()).thenReturn("ABCDEF");

            ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR, false);
            Ticket ticket = new Ticket();
            ticket.setInTime(new Date(System.currentTimeMillis() - (60 * 60 * 1000)));
            ticket.setParkingSpot(parkingSpot);
            ticket.setVehicleRegNumber("ABCDEF");
            when(ticketDAO.getTicket(anyString())).thenReturn(ticket);
            when(ticketDAO.updateTicket(any(Ticket.class))).thenReturn(true);

            when(parkingSpotDAO.updateParking(any(ParkingSpot.class))).thenReturn(true);

            parkingService = new ParkingService(inputReaderUtil, parkingSpotDAO, ticketDAO);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to set up test mock objects");
        }
    }

    @Test
    public void testProcessIncomingVehicleSuccess() throws Exception {
        // Configurer les mocks pour simuler un scénario réussi
        ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR, true);
        when(parkingService.getNextParkingNumberIfAvailable()).thenReturn(parkingSpot);
        when(inputReaderUtil.readVehicleRegistrationNumber()).thenReturn("ABC123");
        when(parkingSpotDAO.updateParking(parkingSpot)).thenReturn(true);
        when(ticketDAO.saveTicket(any(Ticket.class))).thenReturn(true);

        // Appeler la méthode
        parkingService.processIncomingVehicle();

        // Vérifications
        verify(parkingSpotDAO, times(1)).updateParking(parkingSpot);
        verify(ticketDAO, times(1)).saveTicket(any(Ticket.class));
        verify(inputReaderUtil, times(1)).readVehicleRegistrationNumber();
    }

    @Test
    public void testProcessIncomingVehicleNoParkingSpotAvailable() throws Exception {
        // Simuler un cas où aucune place n'est disponible
        when(parkingService.getNextParkingNumberIfAvailable()).thenReturn(null);

        // Appeler la méthode
        parkingService.processIncomingVehicle();

        // Vérifications
        verify(parkingSpotDAO, never()).updateParking(any(ParkingSpot.class));
        verify(ticketDAO, never()).saveTicket(any(Ticket.class));
        verify(inputReaderUtil, never()).readVehicleRegistrationNumber();
    }

    @Test
    public void testProcessIncomingVehicleThrowsException() throws Exception {
        // Simuler une exception lors de l'appel
        when(parkingService.getNextParkingNumberIfAvailable()).thenThrow(new RuntimeException("Exception"));

        // Appeler la méthode et vérifier qu'une exception est gérée
        assertDoesNotThrow(() -> parkingService.processIncomingVehicle());

        // Vérifications
        verify(parkingSpotDAO, never()).updateParking(any(ParkingSpot.class));
        verify(ticketDAO, never()).saveTicket(any(Ticket.class));
        verify(inputReaderUtil, never()).readVehicleRegistrationNumber();
    }

    @Test
    public void testGetNextParkingNumberIfAvailableSuccess() throws Exception {
        // Simuler le type de véhicule
        when(inputReaderUtil.readSelection()).thenReturn(1); // CAR
        when(parkingSpotDAO.getNextAvailableSlot(any(ParkingType.class))).thenReturn(1);

        ParkingSpot result = parkingService.getNextParkingNumberIfAvailable();

        // Vérifications
        assertNotNull(result);
        assertEquals(1, result.getId());
        assertEquals(ParkingType.CAR, result.getParkingType());
        assertTrue(result.isAvailable());

        verify(parkingSpotDAO, times(1)).getNextAvailableSlot(any(ParkingType.class));
    }

    @Test
    public void testGetNextParkingNumberIfAvailableSuccess2() throws Exception {
        // Simuler le type de véhicule
        when(inputReaderUtil.readSelection()).thenReturn(2); // BIKE
        when(parkingSpotDAO.getNextAvailableSlot(any(ParkingType.class))).thenReturn(1);

        ParkingSpot result = parkingService.getNextParkingNumberIfAvailable();

        // Vérifications
        assertNotNull(result);
        assertEquals(1, result.getId());
        assertEquals(ParkingType.CAR, result.getParkingType());
        assertTrue(result.isAvailable());

        verify(parkingSpotDAO, times(1)).getNextAvailableSlot(any(ParkingType.class));
    }

    @Test
    public void testGetNextParkingNumberIfAvailableNoSpotAvailable() throws Exception {
        // Simuler le type de véhicule
        when(inputReaderUtil.readSelection()).thenReturn(1); // CAR
        when(parkingSpotDAO.getNextAvailableSlot(any(ParkingType.class))).thenReturn(0);

        ParkingSpot result = parkingService.getNextParkingNumberIfAvailable();

        // Vérifications
        assertNull(result);
        verify(parkingSpotDAO, times(1)).getNextAvailableSlot(any(ParkingType.class));
    }

    @Test
    public void testGetNextParkingNumberIfAvailableInvalidVehicleType() {
        // Simuler un type de véhicule incorrect
        when(inputReaderUtil.readSelection()).thenReturn(3); // Un type invalide

        ParkingSpot result = parkingService.getNextParkingNumberIfAvailable();

        // Vérifications
        assertNull(result);
        verify(parkingSpotDAO, never()).getNextAvailableSlot(any(ParkingType.class));
    }

    @Test
    public void testGetNextParkingNumberIfAvailableThrowsException() throws Exception {
        // Simuler une exception lors de l'appel à la base de données
        when(inputReaderUtil.readSelection()).thenReturn(1); // CAR
        when(parkingSpotDAO.getNextAvailableSlot(any(ParkingType.class))).thenThrow(new RuntimeException("DB error"));

        ParkingSpot result = parkingService.getNextParkingNumberIfAvailable();

        // Vérifications
        assertNull(result);
        verify(parkingSpotDAO, times(1)).getNextAvailableSlot(any(ParkingType.class));
    }

    @Test
    public void processExitingVehicleThrowsExceptionTest() {
        doThrow(new RuntimeException("Exception during update")).when(ticketDAO).updateTicket(any(Ticket.class));
        parkingService.processExitingVehicle();
        verify(parkingSpotDAO, never()).updateParking(any(ParkingSpot.class)); // Ne devrait pas être appelé en cas d'exception
    }

    @Test
    public void testProcessIncomingVehicle() throws Exception {
        // Simuler l'entrée de l'utilisateur pour le numéro de véhicule
        String simulatedInput = "ABC123\n";
        InputStream originalIn = System.in;
        System.setIn(new ByteArrayInputStream(simulatedInput.getBytes()));

        try {
            // Simuler le comportement de inputReaderUtil pour lire le type de véhicule et le numéro de véhicule
            when(inputReaderUtil.readSelection()).thenReturn(1); // 1 pour CAR
            when(inputReaderUtil.readVehicleRegistrationNumber()).thenReturn("ABC123");

            // Simuler un ID de place de parking valide pour le type CAR
            when(parkingSpotDAO.getNextAvailableSlot(ParkingType.CAR)).thenReturn(1);

            // Simuler la mise à jour de la disponibilité de la place de parking
            when(parkingSpotDAO.updateParking(any(ParkingSpot.class))).thenReturn(true);

            // Appeler la méthode que nous voulons tester
            parkingService.processIncomingVehicle();

            // Vérifier que les méthodes appropriées ont été appelées
            verify(parkingSpotDAO, times(1)).updateParking(any(ParkingSpot.class));
            verify(ticketDAO, times(1)).saveTicket(any(Ticket.class));
        } finally {
            System.setIn(originalIn); // Restaurer l'entrée standard
        }
    }
}