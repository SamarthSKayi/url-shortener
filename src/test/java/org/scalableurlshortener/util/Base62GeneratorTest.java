package org.scalableurlshortener.util;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class Base62GeneratorTest {

    @Test
    void shouldGenerateSevenCharacterCode() {

        String code = Base62Generator.generate();

        assertEquals(7, code.length());
    }

    @Test
    void shouldGenerateOnlyBase62Characters() {

        String code = Base62Generator.generate();

        assertTrue(code.matches("[0-9a-zA-Z]+"));
    }

    @Test
    void shouldGenerateRequestedLength() {

        String code = Base62Generator.generate(10);

        assertEquals(10, code.length());
    }

    @Test
    void shouldRejectInvalidLength() {

        assertThrows(
                IllegalArgumentException.class,
                () -> Base62Generator.generate(0)
        );
    }
}