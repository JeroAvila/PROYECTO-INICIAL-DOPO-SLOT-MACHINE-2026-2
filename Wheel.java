import java.util.ArrayList;
import java.util.Random;
/**
 * Es la representacion de una rueda en la maquina tragamonedas

 * @author Thomas Jeronimo Avila Casillo
 * @author Laura Juliana Parra Velandia
 * @version (a version number or a date)
 */
public abstract class Wheel
{
    private static final int SHAPE_X = 70; //Comienza rectangle
    private static final int SHAPE_Y = 15;
    private static final int FIRST_X = 20; // marco de la primera rueda
    private static final int TOP_Y = 40;
    private static final int SPACING = 70; // es la distancia de ruedass
    private static final int SYMBOL_DX = 5; // se desplaza segun el marco
    private static final int LOCK_DX = 15;
    private static final int ACTIVE_DX = 16;
    private static final int ACTIVE_DY = 34;
    private static final int TYPE_DX = 29;
    private static final int MARK_DY = -14;
    private static final int MARK_SIZE = 10;
    private static final int ACTIVE_SIZE = 8;
    
    private int position;
    private Rectangle frame;
    private Rectangle typeMark;
    private Rectangle activeMark;
    private Rectangle lockMark;
    private int frameX;
    private int frameY;
    private ArrayList<Symbol> symbols;
    private int currentIndex;
    private boolean visible;
    private boolean locked;
    private Wheel left;
    private int group;
    private Random random;
    /**
     * CONSTRUCTOR
     * Crea una rueda vacia en la posicion dada. for objects of class Wheel
     */
    public Wheel(int position, String badgeColor)
    {
        this.position = position;
        symbols = new ArrayList<Symbol>();
        currentIndex = -1;
        locked = false;
        left = null;
        group = 0;
        random = new Random();
        frame = new Rectangle();
        frame.changeColor("gray");
        frameX = SHAPE_X;
        frameY = SHAPE_Y;
        typeMark = createMark(MARK_SIZE, badgeColor, TYPE_DX, MARK_DY);
        lockMark = createMark(MARK_SIZE, "red", LOCK_DX, MARK_DY);
        activeMark = createMark(ACTIVE_SIZE, "green", ACTIVE_DX, ACTIVE_DY);
        updatePosition();
    }
    /**
     * retorna el nombre del tipo de rueda. si es normal, lefty, rebel, o lazy
     */
    public abstract String getType();
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
    //ciclo4
    /**
     * la maquina dice a la rueda cual es la rueda de la izquierda
     * se actualiza cada vez que las ruedas cambian de orden
     */
    public void setLeft(Wheel left){
        this.left = left;   
    }
    /**
     * retorna la rueda de la izquierda
     */
    protected Wheel getLeft(){
        return left;
    }
    /**
     * cuantas ruedas del mismo tipo trae esta rueda al agregarse a la slomachine.
     * @return la cantidad de ruedas que acompanian
     */
    public int getCompanions(){
        return 0;
    }
    /**
     * la maquina asigna el grupo a las ruedas que se agregan juntas
     * @param group es el numero del grupo
     */
    public void setGroup(int group){
        this.group = group;
    }
    /**
     * retorna el numero de la rueda
     */
    public int getGroup(){
        return group;
    }
    /**
     * MC3
     * recibe la lista de simbolos de la maquina y la guarda como una copia.
     * Si el simbolo que ya estaba visible sigue existiendo en la lista
     * nueva, se mantiene; si no, la rueda queda sin simbolo asignado.
     */
    public void setSymbols(ArrayList<Symbol> catalog) {
        String previousColor = getCurrentSymbol();
        hideSymbols();
        symbols = new ArrayList<Symbol>();
        for(int i = 0; i < catalog.size(); i++){
            Symbol copy = catalog.get(i).copy();
            copy.setPosition(frameX + SYMBOL_DX, frameY);
            symbols.add(copy);
        }
        currentIndex = indexOf(previousColor);
        showCurrentSymbol();
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
     * retorna true si la rueda tiene un simbolo
     */
    protected boolean hasSymbols(){
        return !symbols.isEmpty();
    }
    /**
     * Elige un simbolo al azar del catalogo de esta rueda.
     */
    public void spin() {
        if(!locked && hasSymbols()){
            select(random.nextInt(symbols.size()));
        }
    }
    /**
     * copia el estado de una rueda a otra
     * si la otra rueda no muestra nada esta tampoco muestra nada
     * @param other es la rueda que copia el estado de otra
     */
    protected void copyStateOf(Wheel other){
        String color = other.getCurrentSymbol();
        if (color == null){
            currentIndex = -1;
            showCurrentSymbol();
        } else {
            placeSymbol(color);
        }
    }
    /**
     * busca el simbolo en la lista, si no encuentra da -1 y el metodo da false
     * si lo encuentra mueve el currentIndex a esa posicio para que sea visible el simbolo
     */
    public boolean placeSymbol(String color){
        int index = indexOf(color);
        if(index == -1){
            return false;
        }
        select(index);
        return true;
    }
    
    /**
     * marca si esta rueda hace parte de la combinacion ganadora actual.
     * si winning es true, el fondo (frame) se pone amarillo; si es
     * false, vuelve a su gris normal.
     * @param winning true si la maquina esta en estado ganador
     */
    public void setWinning(boolean winning){
        if(winning){
            frame.changeColor("yellow");
        } else {
            frame.changeColor("gray");
        }
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
        typeMark.makeInvisible();
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
        typeMark.makeInvisible();
        hideSymbols();
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
        typeMark.moveHorizontal(0);
        if(getCurrentSymbol() != null){
            symbols.get(currentIndex).bringToFront();
        }
        lockMark.moveHorizontal(0);
        activeMark.moveHorizontal(0);
    }
     /**
     * bloquea la rueda
     * @throw SlotMachineException si este tipo de rueda no se deja bloquear
     */
    public void lock() throws SlotMachineException{
        locked = true;
        updateLockMark();
    }
    /**
     * desbloquea la rueda
     */
    public void unlock(){
        locked = false;
        updateLockMark();
    }
    /**
     * retorna true si la rueda esta bloqueada
     */
    public boolean isLocked(){
        return locked;
    }
    /**
     * revisa si la rueda se puede intercambiar de lugar con otra
     * @throws SlotMachineException si esta rueda no se deja intercambiar
     */
    public void checkSwappable() throws SlotMachineException{   
    }
    
    /**
     * revisa si la rueda se puede eliminar de la maquina
     * @throws SlotMachineExceptions si esta rueda no se deja eliminar
     */
    public void checkDeletable() throws SlotMachineException{   
    }
    /**
     * muestra, oculata la marca del indice que indica si la rueda esta girando
     * @param active es que se esta mostarndo
     */
    public void markActive(boolean active){
        if(active && visible){
            activeMark.makeVisible();
        } else {
            activeMark.makeInvisible();
        }
    }
    /**
     * crea las marcas de la rueda y se ubica en el marco
     * @param size es el lado del cuadrado
     * @param color es el color de la marca
     * @param dx es la distancia horizontal al marco
     * @param dy es la distacia vertical al marco
     * retorna la marca que se creo
     */
    private Rectangle createMark(int size, String color, int dx, int dy){
        Rectangle mark = new Rectangle();
        mark.changeSize(size, size);
        mark.changeColor(color);
        mark.moveHorizontal(dx);
        mark.moveVertical(dy);
        return mark;
    }
    /**
     * busca entre los simbolos de la rueda que color tiene la de al lado
     * @param color es el color que se esta buscando
     * retorna el indice  o -1 si la rueda no tiene el simbolo
     */
    private int indexOf(String color){
        for(int i = 0; i < symbols.size(); i++){
            if(symbols.get(i).getColor().equals(color)){
                return i;
            }
        }
        return -1;
    }
    /**
     * es para que la rueda muestre el simbolo del indice
     * y avisa al simbolo que fue seleccionado.
     * @param index es el indice del simbolo
     */   
    private void select(int index){
        currentIndex = index;
        symbols.get(currentIndex).select();
        showCurrentSymbol();
    }
    /**
     * borra la pantalla todos los simbolos que estan en la rueda
     */
    private void hideSymbols(){
        for(int i = 0; i < symbols.size(); i++){
            symbols.get(i).makeInvisible();
        }
    }
    /**
     * solo deja dibujado unicamente el simbolo actual
     */
    private void showCurrentSymbol(){
        hideSymbols();
        if(visible && currentIndex != -1){
            symbols.get(currentIndex).makeVisible();
        }
    }
    /**
     * muece el marco, las marcas, los simbolos
     * al lugar de la pantalla que le toca segun las ruedas
     */
    private void updatePosition(){
        int dx = FIRST_X + (position -1) * SPACING - frameX;
        int dy = TOP_Y - frameY;
        move(frame, dx, dy);
        move(typeMark, dx, dy);
        move(lockMark, dx, dy);
        move(activeMark, dx, dy);
        frameX = frameX + dx;
        frameY = frameY + dy;
        for(int i = 0; i < symbols.size(); i++){
            symbols.get(i).setPosition(frameX + SYMBOL_DX, frameY);
        }
    }
    /**
     * mueve el rectangulo la distancia que se de
     * @param shape es el rectangulo a mover
     * @param dc es la distancia horizontal
     * @param dy es la distancia vertical
     */
    private void move(Rectangle shape, int dx, int dy){
        shape.moveHorizontal(dx);
        shape.moveVertical(dy);
    }
    /**
     * esto muestra la marca roja solo si la rueda esta bloqueada y es visible
     */
    private void updateLockMark(){
        if(locked && visible){
            lockMark.makeVisible();
        } else {
            lockMark.makeInvisible();
        }
    }
}