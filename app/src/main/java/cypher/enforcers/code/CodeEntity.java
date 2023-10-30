package cypher.enforcers.code;

public class CodeEntity {

    // ID for this code.
    private long id;

    // ID of the account this code belongs to.
    private long accountID;

    /**
     * A String that represents the current code
     */
    private String code;

    /**
     * Create a Code object with the given name, codeName
     *
     * @param ID ID of the code.
     * @param codeName Name of the code
     */
    public CodeEntity(int ID, String codeName) {
        this.id = ID;
        this.code = codeName;
    }

    /**
     * Create a code.
     */
    public CodeEntity() {

    }

    /**
     * Get the ID of this code.
     *
     * @return ID of this code.
     */
    public long getId() {
        return this.id;
    }

    /**
     * Set the ID of this code.
     *
     * @param id The new id.
     */
    public void setId(long id) {
        this.id = id;
    }

    /**
     * Get the ID that this account belongs to.
     *
     * @return The ID of the account.
     */
    public long getAccountID() {
        return accountID;
    }

    /**
     * Set the ID of the account that this code belongs to.
     *
     * @param accountID The new ID.
     */
    public void setAccountID(long accountID) {
        this.accountID = accountID;
    }

    /**
     * Getter to get the code value
     * @return the code value
     */
    public String getCode() {
        return code;
    }

    /**
     * Set the code for this account.
     *
     * @param code The new code.
     */
    public void setCode(String code) {
        this.code = code;
    }
}
