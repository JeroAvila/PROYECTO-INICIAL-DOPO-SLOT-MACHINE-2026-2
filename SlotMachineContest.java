import java.util.ArrayList;

/**
 * Resuelve el problema Slot Machine (ICPC WF 2025) sobre una Slotmachine.
 * @author Laura Juliana Parra Velandia
 * @author Thomas Jeronimo Avila Castillo
 * @version 1.1
 */
public class SlotMachineContest {
    private Slotmachine machine;
    private int queries;
    private boolean show;

    /**
     * Crea una maquina con n ruedas y la deja en jackpot.
     * @param n cantidad de ruedas y simbolos, minimo 3 y maximo 59 
     * @return cantidad de queries usadas
     */
    public int solve(int n) {
        machine = new Slotmachine(n);
        if(show){
            machine.makeVisible();
        }
        n = machine.symbols().length;
        queries = 0;
        makeDistinct(n);
        ArrayList<Integer> order = findOrder(n);
        align(order, n);
        return queries;
    }
    
    /**
     * Deja cada rueda en la posicion que maximiza los simbolos distintos.
     * @param n cantidad de ruedas
     */
    private void makeDistinct(int n) {
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
    private ArrayList<Integer> findOrder(int n) {
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
     * @param order orden de las ruedas obtenido en findOrder(int n)
     * @param n cantidad de ruedas
     */
    private void align(ArrayList<Integer> order, int n) {
        for(int t = 1; t < n; t++){
            rotate(order.get(t), -t);
        }
    }

    /**
     * Gira una rueda de forma determinista por el catalogo de simbolos.
     * Cuenta como una query.
     * @param wheel posicion de la rueda (1..n)
     * @param steps pasos a rotar, puede ser negativo
     * @return cantidad de simbolos distintos visibles despues de rotar
     */
    private int rotate(int wheel, int steps) {
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
        queries++;
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
    public Slotmachine machine() {
        return machine;
    }
    
    /**
     * @return los colores visibles de cada rueda tras el ultimo solve
     */
    public String[] configuration() {
        return machine.configuracion();
    }
    
    /**
     * @return true si la maquina quedo en jackpot tras el ultimo solve
     */
    public boolean isJackpot() {
        return machine.isJackpot();
    }
    
    /**
     * Igual que solve, pero mostrando la maquina mientras se resuelve.
     * @param n cantidad de ruedas y simbolos
     * @return cantidad de queries usadas
     */
    public int simulate(int n) {
        show = true;
        int result = solve(n);
        show = false;
        return result;
    }
}