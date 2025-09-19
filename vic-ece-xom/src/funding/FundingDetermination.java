package funding;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

// ODM annotations
import ilog.rules.bom.annotations.*;

/**
 * FundingDetermination class - represents the outcome of funding eligibility
 * and calculation rules. Simple POJO design for IBM ODM as per project
 * guidelines.
 */
public class FundingDetermination implements Serializable {
	private static final long serialVersionUID = 1L;

	// Core attributes
	private Child.BaseProgramType baseProgramType;

	private Set<String> supplementCodes = new HashSet<>();

	private List<EligibilityResult> eligibilityResults = new ArrayList<>();

	private List<FundingComponent> calculationBreakdown = new ArrayList<>();

	private int hoursPerWeekApproved;

	private int weeksPerYear;

	private LocalDate effectiveFrom;

	private LocalDate effectiveTo;

	private List<String> policyRefs = new ArrayList<>();

	private List<String> decisionEvidenceRefs = new ArrayList<>();

	private double totalAmount;

	/**
	 * EligibilityResult inner class to represent eligibility for each scheme
	 */
	public static class EligibilityResult implements Serializable {
		private static final long serialVersionUID = 1L;
		private String scheme;

		private EligibilityStatus status;

		private List<String> reasons = new ArrayList<>();

		private List<String> policyRefs = new ArrayList<>();

		/**
		 * Default constructor
		 */
		public EligibilityResult() {
		}

		/**
		 * Constructor with fields
		 * 
		 * @param scheme The scheme identifier
		 * @param status The eligibility status
		 */
		@CustomProperty(name = "dataio.default", value = "true")
		public EligibilityResult(@BusinessName("scheme") String scheme,
				@BusinessName("status") EligibilityStatus status) {
			this.scheme = scheme;
			this.status = status;
		}

		// Getters and setters
		public String getScheme() {
			return scheme;
		}

		public void setScheme(String scheme) {
			this.scheme = scheme;
		}

		public EligibilityStatus getStatus() {
			return status;
		}

		public void setStatus(EligibilityStatus status) {
			this.status = status;
		}

		public List<String> getReasons() {
			return reasons;
		}

		public void setReasons(List<String> reasons) {
			this.reasons = reasons;
		}

		public void addReason(String reason) {
			this.reasons.add(reason);
		}

		public List<String> getPolicyRefs() {
			return policyRefs;
		}

		public void setPolicyRefs(List<String> policyRefs) {
			this.policyRefs = policyRefs;
		}

		public void addPolicyRef(String policyRef) {
			this.policyRefs.add(policyRef);
		}
	}

	/**
	 * FundingComponent inner class to represent a single component in funding
	 * calculation
	 */
	public static class FundingComponent implements Serializable {
		private static final long serialVersionUID = 1L;
		private String componentCode;
		private ComponentBasis basis;
		private double quantity;
		private double rate;
		private double amount;

		/**
		 * Component basis enumeration
		 */
		public enum ComponentBasis {
			PER_CHILD, PER_HOUR, PER_SERVICE, ONE_OFF
		}

		/**
		 * Default constructor
		 */
		public FundingComponent() {
		}

		/**
		 * Constructor with fields
		 * 
		 * @param componentCode The component code
		 * @param basis         The component basis type
		 * @param quantity      The quantity value
		 * @param rate          The rate value
		 */
		@CustomProperty(name = "dataio.default", value = "true")
		public FundingComponent(@BusinessName("componentCode") String componentCode,
				@BusinessName("basis") ComponentBasis basis, @BusinessName("quantity") double quantity,
				@BusinessName("rate") double rate) {
			this.componentCode = componentCode;
			this.basis = basis;
			this.quantity = quantity;
			this.rate = rate;
			this.amount = quantity * rate;
		}

		// Getters and setters
		public String getComponentCode() {
			return componentCode;
		}

		public void setComponentCode(String componentCode) {
			this.componentCode = componentCode;
		}

		public ComponentBasis getBasis() {
			return basis;
		}

		public void setBasis(ComponentBasis basis) {
			this.basis = basis;
		}

		public double getQuantity() {
			return quantity;
		}

		public void setQuantity(double quantity) {
			this.quantity = quantity;
			// Recalculate amount when quantity changes
			this.amount = this.quantity * this.rate;
		}

		public double getRate() {
			return rate;
		}

		public void setRate(double rate) {
			this.rate = rate;
			// Recalculate amount when rate changes
			this.amount = this.quantity * this.rate;
		}

		public double getAmount() {
			return amount;
		}

		public void setAmount(double amount) {
			this.amount = amount;
		}
	}

	/**
	 * Eligibility status enumeration
	 */
	public enum EligibilityStatus {
		ELIGIBLE, INELIGIBLE, CONDITIONAL
	}

	/**
	 * Default constructor
	 */
	public FundingDetermination() {
	}

	/**
	 * Constructor with base program
	 * 
	 * @param baseProgramType The base program type
	 */
	@CustomProperty(name = "dataio.default", value = "true")
	public FundingDetermination(@BusinessName("baseProgramType") Child.BaseProgramType baseProgramType) {
		this.baseProgramType = baseProgramType;
	}

	// Getters and setters
	public Child.BaseProgramType getBaseProgramType() {
		return baseProgramType;
	}

	public void setBaseProgramType(Child.BaseProgramType baseProgramType) {
		this.baseProgramType = baseProgramType;
	}

	public Set<String> getSupplementCodes() {
		return supplementCodes;
	}

	public void setSupplementCodes(Set<String> supplementCodes) {
		this.supplementCodes = supplementCodes;
	}

	public void addSupplementCode(String supplementCode) {
		this.supplementCodes.add(supplementCode);
	}

	public boolean hasSupplementCode(String supplementCode) {
		return this.supplementCodes.contains(supplementCode);
	}

	public List<EligibilityResult> getEligibilityResults() {
		return eligibilityResults;
	}

	public void setEligibilityResults(List<EligibilityResult> eligibilityResults) {
		this.eligibilityResults = eligibilityResults;
	}

	public void addEligibilityResult(EligibilityResult eligibilityResult) {
		this.eligibilityResults.add(eligibilityResult);
	}

	public List<FundingComponent> getCalculationBreakdown() {
		return calculationBreakdown;
	}

	public void setCalculationBreakdown(List<FundingComponent> calculationBreakdown) {
		this.calculationBreakdown = calculationBreakdown;
		recalculateTotalAmount();
	}

	public void addFundingComponent(FundingComponent component) {
		this.calculationBreakdown.add(component);
		this.totalAmount += component.getAmount();
	}

	private void recalculateTotalAmount() {
		this.totalAmount = 0;
		for (FundingComponent component : calculationBreakdown) {
			this.totalAmount += component.getAmount();
		}
	}

	public int getHoursPerWeekApproved() {
		return hoursPerWeekApproved;
	}

	public void setHoursPerWeekApproved(int hoursPerWeekApproved) {
		this.hoursPerWeekApproved = hoursPerWeekApproved;
	}

	public int getWeeksPerYear() {
		return weeksPerYear;
	}

	public void setWeeksPerYear(int weeksPerYear) {
		this.weeksPerYear = weeksPerYear;
	}

	public LocalDate getEffectiveFrom() {
		return effectiveFrom;
	}

	public void setEffectiveFrom(LocalDate effectiveFrom) {
		this.effectiveFrom = effectiveFrom;
	}

	public LocalDate getEffectiveTo() {
		return effectiveTo;
	}

	public void setEffectiveTo(LocalDate effectiveTo) {
		this.effectiveTo = effectiveTo;
	}

	public List<String> getPolicyRefs() {
		return policyRefs;
	}

	public void setPolicyRefs(List<String> policyRefs) {
		this.policyRefs = policyRefs;
	}

	public void addPolicyRef(String policyRef) {
		this.policyRefs.add(policyRef);
	}

	public List<String> getDecisionEvidenceRefs() {
		return decisionEvidenceRefs;
	}

	public void setDecisionEvidenceRefs(List<String> decisionEvidenceRefs) {
		this.decisionEvidenceRefs = decisionEvidenceRefs;
	}

	public void addDecisionEvidenceRef(String evidenceRef) {
		this.decisionEvidenceRefs.add(evidenceRef);
	}

	public double getTotalAmount() {
		return totalAmount;
	}

	public void setTotalAmount(double totalAmount) {
		this.totalAmount = totalAmount;
	}

	/**
	 * Get the eligibility status for a specific scheme
	 * 
	 * @param scheme The scheme code to check
	 * @return The eligibility status, or null if not found
	 */
	public EligibilityStatus getEligibilityStatusForScheme(String scheme) {
		for (EligibilityResult result : eligibilityResults) {
			if (result.getScheme().equals(scheme)) {
				return result.getStatus();
			}
		}
		return null;
	}

	/**
	 * Check if the child is eligible for a specific scheme
	 * 
	 * @param scheme The scheme code to check
	 * @return true if eligible, false otherwise
	 */
	public boolean isEligibleForScheme(String scheme) {
		EligibilityStatus status = getEligibilityStatusForScheme(scheme);
		return status == EligibilityStatus.ELIGIBLE;
	}
}
