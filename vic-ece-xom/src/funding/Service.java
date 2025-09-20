package funding;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

// ODM annotations
import ilog.rules.bom.annotations.*;

/**
 * Service class - represents a service site in the Victorian ECE funding model.
 * Simple POJO design for IBM ODM as per project guidelines.
 * Updated for ODM 9.5.x with Pre-Prep rollout and Free Kinder support.
 */
public class Service implements Serializable {
	private static final long serialVersionUID = 1L;

	// Core attributes
	private String serviceId;

	private String providerId;

	private String name;

	private String address;

	private String lgaCode;

	private DeliverySetting deliverySetting = DeliverySetting.SESSIONAL_STANDALONE;

	private boolean deliversVicGovFundedKindergartenProgramInYear;

	private Map<String, Integer> prePrepHoursByCohort = new HashMap<>();
	
	// Pre-Prep rollout fields
	private PrePrepConfig prePrepConfig;
	
	// Free Kinder fields
	private boolean participatesInFreeKinder;
	private FeeStructure feeStructure;

	/**
	 * Delivery setting options for a service
	 */
	public enum DeliverySetting {
		SESSIONAL_STANDALONE, LONG_DAY_CARE_INTEGRATED, SCHOOL_BASED_KINDER
	}
	
	/**
	 * Pre-Prep configuration including rollout details
	 */
	public static class PrePrepConfig implements Serializable {
		private static final long serialVersionUID = 1L;
		
		private int rolloutYear;
		private String rolloutGroup;
		private int initialHours;
		private int midStageHours;
		private int finalHours;
		private int midStageYear;
		private int finalStageYear;
		
		public PrePrepConfig() {}
		
		public int getRolloutYear() {
			return rolloutYear;
		}
		
		public void setRolloutYear(int rolloutYear) {
			this.rolloutYear = rolloutYear;
		}
		
		public String getRolloutGroup() {
			return rolloutGroup;
		}
		
		public void setRolloutGroup(String rolloutGroup) {
			this.rolloutGroup = rolloutGroup;
		}
		
		public int getInitialHours() {
			return initialHours;
		}
		
		public void setInitialHours(int initialHours) {
			this.initialHours = initialHours;
		}
		
		public int getMidStageHours() {
			return midStageHours;
		}
		
		public void setMidStageHours(int midStageHours) {
			this.midStageHours = midStageHours;
		}
		
		public int getFinalHours() {
			return finalHours;
		}
		
		public void setFinalHours(int finalHours) {
			this.finalHours = finalHours;
		}
		
		public int getMidStageYear() {
			return midStageYear;
		}
		
		public void setMidStageYear(int midStageYear) {
			this.midStageYear = midStageYear;
		}
		
		public int getFinalStageYear() {
			return finalStageYear;
		}
		
		public void setFinalStageYear(int finalStageYear) {
			this.finalStageYear = finalStageYear;
		}
		
		/**
		 * Get maximum available hours for a given program year based on rollout schedule
		 * @param programYear The year to check hours for
		 * @return Maximum available hours
		 */
		public int getMaxHoursForYear(int programYear) {
			if (programYear >= finalStageYear) {
				return finalHours;
			} else if (programYear >= midStageYear) {
				return midStageHours;
			} else if (programYear >= rolloutYear) {
				return initialHours;
			} else {
				return 0; // Not yet rolled out
			}
		}
	}
	
	/**
	 * Fee structure for the service
	 */
	public static class FeeStructure implements Serializable {
		private static final long serialVersionUID = 1L;
		
		private double hourlyRate;
		private double weeklyRate;
		private double annualRate;
		
		public FeeStructure() {}
		
		public double getHourlyRate() {
			return hourlyRate;
		}
		
		public void setHourlyRate(double hourlyRate) {
			this.hourlyRate = hourlyRate;
		}
		
		public double getWeeklyRate() {
			return weeklyRate;
		}
		
		public void setWeeklyRate(double weeklyRate) {
			this.weeklyRate = weeklyRate;
		}
		
		public double getAnnualRate() {
			return annualRate;
		}
		
		public void setAnnualRate(double annualRate) {
			this.annualRate = annualRate;
		}
	}

	/**
	 * Default constructor
	 */
	public Service() {
	}

	/**
	 * Constructor with mandatory fields
	 * 
	 * @param serviceId  The service identifier
	 * @param providerId The provider identifier
	 */
	@CustomProperty(name = "dataio.default", value = "true")
	public Service(@BusinessName("serviceId") String serviceId, @BusinessName("providerId") String providerId) {
		this.serviceId = serviceId;
		this.providerId = providerId;
	}

	// Getters and setters
	public String getServiceId() {
		return serviceId;
	}

	public void setServiceId(String serviceId) {
		this.serviceId = serviceId;
	}

	public String getProviderId() {
		return providerId;
	}

	public void setProviderId(String providerId) {
		this.providerId = providerId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
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

	public DeliverySetting getDeliverySetting() {
		return deliverySetting;
	}

	public void setDeliverySetting(DeliverySetting deliverySetting) {
		this.deliverySetting = deliverySetting;
	}

	public boolean isDeliversVicGovFundedKindergartenProgramInYear() {
		return deliversVicGovFundedKindergartenProgramInYear;
	}

	public void setDeliversVicGovFundedKindergartenProgramInYear(
			boolean deliversVicGovFundedKindergartenProgramInYear) {
		this.deliversVicGovFundedKindergartenProgramInYear = deliversVicGovFundedKindergartenProgramInYear;
	}

	public Map<String, Integer> getPrePrepHoursByCohort() {
		return prePrepHoursByCohort;
	}

	public void setPrePrepHoursByCohort(Map<String, Integer> prePrepHoursByCohort) {
		this.prePrepHoursByCohort = prePrepHoursByCohort;
	}

	public void addPrePrepHours(String cohortKey, int hours) {
		this.prePrepHoursByCohort.put(cohortKey, hours);
	}

	/**
	 * Get Pre-Prep hours for a specific cohort
	 * 
	 * @param cohortKey    The cohort key to look up
	 * @param defaultHours Default hours to return if cohort not found
	 * @return Hours per week for the cohort, or defaultHours if not found
	 */
	public int getPrePrepHoursForCohort(String cohortKey, int defaultHours) {
		return prePrepHoursByCohort.getOrDefault(cohortKey, defaultHours);
	}

	/**
	 * Resolve Pre-Prep hours using the cohort resolution order from the rules brief
	 * 1) If specific cohort exists, use it 2) Else use UNIVERSAL if defined 3) If
	 * neither found, throw exception (no silent default)
	 * 
	 * @param cohortKey Primary cohort key to check
	 * @return Hours per week for the resolved cohort
	 * @throws IllegalStateException if no hours configuration found
	 */
	public int resolvePrePrepHours(String cohortKey) {
		if (prePrepHoursByCohort.containsKey(cohortKey)) {
			return prePrepHoursByCohort.get(cohortKey);
		} else if (prePrepHoursByCohort.containsKey("UNIVERSAL")) {
			return prePrepHoursByCohort.get("UNIVERSAL");
		} else {
			throw new IllegalStateException("No Pre-Prep hours configuration found for cohort " + cohortKey
					+ " and no UNIVERSAL fallback defined");
		}
	}

	/**
	 * Check if this service is eligible to deliver funded kindergarten programs
	 * 
	 * @return true if service is eligible
	 */
	public boolean isEligibleToDeliverFundedKinder() {
		return deliversVicGovFundedKindergartenProgramInYear;
	}
	
	/**
	 * Get the Pre-Prep configuration
	 * @return The Pre-Prep configuration
	 */
	public PrePrepConfig getPrePrepConfig() {
		return prePrepConfig;
	}

	/**
	 * Set the Pre-Prep configuration
	 * @param prePrepConfig The Pre-Prep configuration
	 */
	public void setPrePrepConfig(PrePrepConfig prePrepConfig) {
		this.prePrepConfig = prePrepConfig;
	}

	/**
	 * Check if this service participates in Free Kinder program
	 * @return true if the service participates in Free Kinder
	 */
	public boolean isParticipatesInFreeKinder() {
		return participatesInFreeKinder;
	}

	/**
	 * Set whether this service participates in Free Kinder program
	 * @param participatesInFreeKinder true if the service participates
	 */
	public void setParticipatesInFreeKinder(boolean participatesInFreeKinder) {
		this.participatesInFreeKinder = participatesInFreeKinder;
	}

	/**
	 * Get the fee structure for this service
	 * @return The fee structure
	 */
	public FeeStructure getFeeStructure() {
		return feeStructure;
	}

	/**
	 * Set the fee structure for this service
	 * @param feeStructure The fee structure
	 */
	public void setFeeStructure(FeeStructure feeStructure) {
		this.feeStructure = feeStructure;
	}
	
	/**
	 * Check if a service is eligible for Pre-Prep in a specific year
	 * @param programYear The year to check eligibility for
	 * @return true if eligible for Pre-Prep in the specified year
	 */
	public boolean isEligibleForPrePrepInYear(int programYear) {
		return prePrepConfig != null && 
		       programYear >= prePrepConfig.getRolloutYear() && 
		       isDeliversVicGovFundedKindergartenProgramInYear();
	}
}
