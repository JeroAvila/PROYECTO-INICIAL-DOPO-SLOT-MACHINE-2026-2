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

    // ------------------------------------------------------------------
    // Simbolo shy (timido)
    // ------------------------------------------------------------------

    /**
     * Prueba del simbolo timido: alterna visible/invisible en cada seleccion.
     * Escenario: un simbolo timido nuevo se selecciona tres veces.
     * Resultado esperado: empieza visible, y queda escondido, visible, escondido.
     */
    @Test
    public void shouldToggleShySymbol() {
        ShySymbol symbol = new ShySymbol("red");
        assertFalse(symbol.isHidden());
        symbol.select();
        assertTrue(symbol.isHidden());
        symbol.select();
        assertFalse(symbol.isHidden());
        symbol.select();
        assertTrue(symbol.isHidden());
    }

    /**
     * Prueba de la copia del simbolo timido.
     * Escenario: se esconde un simbolo timido y luego se copia.
     * Resultado esperado: la copia es otro objeto, del mismo color y visible.
     */
    @Test
    public void shouldCopyShySymbolVisible() {
        ShySymbol symbol = new ShySymbol("green");
        symbol.select();
        ShySymbol copy = (ShySymbol) symbol.copy();
        assertNotSame(symbol, copy);
        assertEquals("green", copy.getColor());
        assertFalse(copy.isHidden());
        assertTrue(symbol.isHidden());
    }

    /**
     * Prueba del simbolo timido dentro de la maquina: escondido sigue contando.
     * Escenario: dos ruedas con un unico simbolo timido; se pone en ambas.
     * Resultado esperado: la configuracion muestra su color en las dos y hay jackpot.
     */
    @Test
    public void shouldWinWithShySymbol() {
        Slotmachine machine = new Slotmachine();
        machine.addSymbol(1, "red", "shy");
        machine.addWheels(2);
        machine.placeSymbol(1, "red");
        machine.placeSymbol(2, "red");
        assertTrue(machine.ok());
        assertTrue(machine.isJackpot());
    }

    // ------------------------------------------------------------------
    // Tipos de rueda
    // ------------------------------------------------------------------

    /**
     * Prueba de los tipos de rueda disponibles.
     * Escenario: se agrega una rueda de cada tipo (la crazy trae 3 mas).
     * Resultado esperado: wheelTypes los devuelve en el orden de las ruedas.
     */
    @Test
    public void shouldCreateEveryWheelType() {
        Slotmachine machine = new Slotmachine();
        machine.addWheel(1, "normal");
        machine.addWheel(2, "lefty");
        machine.addWheel(3, "rebel");
        machine.addWheel(4, "crazy");
        assertTrue(machine.ok());
        String[] types = machine.wheelTypes();
        assertEquals(7, types.length); // la crazy trae 3 ruedas mas
        assertEquals("normal", types[0]);
        assertEquals("lefty", types[1]);
        assertEquals("rebel", types[2]);
        assertEquals("crazy", types[3]);
        assertEquals("crazy", types[6]);
    }

    /**
     * Prueba del tipo de rueda desconocido.
     * Escenario: se agrega una rueda con un tipo que no existe.
     * Resultado esperado: la operacion falla y no se agrega ninguna rueda.
     */
    @Test
    public void shouldNotAddUnknownWheelType() {
        Slotmachine machine = new Slotmachine();
        machine.addWheel(1, "gigante");
        assertFalse(machine.ok());
        assertEquals(0, machine.wheelTypes().length);
    }

    /**
     * Prueba del tipo nulo (la excepcion se atrapa dentro de la maquina).
     * Escenario: se agrega una rueda y un simbolo con tipo null.
     * Resultado esperado: ambas operaciones fallan, sin lanzar NullPointerException.
     */
    @Test
    public void shouldNotAddNullTypes() {
        Slotmachine machine = new Slotmachine();
        machine.addWheel(1, null);
        assertFalse(machine.ok());
        machine.addSymbol(1, "red", null);
        assertFalse(machine.ok());
        assertEquals(0, machine.wheelTypes().length);
        assertEquals(0, machine.symbols().length);
    }

    /**
     * Prueba de la excepcion no chequeada del simbolo.
     * Escenario: se crea un simbolo con color null.
     * Resultado esperado: se lanza IllegalArgumentException.
     */
    @Test(expected = IllegalArgumentException.class)
    public void shouldRejectSymbolWithNullColor() {
        new NormalSymbol(null);
    }

    // ------------------------------------------------------------------
    // Rueda lefty
    // ------------------------------------------------------------------

    /**
     * Prueba de la rueda lefty: copia a la rueda de su izquierda.
     * Escenario: una rueda normal y una lefty a su derecha, con varios simbolos;
     * se gira la rueda lefty muchas veces despues de poner un simbolo a la normal.
     * Resultado esperado: la lefty siempre muestra el mismo simbolo que la normal.
     */
    @Test
    public void shouldCopyLeftNeighborInLeftyWheel() {
        Slotmachine machine = new Slotmachine();
        machine.addSymbol(1, "red");
        machine.addSymbol(2, "blue");
        machine.addSymbol(3, "green");
        machine.addWheel(1, "normal");
        machine.addWheel(2, "lefty");
        for(int i = 0; i < 10; i++){
            machine.spin(1);
            machine.spin(2);
            assertTrue(machine.ok());
            assertEquals(machine.configuracion()[0], machine.configuracion()[1]);
        }
    }

    /**
     * Prueba de la cascada con ruedas lefty.
     * Escenario: normal, lefty, lefty; se giran todas las ruedas.
     * Resultado esperado: las tres muestran lo mismo y hay jackpot.
     */
    @Test
    public void shouldPropagateLeftyCopyWhenSpinningAll() {
        Slotmachine machine = new Slotmachine();
        machine.addSymbol(1, "red");
        machine.addSymbol(2, "blue");
        machine.addSymbol(3, "green");
        machine.addWheel(1, "normal");
        machine.addWheel(2, "lefty");
        machine.addWheel(3, "lefty");
        machine.spin();
        assertTrue(machine.ok());
        assertTrue(machine.isJackpot());
    }

    /**
     * Prueba de la lefty sin vecina a la izquierda.
     * Escenario: una unica rueda lefty con un simbolo.
     * Resultado esperado: gira como una normal y queda mostrando el simbolo.
     */
    @Test
    public void shouldSpinLeftyWheelWithoutLeftNeighbor() {
        Slotmachine machine = new Slotmachine();
        machine.addSymbol(1, "red");
        machine.addWheel(1, "lefty");
        machine.spin(1);
        assertTrue(machine.ok());
        assertEquals("red", machine.configuracion()[0]);
    }

    /**
     * Prueba de que la vecina se recalcula al intercambiar ruedas.
     * Escenario: normal y lefty; se intercambian y se gira la lefty (ahora primera).
     * Resultado esperado: la lefty gira sola y la operacion es exitosa.
     */
    @Test
    public void shouldUpdateLeftNeighborAfterSwap() {
        Slotmachine machine = new Slotmachine();
        machine.addSymbol(1, "red");
        machine.addSymbol(2, "blue");
        machine.addWheel(1, "normal");
        machine.addWheel(2, "lefty");
        machine.swap(1, 2);
        assertTrue(machine.ok());
        assertEquals("lefty", machine.wheelTypes()[0]);
        machine.spin(1);
        assertTrue(machine.ok());
        assertNotNull(machine.configuracion()[0]);
    }

    // ------------------------------------------------------------------
    // Rueda rebel
    // ------------------------------------------------------------------

    /**
     * Prueba de la rueda rebelde: no se deja bloquear.
     * Escenario: una rueda rebelde; se intenta bloquear y luego girar.
     * Resultado esperado: bloquear falla, y como no quedo bloqueada, gira bien.
     */
    @Test
    public void shouldNotLockRebelWheel() {
        Slotmachine machine = new Slotmachine();
        machine.addSymbol(1, "red");
        machine.addWheel(1, "rebel");
        machine.lock(1);
        assertFalse(machine.ok());
        machine.spin(1);
        assertTrue(machine.ok());
        assertEquals("red", machine.configuracion()[0]);
    }

    /**
     * Prueba de la rueda rebelde: no se deja intercambiar.
     * Escenario: una normal y una rebelde; se intenta intercambiarlas.
     * Resultado esperado: falla y el orden de los tipos no cambia.
     */
    @Test
    public void shouldNotSwapRebelWheel() {
        Slotmachine machine = new Slotmachine();
        machine.addWheel(1, "normal");
        machine.addWheel(2, "rebel");
        machine.swap(1, 2);
        assertFalse(machine.ok());
        assertEquals("normal", machine.wheelTypes()[0]);
        assertEquals("rebel", machine.wheelTypes()[1]);
    }

    /**
     * Prueba de la rueda rebelde: no se deja eliminar.
     * Escenario: una normal y una rebelde; se intenta eliminar la rebelde.
     * Resultado esperado: falla y la maquina sigue con dos ruedas.
     */
    @Test
    public void shouldNotDeleteRebelWheel() {
        Slotmachine machine = new Slotmachine();
        machine.addWheel(1, "normal");
        machine.addWheel(2, "rebel");
        machine.delWheel(2);
        assertFalse(machine.ok());
        assertEquals(2, machine.wheelTypes().length);
    }

    /**
     * Prueba de que las demas ruedas si se dejan eliminar.
     * Escenario: una normal y una rebelde; se elimina la normal.
     * Resultado esperado: es exitoso y solo queda la rebelde.
     */
    @Test
    public void shouldStillDeleteNormalWheelNextToRebel() {
        Slotmachine machine = new Slotmachine();
        machine.addWheel(1, "normal");
        machine.addWheel(2, "rebel");
        machine.delWheel(1);
        assertTrue(machine.ok());
        assertEquals(1, machine.wheelTypes().length);
        assertEquals("rebel", machine.wheelTypes()[0]);
    }

    // ------------------------------------------------------------------
    // Rueda crazy (tipo nuevo propuesto por nosotros)
    // ------------------------------------------------------------------

    /**
     * Prueba de que la rueda loca llega con 3 mas.
     * Escenario: se agrega una sola rueda crazy a una maquina vacia.
     * Resultado esperado: la maquina queda con 4 ruedas, todas crazy.
     */
    @Test
    public void shouldAddCrazyWheelWithThreeMore() {
        Slotmachine machine = new Slotmachine();
        machine.addWheel(1, "crazy");
        assertTrue(machine.ok());
        String[] types = machine.wheelTypes();
        assertEquals(4, types.length);
        for(int i = 0; i < types.length; i++){
            assertEquals("crazy", types[i]);
        }
    }

    /**
     * Prueba de n + 3: girar una rueda del grupo gira las 4, y solo esas.
     * Escenario: una normal y un grupo crazy; se gira una rueda del grupo.
     * Resultado esperado: las 4 crazy muestran un simbolo y la normal sigue sin nada.
     */
    @Test
    public void shouldSpinWholeCrazyGroup() {
        Slotmachine machine = new Slotmachine();
        machine.addSymbol(1, "red");
        machine.addSymbol(2, "blue");
        machine.addWheel(1, "normal");
        machine.addWheel(2, "crazy");
        machine.spin(3);
        assertTrue(machine.ok());
        String[] config = machine.configuracion();
        assertNull(config[0]);
        for(int i = 1; i < 5; i++){
            assertNotNull(config[i]);
        }
    }

    /**
     * Prueba de que poner un simbolo afecta a todo el grupo por igual.
     * Escenario: una normal y un grupo crazy; se pone rojo en una rueda del grupo.
     * Resultado esperado: las 4 crazy muestran rojo y la normal sigue sin nada.
     */
    @Test
    public void shouldPlaceSymbolInWholeCrazyGroup() {
        Slotmachine machine = new Slotmachine();
        machine.addSymbol(1, "red");
        machine.addSymbol(2, "blue");
        machine.addWheel(1, "normal");
        machine.addWheel(2, "crazy");
        machine.placeSymbol(4, "red");
        String[] config = machine.configuracion();
        assertNull(config[0]);
        for(int i = 1; i < 5; i++){
            assertEquals("red", config[i]);
        }
    }

    /**
     * Prueba de que bloquear una rueda bloquea a todo el grupo.
     * Escenario: una normal y un grupo crazy; se bloquea una rueda del grupo.
     * Resultado esperado: ninguna del grupo gira; la normal si; al desbloquear
     * una del grupo, todas vuelven a girar.
     */
    @Test
    public void shouldLockAndUnlockWholeCrazyGroup() {
        Slotmachine machine = new Slotmachine();
        machine.addSymbol(1, "red");
        machine.addWheel(1, "normal");
        machine.addWheel(2, "crazy");
        machine.lock(3);
        assertTrue(machine.ok());
        machine.spin(2);
        assertFalse(machine.ok());
        machine.spin(5);
        assertFalse(machine.ok());
        machine.spin(1);
        assertTrue(machine.ok());
        machine.unlock(4);
        machine.spin(2);
        assertTrue(machine.ok());
        assertEquals("red", machine.configuracion()[1]);
        assertEquals("red", machine.configuracion()[4]);
    }

    /**
     * Prueba de que eliminar una rueda elimina a todo el grupo.
     * Escenario: una normal y un grupo crazy; se bloquea el grupo, se intenta
     * eliminar, se desbloquea y se elimina desde otra rueda del grupo.
     * Resultado esperado: bloqueado no se elimina; desbloqueado se van las 4 y queda la normal.
     */
    @Test
    public void shouldDeleteWholeCrazyGroup() {
        Slotmachine machine = new Slotmachine();
        machine.addWheel(1, "normal");
        machine.addWheel(2, "crazy");
        machine.lock(2);
        machine.delWheel(5);
        assertFalse(machine.ok());
        assertEquals(5, machine.wheelTypes().length);
        machine.unlock(2);
        machine.delWheel(5);
        assertTrue(machine.ok());
        assertEquals(1, machine.wheelTypes().length);
        assertEquals("normal", machine.wheelTypes()[0]);
    }

    /**
     * Prueba de que el grupo se intercambia completo.
     * Escenario: una normal y un grupo crazy; se intercambia la normal con una del grupo.
     * Resultado esperado: el grupo queda primero y la normal al final.
     */
    @Test
    public void shouldSwapWholeCrazyGroupWithNormalWheel() {
        Slotmachine machine = new Slotmachine();
        machine.addWheel(1, "normal");
        machine.addWheel(2, "crazy");
        machine.swap(1, 3);
        assertTrue(machine.ok());
        String[] types = machine.wheelTypes();
        assertEquals(5, types.length);
        for(int i = 0; i < 4; i++){
            assertEquals("crazy", types[i]);
        }
        assertEquals("normal", types[4]);
    }

    /**
     * Prueba del intercambio entre dos grupos crazy.
     * Escenario: dos grupos crazy, uno rojo y otro azul; se intercambian.
     * Resultado esperado: los 4 azules quedan primero y los 4 rojos despues.
     */
    @Test
    public void shouldSwapTwoCrazyGroups() {
        Slotmachine machine = new Slotmachine();
        machine.addSymbol(1, "red");
        machine.addSymbol(2, "blue");
        machine.addWheel(1, "crazy");
        machine.addWheel(5, "crazy");
        machine.placeSymbol(1, "red");
        machine.placeSymbol(5, "blue");
        machine.swap(2, 7);
        assertTrue(machine.ok());
        String[] config = machine.configuracion();
        for(int i = 0; i < 4; i++){
            assertEquals("blue", config[i]);
            assertEquals("red", config[i + 4]);
        }
    }

    /**
     * Prueba de que un grupo no se parte al agregar una rueda en medio.
     * Escenario: un grupo crazy y se agrega una normal en la posicion 3 (en medio del grupo).
     * Resultado esperado: la normal queda despues del grupo, no dentro.
     */
    @Test
    public void shouldNotSplitCrazyGroupWhenAddingInside() {
        Slotmachine machine = new Slotmachine();
        machine.addWheel(1, "crazy");
        machine.addWheel(3, "normal");
        String[] types = machine.wheelTypes();
        assertEquals(5, types.length);
        for(int i = 0; i < 4; i++){
            assertEquals("crazy", types[i]);
        }
        assertEquals("normal", types[4]);
    }
}