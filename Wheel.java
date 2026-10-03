import java.util.ArrayList;
import java.util.Random;
/**
 * Es la representacion de una rueda en la maquina tragamonedas

 * @author Thomas Jeronimo Avila Casillo
 * @author Laura Juliana Parra Velandia
 * @version (a version number or a date)
 */
public class Wheel
{
    private int position;
    private Rectangle frame;
    private ArrayList<Symbol> symbols; 
    private int currentIndex;  
    private boolean winning;  
    private boolean visible;
    private boolean locked;
    private int frameX, frameY; // posicion actual del frame en pantalla
    private int symX, symY;     // posicion actual del espacio del simbolo
    private Random random;
    private Rectangle lockMark;
    private Rectangle activeMark;
    private int lockX, lockY; //posiciom de marcador de bloqueado
    private int activeX, activeY; //posicion del marcador de turno
    /**
     * CONSTRUCTOR
     * Crea una rueda vacia en la posicion dada. for objects of class Wheel
     */
    public Wheel(int position)
    {
        this.position = position;
        frame = new Rectangle();
        frame.changeColor("gray");
        symbols = new ArrayList<>();
        currentIndex = -1; 
        winning = false;
        visible = false;
        frameX = 70; frameY = 15;   
        symX = 20; symY = 15;       
        random = new Random();
        //cuadro que indica esta bloqueado
        lockMark = new Rectangle();
        lockMark.changeSize(10, 10);
        lockMark.changeColor("red");
        lockX = 70;
        lockY = 15;
        
        // cuadro que indica en que posicion se esta haciendo el movimiento
        activeMark = new Rectangle();
        activeMark.changeSize(8, 8);
        activeMark.changeColor("green");
        activeX = 70;
        activeY = 15;
        updatePosition();
    }
    
    /**
     * Cambia la posicion logica de la rueda, usado cuando se agregan o
     * eliminan ruedas en la maquina y hay que renumerar las demas.
     * Tambien reubica el frame y el circulo en pantalla para que no
     * queden varias ruedas dibujadas una encima de otra.
     */
    public void setPosition(int position){
          this.position =  position;
          updatePosition();
    }
    
    /**
     * Consulta la posicion actual de la rueda.
     */
    public int getPosition(){
        return position;
    }
    
    /**
     * MC3
     * recibe la lista de simbolos de la maquina y la guarda como una copia.
     * Si el simbolo que ya estaba visible sigue existiendo en la lista
     * nueva, se mantiene; si no, la rueda queda sin simbolo asignado.
     */
    public void setSymbols(ArrayList<Symbol> catalog) {
        String previousColor = getCurrentSymbol();
        for(int i = 0; i < symbols.size(); i++){
            symbols.get(i).makeInvisible();
        }
        symbols = new ArrayList<>();
        for(int i = 0; i < catalog.size(); i++){
            Symbol copy = catalog.get(i).copy();
            copy.setPosition(symX, symY);
            symbols.add(copy);
        }
        currentIndex = -1;
        for(int i = 0; i < symbols.size(); i++){
            if(symbols.get(i).getColor().equals(previousColor)){
                currentIndex = i;
            }
        }
        updateColor();
    }
    
    /**
     * devuelve el color del simbolo que se esta mostrando actualmente.
     * si la rueda no tiene simbolo asignado da null
     */
    public String getCurrentSymbol() {
        if(symbols.isEmpty() || currentIndex == -1){
            return null;
        }
        return symbols.get(currentIndex).getColor();
    }
    
    /**
     * Elige un simbolo al azar del catalogo de esta rueda.
     */
    public void spin() {
        if(!locked && !symbols.isEmpty()){
            currentIndex = random.nextInt(symbols.size());
            symbols.get(currentIndex).select();
            updateColor();
        }
    }
    
    /**
     * busca el simbolo en la lista, si no encuentra da -1 y el metodo da false
     * si lo encuentra mueve el currentIndex a esa posicio para que sea visible el simbolo
     */
    public boolean placeSymbol(String symbol){
        int index = -1;
        for(int i = 0; i < symbols.size(); i++){
            if(symbols.get(i).getColor().equals(symbol)){
                index = i;
            }
        }
        if (index == -1){
            return false;
        }
        currentIndex = index;
        symbols.get(currentIndex).select();
        updateColor();
        return true;
    }
    
    /**
     * marca si esta rueda hace parte de la combinacion ganadora actual.
     * si winning es true, el fondo (frame) se pone amarillo; si es
     * false, vuelve a su gris normal.
     * @param winning true si la maquina esta en estado ganador
     */
    public void setWinning(boolean winning){
        this.winning = winning;
        frame.changeColor(winning ? "yellow" : "gray");
        if(visible && getCurrentSymbol() != null){
            symbols.get(currentIndex).makeVisible(); 
        }
    }
    
    /**
     * metodo de shapes que hace que sea visible
     */
    public void makeVisible(){
        visible = true;
        frame.makeVisible();
        if(getCurrentSymbol() != null){
            symbols.get(currentIndex).makeVisible();
        }
        updateLockMark();
    }
    
    /**
     * metodo de shape que hace que sea invisible
     */
    public void makeInvisible(){
        visible = false;
        frame.makeInvisible();
        for(int i = 0; i < symbols.size(); i++){
            symbols.get(i).makeInvisible();
        }
        lockMark.makeInvisible();
        activeMark.makeInvisible();
    }
    
    /**
     * Vuelve a dibujar el frame y el circulo sin moverlos, para que
     * queden al frente en el Canvas (por ejemplo, despues de que el
     * fondo negro de la maquina cambie de tamano y quede encima).
     */
    public void bringToFront(){
        frame.moveHorizontal(0);
        if(getCurrentSymbol() != null){
            symbols.get(currentIndex).bringToFront();
        }
        lockMark.moveHorizontal(0);
        activeMark.moveHorizontal(0);
    }
    
    /**
     * actualiza el circulo del simbolo para que coincida con el color
     * actualmente visible. Si no hay simbolo asignado (getCurrentSymbol
     * da null), el circulo se oculta.
     */
    private void updateColor(){
        for(int i = 0; i < symbols.size(); i++){
            symbols.get(i).makeInvisible();
        }
        if(getCurrentSymbol() != null && visible){
            symbols.get(currentIndex).makeVisible();
        }
    }
    
    /**
     * Mueve el frame y el circulo a la posicion en pantalla que le
     * corresponde segun la posicion logica de la rueda, para que cada
     * rueda quede separada de las demas en el Canvas.
     */
    private void updatePosition(){
        int targetFrameX = 20 + (position - 1) * 70;
        int targetFrameY = 40;
        frame.moveHorizontal(targetFrameX - frameX);
        frame.moveVertical(targetFrameY - frameY);
        frameX = targetFrameX;
        frameY = targetFrameY;
        
        int targetSymX = targetFrameX + 5; 
        int targetSymY = targetFrameY;
        symX = targetSymX;
        symY = targetSymY;
        for(int i = 0; i < symbols.size(); i++){
            symbols.get(i).setPosition(symX, symY);
        }
        
        int targetLockX = targetFrameX + 15;
        int targetLockY = targetFrameY - 14;
        lockMark.moveHorizontal(targetLockX - lockX );
        lockMark.moveVertical(targetLockY - lockY);
        lockX = targetLockX;
        lockY = targetLockY;
        
        int targetActiveX = targetFrameX + 16;
        int targetActiveY = targetFrameY + 34;
        activeMark.moveHorizontal(targetActiveX - activeX);
        activeMark.moveVertical(targetActiveY - activeY);
        activeX = targetActiveX;
        activeY = targetActiveY;
    }
    
    public void lock(){
        locked = true;
        updateLockMark();
    }
    
    public void unlock(){
        locked = false;
        updateLockMark();
    }
    /**
     * actualiza la posicion del bloqueo de una rueda
     */
    private void updateLockMark(){
        if (locked && visible){
            lockMark.makeVisible();
        } else {
            lockMark.makeInvisible();
        }
    }
    public boolean isLocked(){
        return locked;
    }
    public void markActive(boolean active){
        if (active && visible){
            activeMark.makeVisible();
        } else {
            activeMark.makeInvisible();
        }
    }
}