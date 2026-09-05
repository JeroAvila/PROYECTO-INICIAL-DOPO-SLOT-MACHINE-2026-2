import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;

/**
 * Simula una maquina tragamonedas 
 * @author Laura Juliana Parra Velandia
 * @author Thomas Jeronimo Avila Castillo
 * @version 1.0
 */
public class Slotmachine
{
    private List<Wheel> wheels;
    private List<String> symbols;
    private boolean ok;
    private boolean visible;
    private Rectangle background;

    /**
     * Crear una maquina tragamonedas sin ruedas
     */
    public Slotmachine()
    {
        wheels = new ArrayList <>();
        symbols = new ArrayList <>();
        ok = true;
        visible = false;
        background = new Rectangle();
        background.changeColor("black");
        background.moveHorizontal(10 - 70); // posicion por defecto de Rectangle es (70,15)
        background.moveVertical(10 - 15);
        updateBackground();
    }

    /**
     * Agregar una rueda nueva en la posicion dada, desplazando las
     * demas ruedas. Si la posicion es menor a 1 se usa 1, y si es mayor
     * al numero de ruedas mas uno se usa ese maximo. 
     *  @param pos posicion donde se va a insertar la rueda
     */
    public void addWheel(int pos){
        if(pos < 1){
            pos = 1;
        }
        if(pos > wheels.size() + 1){
            pos = wheels.size() + 1;
        }
        Wheel newWheel = new Wheel(pos);
        wheels.add(pos -1, newWheel);
        for(int i = 0; i <wheels.size(); i++){
            wheels.get(i).setPosition(i+1);
        }
        updateWheelSymbols(); // modificado para mostrar en configuracion
        updateBackground();
        if(visible){
            newWheel.makeVisible();
        }
        ok = true;
    }
    
    /**
     * Eliminar la rueda que esta en la posicion dada, desplazando las
     * demas ruedas. Si la maquina no tiene ruedas la operacion falla.
     * @param pos posicion de la rueda a eliminar
     */
    public void delWheel(int pos){
        if(wheels.size()==0){
            fail("No hay ruedas para eliminar.");
            return;
        }
        if (pos <1){
            pos = 1;
        }
        if(pos > wheels.size()){
            pos = wheels.size();
        }
        Wheel targetWheel = wheels.get(pos-1);
        if(targetWheel.isLocked()){
            fail("No se puede elimanar puesto que la rueda" + pos + "esta bloqueada, desbloqueala");
            return;
        }
        wheels.remove(pos-1);
        for (int i = 0; i < wheels.size(); i++){
            wheels.get(i).setPosition(i+1);
        }
        updateJackpotVisual();
        updateBackground();
        ok = true;
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
     * Agregar un símbolo del color indicado en la posición dada
     * El color debe ser válido y no repetirse. Si la posición es menor que 1, se usa 1,
     * si supera el número de símbolos más uno, se usa ese máximo.
     * @param pos posición donde se insertará el símbolo 
     * @param color color del símbolo
     */
    public void addSymbol(int pos, String color){
        if(!(color.equals("red") || color.equals("black") || color.equals("blue")
            || color.equals("green") || color.equals("magenta")
            || color.equals("orange") || color.equals("purple") || color.equals("pink")
            || color.equals("brown") || color.equals("cyan")
            || color.equals("lime") || color.equals("navy") || color.equals("teal")
            || color.equals("gold") || color.equals("maroon") || color.equals("olive")
            || color.equals("indigo") || color.equals("violet") || color.equals("turquoise")
            || color.equals("coral") || color.equals("salmon") || color.equals("silver"))){
            fail("El color '" + color + "' no es un color valido.");
            return;
        }
        if(symbols.contains(color)){
            fail("El color '" + color + "' ya esta registrado. Los simbolos deben ser de colores diferentes.");
            return;
        }
        if(pos < 1 || pos > symbols.size() + 1){
            fail("La posicion " + pos + " no es valida.");
            return;
        }
        symbols.add(pos - 1, color);
        updateWheelSymbols();
        ok = true;
    }
    
    /**
     * Marca la ultima operacion como fallida y, si la maquina esta
     * visible, le muestra el mensaje al usuario con un JOptionPane.
     * @param message el mensaje a mostrar
     */
    private void fail(String message){
        ok = false;
        JOptionPane.showMessageDialog(null, message);
    }
    
     /**
     * Eliminar el simbolo con el color dado. Falla si ese color no
     * esta registrado en la maquina.
     * @param symbol color del simbolo a eliminar
     */
    public void delSymbol(String symbol){
        if(!symbols.contains(symbol)){
            fail("El color '" + symbol + "' no esta registrado en la maquina.");
            return;
        }
        symbols.remove(symbol);
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
            result[i] = symbols.get(i);
        }
        return result;
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
     * verifica que haya ruedas y simbolos, luego ajusta la posicion si esta fuera del rango
     * obtiene las ruedas y llama a los simbolos, si la rueda dice false el simbolo no existe
     */
    public void placeSymbol (int wheel, String symbol) {
        if (wheels.isEmpty()){
            fail("No hay ruedas en la maquina.");
            return;
        }
        if (symbols.isEmpty()){
            fail("No hay simbolos registrados en la maquina.");
            return;
        }
        if(wheel < 1){
            wheel = 1;
        }
        if(wheel > wheels.size()){
            wheel = wheels.size();
        }
        boolean placed = wheels.get(wheel -1).placeSymbol(symbol);
        if(!placed){
            fail("El simbolo '" + symbol + "' no esta registrado en la maquina.");
            return;
        }
        updateJackpotVisual();
        ok = true;
    }
    
    /**
     * gira una rueda
     */
    public void spin(int wheel){
        if(wheels.isEmpty()){
            fail("No hay ruedas en la maquina.");
            return;
        }
        if(symbols.isEmpty()){
            fail("No hay simbolos registrados en la maquina.");
            return;
        }
        if(wheel < 1){
            wheel = 1;
        }
        if(wheel > wheels.size()){
            wheel = wheels.size();
        }
        wheels.get(wheel - 1).spin();
        updateJackpotVisual();
        ok = true;
    }
    
    /**
     * gira todas las ruedas
     * 
     */
    public void spin(){
        if(wheels.isEmpty()){
            fail("No hay ruedas en la maquina.");
            return;
        }
        if(symbols.isEmpty()){
            fail("No hay simbolos registrados en la maquina.");
            return;
        }
        for (int i = 0; i < wheels.size(); i++){
            wheels.get(i).spin();
        }
        updateJackpotVisual();
        ok = true;        
    }
    
    /**
     * hace visible la maquina tragaperras
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
     * hace que no sea visible en la maquina tragaperras
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
        if (wheels.isEmpty()) {
            fail("No hay ruedas en la maquina.");
            return;
        }
        if (wheel1 < 1 || wheel1 > wheels.size() || wheel2 < 1 || wheel2 > wheels.size()) {
            fail("Posición de rueda inválida para el intercambio.");
            return;
        }
        
        Wheel w1 = wheels.get(wheel1 - 1);
        Wheel w2 = wheels.get(wheel2 - 1);

        if (w1.isLocked()) {
            fail("No se puede rotar puesto que la rueda " + wheel1 + " esta bloqueada, desbloqueala");
            return;
        }
        if (w2.isLocked()) {
            fail("No se puede rotar puesto que la rueda " + wheel2 + " esta bloqueada, desbloqueala");
            return;
        }

        // Intercambiar en la lista
        wheels.set(wheel1 - 1, w2);
        wheels.set(wheel2 - 1, w1);

        // Actualizar posiciones visuales y lógicas
        for (int i = 0; i < wheels.size(); i++) {
            wheels.get(i).setPosition(i + 1);
        }
        updateBackground();
        ok = true;
    }

    /**
     * Bloquea una rueda específica para que no pueda ser rotada ni eliminada.
     * @param wheel posición de la rueda a bloquear
     */
    public void lock(int wheel) {
        if (wheels.isEmpty()) {
            fail("No hay ruedas en la maquina.");
            return;
        }
        if (wheel < 1 || wheel < 1) {
            wheel = 1;
        }
        if (wheel > wheels.size()) {
            wheel = wheels.size();
        }
        wheels.get(wheel - 1).lock();
        ok = true;
    }

    /**
     * Desbloquea una rueda específica.
     * @param wheel posición de la rueda a desbloquear
     */
    public void unlock(int wheel) {
        if (wheels.isEmpty()) {
            fail("No hay ruedas en la maquina.");
            return;
        }
        if (wheel < 1) {
            wheel = 1;
        }
        if (wheel > wheels.size()) {
            wheel = wheels.size();
        }
        wheels.get(wheel - 1).unlock();
        ok = true;
    }
}
