package com.parkit.parkingsystem.util;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import static org.junit.jupiter.api.Assertions.*;

public class InputReaderUtilTest {

    private InputReaderUtil inputReaderUtil;

    @BeforeEach
    public void setUp() {
        inputReaderUtil = new InputReaderUtil();
    }

    @Test
    public void testReadSelectionWithValidInput() {
        // Simuler une entrée numérique valide "1\n"
        String simulatedInput = "1";
        InputStream originalIn = System.in;
        System.setIn(new ByteArrayInputStream(simulatedInput.getBytes()));

        try {
            // Appeler la méthode et vérifier le résultat
            int result = inputReaderUtil.readSelection();
            assertEquals(1, result); // Doit retourner 1 pour une entrée valide
        } finally {
            System.setIn(originalIn); // Restaurer l'entrée standard
        }
    }

//    @Test
//    void readSelection_goodInput() {
//        // can be 1, 2 or 3 -----|
//        String simulatedInput = "1\n";
//        System.setIn(new ByteArrayInputStream(simulatedInput.getBytes()));
//        InputReaderUtil inputReaderUtil = new InputReaderUtil();
//        int result = inputReaderUtil.readSelection();
//        assertEquals(1, result);
//    }

    @Test
    public void testReadSelectionWithInvalidInput() {
        // Simuler une entrée non numérique "abc"
        String simulatedInput = "abc\n";
        InputStream originalIn = System.in;
        System.setIn(new ByteArrayInputStream(simulatedInput.getBytes()));

        try {
            int result = inputReaderUtil.readSelection();
            assertEquals(-1, result); // Doit retourner -1 pour une entrée invalide
        } finally {
            System.setIn(originalIn); // Restaurer l'entrée standard
        }
    }

    @Test
    public void testReadVehicleRegistrationNumberWithEmptyInput() {
        // Simuler une entrée vide
        String simulatedInput = "\n";
        InputStream originalIn = System.in;
        System.setIn(new ByteArrayInputStream(simulatedInput.getBytes()));

        try {
            Exception exception = assertThrows(IllegalArgumentException.class, () -> {
                inputReaderUtil.readVehicleRegistrationNumber();
            });
            assertEquals("Invalid input provided", exception.getMessage());
        } finally {
            System.setIn(originalIn); // Restaurer l'entrée standard
        }
    }

    @Test
    public void testReadVehicleRegistrationNumberWithValidInput() throws Exception {
        // Simuler une entrée valide "ABC123"
        String simulatedInput = "ABC123\n";
        InputStream originalIn = System.in;
        System.setIn(new ByteArrayInputStream(simulatedInput.getBytes()));

        try {
            String result = inputReaderUtil.readVehicleRegistrationNumber();
            assertEquals("ABC123", result);
        } finally {
            System.setIn(originalIn); // Restaurer l'entrée standard
        }
    }

}
