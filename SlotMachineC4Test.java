import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Pruebas del ciclo 4: tipos de simbolos y de ruedas.
 */
public class SlotMachineC4Test {

    /**
     * Prueba del simbolo efimero: se encoge un paso cada vez que se selecciona.
     * Escenario: un simbolo efimero nuevo se selecciona dos veces.
     * Resultado esperado: parte en 30 y baja a 25 y luego a 20.
     */
    @Test
    public void shouldShrinkEphemeralSymbol() {
        EphemeralSymbol symbol = new EphemeralSymbol("red");
        assertEquals(30, symbol.getSize());
        symbol.select();
        assertEquals(25, symbol.getSize());
        symbol.select();
        assertEquals(20, symbol.getSize());
    }

    /**
     * Prueba del limite del simbolo efimero: nunca desaparece del todo.
     * Escenario: un simbolo efimero se selecciona muchas mas veces de las necesarias.
     * Resultado esperado: queda en 2, el tamano de un punto, y no baja mas.
     */
    @Test
    public void shouldStopEphemeralAtDot() {
        EphemeralSymbol symbol = new EphemeralSymbol("red");
        for(int i = 0; i < 50; i++){
            symbol.select();
        }
        assertEquals(2, symbol.getSize());
    }

    /**
     * Prueba de la copia: cada rueda trabaja con su propia copia del simbolo.
     * Escenario: se encoge un simbolo efimero y luego se copia.
     * Resultado esperado: la copia es otro objeto, del mismo color y con el tamano completo.
     */
    @Test
    public void shouldCopyEphemeralWithFullSize() {
        EphemeralSymbol symbol = new EphemeralSymbol("blue");
        symbol.select();
        symbol.select();
        EphemeralSymbol copy = (EphemeralSymbol) symbol.copy();
        assertNotSame(symbol, copy);
        assertEquals("blue", copy.getColor());
        assertEquals(30, copy.getSize());
        assertEquals(20, symbol.getSize());
    }

    /**
     * Prueba del tipo desconocido en la maquina.
     * Escenario: se agrega un simbolo con un tipo que no existe.
     * Resultado esperado: la operacion falla y el simbolo no se registra.
     */
    @Test
    public void shouldNotAddUnknownSymbolType() {
        Slotmachine machine = new Slotmachine();
        machine.addSymbol(1, "red", "invisible");
        assertFalse(machine.ok());
        assertEquals(0, machine.symbols().length);
    }

    /**
     * Prueba del simbolo efimero dentro de la maquina: su identidad sigue siendo el color.
     * Escenario: dos ruedas con un catalogo de un simbolo efimero y uno normal;
     * se pone el efimero en ambas ruedas.
     * Resultado esperado: la configuracion muestra el color rojo en las dos y hay jackpot.
     */
    @Test
    public void shouldWinWithEphemeralSymbol() {
        Slotmachine machine = new Slotmachine();
        machine.addSymbol(1, "red", "ephemeral");
        machine.addSymbol(2, "blue");
        machine.addWheels(2);
        machine.placeSymbol(1, "red");
        machine.placeSymbol(2, "red");
        assertTrue(machine.ok());
        assertEquals("red", machine.configuracion()[0]);
        assertEquals("red", machine.configuracion()[1]);
        assertTrue(machine.isJackpot());
    }

    /**
     * Prueba de que el catalogo no repite colores aunque los tipos sean distintos.
     * Escenario: se agrega un simbolo normal rojo y luego uno efimero rojo.
     * Resultado esperado: el segundo falla y el catalogo queda con un solo simbolo.
     */
    @Test
    public void shouldNotRepeatColorWithOtherType() {
        Slotmachine machine = new Slotmachine();
        machine.addSymbol(1, "red");
        machine.addSymbol(2, "red", "ephemeral");
        assertFalse(machine.ok());
        assertEquals(1, machine.symbols().length);
    }
}