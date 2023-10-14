package cypher.enforcers.code;

public class Code {

    // ID for this code.
    private final int ID;

    /**
     * A String that represents the current code
     */
    private final String code;

    /**
     * Create a Code object with the given name, codeName
     *
     * @param ID ID of the code.
     * @param codeName Name of the code
     */
    public Code(int ID, String codeName) {
        this.ID = ID;
        this.code = codeName;
    }

    /**
     * Get the ID of this code.
     *
     * @return ID of this code.
     */
    public int getID() {
        return this.ID;
    }

    /**
     * Getter to get the code value
     * @return the code value
     */
    public String getCode() {
        return code;
    }

}
