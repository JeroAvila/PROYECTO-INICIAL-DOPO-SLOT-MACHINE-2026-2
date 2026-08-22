

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * The test class SlotmachineTest.
 *
 * @author  (your name)
 * @version (a version number or a date)
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
    
    /**
     * Agrega una rueda en posición 1 a una máquina vacía; verifica que ok() sea true.
     */
    @Test
    public void testAddWheelEnMaquinaVacia()
    {
        machine.addWheel(1);
        assertTrue(machine.ok());
    }
    
    /**
     * Agrega 3 ruedas, la última insertada en medio de las otras dos; verifica que ok() sea true
     */
    @Test
    public void testAddWheelEnPosicionIntermedia()
    {
        machine.addWheel(1);
        machine.addWheel(2);
        machine.addWheel(2);
        assertTrue(machine.ok());
    }
    
    /**
     * Llama addWheel(0), una posición inválida por debajo del mínimo; 
     * verifica que la máquina la ajuste a 1 sin fallar.
     */
    @Test
    public void testAddWheelConPosicionMenorA1()
    {
        machine.addWheel(0);
        assertTrue(machine.ok());
    }
    
    /**
     * Con 2 ruedas ya puestas, llama addWheel(10) una posición fuera de rango; 
     * verifica que se ajuste al máximo permitido sin fallar.
     */
    @Test
    public void testAddWheelConPosicionMayorAlMaximo()
    {
        machine.addWheel(1);
        machine.addWheel(2);
        machine.addWheel(10);
        assertTrue(machine.ok());
    }
    
    /**
     * Llama delWheel(1) sobre una máquina recién creada,
     * sin ruedas; verifica que ok() sea false, porque no hay nada que eliminar.
     */
    @Test
    public void testDelWheelEnMaquinaVacia()
    {
        machine.delWheel(1);
        assertFalse(machine.ok());
    }
    
    /**
     * Agrega 3 ruedas y elimina la del medio (posición 2); verifica que ok() sea true.
     */
    @Test
    public void testDelWheelEnPosicionValida()
    {
        machine.addWheel(1);
        machine.addWheel(2);
        machine.addWheel(3);
        machine.delWheel(2);
        assertTrue(machine.ok());
    }
    
    /**
     * Con 2 ruedas, llama delWheel(0) una posición inválida por debajo del mínimo; 
     * verifica que se ajuste a 1 y elimine sin fallar.
     */
    @Test
    public void testDelWheelConPosicionMenorA1()
    {
        machine.addWheel(1);
        machine.addWheel(2);
        machine.delWheel(0);
        assertTrue(machine.ok());
    }
    
    /**
     * Agrega 1 rueda, la elimina (queda vacía), e intenta eliminar otra vez,
     * verifica que la segunda eliminación falle. Confirmando que el estado "vacía" se detecta 
     * correctamente después de vaciarla, no solo al crear la máquina.
     */
    @Test
    public void testDelWheelDespuesDeVaciarLaMaquina()
    {
        machine.addWheel(1);
        machine.delWheel(1);
        machine.delWheel(1);
        assertFalse(machine.ok());
    }
    
    /**
     * Agrega el símbolo "red", que sí es un color reconocido por Canvas; 
     * verifica que ok() sea true
     */
    @Test
    public void testAddSymbolColorValido()
    {
        machine.addSymbol(1, "red");
        assertTrue(machine.ok());
    }
    
    /**
     * Intenta agregar "purple", que no está en la lista de 7 colores que soporta Canvas,
     * verifica que ok() sea false
     */
    @Test
    public void testAddSymbolColorInvalido()
    {
        machine.addSymbol(1, "purple");
        assertFalse(machine.ok());
    }
    
    /**
     * Agrega "red" y luego intenta agregar "red" otra vez,
     * verifica que la segunda llamada deje ok() en false. Confirma la regla de "colores diferentes"
     */
    @Test
    public void testAddSymbolColorRepetido()
    {
        machine.addSymbol(1, "red");
        machine.addSymbol(2, "red");
        assertFalse(machine.ok());
    }
    
    /**
     * Llama addSymbol(0, "blue"), posición inválida por debajo del mínimo; 
     * verifica que se ajuste a 1 y la operación no falle
     */
    @Test
    public void testAddSymbolConPosicionMenorA1()
    {
        machine.addSymbol(0, "blue");
        assertTrue(machine.ok());
    }
    
    /**
     * Con 2 símbolos ya agregados, llama addSymbol(10, "green") una posición fuera de rango
     */
    @Test
    public void testAddSymbolConPosicionMayorAlMaximo()
    {
        machine.addSymbol(1, "red");
        machine.addSymbol(2, "blue");
        machine.addSymbol(10, "green");
        assertTrue(machine.ok());
    }
    
    /**
     * Agrega "red" y lo elimina; verifica que ok() sea true
     */
    @Test
    public void testDelSymbolExistente()
    {
        machine.addSymbol(1, "red");
        machine.delSymbol("red");
        assertTrue(machine.ok());
    }
    
    /**
     * Intenta eliminar "red" de la máquina que nunca lo tuvo, 
     * verifica que ok() sea false, porque no hay nada que borrar.
     */
    @Test
    public void testDelSymbolInexistente()
    {
        machine.delSymbol("red");
        assertFalse(machine.ok());
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