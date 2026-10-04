import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import javax.swing.JOptionPane;

/**
 * Simula una maquina tragamonedas 
 * @author Laura Juliana Parra Velandia
 * @author Thomas Jeronimo Avila Castillo
 * @version 2.0
 */
public class Slotmachine
{
    /**
     * Colores validos para los simbolos (blanco, gris y amarillo estan
     * reservados para el fondo/jackpot de las ruedas, no van aqui).
     * Se usa tanto para validar addSymbol como para generar los
     * simbolos de SlotMachine(n).
     */
    private static final String[] VALID_SYMBOL_COLORS = {
        "red", "black", "blue", "green", "magenta", "orange", "purple", "pink",
        "brown", "cyan", "lime", "navy", "teal", "gold", "maroon", "olive",
        "indigo", "violet", "turquoise", "coral", "salmon", "silver",
        "crimson", "chocolate", "darkblue", "darkgreen", "darkorange", "darkred",
        "darkviolet", "deeppink", "deepskyblue", "dodgerblue", "firebrick",
        "forestgreen", "hotpink", "indianred", "khaki", "lavender", "lightblue",
        "lightgreen", "lightpink", "limegreen", "mediumblue", "mediumpurple",
        "mediumseagreen", "midnightblue", "orchid", "peru", "plum", "royalblue",
        "saddlebrown", "seagreen", "sienna", "skyblue", "slateblue",
        "springgreen", "tan", "tomato", "wheat"
    };
    private static final int CASCADE_DELAY = 2000;
    private List<Wheel> wheels;
    private ArrayList<Symbol> symbols;
    private boolean ok;
    private boolean visible;
    private Rectangle background;
    private Random random;
    private int nextGroup;
    /**
     * CONSTRUCTOR
     * Crear una maquina tragamonedas sin ruedas
     */
    public Slotmachine()
    {
        wheels = new ArrayList <>();
        nextGroup = 1;
        symbols = new ArrayList <>();
        ok = true;
        visible = false;
        random = new Random();
        background = new Rectangle();
        background.changeColor("black");
        background.moveHorizontal(10 - 70); // posicion por defecto de Rectangle es (70,15)
        background.moveVertical(10 - 15);
        updateBackground();
    }

    /**
     * Crea una maquina con n ruedas y n simbolos, todos inicializados automaticamente: los n primeros colores validadados del
     * se registran como catalogo, se crean n ruedas y cada una arranca mostrando un simbolo aleatorio de ese catalogo.
     * Si n es menor a 3 se usa 3, y si es mayor al numero de colores disponibles se usa ese maximo.
     * @param n cantidad de ruedas y de simbolos a crear
     */
    public Slotmachine(int n)
    {
        this();
        if(n < 3){
            n = 3;
        }
        if(n > VALID_SYMBOL_COLORS.length){
            n = VALID_SYMBOL_COLORS.length;
        }
        for(int i = 1; i <= n; i++){
            addSymbol(i, VALID_SYMBOL_COLORS[i - 1]);
        }
        for(int i = 1; i <= n; i++){
            addWheel(i);
        }
        for(int i = 1; i <= n; i++){
            spin(i);
        }
        ok = true;
    }
    /**
     * agrega una rueda normal
     * @param pos posicion donde se va a insertar la rueda
     */
    public void addWheel(int pos){
        addWheel(pos, "normal");
    }
    /**
     * agregar una rueda del tipo dado en la posicion dada
     */
    public void addWheel(String type, int pos){
        addWheel(pos, type);
    }
    /**
     * Agregar una rueda nueva en la posicion dada, desplazando las
     * demas ruedas. Si la posicion es menor a 1 se usa 1, y si es mayor
     * al numero de ruedas mas uno se usa ese maximo. 
     *  @param pos posicion donde se va a insertar la rueda
     *  @param type tipo de rueda
     */
    public void addWheel(int pos, String type){
        if(pos < 1){
            pos = 1;
        }
        if(pos > wheels.size() + 1){
            pos = wheels.size() + 1;
        }
        ArrayList<Wheel> created;
        try{
        created = createWheels(type, pos);
        } catch(SlotMachineException e){
            fail(e.getMessage());
            return;
        }
        int index = pos -1;
        while(index > 0 && index < wheels.size() && sameGroup(wheels.get(index - 1), 
            wheels.get(index))){
                index++;
        }
        for (int i = 0; i < created.size(); i++){
            wheels.add(index + i, created.get(i));
        }
        renumber();
        updateWheelSymbols();
        updateBackground();
        if(visible){
            for(int i = 0; i < created.size(); i++){
                created.get(i).makeVisible();
            }        
        }
        ok = true;
    }
    public void addWheels(int n){
        if(n < 1){
            fail("la cantidad de ruedas debe ser al menos 1");
            return;
        }
        for(int i = 0; i < n; i++){
            addWheel(wheels.size() + 1);
        }
        ok = true;
    }
        
    /**
     * Eliminar la rueda que esta en la posicion dada, desplazando las
     * demas ruedas. Si la maquina no tiene ruedas la operacion falla.
     * @param pos posicion de la rueda a eliminar
     */
    public void delWheel(int pos){
        if(noWheels("No hay ruedas para eliminar.")){
            return;
        }
        pos = clamp(pos);
        Wheel targetWheel = wheels.get(pos-1);
        int start = blockStart(pos -1);
        int end = blockEnd(pos -1);
        for(int i = start; i < end; i++){
            if(blocked(wheels.get(i), "no se puede eliminar porque la rueda" + pos                + "esta bloqueada")){
                return;
            }
        }
        try{
            for(int i = start; i < end; i++){
                wheels.get(i).checkDeletable();
            }
        } catch(SlotMachineException e){
            fail(e.getMessage());
            return;
        }
        for (int i = end -1; i >= start; i--){
            wheels.get(i).makeInvisible();
            wheels.remove(i);
        }
        renumber();
        updateJackpotVisual();
        updateBackground();
        ok = true;
    }
    /**
     * crea la rueda pedida, si es la rueda crazy tambien crea las otras 3
     * @param type tipo de rueda
     * @param pos posicion de la rueda
     * retorna la rueda y si hace falta las otras 3 de crazy
     * @throws SlotMachineException pasa si el tipo es nulo o no exisste
     */
    private ArrayList<Wheel> createWheels(String type, int pos) throws SlotMachineException{
        ArrayList<Wheel> created = new ArrayList<>();
        Wheel first = createWheel(type, pos);
        created.add(first);
        int extra = first.getCompanions();
        if(extra > 0){
            int group = nextGroup;
            nextGroup++;
            first.setGroup(group);
            for(int i = 1; i <= extra; i++){
                    Wheel companion = createWheel(type, pos + i);
                    companion.setGroup(group);
                    created.add(companion);
            }
        }
        return created;
    }
    /**
     * retorna true si las dos ruedas pertenecen al mismo grupo
     * @param a una rueda
     * @param b una rueda
     */
    private boolean sameGroup(Wheel a, Wheel b){
        return a.getGroup() != 0 && a.getGroup() == b.getGroup();
    }
    /**
     * retorna el indice de la primera rueda del grupo
     * @param index indice de uuna rueda
     */
    private int blockStart(int index){
        int start = index;
        while(start >0 && sameGroup(wheels.get(start -1), wheels.get(index))){
            start--;
        }
        return start;
    }
    /**
     * retorna el indice siguiente a la ultima rueda del grupo
     */
    private int blockEnd(int index){
        int end = index + 1;
        while(end < wheels.size() && sameGroup(wheels.get(end), wheels.get(index))){
            end++;
        }
        return end;
    }
    /**
     * Indica si la ultima operacion realizada sobre la maquina se completo con exito.
     * @return true si la ultima operacion fue exitosa, false en caso
     * contrario
     */
    public boolean ok(){
        return ok;
    }
    
    /**
     * Agrega un simbolo normal del color indicado en la posicion dada.
     * @param pos posicion donde se insertara el simbolo
     * @param color color del simbolo
     */
    public void addSymbol(int pos, String color){
        addSymbol(pos, color, "normal");
    }
    
    /**
     * Agregar un símbolo del color indicado en la posición dada
     * El color debe ser válido y no repetirse. Si la posición es menor que 1, se usa 1,
     * si supera el número de símbolos más uno, se usa ese máximo.
     * @param pos posición donde se insertará el símbolo 
     * @param color color del símbolo
     * @param type tipo de simbolo: "normal" o "ephemeral" o "shy"
     */
    public void addSymbol(int pos, String color, String type){
        boolean isValidColor = false;
        for(int i = 0; i < VALID_SYMBOL_COLORS.length; i++){
            if(VALID_SYMBOL_COLORS[i].equals(color)){
                isValidColor = true;
            }
        }
        if(!isValidColor){
            fail("El color '" + color + "' no es un color valido.");
            return;
        }
        if(indexOfColor(color) != -1){
            fail("El color '" + color + "' ya esta registrado. Los simbolos deben ser de colores diferentes.");
            return;
        }
        if(pos < 1 || pos > symbols.size() + 1){
            fail("La posicion " + pos + " no es valida.");
            return;
        }
        Symbol symbol;
        try{
            symbol = createSymbol(type, color);
        } catch(SlotMachineException e){
            fail(e.getMessage());
            return;
        }
        symbols.add(pos -1, symbol);
        updateWheelSymbols();
        ok = true;
    }
    /**
     * agrega un simbolo dado
     * @param type tipo de simbolo
     * @param pos posicion donde se inserta el simbolo
     * @param color es el color del simbolo
     */
    public void addSymbol(String type, int pos, String color){
        addSymbol(pos, color, type);
    
    }
    /**
     * Marca la ultima operacion como fallida y, si la maquina esta
     * visible, le muestra el mensaje al usuario con un JOptionPane.
     * @param message el mensaje a mostrar
     */
    private void fail(String message){
        ok = false;
        if(visible){
            JOptionPane.showMessageDialog(null, message);
        }
    }
    
     /**
     * Eliminar el simbolo con el color dado. Falla si ese color no
     * esta registrado en la maquina.
     * @param symbol color del simbolo a eliminar
     */
    public void delSymbol(String symbol){
        if(indexOfColor(symbol) == -1){
            fail("El color'" + symbol + "' no esta registrado en la maquina.");
            return;
        }
        symbols.remove(indexOfColor(symbol));
        updateWheelSymbols(); // modificado para la configuracion
        ok = true;
    }
    
    /**
     * termina el simulador, limpia el estado interno de la maquina, dejandola sin ruedas ni simbolos
     */
    public void exit(){
        wheels.clear();
        symbols.clear();
        ok = true;
    }
    
    /**
     * consulta cuantos simbolos distintos tiene la maquina.
     * como en addsimbol no se permiten los colores repetidos,
     * este numero siempre sera igual a la cantidad total de simbolos.
     */
    public int distinctSymbolos(){
        ok = true;
        return symbols.size();
    }
    
    /**
     * @return un arreglo con colores
     */
    public String[] symbols(){
        ok = true;
        String[] result = new String[symbols.size()];
        for (int i = 0; i < symbols.size(); i++){
            result[i] = symbols.get(i).getColor();
        }
        return result;
    }
    
    /**
     * Busca el simbolo del color dado en el catalogo.
     * @param color color del simbolo a buscar
     * @return la posicion del simbolo en el catalogo (desde 0), o -1 si no esta
     */
    private int indexOfColor(String color){
        for(int i = 0; i < symbols.size(); i++){
            if(symbols.get(i).getColor().equals(color)){
                return i;
            }
        }
        return -1;
    }
    
    /**
     * Ajusta el tamano del rectangulo negro de fondo segun cuantas
     * ruedas hay, y vuelve a traer las ruedas al frente porque
     * redimensionar el fondo las tapa.
     */
    private void updateBackground(){
        int width = 40 + wheels.size() * 70;
        background.changeSize(150, width);
        for(int i = 0; i < wheels.size(); i++){
            wheels.get(i).bringToFront();
        }
    }
    
    /**
     * recorre todas las ruedas y le pasa a cada una la lista de simbolos actualizada
     */
    private void updateWheelSymbols(){
        for (int i = 0; i < wheels.size(); i++){
            wheels.get(i).setSymbols(symbols);
        }
        updateJackpotVisual();
    }
    
    /**
     * revisa si la configuracion actual es jackpot y le avisa a cada
     * rueda, para que luzca distinta (triangulo dorado) cuando la
     * maquina esta en estado ganador.
     */
    private void updateJackpotVisual(){
        boolean winning = isJackpot();
        for(int i = 0; i < wheels.size(); i++){
            wheels.get(i).setWinning(winning);
        }
    }
    
    /**
     * Renumera las posiciones de todas las ruedas segun el orden actual en la lsita. Se usa cada 
     * vez que la lista de ruedas cambia de orden ya sea al agregar, eliminar o intercambair ruedas
     */
    private void renumber(){
        for(int i = 0; i < wheels.size(); i++){
            wheels.get(i).setPosition(i+1);
             wheels.get(i).setLeft(i == 0 ? null : wheels.get(i - 1));
        }
    }
    
    /**
     * Si no hay ruedas registradas, la operación falla
     * @param message mensaje a mostrar la falla
     * @return true si la maquina no tiene ruedas
     */
    private boolean noWheels(String message){
        if(wheels.isEmpty()){
            fail(message);
            return true;
        }
        return false;
    }
    
    /**
     * Si no hay simbolos registrado falla
     * @param message mensaje a mostrar si falla
     * @return true si la maquina no tiene simbolos
     */
    private boolean noSymbols(String message){
        if(symbols.isEmpty()){
            fail(message);
            return true;
        }
        return false;
    }
    
    /**
     * Ajusta un indice de rueda al rango valido.
     * @param wheel indice de rueda a ajustar
     * @return el indice ya dentro del rango valido
     */
    private int clamp(int wheel){
        if(wheel < 1){
            return 1;
        }
        if(wheel > wheels.size()){
            return wheels.size();
        }
        return wheel;
    }
    
     /**
     * Si la rueda dada esta bloqueada falla 
     * @param wheel la rueda a revisar
     * @param message mensaje a mostrar si esta bloqueada
     * @return true si la rueda esta bloqueada
     */
    private boolean blocked(Wheel wheel, String message){
        if(wheel.isLocked()){
            fail(message);
            return true;
        }
        return false;
    }
    
    /**
     * verifica que haya ruedas y simbolos, luego ajusta la posicion si esta fuera del rango
     * obtiene las ruedas y llama a los simbolos, si la rueda dice false el simbolo no existe
     */
    public void placeSymbol (int wheel, String symbol) {
        if(noWheels("No hay ruedas en la maquina.")){
            return;
        }
        if(noSymbols("No hay simbolos registrados en la maquina.")){
            return;
        }
        wheel = clamp(wheel);
        Wheel targetWheel = wheels.get(wheel - 1);
        if(blocked(targetWheel, "La rueda " + wheel + " esta bloqueada.")){
            return;
        }
        for (int i = blockStart(wheel - 1); i < blockEnd(wheel - 1); i++){
            boolean placed = wheels.get(i).placeSymbol(symbol);
            if(!placed){
                fail("el simbolo" + symbol + "no esta registrado");
                return;
            }
        }
        updateJackpotVisual();
        ok = true;
    }
    
    /**
     * gira una rueda
     */
    public void spin(int wheel){
        if(noWheels("No hay ruedas en la maquina.")){
            return;
        }
        if(noSymbols("No hay simbolos registrados en la maquina.")){
            return;
        }
        wheel = clamp(wheel);
        Wheel targetWheel = wheels.get(wheel - 1);
        if(blocked(targetWheel, "La rueda " + wheel + " esta bloqueada.")){
            return;
        }
        for(int i = blockStart(wheel -1); i < blockEnd(wheel - 1); i ++){
            wheels.get(i).spin();
        }
        updateJackpotVisual();
        ok = true;
    }
    
    /**
     * gira todas las ruedas
     * 
     */
    public void spin(){
        if(noWheels("No hay ruedas en la maquina.")){
            return;
        }
        if(noSymbols("No hay simbolos registrados en la maquina.")){
            return;
        }
        for (int i = 0; i < wheels.size(); i++){
            Wheel current = wheels.get(i);
            current.markActive(true);
            current.spin();
            if(visible){
                Canvas.getCanvas().wait(CASCADE_DELAY);// partedecascadadelay
            }
            current.markActive(false);
        }
        updateJackpotVisual();
        ok = true;         
    }
    
    /**
     * Este Spin gira una rueda especifica con un numero determinado de pasos
     * por cada paso se elige un simbolo al azar
     * @param wheel posicion de la rueda a girar
     * @param steps numero de pasos a girar
     */
    public void spin(int wheel, int steps) {
       if(noWheels("No hay ruedas en la maquina")){
            return;
        } 
       if(noSymbols("No hay simbolos registrados en la maquina")){
            return;
        } 
       wheel = clamp(wheel);
       if(steps < 1){
           fail("El numero de pasos debe ser al menos 1");
           return;
        }
       for(int j = blockStart(wheel - 1); j < blockEnd(wheel - 1); j++){
            wheels.get(j).spin();
        }
       Wheel target = wheels.get(wheel - 1);  
       if(blocked(target, "No se puede girar si esta bloqueada la rueda")){
            return;
        }
       for(int i = 0; i < steps; i++){
            target.spin();
            if(visible){
                Canvas.getCanvas().wait(300);
            }
        }
       updateJackpotVisual();
       ok = true;
    }
    
    /**
     * le da a la maquina una configuracion que querramos
     * recibiendo un arreglo de colores
     * coloca a cada rueda el colo que debe estar
     * @param setSymbols arreglo del color que deberia mostrar
     */
    public void spin(String[] setSymbols){
        if(noWheels("No hay ruedas en la maquina")){
            return;
        }
        if(noSymbols("No hay simbolos registrados de momento en la maquina")){
            return;
        }
        if(setSymbols.length != wheels.size()){
            fail("El arreglo debe tener" + wheels.size() + "colores uno por cada rueda");
            return;
        }
        for(int i = 0; i < setSymbols.length; i++){
            if(indexOfColor(setSymbols[i]) == -1){
                fail("El color'" + setSymbols[i] + "'no esta registrado");
                return;            
            }            
        }
        for(int i = 0; i < wheels.size(); i++){
            Wheel target = wheels.get(i);
            if(!target.isLocked()){
                target.placeSymbol(setSymbols[i]);
            }
        }
        updateJackpotVisual();
        ok = true;
    }
    
    /**
     * hace visible la maquina slotmachine
     */
    public void makeVisible(){
        visible = true;
        background.makeVisible();
        for(int i = 0; i < wheels.size(); i++){
            wheels.get(i).makeVisible();
        }
        ok = true;
    }
    
    /**
     * hace que no sea visible en la maquina slotmachine
     */
    public void makeInvisible(){
        visible = false;
        background.makeInvisible();
        for(int i = 0; i < wheels.size(); i++){
            wheels.get(i).makeInvisible();
        }
        ok = true;
    }
    
    /**
     * retorna los colores de los simbolos visibles
     * en todas las ruedas de la maquina
     * ordenados de izq a der
     * @return arreglo con el color visible de cada rueda
     */
    public String[] configuracion() {
        ok = true;
        String[] result = new String[wheels.size()];
        for(int i = 0; i < wheels.size(); i++){
            result[i] = wheels.get(i).getCurrentSymbol();
        }
        return result;
    }
    
    /**
     * consulta si la configuracion actual de la maquina es la ganadora
     * es "jackpot" si todas las ruedas muestran el mismo simbolo
     * @return da true si todas las ruedas muestran el mismo color
     * si no da false
     */
    public boolean isJackpot(){
        ok = true;
        if(wheels.isEmpty()){
            return false;
        }
        String first = wheels.get(0).getCurrentSymbol();
        if(first == null){
            return false;
        }
        for(int i = 1; i < wheels.size(); i++){
            if(!first.equals(wheels.get(i).getCurrentSymbol())){
                return false;
            }
        }
        return true;       
    }
    
    
    /**
     * Intercambia dos ruedas de posición dadas por sus índices.
     * Falla si alguna de las ruedas está bloqueada.
     * @param wheel1 posición de la primera rueda
     * @param wheel2 posición de la segunda rueda
     */
    public void swap(int wheel1, int wheel2) {
        if (noWheels("No hay ruedas en la maquina.")) {
            return;
        }
        if (wheel1 < 1 || wheel1 > wheels.size() || wheel2 < 1 || wheel2 > wheels.size()) {
            fail("Posición de rueda inválida para el intercambio.");
            return;
        }
        int start1 = blockStart(wheel1 -1);
        int end1 = blockEnd(wheel1 - 1);
        int start2 = blockStart(wheel2 - 1);
        int end2 = blockEnd(wheel2 -1);
        for (int i = start1; i < end1; i++){
            if(blocked(wheels.get(i), "no se puede rotar la rueda" + wheel1 + 
                "esta bloqueada")){
                return;
            }
        }
        for(int i = start2; i < end2; i++){
            if(blocked(wheels.get(i), "no se puede rotar la rueda" + wheel2 + 
                "esta bloqueada")){
                return;
            }
        }
        try{
            for (int i = start1; i < end1; i++){
                wheels.get(i).checkSwappable();
            }
            for (int i = start2; i < end2; i++){
                wheels.get(i).checkSwappable();
            }
        } catch (SlotMachineException e){
            fail(e.getMessage());
            return;
        }
        if(start1 != start2){
            if(start1 > start2){
                int tempStart = start1;
                int tempEnd = end1;
                start1 = start2;
                end1 = end2;
                start2 = tempStart;
                end2 = tempEnd;
            }
            List<Wheel> reorder = new ArrayList<>();
            reorder.addAll(wheels.subList(0, start1));
            reorder.addAll(wheels.subList(start2, end2));
            reorder.addAll(wheels.subList(end1, start2));
            reorder.addAll(wheels.subList(start1, end1));
            reorder.addAll(wheels.subList(end2, wheels.size()));
            wheels = reorder;
        }
        // Actualizar posiciones visuales y lógicas
        renumber();
        updateJackpotVisual();
        updateBackground();
        ok = true;
    }

    /**
     * Bloquea una rueda específica para que no pueda ser rotada ni eliminada.
     * @param wheel posición de la rueda a bloquear
     */
    public void lock(int wheel) {
         if (noWheels("No hay ruedas en la maquina.")) {
            return;
        }
        wheel = clamp(wheel);
        try{
            for (int i = blockStart(wheel - 1); i < blockEnd(wheel - 1); i++){
                wheels.get(i).lock();
            }
        } catch (SlotMachineException e) {
            fail(e.getMessage());
            return;
        }
        ok = true;
    }

    /**
     * Desbloquea una rueda específica.
     * @param wheel posición de la rueda a desbloquear
     */
    public void unlock(int wheel) {
        if (noWheels("No hay ruedas en la maquina.")) {
            return;
        }
        wheel = clamp(wheel);
        for (int i = blockStart(wheel - 1); i < blockEnd(wheel - 1); i++) {
            wheels.get(i).unlock();
        }
        ok = true;
    }
    /**
     * crea una rueda de un tipo dado
     * @param type es el tipo del simbolo
     * @param color es el color del simbolo
     * retorna el simbolo creado
     * @throws SlotMachineException es si el tipo es nulo o no existe
     */
    private Symbol createSymbol(String type, String color) throws SlotMachineException{
        if(type == null){
            throw new SlotMachineException("el tipo de simbolo no puyede ser nulo");
        }
        if(type.equals("normal")){
            return new NormalSymbol(color);
        } else if(type.equals("ephemeral")){
            return new EphemeralSymbol(color);
        } else if (type.equals("shy")){
            return new ShySymbol(color);
        }
        throw new SlotMachineException("El tipo de simbolo '" + type + "' no existe.");
    }
    /**
     * crea una rueda de un tipo dado
     * @param type rueda normal, lefty, rebel, lazy
     * @param pos posicion de la rueda
     * @throws SlotMachineException si el tipo es null 
     */
    private Wheel createWheel(String type, int pos) throws SlotMachineException{
        if(type == null){
            throw new SlotMachineException("El tipo de rueda no puede ser nulo.");
        }
        if(type.equals("normal")){
            return new NormalWheel(pos);
        } else if(type.equals("lefty")){
            return new LeftyWheel(pos);
        } else if(type.equals("rebel")){
            return new RebelWheel(pos);
        } else if(type.equals("crazy")){
            return new CrazyWheel(pos);
        }
        throw new SlotMachineException("El tipo de rueda '" + type + "' no existe.");
    }
    /**
     * consulta el tipo de rueda de cada maquina
     */
    public String[] wheelTypes(){
        ok = true;
        String[] result = new String[wheels.size()];
        for(int i = 0; i < wheels.size(); i++){
            result[i] = wheels.get(i).getType();
        }
        return result;
    }
}