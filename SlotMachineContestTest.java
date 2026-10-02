import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Pruebas de SlotMachineContest.
 */
public class SlotMachineContestTest {
    private static final int ACTION_LIMIT = 10000;
    
    /**
     * Prueba principal del ciclo: solve debe dejar la maquina en jackpot
     * sin importar cuantas ruedas tenga.
     * Escenario: para cada n valido (3 a 59) se ejecuta solve tres veces,
     * porque cada ejecucion arranca con las ruedas en posiciones aleatorias.
     * Resultado esperado: en todas las ejecuciones la maquina queda en jackpot.
     */
    @Test
    public void shouldWinAllN() {
        for(int n = 3; n <= 59; n++){
            for(int rep = 0; rep < 3; rep++){
                SlotMachineContest.solve(n);
                assertTrue(SlotMachineContest.isJackpot());
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
        for(int n = 3; n <= 59; n++){
            for(int rep = 0; rep < 3; rep++){
                int[][] actions = SlotMachineContest.solve(n);
                assertTrue(actions.length <= 3 * n * n);
            }
        }
    }

    @Test
    public void shouldWinMin() {
        for(int rep = 0; rep < 100; rep++){
            SlotMachineContest.solve(3);
            assertTrue(SlotMachineContest.isJackpot());
        }
    }

    @Test
    public void shouldWinAnyStart() {
        for(int rep = 0; rep < 100; rep++){
            SlotMachineContest.solve(4);
            assertTrue(SlotMachineContest.isJackpot());
        }
    }

    @Test
    public void shouldShowCatalogColor() {
        SlotMachineContest.solve(6);
        String shown = SlotMachineContest.configuration()[0];
        String[] catalog = SlotMachineContest.machine().symbols();
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
        SlotMachineContest.solve(1);
        assertEquals(3, SlotMachineContest.configuration().length);
        assertTrue(SlotMachineContest.isJackpot());
        SlotMachineContest.solve(-5);
        assertEquals(3, SlotMachineContest.configuration().length);
        assertTrue(SlotMachineContest.isJackpot());
    }

    @Test
    public void shouldUseMax() {
        SlotMachineContest.solve(100);
        assertEquals(59, SlotMachineContest.configuration().length);
        assertTrue(SlotMachineContest.isJackpot());
    }

    @Test
    public void shouldSolveTwice() {
        SlotMachineContest.solve(5);
        SlotMachineContest.solve(7);
        assertEquals(7, SlotMachineContest.configuration().length);
        assertTrue(SlotMachineContest.isJackpot());
    }

    @Test
    public void shouldNotMixColors() {
        SlotMachineContest.solve(8);
        String[] config = SlotMachineContest.configuration();
        assertNotNull(config[0]);
        for(int i = 1; i < config.length; i++){
            assertEquals(config[0], config[i]);
        }
    }

    @Test
    public void shouldNotLoseWheels() {
        SlotMachineContest.solve(6);
        assertEquals(6, SlotMachineContest.configuration().length);
    }

    @Test
    public void shouldNotLoseSymbols() {
        SlotMachineContest.solve(6);
        assertEquals(6, SlotMachineContest.machine().symbols().length);
    }

    @Test
    public void shouldNotFail() {
        for(int n = 3; n <= 59; n++){
            SlotMachineContest.solve(n);
            assertTrue(SlotMachineContest.machine().ok());
        }
    }

    @Test
    public void shouldNotReuseMachine() {
        SlotMachineContest.solve(5);
        Slotmachine first = SlotMachineContest.machine();
        SlotMachineContest.solve(5);
        assertNotSame(first, SlotMachineContest.machine());
    }
    
    /**
     * Prueba de aceptacion
     * escerario. el usuario pide resolver maquinas de varios sizes
     * desde la mas pequeña hasta la mas grande en todos los casos
     * debe dar jackpot
     */
    @Test
    public void shouldSolveMachineOfEverySize(){
        int[] sizes = {3, 4, 5, 10, 25, 50};
        
        for (int i = 0; i < sizes.length; i++){
            int[][] actions = SlotMachineContest.solve(sizes[i]);
            
            assertTrue("No gano con n=" + sizes[i], SlotMachineContest.isJackpot());
            assertTrue("Se paso del limite con n=" + sizes[i], 
                actions.length <= ACTION_LIMIT);
            assertEquals(sizes[i], SlotMachineContest.configuration().length);
        }
    }
    
    /**
     * prueba de aceptacion gana siempre no por suerte
     * escenario. la maquina se inicia al azar, si se gana no demuestra nada
     * posible casualidad, para eso se hacen 30 maquinas del mismo size
     * y se exige ganar todas y se registra la peor partida con costo de las
     * acciones.
     */
    @Test
    public void shouldWinEveryTimeAndNotByLuck(){
        int attempts = 30;
        int wins = 0;
        int worstCase = 0;
        
        for (int i = 0; i < attempts; i++){
            int[][] actions = SlotMachineContest.solve(8);
            if(SlotMachineContest.isJackpot()){
                wins++;
            }
            if(actions.length > worstCase){
                worstCase = actions.length;
            }
        }
        assertEquals("Perdio" + (attempts - wins) + " de " + attempts,
            attempts, wins);
        assertTrue("La peor partida uso " + worstCase + "acciones",
            worstCase <= ACTION_LIMIT);
            
    }
}