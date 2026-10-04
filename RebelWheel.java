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
    public void lock() throws SlotMachineException{
        throw new SlotMachineException(SlotMachineException.REBEL_NOT_LOCKABLE);
    }
    /**
     *throws SlotMachineException siempre que la rueba rebekde no se deja bloquear 
     */
    public void checkSwappable() throws SlotMachineException{
        throw new SlotMachineException(SlotMachineException.REBEL_NOT_SWAPPABLE);
    }
    /**
     * @throws SlotMachineException siempre que la rueda rebelde no se deja eliminar
     */
    public void checkDeletable() throws SlotMachineException{
        throw new SlotMachineException(SlotMachineException.REBEL_NOT_DELETABLE);
    }
    }

