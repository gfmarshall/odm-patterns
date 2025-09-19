package funding;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.EnumMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

// ODM annotations
import ilog.rules.bom.annotations.*;

/**
 * Child class - extends Person for the Victorian ECE funding model. Simple POJO
 * design for IBM ODM as per project guidelines.
 */
public class Child extends Person implements Serializable {
	private static final long serialVersionUID = 1L;

	// Child-specific attributes
	private Set<String> veylfDelays = new HashSet<>();

	private Map<BaseProgramType, Integer> fundedKinderYearsCompleted = new EnumMap<>(BaseProgramType.class);

	private BaseProgramType yearLevelIntent;

	/**
	 * Program type enumeration for base kindergarten programs
	 */
	public enum BaseProgramType {
		THREE_YEAR_OLD_KINDER, PRE_PREP
	}

	/**
	 * Default constructor
	 */
	public Child() {
		super();
		// Initialize default funded years
		fundedKinderYearsCompleted.put(BaseProgramType.THREE_YEAR_OLD_KINDER, 0);
		fundedKinderYearsCompleted.put(BaseProgramType.PRE_PREP, 0);
	}

	/**
	 * Constructor with mandatory fields
	 * 
	 * @param personId    The person identifier
	 * @param firstName   The first name
	 * @param lastName    The last name
	 * @param dateOfBirth The date of birth
	 */
	@CustomProperty(name = "dataio.default", value = "true")
	public Child(@BusinessName("personId") String personId, @BusinessName("firstName") String firstName,
			@BusinessName("lastName") String lastName, @BusinessName("dateOfBirth") LocalDate dateOfBirth) {
		super(personId, firstName, lastName, dateOfBirth);
		// Initialize default funded years
		fundedKinderYearsCompleted.put(BaseProgramType.THREE_YEAR_OLD_KINDER, 0);
		fundedKinderYearsCompleted.put(BaseProgramType.PRE_PREP, 0);
	}

	// Getters and setters for Child-specific properties
	public Set<String> getVeylfDelays() {
		return veylfDelays;
	}

	public void setVeylfDelays(Set<String> veylfDelays) {
		this.veylfDelays = veylfDelays;
	}

	public void addVeylfDelay(String veylfDelay) {
		this.veylfDelays.add(veylfDelay);
	}

	public int getVeylfDelaysCount() {
		return veylfDelays.size();
	}

	public Map<BaseProgramType, Integer> getFundedKinderYearsCompleted() {
		return fundedKinderYearsCompleted;
	}

	public void setFundedKinderYearsCompleted(Map<BaseProgramType, Integer> fundedKinderYearsCompleted) {
		this.fundedKinderYearsCompleted = fundedKinderYearsCompleted;
	}

	public int getFundedKinderYearsCompleted(BaseProgramType programType) {
		return fundedKinderYearsCompleted.getOrDefault(programType, 0);
	}

	public void setFundedKinderYearsCompleted(BaseProgramType programType, int yearsCompleted) {
		this.fundedKinderYearsCompleted.put(programType, yearsCompleted);
	}

	public BaseProgramType getYearLevelIntent() {
		return yearLevelIntent;
	}

	public void setYearLevelIntent(BaseProgramType yearLevelIntent) {
		this.yearLevelIntent = yearLevelIntent;
	}

	/**
	 * Checks if the child is eligible for an additional year based on VEYLDF delays
	 * 
	 * @return true if eligible based on delay count, false otherwise
	 */
	public boolean hasMinimumVeylfDelaysForAdditionalYear() {
		return getVeylfDelaysCount() >= 2;
	}

	/**
	 * Checks if the child has already completed an additional year of the specified
	 * program type
	 * 
	 * @param programType The program type to check
	 * @return true if the child has already completed an additional year
	 */
	public boolean hasCompletedAdditionalYearFor(BaseProgramType programType) {
		return getFundedKinderYearsCompleted(programType) > 0;
	}

	/**
	 * Helper method to check if the child has refugee or asylum seeker status based
	 * on immigration status codes
	 * 
	 * @return true if the child has refugee or asylum status codes
	 */
	public boolean hasRefugeeOrAsylumStatus() {
		return getImmigrationStatusCodes().contains("REFUGEE") || getImmigrationStatusCodes().contains("ASYLUM_SEEKER");
	}
}
