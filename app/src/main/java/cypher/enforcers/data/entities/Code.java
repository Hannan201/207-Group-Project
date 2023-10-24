package cypher.enforcers.data.entities;

import jakarta.persistence.*;

/**
 * Java representation of the codes table in the database.
 */
@Entity
@Table(name = "codes")
public class Code {

    // ID for this code.
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long ID;

    // Code as a string.
    @Column(name = "code", nullable = false, columnDefinition = "CHECK ( length(code) > 0 )")
    private String code;

    // The account this code belongs to.
    @ManyToOne
    private Account account;

    /**
     * Create a new code.
     */
    public Code() {

    }

    /**
     * Get the ID for this code.
     *
     * @return ID of this code.
     */
    public long getID() {
        return this.ID;
    }

    /**
     * Get the code.
     *
     * @return The code.
     */
    public String getCode() {
        return code;
    }

    /**
     * Get the account this code belongs to.
     *
     * @return Account this code belongs to.
     */
    public Account getAccount() {
        return account;
    }

    /**
     * Set the ID for this code.
     *
     * @param ID New ID for this code.
     */
    public void setID(long ID) {
        this.ID = ID;
    }

    /**
     * Set the code.
     *
     * @param code The new code.
     */
    public void setCode(String code) {
        this.code = code;
    }

    /**
     * Set the account this code belongs to.
     *
     * @param account The account this code belongs to.
     */
    public void setAccount(Account account) {
        this.account = account;
    }
}
