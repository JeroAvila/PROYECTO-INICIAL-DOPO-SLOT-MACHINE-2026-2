import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Tests de Slotmachine, cubriendo unicamente los metodos del ciclo 1:
 * addWheel, delWheel, addSymbol, delSymbol, placeSymbol, spin(wheel),
 * spin(), symbols(), distinctSymbolos(), configuracion(), isJackpot(),
 * makeVisible(), makeInvisible(), exit(), ok().
 *
 * @author Laura Juliana Parra Velandia
 * @author Thomas Jeronimo Avila Castillo
 * @version 1.0
 */
public class SlotmachineTest
{
    private Slotmachine machine;

    @BeforeEach
    public void setUp()
    {
        machine = new Slotmachine();
    }

    @AfterEach
    public void tearDown()
    {
    }

    @Test
    public void shouldAddWheelToEmptyMachine()
    {
        machine.addWheel(1);
        assertTrue(machine.ok());
    }

    @Test
    public void shouldAddWheelAtMiddlePosition()
    {
        machine.addWheel(1);
        machine.addWheel(2);
        machine.addWheel(2);
        assertTrue(machine.ok());
    }

    @Test
    public void shouldClampPositionBelowMinimumWhenAddingWheel()
    {
        machine.addWheel(0);
        assertTrue(machine.ok());
    }

    @Test
    public void shouldClampPositionAboveMaximumWhenAddingWheel()
    {
        machine.addWheel(1);
        machine.addWheel(2);
        machine.addWheel(10);
        assertTrue(machine.ok());
    }

    @Test
    public void shouldNotAutomaticallyShowSymbolOnNewWheel()
    {
        machine.addSymbol(1, "red");
        machine.addSymbol(2, "blue");
        machine.addWheel(1);
        String[] config = machine.configuracion();
        assertNull(config[0]);
    }

    @Test
    public void shouldNotDeleteWheelFromEmptyMachine()
    {
        machine.delWheel(1);
        assertFalse(machine.ok());
    }

    @Test
    public void shouldDeleteWheelAtValidPosition()
    {
        machine.addWheel(1);
        machine.addWheel(2);
        machine.addWheel(3);
        machine.delWheel(2);
        assertTrue(machine.ok());
    }

    @Test
    public void shouldClampPositionBelowMinimumWhenDeletingWheel()
    {
        machine.addWheel(1);
        machine.addWheel(2);
        machine.delWheel(0);
        assertTrue(machine.ok());
    }

    @Test
    public void shouldClampPositionAboveMaximumWhenDeletingWheel()
    {
        machine.addWheel(1);
        machine.addWheel(2);
        machine.delWheel(100);
        assertTrue(machine.ok());
    }

    @Test
    public void shouldNotDeleteAgainAfterMachineBecomesEmpty()
    {
        machine.addWheel(1);
        machine.delWheel(1);
        machine.delWheel(1);
        assertFalse(machine.ok());
    }

    @Test
    public void shouldNotDeleteLockedWheel()
    {
        machine.addWheel(1);
        machine.lock(1);
        machine.delWheel(1);
        assertFalse(machine.ok());
    }

    @Test
    public void shouldAddValidSymbol()
    {
        machine.addSymbol(1, "red");
        assertTrue(machine.ok());
    }

    @Test
    public void shouldNotAddSymbolWithInvalidColor()
    {
        machine.addSymbol(1, "blanco");
        assertFalse(machine.ok());
    }

    @Test
    public void shouldNotAddSymbolWithDuplicateColor()
    {
        machine.addSymbol(1, "red");
        machine.addSymbol(2, "red");
        assertFalse(machine.ok());
    }

    @Test
    public void shouldNotAddSymbolWithPositionBelowMinimum()
    {
        machine.addSymbol(0, "blue");
        assertFalse(machine.ok());
    }

    @Test
    public void shouldNotAddSymbolWithPositionAboveMaximum()
    {
        machine.addSymbol(1, "red");
        machine.addSymbol(2, "blue");
        machine.addSymbol(10, "green");
        assertFalse(machine.ok());
    }

    @Test
    public void shouldNotChangeCatalogWhenAddSymbolFails()
    {
        machine.addSymbol(1, "red");
        machine.addSymbol(0, "blue");
        assertEquals(1, machine.symbols().length);
    }

    @Test
    public void shouldAddSymbolAtLowerBoundaryPosition()
    {
        machine.addSymbol(1, "red");
        assertTrue(machine.ok());
        assertEquals(1, machine.symbols().length);
    }

    @Test
    public void shouldAddSymbolAtUpperBoundaryPosition()
    {
        machine.addSymbol(1, "red");
        machine.addSymbol(2, "blue");
        assertTrue(machine.ok());
        assertEquals(2, machine.symbols().length);
    }

    @Test
    public void shouldNotAffectExistingWheelsWhenAddSymbolFails()
    {
        machine.addWheel(1);
        machine.addSymbol(1, "red");
        machine.placeSymbol(1, "red");
        machine.addSymbol(5, "blue");
        assertEquals("red", machine.configuracion()[0]);
    }

    @Test
    public void shouldRemoveExistingSymbol()
    {
        machine.addSymbol(1, "red");
        machine.delSymbol("red");
        assertTrue(machine.ok());
    }

    @Test
    public void shouldNotRemoveNonExistentSymbol()
    {
        machine.delSymbol("red");
        assertFalse(machine.ok());
    }

    @Test
    public void shouldReflectRemovalInSymbolsList()
    {
        machine.addSymbol(1, "red");
        machine.addSymbol(2, "blue");
        machine.delSymbol("red");
        assertEquals(1, machine.symbols().length);
        assertEquals("blue", machine.symbols()[0]);
    }

    @Test
    public void shouldNotPlaceSymbolWithoutWheels()
    {
        machine.placeSymbol(1, "red");
        assertFalse(machine.ok());
    }

    @Test
    public void shouldNotPlaceSymbolWithoutRegisteredSymbols()
    {
        machine.addWheel(1);
        machine.placeSymbol(1, "red");
        assertFalse(machine.ok());
    }

    @Test
    public void shouldNotPlaceNonExistentSymbol()
    {
        machine.addWheel(1);
        machine.addSymbol(1, "red");
        machine.placeSymbol(1, "blue");
        assertFalse(machine.ok());
    }

    @Test
    public void shouldPlaceValidSymbol()
    {
        machine.addWheel(1);
        machine.addSymbol(1, "red");
        machine.addSymbol(2, "blue");
        machine.placeSymbol(1, "blue");
        assertTrue(machine.ok());
        assertEquals("blue", machine.configuracion()[0]);
    }

    @Test
    public void shouldClampWheelBelowMinimumWhenPlacingSymbol()
    {
        machine.addWheel(1);
        machine.addWheel(2);
        machine.addSymbol(1, "red");
        machine.placeSymbol(0, "red");
        assertTrue(machine.ok());
        assertEquals("red", machine.configuracion()[0]);
    }

    @Test
    public void shouldClampWheelAboveMaximumWhenPlacingSymbol()
    {
        machine.addWheel(1);
        machine.addWheel(2);
        machine.addSymbol(1, "red");
        machine.placeSymbol(10, "red");
        assertTrue(machine.ok());
        assertEquals("red", machine.configuracion()[1]);
    }

    @Test
    public void shouldNotPlaceSymbolOnLockedWheel()
    {
        machine.addWheel(1);
        machine.addSymbol(1, "red");
        machine.lock(1);
        machine.placeSymbol(1, "red");
        assertFalse(machine.ok());
    }

    @Test
    public void shouldNotSpinSingleWheelWithoutWheels()
    {
        machine.spin(1);
        assertFalse(machine.ok());
    }

    @Test
    public void shouldNotSpinSingleWheelWithoutSymbols()
    {
        machine.addWheel(1);
        machine.spin(1);
        assertFalse(machine.ok());
    }

    @Test
    public void shouldSpinSingleWheelToRegisteredSymbol()
    {
        machine.addWheel(1);
        machine.addSymbol(1, "red");
        machine.addSymbol(2, "blue");
        machine.spin(1);
        assertTrue(machine.ok());
        String result = machine.configuracion()[0];
        assertTrue(result.equals("red") || result.equals("blue"));
    }

    @Test
    public void shouldNotSpinLockedWheel()
    {
        machine.addWheel(1);
        machine.addSymbol(1, "red");
        machine.placeSymbol(1, "red");
        machine.lock(1);
        machine.spin(1);
        assertFalse(machine.ok());
        assertEquals("red", machine.configuracion()[0]);
    }

    @Test
    public void shouldNotSpinAllWithoutWheels()
    {
        machine.spin();
        assertFalse(machine.ok());
    }

    @Test
    public void shouldNotSpinAllWithoutSymbols()
    {
        machine.addWheel(1);
        machine.spin();
        assertFalse(machine.ok());
    }

    @Test
    public void shouldSpinAllWheelsToRegisteredSymbols()
    {
        machine.addWheel(1);
        machine.addWheel(2);
        machine.addSymbol(1, "red");
        machine.addSymbol(2, "blue");
        machine.spin();
        assertTrue(machine.ok());
        for(String result : machine.configuracion()){
            assertTrue(result.equals("red") || result.equals("blue"));
        }
    }

    @Test
    public void shouldNotChangeLockedWheelWhenSpinningAll()
    {
        machine.addWheel(1);
        machine.addWheel(2);
        machine.addSymbol(1, "red");
        machine.addSymbol(2, "blue");
        machine.placeSymbol(1, "red");
        machine.lock(1);
        machine.spin();
        assertEquals("red", machine.configuracion()[0]);
    }

    @Test
    public void shouldReturnEmptyArrayWhenNoSymbolsRegistered()
    {
        assertEquals(0, machine.symbols().length);
    }

    @Test
    public void shouldReturnSymbolsInInsertionOrder()
    {
        machine.addSymbol(1, "red");
        machine.addSymbol(2, "blue");
        String[] resultado = machine.symbols();
        assertEquals(2, resultado.length);
        assertEquals("red", resultado[0]);
        assertEquals("blue", resultado[1]);
    }

    @Test
    public void shouldReturnZeroDistinctSymbolsWhenEmpty()
    {
        assertEquals(0, machine.distinctSymbolos());
    }

    @Test
    public void shouldReturnCorrectDistinctSymbolCount()
    {
        machine.addSymbol(1, "red");
        machine.addSymbol(2, "blue");
        machine.addSymbol(3, "green");
        assertEquals(3, machine.distinctSymbolos());
    }

    @Test
    public void shouldReturnEmptyConfigurationWhenNoWheels()
    {
        assertEquals(0, machine.configuracion().length);
    }

    @Test
    public void shouldReturnNullForWheelWithoutSymbolAssigned()
    {
        machine.addWheel(1);
        assertNull(machine.configuracion()[0]);
    }

    @Test
    public void shouldReturnCurrentSymbolsInWheelOrder()
    {
        machine.addWheel(1);
        machine.addWheel(2);
        machine.addSymbol(1, "red");
        machine.addSymbol(2, "blue");
        machine.placeSymbol(1, "red");
        machine.placeSymbol(2, "blue");
        String[] config = machine.configuracion();
        assertEquals("red", config[0]);
        assertEquals("blue", config[1]);
    }

    @Test
    public void shouldNotBeJackpotWithoutWheels()
    {
        assertFalse(machine.isJackpot());
    }

    @Test
    public void shouldNotBeJackpotWhenWheelHasNoSymbol()
    {
        machine.addWheel(1);
        assertFalse(machine.isJackpot());
    }

    @Test
    public void shouldBeJackpotWhenAllWheelsMatch()
    {
        machine.addWheel(1);
        machine.addWheel(2);
        machine.addSymbol(1, "red");
        machine.placeSymbol(1, "red");
        machine.placeSymbol(2, "red");
        assertTrue(machine.isJackpot());
    }

    @Test
    public void shouldNotBeJackpotWhenWheelsDiffer()
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
    public void shouldUpdateJackpotStatusWhenDifferentWheelIsRemoved()
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
    public void shouldSucceedWhenMakingMachineVisible()
    {
        machine.addWheel(1);
        machine.makeVisible();
        assertTrue(machine.ok());
    }

    @Test
    public void shouldSucceedWhenMakingMachineInvisible()
    {
        machine.addWheel(1);
        machine.makeVisible();
        machine.makeInvisible();
        assertTrue(machine.ok());
    }

    @Test
    public void shouldClearWheelsOnExit()
    {
        machine.addWheel(1);
        machine.addWheel(2);
        machine.exit();
        machine.delWheel(1);
        assertFalse(machine.ok());
    }

    @Test
    public void shouldClearSymbolsOnExit()
    {
        machine.addSymbol(1, "red");
        machine.addSymbol(2, "blue");
        machine.exit();
        assertEquals(0, machine.symbols().length);
    }

    @Test
    public void shouldAlwaysSucceedOnExit()
    {
        machine.exit();
        assertTrue(machine.ok());
    }

    @Test
    public void shouldBeTrueByDefaultAfterCreation()
    {
        assertTrue(machine.ok());
    }

    @Test
    public void shouldBeFalseAfterFailedOperation()
    {
        machine.delWheel(1);
        assertFalse(machine.ok());
    }

    @Test
    public void shouldBeTrueAfterOperationFollowingAFailure()
    {
        machine.delWheel(1);
        assertFalse(machine.ok());
        machine.addWheel(1);
        assertTrue(machine.ok());
    }
}
