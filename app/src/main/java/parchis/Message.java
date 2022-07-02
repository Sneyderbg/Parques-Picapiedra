package parchis;

/**
 * Esta clase se usa para alamacenar mensajes que describen brevemente el
 * resultado de un método que normalmente no retornaría nada.
 */
public class Message {

    enum Type {
        SUCCESS,
        ERROR,
    }

    Type type;

    String info;

    /**
     * Constructor
     * 
     * @param type Tipo de resultado.
     * @param info {@link String} que contiene una breve información del resultado.
     */
    public Message(Type type, String info) {
        this.type = type;
        this.info = info;
    }

    /**
     * Retorna un String que describe el resultado.
     * 
     * @return {@link String} descripcíon.
     */
    public String getMessage() {
        return String.format("%s: %s.", this.type, this.info);
    }

}
