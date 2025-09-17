package funding;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Person class - represents a person in the Victorian ECE funding model.
 * Simple POJO design for IBM ODM as per project guidelines.
 */
public class Person implements Serializable {
    private static final long serialVersionUID = 1L;
    
    // Core person attributes
    private String personId;
    private String firstName;
    private String lastName;
    private LocalDate dateOfBirth;
    private Gender gender = Gender.UNSTATED;
    private IndigenousStatus indigenousStatus = IndigenousStatus.UNSTATED;
    private List<String> immigrationStatusCodes = new ArrayList<>();
    private boolean outOfHomeCareStatus;
    private boolean disabilityOrAddNeeds;
    private List<String> diagnoses = new ArrayList<>();
    private List<String> supportPlanRefs = new ArrayList<>();
    
    /**
     * Gender enumeration
     */
    public enum Gender {
        MALE, FEMALE, X, UNSTATED
    }
    
    /**
     * Indigenous status enumeration
     */
    public enum IndigenousStatus {
        ABORIGINAL, TORRES_STRAIT, BOTH, NONE, UNSTATED
    }
    
    /**
     * Default constructor
     */
    public Person() {
    }
    
    /**
     * Constructor with mandatory fields
     */
    public Person(String personId, String firstName, String lastName, LocalDate dateOfBirth) {
        this.personId = personId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.dateOfBirth = dateOfBirth;
    }
    
    // Getters and setters
    public String getPersonId() {
        return personId;
    }
    
    public void setPersonId(String personId) {
        this.personId = personId;
    }
    
    public String getFirstName() {
        return firstName;
    }
    
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }
    
    public String getLastName() {
        return lastName;
    }
    
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
    
    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }
    
    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }
    
    public Gender getGender() {
        return gender;
    }
    
    public void setGender(Gender gender) {
        this.gender = gender;
    }
    
    public IndigenousStatus getIndigenousStatus() {
        return indigenousStatus;
    }
    
    public void setIndigenousStatus(IndigenousStatus indigenousStatus) {
        this.indigenousStatus = indigenousStatus;
    }
    
    public List<String> getImmigrationStatusCodes() {
        return immigrationStatusCodes;
    }
    
    public void setImmigrationStatusCodes(List<String> immigrationStatusCodes) {
        this.immigrationStatusCodes = immigrationStatusCodes;
    }
    
    public boolean isOutOfHomeCareStatus() {
        return outOfHomeCareStatus;
    }
    
    public void setOutOfHomeCareStatus(boolean outOfHomeCareStatus) {
        this.outOfHomeCareStatus = outOfHomeCareStatus;
    }
    
    public boolean isDisabilityOrAddNeeds() {
        return disabilityOrAddNeeds;
    }
    
    public void setDisabilityOrAddNeeds(boolean disabilityOrAddNeeds) {
        this.disabilityOrAddNeeds = disabilityOrAddNeeds;
    }
    
    public List<String> getDiagnoses() {
        return diagnoses;
    }
    
    public void setDiagnoses(List<String> diagnoses) {
        this.diagnoses = diagnoses;
    }
    
    public List<String> getSupportPlanRefs() {
        return supportPlanRefs;
    }
    
    public void setSupportPlanRefs(List<String> supportPlanRefs) {
        this.supportPlanRefs = supportPlanRefs;
    }
    
    /**
     * Calculate age at a specific reference date
     * 
     * @param referenceDate The date to calculate age against
     * @return The age in years at the reference date
     */
    public int getAgeAt(LocalDate referenceDate) {
        if (referenceDate == null) {
            throw new IllegalArgumentException("Reference date cannot be null");
        }
        
        int years = referenceDate.getYear() - dateOfBirth.getYear();
        
        // Adjust if birthday hasn't occurred yet this year
        if (referenceDate.getMonthValue() < dateOfBirth.getMonthValue() || 
            (referenceDate.getMonthValue() == dateOfBirth.getMonthValue() && 
             referenceDate.getDayOfMonth() < dateOfBirth.getDayOfMonth())) {
            years--;
        }
        
        return years;
    }
    
    /**
     * Determines if person is potentially eligible for Early Start Kindergarten (ESK)
     * 
     * @return true if potentially ESK-eligible based on attributes
     */
    public boolean isPotentiallyEskEligible() {
        return indigenousStatus != IndigenousStatus.NONE || 
               outOfHomeCareStatus ||
               immigrationStatusCodes.contains("REFUGEE") || 
               immigrationStatusCodes.contains("ASYLUM_SEEKER");
    }
}
