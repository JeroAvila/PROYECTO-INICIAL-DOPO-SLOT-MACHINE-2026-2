import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Tests de Slotmachine para el ciclo 2, cubriendo:
 * swap(wheel1, wheel2), lock(wheel), unlock(wheel),
 * spin(wheel, steps) y spin(String[] setSymbols).
 *
 * @author Laura Juliana Parra Velandia
 * @author Thomas Jeronimo Avila Castillo
 * @version 1.0
 */
public class SlotmachineC2Test
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
    public void shouldNotSwapWithoutWheels()
    {
        machine.swap(1, 2);
        assertFalse(machine.ok());
    }

    @Test
    public void shouldNotSwapWhenWheel1IsBelowMinimum()
    {
        machine.addWheel(1);
        machine.addWheel(2);
        machine.swap(0, 2);
        assertFalse(machine.ok());
    }

    @Test
    public void shouldNotSwapWhenWheel1IsAboveMaximum()
    {
        machine.addWheel(1);
        machine.addWheel(2);
        machine.swap(10, 2);
        assertFalse(machine.ok());
    }

    @Test
    public void shouldNotSwapWhenWheel2IsBelowMinimum()
    {
        machine.addWheel(1);
        machine.addWheel(2);
        machine.swap(1, 0);
        assertFalse(machine.ok());
    }

    @Test
    public void shouldNotSwapWhenWheel2IsAboveMaximum()
    {
        machine.addWheel(1);
        machine.addWheel(2);
        machine.swap(1, 10);
        assertFalse(machine.ok());
    }

    @Test
    public void shouldNotSwapWhenWheel1IsLocked()
    {
        machine.addWheel(1);
        machine.addWheel(2);
        machine.lock(1);
        machine.swap(1, 2);
        assertFalse(machine.ok());
    }

    @Test
    public void shouldNotSwapWhenWheel2IsLocked()
    {
        machine.addWheel(1);
        machine.addWheel(2);
        machine.lock(2);
        machine.swap(1, 2);
        assertFalse(machine.ok());
    }

    @Test
    public void shouldSwapTwoWheelsSuccessfully()
    {
        machine.addWheel(1);
        machine.addWheel(2);
        machine.addSymbol(1, "red");
        machine.addSymbol(2, "blue");
        machine.placeSymbol(1, "red");
        machine.placeSymbol(2, "blue");
        machine.swap(1, 2);
        assertTrue(machine.ok());
        assertEquals("blue", machine.configuracion()[0]);
        assertEquals("red", machine.configuracion()[1]);
    }

    @Test
    public void shouldKeepConfigurationUnchangedWhenSwapFails()
    {
        machine.addWheel(1);
        machine.addWheel(2);
        machine.addSymbol(1, "red");
        machine.addSymbol(2, "blue");
        machine.placeSymbol(1, "red");
        machine.placeSymbol(2, "blue");
        machine.lock(1);
        machine.swap(1, 2);
        assertFalse(machine.ok());
        assertEquals("red", machine.configuracion()[0]);
        assertEquals("blue", machine.configuracion()[1]);
    }

    @Test
    public void shouldSucceedWhenSwappingWheelWithItself()
    {
        machine.addWheel(1);
        machine.addSymbol(1, "red");
        machine.placeSymbol(1, "red");
        machine.swap(1, 1);
        assertTrue(machine.ok());
        assertEquals("red", machine.configuracion()[0]);
    }

    @Test
    public void shouldNotLockWithoutWheels()
    {
        machine.lock(1);
        assertFalse(machine.ok());
    }

    @Test
    public void shouldClampPositionBelowMinimumWhenLocking()
    {
        machine.addWheel(1);
        machine.addWheel(2);
        machine.lock(0);
        assertTrue(machine.ok());
        // la rueda 1 debe haber quedado bloqueada
        machine.delWheel(1);
        assertFalse(machine.ok());
    }

    @Test
    public void shouldClampPositionAboveMaximumWhenLocking()
    {
        machine.addWheel(1);
        machine.addWheel(2);
        machine.lock(10);
        assertTrue(machine.ok());
        // la rueda 2 debe haber quedado bloqueada
        machine.delWheel(2);
        assertFalse(machine.ok());
    }

    @Test
    public void shouldSucceedWhenLockingValidWheel()
    {
        machine.addWheel(1);
        machine.lock(1);
        assertTrue(machine.ok());
    }

    @Test
    public void shouldPreventSpinOnLockedWheel()
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
    public void shouldAllowLockingAlreadyLockedWheel()
    {
        machine.addWheel(1);
        machine.lock(1);
        machine.lock(1);
        assertTrue(machine.ok());
    }

    @Test
    public void shouldNotUnlockWithoutWheels()
    {
        machine.unlock(1);
        assertFalse(machine.ok());
    }

    @Test
    public void shouldClampPositionBelowMinimumWhenUnlocking()
    {
        machine.addWheel(1);
        machine.addWheel(2);
        machine.addSymbol(1, "red");
        machine.lock(1);
        machine.unlock(0);
        assertTrue(machine.ok());
        // si desbloqueo la rueda 1, ahora deberia poder girar
        machine.spin(1);
        assertTrue(machine.ok());
    }

    @Test
    public void shouldClampPositionAboveMaximumWhenUnlocking()
    {
        machine.addWheel(1);
        machine.addWheel(2);
        machine.addSymbol(1, "red");
        machine.lock(2);
        machine.unlock(10);
        assertTrue(machine.ok());
        // si desbloqueo la rueda 2, ahora deberia poder girar
        machine.spin(2);
        assertTrue(machine.ok());
    }

    @Test
    public void shouldSucceedWhenUnlockingValidWheel()
    {
        machine.addWheel(1);
        machine.lock(1);
        machine.unlock(1);
        assertTrue(machine.ok());
    }

    @Test
    public void shouldAllowSpinAfterUnlockingWheel()
    {
        machine.addWheel(1);
        machine.addSymbol(1, "red");
        machine.placeSymbol(1, "red");
        machine.lock(1);
        machine.spin(1);
        assertFalse(machine.ok());
        machine.unlock(1);
        machine.spin(1);
        assertTrue(machine.ok());
    }

    @Test
    public void shouldAllowUnlockingAlreadyUnlockedWheel()
    {
        machine.addWheel(1);
        machine.unlock(1);
        assertTrue(machine.ok());
    }

    @Test
    public void shouldNotSpinWithStepsWithoutWheels()
    {
        machine.spin(1, 3);
        assertFalse(machine.ok());
    }

    @Test
    public void shouldNotSpinWithStepsWithoutSymbols()
    {
        machine.addWheel(1);
        machine.spin(1, 3);
        assertFalse(machine.ok());
    }

    @Test
    public void shouldNotSpinWithZeroSteps()
    {
        machine.addWheel(1);
        machine.addSymbol(1, "red");
        machine.spin(1, 0);
        assertFalse(machine.ok());
    }

    @Test
    public void shouldNotSpinWithNegativeSteps()
    {
        machine.addWheel(1);
        machine.addSymbol(1, "red");
        machine.spin(1, -2);
        assertFalse(machine.ok());
    }

    @Test
    public void shouldNotSpinLockedWheelWithSteps()
    {
        machine.addWheel(1);
        machine.addSymbol(1, "red");
        machine.placeSymbol(1, "red");
        machine.lock(1);
        machine.spin(1, 3);
        assertFalse(machine.ok());
        assertEquals("red", machine.configuracion()[0]);
    }

    @Test
    public void shouldClampWheelBelowMinimumWhenSpinningWithSteps()
    {
        machine.addWheel(1);
        machine.addWheel(2);
        machine.addSymbol(1, "red");
        machine.addSymbol(2, "blue");
        machine.spin(0, 3);
        assertTrue(machine.ok());
        String result = machine.configuracion()[0];
        assertTrue(result.equals("red") || result.equals("blue"));
    }

    @Test
    public void shouldClampWheelAboveMaximumWhenSpinningWithSteps()
    {
        machine.addWheel(1);
        machine.addWheel(2);
        machine.addSymbol(1, "red");
        machine.addSymbol(2, "blue");
        machine.spin(10, 3);
        assertTrue(machine.ok());
        String result = machine.configuracion()[1];
        assertTrue(result.equals("red") || result.equals("blue"));
    }

    @Test
    public void shouldResultInRegisteredSymbolAfterSpinningWithSteps()
    {
        machine.addWheel(1);
        machine.addSymbol(1, "red");
        machine.addSymbol(2, "blue");
        machine.spin(1, 5);
        assertTrue(machine.ok());
        String result = machine.configuracion()[0];
        assertTrue(result.equals("red") || result.equals("blue"));
    }

    @Test
    public void shouldNotAffectOtherWheelsWhenSpinningOneWheelWithSteps()
    {
        machine.addWheel(1);
        machine.addWheel(2);
        machine.addSymbol(1, "red");
        machine.addSymbol(2, "blue");
        machine.placeSymbol(2, "blue");
        machine.spin(1, 4);
        assertTrue(machine.ok());
        assertEquals("blue", machine.configuracion()[1]);
    }

    @Test
    public void shouldSucceedWithMultipleSteps()
    {
        machine.addWheel(1);
        machine.addSymbol(1, "red");
        machine.spin(1, 10);
        assertTrue(machine.ok());
        assertEquals("red", machine.configuracion()[0]);
    }

    @Test
    public void shouldNotSetConfigurationWithoutWheels()
    {
        machine.spin(new String[]{"red"});
        assertFalse(machine.ok());
    }

    @Test
    public void shouldNotSetConfigurationWithoutSymbols()
    {
        machine.addWheel(1);
        machine.spin(new String[]{"red"});
        assertFalse(machine.ok());
    }

    @Test
    public void shouldNotSetConfigurationWhenArrayIsTooShort()
    {
        machine.addWheel(1);
        machine.addWheel(2);
        machine.addSymbol(1, "red");
        machine.spin(new String[]{"red"});
        assertFalse(machine.ok());
    }

    @Test
    public void shouldNotSetConfigurationWhenArrayIsTooLong()
    {
        machine.addWheel(1);
        machine.addSymbol(1, "red");
        machine.spin(new String[]{"red", "red"});
        assertFalse(machine.ok());
    }

    @Test
    public void shouldNotSetConfigurationWithUnregisteredColor()
    {
        machine.addWheel(1);
        machine.addSymbol(1, "red");
        machine.spin(new String[]{"blue"});
        assertFalse(machine.ok());
    }

    @Test
    public void shouldSetConfigurationSuccessfully()
    {
        machine.addWheel(1);
        machine.addWheel(2);
        machine.addSymbol(1, "red");
        machine.addSymbol(2, "blue");
        machine.spin(new String[]{"blue", "red"});
        assertTrue(machine.ok());
        assertEquals("blue", machine.configuracion()[0]);
        assertEquals("red", machine.configuracion()[1]);
    }

    @Test
    public void shouldSkipLockedWheelsWhenSettingConfiguration()
    {
        machine.addWheel(1);
        machine.addWheel(2);
        machine.addSymbol(1, "red");
        machine.addSymbol(2, "blue");
        machine.placeSymbol(1, "red");
        machine.lock(1);
        machine.spin(new String[]{"blue", "blue"});
        assertTrue(machine.ok());
        assertEquals("red", machine.configuracion()[0]);
        assertEquals("blue", machine.configuracion()[1]);
    }

    @Test
    public void shouldSucceedButNotChangeAnythingWhenAllWheelsLocked()
    {
        machine.addWheel(1);
        machine.addSymbol(1, "red");
        machine.addSymbol(2, "blue");
        machine.placeSymbol(1, "red");
        machine.lock(1);
        machine.spin(new String[]{"blue"});
        assertTrue(machine.ok());
        assertEquals("red", machine.configuracion()[0]);
    }

    @Test
    public void shouldKeepConfigurationUnchangedWhenSetConfigurationFails()
    {
        machine.addWheel(1);
        machine.addSymbol(1, "red");
        machine.addSymbol(2, "blue");
        machine.placeSymbol(1, "red");
        machine.spin(new String[]{"blue", "blue"}); // longitud incorrecta
        assertFalse(machine.ok());
        assertEquals("red", machine.configuracion()[0]);
    }

    @Test
    public void shouldUpdateJackpotAfterSettingMatchingConfiguration()
    {
        machine.addWheel(1);
        machine.addWheel(2);
        machine.addSymbol(1, "red");
        machine.addSymbol(2, "blue");
        machine.spin(new String[]{"red", "blue"});
        assertFalse(machine.isJackpot());
        machine.spin(new String[]{"red", "red"});
        assertTrue(machine.isJackpot());
    }
}
