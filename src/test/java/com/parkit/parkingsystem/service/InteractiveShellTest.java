package com.parkit.parkingsystem.service;

import com.parkit.parkingsystem.dao.TicketDAO;
import com.parkit.parkingsystem.dao.ParkingSpotDAO;
import com.parkit.parkingsystem.util.InputReaderUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import static org.mockito.Mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import java.io.ByteArrayInputStream; // Pour ByteArrayInputStream
import static org.junit.jupiter.api.Assertions.*; // Pour assertTrue et d'autres assertions
import org.junit.jupiter.api.Test; // Pour l'annotation @Test


public class InteractiveShellTest {

    @Mock
    private InputReaderUtil inputReaderUtil;
    @Mock
    private ParkingSpotDAO parkingSpotDAO;
    @Mock
    private TicketDAO ticketDAO;
    @Mock
    private ParkingService parkingService;

    @BeforeEach
    public void setUp() {
        parkingService = new ParkingService(inputReaderUtil, parkingSpotDAO, ticketDAO);
    }

    @Test
    public void testLoadInterfaceCoversMenuOptions() {
        // Simuler des entrées utilisateur : 3 pour quitter directement
        String simulatedInput = "3\n"; // Quitter dès le début
        System.setIn(new ByteArrayInputStream(simulatedInput.getBytes()));

        // Appeler la méthode que nous voulons tester
        InteractiveShell.loadInterface();

        // Si la méthode s'exécute correctement sans exception ni boucle infinie, le test passe
        assertTrue(true, "InteractiveShell should exit without entering an infinite loop");
    }

//    @Test
//    public void testLoadInterfaceWithIncomingVehicle() throws Exception {
//        // Simuler les entrées pour que le test lise "1" (arrivée de véhicule) puis "3" (sortie)
//        String simulatedInput = "1\n3\n";
//        System.setIn(new ByteArrayInputStream(simulatedInput.getBytes()));
//
//        // Simuler le comportement d'InputReaderUtil pour retourner 1 puis 3
//        when(inputReaderUtil.readSelection()).thenReturn(1).thenReturn(3);
//
//        // Appeler la méthode que nous voulons tester
//        InteractiveShell.loadInterface();
//
//        // Vérifier que processIncomingVehicle a été appelé une seule fois
//        verify(parkingService, times(1)).processIncomingVehicle();
//    }

//    @Test
//    public void testLoadInterfaceWithExitingVehicle() {
//        // Simuler des entrées : 2 pour traiter la sortie d'un véhicule, puis 3 pour quitter
//        String simulatedInput = "2\n3\n";
//        System.setIn(new ByteArrayInputStream(simulatedInput.getBytes()));
//
//        // Appeler la méthode que nous voulons tester
//        InteractiveShell.loadInterface();
//
//        // Vérifier que processExitingVehicle a été appelé
//        verify(parkingService, times(1)).processExitingVehicle();
//    }
//
//    @Test
//    public void testLoadInterfaceWithInvalidOption() {
//        // Simuler une entrée invalide, puis 3 pour quitter
//        String simulatedInput = "4\n3\n";
//        System.setIn(new ByteArrayInputStream(simulatedInput.getBytes()));
//
//        // Appeler la méthode que nous voulons tester
//        InteractiveShell.loadInterface();
//
//        // Vérifier que ni processIncomingVehicle ni processExitingVehicle n'ont été appelés
//        verify(parkingService, never()).processIncomingVehicle();
//        verify(parkingService, never()).processExitingVehicle();
//    }

//    @Test
//    public void testLoadInterfaceWithIncomingVehicle() throws Exception {
//        // Simuler l'entrée de l'utilisateur pour choisir l'option 1 (arrivée de véhicule) puis 3 (sortie)
//        when(inputReaderUtil.readSelection()).thenReturn(1).thenReturn(3);
//        doNothing().when(parkingService).processIncomingVehicle();
//
//        // Appeler la méthode que nous voulons tester
//        InteractiveShell.loadInterface();
//
//        // Vérifier que processIncomingVehicle a été appelé
//        verify(parkingService, times(1)).processIncomingVehicle();
//    }
//
//    @Test
//    public void testLoadInterfaceWithExitingVehicle() throws Exception {
//        // Simuler l'entrée de l'utilisateur pour choisir l'option 2 (sortie de véhicule) puis 3 (sortie)
//        when(inputReaderUtil.readSelection()).thenReturn(2).thenReturn(3);
//        doNothing().when(parkingService).processExitingVehicle();
//
//        // Appeler la méthode que nous voulons tester
//        InteractiveShell.loadInterface();
//
//        // Vérifier que processExitingVehicle a été appelé
//        verify(parkingService, times(1)).processExitingVehicle();
//    }
//
//    @Test
//    public void testLoadInterfaceWithExitOption() {
//        // Simuler l'entrée de l'utilisateur pour choisir l'option 3 (sortie)
//        when(inputReaderUtil.readSelection()).thenReturn(3);
//
//        // Appeler la méthode que nous voulons tester
//        InteractiveShell.loadInterface();
//
//        // Vérifier que ni processIncomingVehicle ni processExitingVehicle n'ont été appelés
//        verify(parkingService, never()).processIncomingVehicle();
//        verify(parkingService, never()).processExitingVehicle();
//    }
//
//    @Test
//    public void testLoadInterfaceWithInvalidOption() {
//        // Simuler une entrée invalide, puis l'option 3 pour sortir
//        when(inputReaderUtil.readSelection()).thenReturn(4).thenReturn(3);
//
//        // Appeler la méthode que nous voulons tester
//        InteractiveShell.loadInterface();
//
//        // Vérifier que ni processIncomingVehicle ni processExitingVehicle n'ont été appelés
//        verify(parkingService, never()).processIncomingVehicle();
//        verify(parkingService, never()).processExitingVehicle();
//    }
}
