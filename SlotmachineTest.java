import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
/**
 * The test class SlotmachineTest.
 *
 * @author Laura Juliana Parra Velandia
 * @author Thomas Jeronimo Avila Castillo
 * @version 1.0
 */
public class SlotmachineTest
{
    private Slotmachine machine;
    
    /**
     * Default constructor for test class SlotmachineTest
     */
    public SlotmachineTest()
    {
    }
    /**
     * Sets up the test fixture.
     *
     * Called before every test case method.
     */
    @BeforeEach
    public void setUp()
    {
        machine = new Slotmachine();
    }
    
    @Test
    public void testAddWheelEnMaquinaVacia()
    {
        machine.addWheel(1);
        assertTrue(machine.ok());
    }
    
    @Test
    public void testAddWheelEnPosicionIntermedia()
    {
        machine.addWheel(1);
        machine.addWheel(2);
        machine.addWheel(2);
        assertTrue(machine.ok());
    }
    
    @Test
    public void testAddWheelConPosicionMenorA1()
    {
        machine.addWheel(0);
        assertTrue(machine.ok());
    }
    
    @Test
    public void testAddWheelConPosicionMayorAlMaximo()
    {
        machine.addWheel(1);
        machine.addWheel(2);
        machine.addWheel(10);
        assertTrue(machine.ok());
    }
    
    @Test
    public void testDelWheelEnMaquinaVacia()
    {
        machine.delWheel(1);
        assertFalse(machine.ok());
    }
    
    @Test
    public void testDelWheelEnPosicionValida()
    {
        machine.addWheel(1);
        machine.addWheel(2);
        machine.addWheel(3);
        machine.delWheel(2);
        assertTrue(machine.ok());
    }
    
    @Test
    public void testDelWheelConPosicionMenorA1()
    {
        machine.addWheel(1);
        machine.addWheel(2);
        machine.delWheel(0);
        assertTrue(machine.ok());
    }
    
    @Test
    public void testDelWheelConPosicionMayorAlMaximo()
    {
        machine.addWheel(1);
        machine.addWheel(2);
        machine.delWheel(100);
        assertTrue(machine.ok());
    }
    
    @Test
    public void testDelWheelDespuesDeVaciarLaMaquina()
    {
        machine.addWheel(1);
        machine.delWheel(1);
        machine.delWheel(1);
        assertFalse(machine.ok());
    }
    
    @Test
    public void testAddWheelDespuesDeTenerSimbolos()
    {
        machine.addSymbol(1, "red");
        machine.addSymbol(2, "blue");
        machine.addWheel(1);
        String[] config = machine.configuracion();
        assertEquals(1, config.length);
        assertEquals("red", config[0]);
    }
    
    @Test
    public void testAddSymbolColorValido()
    {
        machine.addSymbol(1, "red");
        assertTrue(machine.ok());
    }
    
    @Test
    public void testAddSymbolColorInvalido()
    {
        machine.addSymbol(1, "purple");
        assertFalse(machine.ok());
    }
    
    @Test
    public void testAddSymbolColorRepetido()
    {
        machine.addSymbol(1, "red");
        machine.addSymbol(2, "red");
        assertFalse(machine.ok());
    }
    
    @Test
    public void testAddSymbolConPosicionMenorA1()
    {
        machine.addSymbol(0, "blue");
        assertFalse(machine.ok());
    }
    
    @Test
    public void testAddSymbolConPosicionMayorAlMaximo()
    {
        machine.addSymbol(1, "red");
        machine.addSymbol(2, "blue");
        machine.addSymbol(10, "green");
        assertFalse(machine.ok());
    }
    
    @Test
    public void testAddSymbolPosicionInvalidaNoAgregaNada()
    {
        machine.addSymbol(1, "red");
        machine.addSymbol(0, "blue");
        assertEquals(1, machine.symbols().length);
    }
    
    @Test
    public void testAddSymbolPosicionEnElLimiteInferiorEsValida()
    {
        machine.addSymbol(1, "red");
        assertTrue(machine.ok());
        assertEquals(1, machine.symbols().length);
    }
    
    @Test
    public void testAddSymbolPosicionEnElLimiteSuperiorEsValida()
    {
        machine.addSymbol(1, "red");
        machine.addSymbol(2, "blue");
        assertTrue(machine.ok());
        assertEquals(2, machine.symbols().length);
    }
    
    @Test
    public void testAddSymbolInvalidoNoAfectaRuedasExistentes()
    {
        machine.addWheel(1);
        machine.addSymbol(1, "red");
        machine.addSymbol(5, "blue");
        assertEquals("red", machine.configuracion()[0]);
    }
    
    @Test
    public void testPlaceSymbolSinRuedas()
    {
        machine.placeSymbol(1, "red");
        assertFalse(machine.ok());
    }
    
    @Test
    public void testPlaceSymbolSinSimbolos()
    {
        machine.addWheel(1);
        machine.placeSymbol(1, "red");
        assertFalse(machine.ok());
    }
    
    @Test
    public void testPlaceSymbolConSimboloInexistente()
    {
        machine.addWheel(1);
        machine.addSymbol(1, "red");
        machine.placeSymbol(1, "blue");
        assertFalse(machine.ok());
    }
    
    @Test
    public void testPlaceSymbolValido()
    {
        machine.addWheel(1);
        machine.addSymbol(1, "red");
        machine.addSymbol(2, "blue");
        machine.placeSymbol(1, "blue");
        assertTrue(machine.ok());
        assertEquals("blue", machine.configuracion()[0]);
    }
    
    @Test
    public void testPlaceSymbolConRuedaMenorA1()
    {
        machine.addWheel(1);
        machine.addWheel(2);
        machine.addSymbol(1, "red");
        machine.placeSymbol(0, "red");
        assertTrue(machine.ok());
        assertEquals("red", machine.configuracion()[0]);
    }
    
    @Test
    public void testPlaceSymbolConRuedaMayorAlMaximo()
    {
        machine.addWheel(1);
        machine.addWheel(2);
        machine.addSymbol(1, "red");
        machine.placeSymbol(10, "red");
        assertTrue(machine.ok());
        assertEquals("red", machine.configuracion()[1]);
    }
    
    @Test
    public void testSpinUnaRuedaSinRuedas()
    {
        machine.spin(1);
        assertFalse(machine.ok());
    }
    
    @Test
    public void testSpinUnaRuedaSinSimbolos()
    {
        machine.addWheel(1);
        machine.spin(1);
        assertFalse(machine.ok());
    }
    
    @Test
    public void testSpinUnaRuedaAvanzaAlSiguienteSimbolo()
    {
        machine.addWheel(1);
        machine.addSymbol(1, "red");
        machine.addSymbol(2, "blue");
        machine.spin(1);
        assertTrue(machine.ok());
        assertEquals("blue", machine.configuracion()[0]);
    }
    
    @Test
    public void testSpinTodasSinRuedas()
    {
        machine.spin();
        assertFalse(machine.ok());
    }
    
    @Test
    public void testSpinTodasSinSimbolos()
    {
        machine.addWheel(1);
        machine.spin();
        assertFalse(machine.ok());
    }
    
    @Test
    public void testSpinTodasLasRuedas()
    {
        machine.addWheel(1);
        machine.addWheel(2);
        machine.addSymbol(1, "red");
        machine.addSymbol(2, "blue");
        machine.spin();
        String[] config = machine.configuracion();
        assertEquals("blue", config[0]);
        assertEquals("blue", config[1]);
    }
    
    @Test
    public void testIsJackpotSinRuedas()
    {
        assertFalse(machine.isJackpot());
    }
    
    @Test
    public void testIsJackpotConRuedaSinSimbolo()
    {
        machine.addWheel(1);
        assertFalse(machine.isJackpot());
    }
    
    @Test
    public void testIsJackpotConTodasIguales()
    {
        machine.addWheel(1);
        machine.addWheel(2);
        machine.addSymbol(1, "red");
        machine.placeSymbol(1, "red");
        machine.placeSymbol(2, "red");
        assertTrue(machine.isJackpot());
    }
    
    @Test
    public void testIsJackpotConDistintas()
    {
        machine.addWheel(1);
        machine.addWheel(2);
        machine.addSymbol(1, "red");
        machine.addSymbol(2, "blue");
        machine.placeSymbol(1, "red");
        machine.placeSymbol(2, "blue");
        assertFalse(machine.isJackpot());
    }
    
    @Test
    public void testIsJackpotCambiaAlEliminarLaRuedaDistinta()
    {
        machine.addWheel(1);
        machine.addWheel(2);
        machine.addWheel(3);
        machine.addSymbol(1, "red");
        machine.addSymbol(2, "blue");
        machine.placeSymbol(1, "red");
        machine.placeSymbol(2, "red");
        machine.placeSymbol(3, "blue");
        assertFalse(machine.isJackpot());
        machine.delWheel(3);
        assertTrue(machine.isJackpot());
    }
    
    @Test
    public void testDelSymbolExistente()
    {
        machine.addSymbol(1, "red");
        machine.delSymbol("red");
        assertTrue(machine.ok());
    }
    
    @Test
    public void testDelSymbolInexistente()
    {
        machine.delSymbol("red");
        assertFalse(machine.ok());
    }
    
    @Test
    public void testSymbolsEnMaquinaVacia()
    {
        String[] resultado = machine.symbols();
        assertEquals(0, resultado.length);
    }
    
    @Test
    public void testSymbolsConVariosSimbolos()
    {
        machine.addSymbol(1, "red");
        machine.addSymbol(2, "blue");
        String[] resultado = machine.symbols();
        assertEquals(2, resultado.length);
        assertEquals("red", resultado[0]);
        assertEquals("blue", resultado[1]);
    }
    
    @Test
    public void testDistinctSymbolsEnMaquinaVacia()
    {
        assertEquals(0, machine.distinctSymbolos());
    }
    
    @Test
    public void testDistinctSymbolsConVariosSimbolos()
    {
        machine.addSymbol(1, "red");
        machine.addSymbol(2, "blue");
        machine.addSymbol(3, "green");
        assertEquals(3, machine.distinctSymbolos());
    }
    
    @Test
    public void testExitDejaLaMaquinaSinRuedas()
    {
        machine.addWheel(1);
        machine.addWheel(2);
        machine.exit();
        assertEquals(0, machine.symbols().length);
        machine.delWheel(1);
        assertFalse(machine.ok());
    }
    
    @Test
    public void testExitDejaLaMaquinaSinSimbolos()
    {
        machine.addSymbol(1, "red");
        machine.addSymbol(2, "blue");
        machine.exit();
        assertEquals(0, machine.symbols().length);
    }
    
    @Test
    public void testExitSiempreTieneExito()
    {
        machine.exit();
        assertTrue(machine.ok());
    }
    /**
     * Tears down the test fixture.
     *
     * Called after every test case method.
     */
    @AfterEach
    public void tearDown()
    {
    }
}
