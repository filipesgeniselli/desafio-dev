package com.filipegeniselli.desafiodev;

import com.filipegeniselli.desafiodev.exception.InvalidCnabException;
import com.filipegeniselli.desafiodev.transactions.data.Cnab;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

class CnabUnitTests {

    @Test
    void shouldParseLine() throws InvalidCnabException {
        String line = "3201903010000014200096206760174753****3153153453JOÃO MACEDO   BAR DO JOÃO       ";
        Cnab cnab = Cnab.fromFileLine(line);

        assertEquals("3", cnab.getType());
        assertEquals("20190301", cnab.getDate());
        assertEquals("0000014200", cnab.getAmount());
        assertEquals("09620676017", cnab.getCpf());
        assertEquals("4753****3153", cnab.getCard());
        assertEquals("153453", cnab.getTime());
        assertEquals("JOÃO MACEDO", cnab.getOwner());
        assertEquals("BAR DO JOÃO", cnab.getStore());
    }

    @ParameterizedTest
    @ValueSource(strings = {
        "3201903010000014200096206760174753****3153153453JOÃO MACEDO   BAR DO JOÃO          ",
        "3201903010000014200096206760174753****3153153453JOÃO MACEDO   BAR DO JOÃO",
        ""
    })
    void whenExceptionThrown_thenAssertionSucceeds(String line) {
        Exception exception = assertThrows(InvalidCnabException.class, () -> {
            Cnab.fromFileLine(line);
        });

        String expectedMessage = "Linha não está no formato correto";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

}
