import java.util.ArrayList;

/**
 * Resuelve el problema Slot Machine (ICPC WF 2025) sobre una Slotmachine.
 * @author Laura Juliana Parra Velandia
 * @author Thomas Jeronimo Avila Castillo
 * @version 1.2
 */
public class SlotMachineContest {
    private static Slotmachine machine;
    private static boolean show;
    private static ArrayList<int[]> actions;

    /**
     * Crea una maquina con n ruedas y la deja en jackpot.
     * @param n cantidad de ruedas y simbolos, minimo 3 y maximo 59
     * @return secuencia de acciones {rueda, pasos} hechas hasta llegar al jackpot
     */
    public static int[][] solve(int n) {
        machine = new Slotmachine(n);
        if(show){
            machine.makeVisible();
        }
        n = machine.symbols().length;
        actions = new ArrayList<>();
        makeDistinct(n);
        ArrayList<Integer> order = findOrder(n);
        align(order, n);
        int[][] result = new int[actions.size()][];
        for(int i = 0; i < actions.size(); i++){
            result[i] = actions.get(i);
        }
        return result;
    }

    /**
     * Deja cada rueda en la posicion que maximiza los simbolos distintos.
     * @param n cantidad de ruedas
     */
    private static void makeDistinct(int n) {
        for(int i = 1; i <= n; i++){
            int best = 0;
            int bestStep = 0;
            for(int step = 1; step <= n; step++){
                int k = rotate(i, 1);
                if(k > best){
                    best = k;
                    bestStep = step;
                }
            }
            rotate(i, bestStep);
        }
    }

    /**
     * Halla el orden de las ruedas segun el simbolo que muestran.
     * @param n cantidad de ruedas
     * @return lista con las ruedas en orden; la de la posicion t muestra el simbolo de la rueda 1 mas t
     */
    private static ArrayList<Integer> findOrder(int n) {
        ArrayList<Integer> order = new ArrayList<>();
        order.add(1);
        for(int t = 1; t < n; t++){
            int a = order.get(t - 1);
            rotate(a, 1);
            boolean found = false;
            for(int b = 1; b <= n && !found; b++){
                if(!order.contains(b)){
                    int k = rotate(b, -1);
                    rotate(b, 1);
                    if(k == n){
                        order.add(b);
                        found = true;
                    }
                }
            }
            rotate(a, -1);
        }
        return order;
    }

    /**
     * Lleva cada rueda al simbolo de la rueda 1.
     * @param order orden de las ruedas obtenido en findOrder
     * @param n cantidad de ruedas
     */
    private static void align(ArrayList<Integer> order, int n) {
        for(int t = 1; t < n; t++){
            rotate(order.get(t), -t);
        }
    }

    /**
     * Gira una rueda de forma determinista por el catalogo de simbolos.
     * Guarda la accion {rueda, pasos} en la secuencia.
     * @param wheel posicion de la rueda (1..n)
     * @param steps pasos a rotar, puede ser negativo
     * @return cantidad de simbolos distintos visibles despues de rotar
     */
    private static int rotate(int wheel, int steps) {
        String[] symbols = machine.symbols();
        int n = symbols.length;
        String current = machine.configuracion()[wheel - 1];
        int index = 0;
        for(int i = 0; i < n; i++){
            if(symbols[i].equals(current)){
                index = i;
            }
        }
        int newIndex = ((index + steps) % n + n) % n;
        machine.placeSymbol(wheel, symbols[newIndex]);
        actions.add(new int[]{wheel, steps});
        if(show){
            Canvas.getCanvas().wait(300);
        }
        String[] config = machine.configuracion();
        ArrayList<String> distinct = new ArrayList<>();
        for(int i = 0; i < config.length; i++){
            if(!distinct.contains(config[i])){
                distinct.add(config[i]);
            }
        }
        return distinct.size();
    }

    /**
     * @return la maquina usada en el ultimo solve
     */
    public static Slotmachine machine() {
        return machine;
    }

    /**
     * @return los colores visibles de cada rueda tras el ultimo solve
     */
    public static String[] configuration() {
        return machine.configuracion();
    }

    /**
     * @return true si la maquina quedo en jackpot tras el ultimo solve
     */
    public static boolean isJackpot() {
        return machine.isJackpot();
    }

    /**
     * Igual que solve, pero mostrando la maquina mientras se resuelve.
     * @param n cantidad de ruedas y simbolos
     */
    public static void simulate(int n) {
        show = true;
        solve(n);
        show = false;
    }
}