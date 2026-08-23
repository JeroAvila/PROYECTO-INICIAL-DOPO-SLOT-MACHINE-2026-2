import java.util.ArrayList;
import java.util.List;

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

    /**
     * Crear una maquina tragamonedas sin ruedas
     */
    public Slotmachine()
    {
        wheels = new ArrayList <>();
        symbols = new ArrayList <>();
        ok = true;
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
        wheels.add(pos -1, new Wheel(pos));
        for(int i = 0; i <wheels.size(); i++){
            wheels.get(i).setPosition(i+1);
        }
        updateWheelSymbols();
        ok = true;
    }
    
    /**
     * Eliminar la rueda que esta en la posicion dada, desplazando las
     * demas ruedas. Si la maquina no tiene ruedas la operacion falla.
     * @param pos posicion de la rueda a eliminar
     */
    public void delWheel(int pos){
        if(wheels.size()==0){
            ok = false;
            return;
        }
        if (pos <1){
            pos = 1;
        }
        if(pos > wheels.size()){
            pos = wheels.size();
        }
        wheels.remove(pos-1);
        for (int i = 0; i < wheels.size(); i++){
            wheels.get(i).setPosition(i+1);
        }
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
            || color.equals("yellow") || color.equals("green") || color.equals("magenta")
            || color.equals("white"))){
            ok = false;
            return;
        }
        if(symbols.contains(color)){
            ok = false;
            return;
        }
        if(pos < 1){
            pos = 1;
        }
        if(pos > symbols.size() + 1){
            pos = symbols.size() + 1;
        }
        symbols.add(pos - 1, color);
        updateWheelSymbols();
        ok = true;
    }
    
     /**
     * Eliminar el simbolo con el color dado. Falla si ese color no
     * esta registrado en la maquina.
     * @param symbol color del simbolo a eliminar
     */
    public void delSymbol(String symbol){
        if(!symbols.contains(symbol)){
            ok = false;
            return;
        }
        symbols.remove(symbol);
        updateWheelSymbols();
        ok = true;
    }

    /**
     * Consulta los colores de los simbolos registrados en la maquina, en el orden que fueron
     * agregados, empezando por la posición 1.
     * 
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
     * Consulta cuantos simbolos distintos tiene la maquina. Como en addSymbol no permitimos colores 
     * repetidos, este numero siempre sera igual a la cantidad total de simbolos 
     * 
     * @return Cantidad de simbolos distintos
     */
    public int distinctSymbols(){
        ok = true;
        return symbols.size();
    }

    /**
     * Termina el simulador, limpia el estado interno de la maquina, dejandola sin ruedas ni simbolos registrados
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
     * recorre todas las ruedas y le pasa a cada una la lista de simbolos actualizada
     */
    private void updateWheelSymbols(){
        for (int i = 0; i < wheels.size(); i++){
            wheels.get(i).setSymbols(symbols);
        }
    }
    /**
     * verifica que haya ruedas y simbolos, luego ajusta la posicion si esta fuera del rango
     * obtiene las ruedas y llama a los simbolos, si la rueda dice false el simbolo no existe
     */
    public void placeSymbol (int wheel, String symbol) {
        if (wheels.isEmpty() || symbols.isEmpty()){
            ok = false;
            return;
        }
        if(wheel < 1) wheel = 1;
        if(wheel > wheels.size()) 
        wheel = wheels.size();
        boolean placed = wheels.get(wheel -1).placeSymbol(symbol);
        if(!placed){
            ok = false;
            return;
        }
        ok = true;
    }
    /**
     * gira una rueda
     */
    public void spin(int wheel){
        if(wheels.isEmpty()){
            ok = false;
            return;
        }
        if(wheel < 1) wheel = 1;
        if(wheel > wheels.size()) wheel = wheels.size();
        wheels.get(wheel - 1).spin();
        ok = true;
    }
    /**
     * gira todas las ruedas
     * 
     */
    public void spin(){
        if(wheels.isEmpty()){
            ok = false;
            return;
        }
        for (int i = 0; i < wheels.size(); i++){
            wheels.get(i).spin();
        }
        ok = true;        
    }
    /**
     * hace visible la maquina tragaperras
     */
    public void makeVisible(){
        for(int i = 0; i < wheels.size(); i++){
            wheels.get(i).makeVisible();
        }
        ok = true;
    }
    /**
     * hace que no sea visible en la maquina tragaperras
     */
    public void makeInvisible(){
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
}
