package funding;

import java.io.Serializable;

/**
 * Family class - represents a family unit in the Victorian ECE funding model.
 * Simple POJO design for IBM ODM as per project guidelines.
 */
public class Family implements Serializable {
    private static final long serialVersionUID = 1L;
    
    // Core attributes
    private String householdId;
    private String address;
    private String lgaCode;
    private int seifaQuintile;
    private String primaryLanguage;
    private boolean interpreterRequired;
    
    /**
     * Default constructor
     */
    public Family() {
    }
    
    /**
     * Constructor with mandatory fields
     */
    public Family(String householdId, String address, String lgaCode) {
        this.householdId = householdId;
        this.address = address;
        this.lgaCode = lgaCode;
    }
    
    // Getters and setters
    public String getHouseholdId() {
        return householdId;
    }
    
    public void setHouseholdId(String householdId) {
        this.householdId = householdId;
    }
    
    public String getAddress() {
        return address;
    }
    
    public void setAddress(String address) {
        this.address = address;
    }
    
    public String getLgaCode() {
        return lgaCode;
    }
    
    public void setLgaCode(String lgaCode) {
        this.lgaCode = lgaCode;
    }
    
    public int getSeifaQuintile() {
        return seifaQuintile;
    }
    
    public void setSeifaQuintile(int seifaQuintile) {
        if (seifaQuintile < 0 || seifaQuintile > 5) {
            throw new IllegalArgumentException("SEIFA quintile must be between 0 and 5");
        }
        this.seifaQuintile = seifaQuintile;
    }
    
    public String getPrimaryLanguage() {
        return primaryLanguage;
    }
    
    public void setPrimaryLanguage(String primaryLanguage) {
        this.primaryLanguage = primaryLanguage;
    }
    
    public boolean isInterpreterRequired() {
        return interpreterRequired;
    }
    
    public void setInterpreterRequired(boolean interpreterRequired) {
        this.interpreterRequired = interpreterRequired;
    }
    
    /**
     * Check if this family is in a specified LGA
     * 
     * @param lgaCodeToCheck The LGA code to check against
     * @return true if the family is in the specified LGA
     */
    public boolean isInLga(String lgaCodeToCheck) {
        return lgaCode != null && lgaCode.equals(lgaCodeToCheck);
    }
    
    /**
     * Check if this family is in a disadvantaged area
     * Based on SEIFA quintiles where lower values indicate more disadvantage
     * 
     * @return true if SEIFA quintile is 1 or 2
     */
    public boolean isInDisadvantagedArea() {
        return seifaQuintile == 1 || seifaQuintile == 2;
    }
}
