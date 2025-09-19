package funding;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

// ODM annotations
import ilog.rules.bom.annotations.*;

/**
 * Guardian class - extends Person for representing guardians in the Victorian
 * ECE funding model. Simple POJO design for IBM ODM as per project guidelines.
 */
public class Guardian extends Person implements Serializable {
	private static final long serialVersionUID = 1L;

	// Guardian-specific attributes
	private String guardianId;

	private String relationshipToChild;

	private Set<String> concessions = new HashSet<>();

	/**
	 * Default constructor
	 */
	public Guardian() {
		super();
	}

	/**
	 * Constructor with mandatory fields
	 * 
	 * @param guardianId  The guardian identifier
	 * @param personId    The person identifier
	 * @param firstName   The first name
	 * @param lastName    The last name
	 * @param dateOfBirth The date of birth
	 */
	@CustomProperty(name = "dataio.default", value = "true")
	public Guardian(@BusinessName("guardianId") String guardianId, @BusinessName("personId") String personId,
			@BusinessName("firstName") String firstName, @BusinessName("lastName") String lastName,
			@BusinessName("dateOfBirth") LocalDate dateOfBirth) {
		super(personId, firstName, lastName, dateOfBirth);
		this.guardianId = guardianId;
	}

	// Getters and setters
	public String getGuardianId() {
		return guardianId;
	}

	public void setGuardianId(String guardianId) {
		this.guardianId = guardianId;
	}

	public String getRelationshipToChild() {
		return relationshipToChild;
	}

	public void setRelationshipToChild(String relationshipToChild) {
		this.relationshipToChild = relationshipToChild;
	}

	public Set<String> getConcessions() {
		return concessions;
	}

	public void setConcessions(Set<String> concessions) {
		this.concessions = concessions;
	}

	public void addConcession(String concession) {
		this.concessions.add(concession);
	}

	public void removeConcession(String concession) {
		this.concessions.remove(concession);
	}

	/**
	 * Check if guardian holds a specific concession
	 * 
	 * @param concessionCode The concession code to check
	 * @return true if the guardian holds the specified concession
	 */
	public boolean hasConcession(String concessionCode) {
		return concessions.contains(concessionCode);
	}

	/**
	 * Check if guardian holds any concession from a set of codes
	 * 
	 * @param concessionCodes Set of concession codes to check against
	 * @return true if the guardian holds any of the specified concessions
	 */
	public boolean hasAnyConcession(Set<String> concessionCodes) {
		for (String code : concessionCodes) {
			if (concessions.contains(code)) {
				return true;
			}
		}
		return false;
	}
}
