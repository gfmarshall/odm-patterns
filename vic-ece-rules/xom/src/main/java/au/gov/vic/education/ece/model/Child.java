package au.gov.vic.education.ece.model;

import java.time.LocalDate;
import java.util.*;

/**
 * Represents a child in the Victorian ECE funding model.
 * Extends the base Person class with child-specific attributes.
 *
 * @version 1.0
 */
public class Child extends Person {

    /**
     * Program type enumeration for base kindergarten programs
     */
    public enum BaseProgramType {
        THREE_YEAR_OLD_KINDER,
        PRE_PREP
    }

    private final Set<String> veylfDelays;
    private final Map<BaseProgramType, Integer> fundedKinderYearsCompleted;
    private final BaseProgramType yearLevelIntent;
    
    /**
     * Builder for creating immutable Child instances
     */
    public static class Builder {
        private final Person.Builder personBuilder;
        private Set<String> veylfDelays = new HashSet<>();
        private Map<BaseProgramType, Integer> fundedKinderYearsCompleted = new EnumMap<>(BaseProgramType.class);
        private BaseProgramType yearLevelIntent;

        public Builder(String personId, String firstName, String lastName, LocalDate dateOfBirth) {
            personBuilder = new Person.Builder(personId, firstName, lastName, dateOfBirth);
            
            // Initialize default values for funded years
            fundedKinderYearsCompleted.put(BaseProgramType.THREE_YEAR_OLD_KINDER, 0);
            fundedKinderYearsCompleted.put(BaseProgramType.PRE_PREP, 0);
        }

        // Delegate methods to Person.Builder
        public Builder gender(Person.Gender gender) {
            personBuilder.gender(gender);
            return this;
        }

        public Builder indigenousStatus(Person.IndigenousStatus indigenousStatus) {
            personBuilder.indigenousStatus(indigenousStatus);
            return this;
        }

        public Builder immigrationStatusCodes(List<String> immigrationStatusCodes) {
            personBuilder.immigrationStatusCodes(immigrationStatusCodes);
            return this;
        }

        public Builder outOfHomeCareStatus(boolean outOfHomeCareStatus) {
            personBuilder.outOfHomeCareStatus(outOfHomeCareStatus);
            return this;
        }

        public Builder disabilityOrAddNeeds(boolean disabilityOrAddNeeds) {
            personBuilder.disabilityOrAddNeeds(disabilityOrAddNeeds);
            return this;
        }

        public Builder diagnoses(List<String> diagnoses) {
            personBuilder.diagnoses(diagnoses);
            return this;
        }

        public Builder supportPlanRefs(List<String> supportPlanRefs) {
            personBuilder.supportPlanRefs(supportPlanRefs);
            return this;
        }

        // Child-specific builder methods
        public Builder veylfDelays(Set<String> veylfDelays) {
            this.veylfDelays = new HashSet<>(veylfDelays);
            return this;
        }

        public Builder addVeylfDelay(String veylfDelay) {
            this.veylfDelays.add(veylfDelay);
            return this;
        }

        public Builder fundedKinderYearsCompleted(BaseProgramType programType, int yearsCompleted) {
            this.fundedKinderYearsCompleted.put(programType, yearsCompleted);
            return this;
        }
        
        public Builder yearLevelIntent(BaseProgramType yearLevelIntent) {
            this.yearLevelIntent = yearLevelIntent;
            return this;
        }

        public Child build() {
            return new Child(this);
        }
    }

    private Child(Builder builder) {
        super(builder.personBuilder.build());
        this.veylfDelays = Collections.unmodifiableSet(new HashSet<>(builder.veylfDelays));
        
        // Create immutable copy of funded years map
        Map<BaseProgramType, Integer> tempMap = new EnumMap<>(BaseProgramType.class);
        for (Map.Entry<BaseProgramType, Integer> entry : builder.fundedKinderYearsCompleted.entrySet()) {
            tempMap.put(entry.getKey(), entry.getValue());
        }
        this.fundedKinderYearsCompleted = Collections.unmodifiableMap(tempMap);
        
        this.yearLevelIntent = builder.yearLevelIntent;
    }

    // Getters
    public Set<String> getVeylfDelays() {
        return veylfDelays;
    }

    public int getVeylfDelaysCount() {
        return veylfDelays.size();
    }

    public Map<BaseProgramType, Integer> getFundedKinderYearsCompleted() {
        return fundedKinderYearsCompleted;
    }

    public int getFundedKinderYearsCompleted(BaseProgramType programType) {
        return fundedKinderYearsCompleted.getOrDefault(programType, 0);
    }
    
    public BaseProgramType getYearLevelIntent() {
        return yearLevelIntent;
    }
    
    /**
     * Checks if the child is eligible for an additional year based on VEYLDF delays
     * @return true if eligible based on delay count, false otherwise
     */
    public boolean hasMinimumVeylfDelaysForAdditionalYear() {
        return getVeylfDelaysCount() >= 2;
    }
    
    /**
     * Checks if the child has already completed an additional year of the specified program type
     * @param programType The program type to check
     * @return true if the child has already completed an additional year
     */
    public boolean hasCompletedAdditionalYearFor(BaseProgramType programType) {
        return getFundedKinderYearsCompleted(programType) > 0;
    }
    
    /**
     * Determines if the child is potentially eligible for Early Start Kindergarten (ESK)
     * based on their attributes
     * 
     * @return true if potentially ESK-eligible based on indigenous status, OOHC, or refugee status
     */
    public boolean isPotentiallyEskEligible() {
        return getIndigenousStatus() != IndigenousStatus.NONE || 
               isOutOfHomeCareStatus() ||
               hasRefugeeOrAsylumStatus();
    }
    
    /**
     * Helper method to check if the child has refugee or asylum seeker status
     * based on immigration status codes
     * 
     * @return true if the child has refugee or asylum status codes
     */
    public boolean hasRefugeeOrAsylumStatus() {
        // In a real implementation, this would check against a configured list of codes
        // For now we're hardcoding REFUGEE and ASYLUM_SEEKER as examples
        List<String> codes = getImmigrationStatusCodes();
        return codes.contains("REFUGEE") || codes.contains("ASYLUM_SEEKER");
    }
}
