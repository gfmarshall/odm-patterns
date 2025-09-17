package au.gov.vic.education.ece.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * Base class representing a person in the Victorian ECE funding model.
 * Implements core attributes common to all person types (child, guardian, etc.)
 * 
 * @version 1.0
 */
public class Person {
    private final String personId;
    private final String firstName;
    private final String lastName;
    private final LocalDate dateOfBirth;
    private final Gender gender;
    private final IndigenousStatus indigenousStatus;
    private final List<String> immigrationStatusCodes;
    private final boolean outOfHomeCareStatus;
    private final boolean disabilityOrAddNeeds;
    private final List<String> diagnoses;
    private final List<String> supportPlanRefs;
    
    /**
     * Gender enumeration as defined in the canonical data model
     */
    public enum Gender {
        MALE, FEMALE, X, UNSTATED
    }
    
    /**
     * Indigenous status enumeration as defined in the canonical data model
     */
    public enum IndigenousStatus {
        ABORIGINAL, TORRES_STRAIT, BOTH, NONE, UNSTATED
    }

    /**
     * Builder pattern implementation for Person class
     */
    public static class Builder {
        // Required parameters
        private final String personId;
        private final String firstName;
        private final String lastName;
        private final LocalDate dateOfBirth;
        
        // Optional parameters with defaults
        private Gender gender = Gender.UNSTATED;
        private IndigenousStatus indigenousStatus = IndigenousStatus.UNSTATED;
        private List<String> immigrationStatusCodes = new ArrayList<>();
        private boolean outOfHomeCareStatus = false;
        private boolean disabilityOrAddNeeds = false;
        private List<String> diagnoses = new ArrayList<>();
        private List<String> supportPlanRefs = new ArrayList<>();
        
        public Builder(String personId, String firstName, String lastName, LocalDate dateOfBirth) {
            this.personId = personId;
            this.firstName = firstName;
            this.lastName = lastName;
            this.dateOfBirth = dateOfBirth;
        }
        
        public Builder gender(Gender gender) {
            this.gender = gender;
            return this;
        }
        
        public Builder indigenousStatus(IndigenousStatus indigenousStatus) {
            this.indigenousStatus = indigenousStatus;
            return this;
        }
        
        public Builder immigrationStatusCodes(List<String> immigrationStatusCodes) {
            this.immigrationStatusCodes = new ArrayList<>(immigrationStatusCodes);
            return this;
        }
        
        public Builder outOfHomeCareStatus(boolean outOfHomeCareStatus) {
            this.outOfHomeCareStatus = outOfHomeCareStatus;
            return this;
        }
        
        public Builder disabilityOrAddNeeds(boolean disabilityOrAddNeeds) {
            this.disabilityOrAddNeeds = disabilityOrAddNeeds;
            return this;
        }
        
        public Builder diagnoses(List<String> diagnoses) {
            this.diagnoses = new ArrayList<>(diagnoses);
            return this;
        }
        
        public Builder supportPlanRefs(List<String> supportPlanRefs) {
            this.supportPlanRefs = new ArrayList<>(supportPlanRefs);
            return this;
        }
        
        public Person build() {
            return new Person(this);
        }
    }
    
    private Person(Builder builder) {
        personId = builder.personId;
        firstName = builder.firstName;
        lastName = builder.lastName;
        dateOfBirth = builder.dateOfBirth;
        gender = builder.gender;
        indigenousStatus = builder.indigenousStatus;
        immigrationStatusCodes = Collections.unmodifiableList(new ArrayList<>(builder.immigrationStatusCodes));
        outOfHomeCareStatus = builder.outOfHomeCareStatus;
        disabilityOrAddNeeds = builder.disabilityOrAddNeeds;
        diagnoses = Collections.unmodifiableList(new ArrayList<>(builder.diagnoses));
        supportPlanRefs = Collections.unmodifiableList(new ArrayList<>(builder.supportPlanRefs));
    }
    
    // Getters - no setters to maintain immutability
    public String getPersonId() { return personId; }
    public String getFirstName() { return firstName; }
    public String getLastName() { return lastName; }
    public LocalDate getDateOfBirth() { return dateOfBirth; }
    public Gender getGender() { return gender; }
    public IndigenousStatus getIndigenousStatus() { return indigenousStatus; }
    public List<String> getImmigrationStatusCodes() { return immigrationStatusCodes; }
    public boolean isOutOfHomeCareStatus() { return outOfHomeCareStatus; }
    public boolean hasDisabilityOrAddNeeds() { return disabilityOrAddNeeds; }
    public List<String> getDiagnoses() { return diagnoses; }
    public List<String> getSupportPlanRefs() { return supportPlanRefs; }
    
    /**
     * Calculate age at a specific reference date
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
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Person person = (Person) o;
        return personId.equals(person.personId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(personId);
    }
    
    @Override
    public String toString() {
        return "Person{" +
                "personId='" + personId + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", dateOfBirth=" + dateOfBirth +
                '}';
    }
}
