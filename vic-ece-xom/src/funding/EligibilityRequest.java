package funding;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

// ODM annotations
import ilog.rules.bom.annotations.*;

/**
 * EligibilityRequest class - represents the input for eligibility determination
 * rules. Simple POJO design for IBM ODM as per project guidelines.
 */
public class EligibilityRequest implements Serializable {
	private static final long serialVersionUID = 1L;

	// Core request fields
	private int fundingYear;

	private int programYear;

	// Reference timestamp to be used for all time-based calculations
	// Using java.time.LocalDate to match domain model date type
	private java.time.LocalDate referenceDate;

	private Child child;

	private Family family;

	private List<Guardian> guardians = new ArrayList<>();

	private ServiceProvider serviceProvider;

	private Service service;

	private EnrolmentApplication application;

	private List<String> supportingEvidenceRefs = new ArrayList<>();

	/**
	 * Inner class representing an enrollment application
	 */
	public static class EnrolmentApplication implements Serializable {
		private static final long serialVersionUID = 1L;

		private String applicationId;

		private Child.BaseProgramType requestedBaseProgramType;

		private int requestedWeeklyHours;

		private List<String> allocationPriorityCodes = new ArrayList<>();
		
		private String priorityGroup;

		/**
		 * Default constructor
		 */
		public EnrolmentApplication() {
		}

		/**
		 * Constructor with fields
		 * 
		 * @param applicationId            The application ID
		 * @param requestedBaseProgramType The requested base program type
		 * @param requestedWeeklyHours     The requested weekly hours
		 */
		@CustomProperty(name = "dataio.default", value = "true")
		public EnrolmentApplication(@BusinessName("applicationId") String applicationId,
				@BusinessName("requestedBaseProgramType") Child.BaseProgramType requestedBaseProgramType,
				@BusinessName("requestedWeeklyHours") int requestedWeeklyHours) {
			this.applicationId = applicationId;
			this.requestedBaseProgramType = requestedBaseProgramType;
			this.requestedWeeklyHours = requestedWeeklyHours;
		}

		// Getters and setters
		public String getApplicationId() {
			return applicationId;
		}

		public void setApplicationId(String applicationId) {
			this.applicationId = applicationId;
		}

		public Child.BaseProgramType getRequestedBaseProgramType() {
			return requestedBaseProgramType;
		}

		public void setRequestedBaseProgramType(Child.BaseProgramType requestedBaseProgramType) {
			this.requestedBaseProgramType = requestedBaseProgramType;
		}

		public int getRequestedWeeklyHours() {
			return requestedWeeklyHours;
		}

		public void setRequestedWeeklyHours(int requestedWeeklyHours) {
			this.requestedWeeklyHours = requestedWeeklyHours;
		}

		public List<String> getAllocationPriorityCodes() {
			return allocationPriorityCodes;
		}

		public void setAllocationPriorityCodes(List<String> allocationPriorityCodes) {
			this.allocationPriorityCodes = allocationPriorityCodes;
		}

		public void addAllocationPriorityCode(String code) {
			this.allocationPriorityCodes.add(code);
		}
		
		/**
		 * Get the priority group
		 * @return The priority group
		 */
		public String getPriorityGroup() {
			return priorityGroup;
		}

		/**
		 * Set the priority group
		 * @param priorityGroup The priority group
		 */
		public void setPriorityGroup(String priorityGroup) {
			this.priorityGroup = priorityGroup;
		}
	}

	/**
	 * Default constructor
	 */
	public EligibilityRequest() {
	}

	/**
	 * Constructor with mandatory fields
	 * 
	 * @param fundingYear   The funding year
	 * @param programYear   The program year
	 * @param referenceDate The reference date for time-based calculations
	 * @param child         The child information
	 * @param service       The service information
	 */
	@CustomProperty(name = "dataio.default", value = "true")
	public EligibilityRequest(@BusinessName("fundingYear") int fundingYear,
			@BusinessName("programYear") int programYear,
			@BusinessName("referenceDate") java.time.LocalDate referenceDate, @BusinessName("child") Child child,
			@BusinessName("service") Service service) {
		this.fundingYear = fundingYear;
		this.programYear = programYear;
		this.referenceDate = referenceDate;
		this.child = child;
		this.service = service;
	}

	// Getters and setters
	public int getFundingYear() {
		return fundingYear;
	}

	public void setFundingYear(int fundingYear) {
		this.fundingYear = fundingYear;
	}

	public int getProgramYear() {
		return programYear;
	}

	public void setProgramYear(int programYear) {
		this.programYear = programYear;
	}

	public java.time.LocalDate getReferenceDate() {
		return referenceDate;
	}

	public void setReferenceDate(java.time.LocalDate referenceDate) {
		this.referenceDate = referenceDate;
	}

	public Child getChild() {
		return child;
	}

	public void setChild(Child child) {
		this.child = child;
	}

	public Family getFamily() {
		return family;
	}

	public void setFamily(Family family) {
		this.family = family;
	}

	public List<Guardian> getGuardians() {
		return guardians;
	}

	public void setGuardians(List<Guardian> guardians) {
		this.guardians = guardians;
	}

	public void addGuardian(Guardian guardian) {
		this.guardians.add(guardian);
	}

	public ServiceProvider getServiceProvider() {
		return serviceProvider;
	}

	public void setServiceProvider(ServiceProvider serviceProvider) {
		this.serviceProvider = serviceProvider;
	}

	public Service getService() {
		return service;
	}

	public void setService(Service service) {
		this.service = service;
	}

	public EnrolmentApplication getApplication() {
		return application;
	}

	public void setApplication(EnrolmentApplication application) {
		this.application = application;
	}

	public List<String> getSupportingEvidenceRefs() {
		return supportingEvidenceRefs;
	}

	public void setSupportingEvidenceRefs(List<String> supportingEvidenceRefs) {
		this.supportingEvidenceRefs = supportingEvidenceRefs;
	}

	public void addSupportingEvidenceRef(String evidenceRef) {
		this.supportingEvidenceRefs.add(evidenceRef);
	}

	/**
	 * Check if the request has at least one guardian with a specified concession
	 * 
	 * @param concessionCode The concession code to check
	 * @return true if any guardian has the concession
	 */
	public boolean hasGuardianWithConcession(String concessionCode) {
		for (Guardian guardian : guardians) {
			if (guardian.hasConcession(concessionCode)) {
				return true;
			}
		}
		return false;
	}
}
