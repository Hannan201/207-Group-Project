package views.utilities.CodeViewUtilities;

public class CodeCell {

    /**
     * A String that represents the current code
     */
    private String code;

    /**
     * Create a CodeCell object with the given name, codeName
     * @param codeName Name of the code
     */
    public CodeCell(String codeName) {this.code = codeName;}

    /**
     * Getter to get the code value
     * @return the code value
     */
    public String getCode() {return code;}

    /**
     * Setter to change the code value
     * @param newCode The name of the code to switch to
     */
    public void setCode(String newCode) {this.code = newCode;}

}
