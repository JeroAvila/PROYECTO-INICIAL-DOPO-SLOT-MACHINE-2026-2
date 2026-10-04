/**
 * rueda rebelde. no se deja bloquear, ni intercambiar, ni eliminar
 * no deja hacer nada basicamente
 * @author Laura Juliana Parra Velandia
 * @author Thomas Jeronimo Avila Castillo
 */
public class RebelWheel extends Wheel{
    /**
     * CONSTRUCTOR
     * crea una rueda rebelde en una posicion dada
     * @param position posicion logica de la rueda
     */
    public RebelWheel(int position){
            super(position, "orange");
    }
    /**
     * retorna "rebel"
     */
    public String getType(){
        return "rebel";
    }
    /**
     * @throws SlotMachineException siempre que la rueda rebelde no deja bloquear
     */
    public void Lock() throws SlotMachineException{
        throw new SlotMachineException("la rueda rebelde no se deja bloquear");
    }
    /**
     *throws SlotMachineException siempre que la rueba rebekde no se deja bloquear 
     */
    public void checkSwappable() throws SlotMachineException{
        throw new SlotMachineException("la rueda rebelde no se deja intercambiar");
    }
    /**
     * @throws SlotMachineException siempre que la rueda rebelde no se deja eliminar
     */
    public void checkDeletable() throws SlotMachineException{
        throw new SlotMachineException("la rueda rebelde no se deja eliminar");
    }
    }

