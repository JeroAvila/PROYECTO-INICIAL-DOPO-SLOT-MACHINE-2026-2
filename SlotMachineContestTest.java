import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Pruebas de SlotMachineContest.
 */
public class SlotMachineContestTest {

    /**
     * Prueba principal del ciclo: solve debe dejar la maquina en jackpot
     * sin importar cuantas ruedas tenga.
     * Escenario: para cada n valido (3 a 59) se ejecuta solve tres veces,
     * porque cada ejecucion arranca con las ruedas en posiciones aleatorias.
     * Resultado esperado: en todas las ejecuciones la maquina queda en jackpot.
     */
    @Test
    public void shouldWinAllN() {
        SlotMachineContest contest = new SlotMachineContest();
        for(int n = 3; n <= 59; n++){
            for(int rep = 0; rep < 3; rep++){
                contest.solve(n);
                assertTrue(contest.isJackpot());
            }
        }
    }

    /**
     * Prueba del limite de queries: el problema es interactivo, asi que
     * resolverlo con demasiadas consultas equivale a no resolverlo.
     * Escenario: para cada n valido (3 a 59) se ejecuta solve tres veces
     * con posiciones iniciales aleatorias.
     * Resultado esperado: las queries usadas nunca superan 3n^2, la cota
     * que da el analisis oficial de los jueces como holgada.
     */
    @Test
    public void shouldNotExceedLimit() {
        SlotMachineContest contest = new SlotMachineContest();
        for(int n = 3; n <= 59; n++){
            for(int rep = 0; rep < 3; rep++){
                int queries = contest.solve(n);
                assertTrue(queries <= 3 * n * n);
            }
        }
    }

    @Test
    public void shouldWinMin() {
        SlotMachineContest contest = new SlotMachineContest();
        for(int rep = 0; rep < 100; rep++){
            contest.solve(3);
            assertTrue(contest.isJackpot());
        }
    }

    @Test
    public void shouldWinAnyStart() {
        SlotMachineContest contest = new SlotMachineContest();
        for(int rep = 0; rep < 100; rep++){
            contest.solve(4);
            assertTrue(contest.isJackpot());
        }
    }

    @Test
    public void shouldShowCatalogColor() {
        SlotMachineContest contest = new SlotMachineContest();
        contest.solve(6);
        String shown = contest.configuration()[0];
        String[] catalog = contest.machine().symbols();
        boolean found = false;
        for(int i = 0; i < catalog.length; i++){
            if(catalog[i].equals(shown)){
                found = true;
            }
        }
        assertTrue(found);
    }

    @Test
    public void shouldUseMin() {
        SlotMachineContest contest = new SlotMachineContest();
        contest.solve(1);
        assertEquals(3, contest.configuration().length);
        assertTrue(contest.isJackpot());
        contest.solve(-5);
        assertEquals(3, contest.configuration().length);
        assertTrue(contest.isJackpot());
    }

    @Test
    public void shouldUseMax() {
        SlotMachineContest contest = new SlotMachineContest();
        contest.solve(100);
        assertEquals(59, contest.configuration().length);
        assertTrue(contest.isJackpot());
    }

    @Test
    public void shouldSolveTwice() {
        SlotMachineContest contest = new SlotMachineContest();
        contest.solve(5);
        contest.solve(7);
        assertEquals(7, contest.configuration().length);
        assertTrue(contest.isJackpot());
    }

    @Test
    public void shouldNotMixColors() {
        SlotMachineContest contest = new SlotMachineContest();
        contest.solve(8);
        String[] config = contest.configuration();
        assertNotNull(config[0]);
        for(int i = 1; i < config.length; i++){
            assertEquals(config[0], config[i]);
        }
    }

    @Test
    public void shouldNotLoseWheels() {
        SlotMachineContest contest = new SlotMachineContest();
        contest.solve(6);
        assertEquals(6, contest.configuration().length);
    }

    @Test
    public void shouldNotLoseSymbols() {
        SlotMachineContest contest = new SlotMachineContest();
        contest.solve(6);
        assertEquals(6, contest.machine().symbols().length);
    }

    @Test
    public void shouldNotFail() {
        SlotMachineContest contest = new SlotMachineContest();
        for(int n = 3; n <= 59; n++){
            contest.solve(n);
            assertTrue(contest.machine().ok());
        }
    }

    @Test
    public void shouldNotReuseMachine() {
        SlotMachineContest contest = new SlotMachineContest();
        contest.solve(5);
        Slotmachine first = contest.machine();
        contest.solve(5);
        assertNotSame(first, contest.machine());
    }
}