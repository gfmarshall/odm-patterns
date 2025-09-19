package funding;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

// ODM annotations
import ilog.rules.bom.annotations.BusinessName;
import ilog.rules.bom.annotations.CustomProperty;

/**
 * CalculationRequest class - represents the input for funding calculation
 * rules. Simple POJO design for IBM ODM as per project guidelines.
 */
public class CalculationRequest implements Serializable {
	private static final long serialVersionUID = 1L;

	// Core request fields
	private int fundingYear;

	// Reference timestamp to be used for all time-based calculations
	// Using java.time.LocalDate to match domain model date type
	private java.time.LocalDate referenceDate;

	private FundingDetermination eligibilityDetermination;

	private Child child;

	private Service service;

	private Map<String, Double> rateOverrides = new HashMap<>();

	/**
	 * Default constructor
	 */
	public CalculationRequest() {
	}

	/**
	 * Constructor with mandatory fields
	 * 
	 * @param fundingYear              The funding year
	 * @param referenceDate            The reference date for time-based
	 *                                 calculations
	 * @param eligibilityDetermination The eligibility determination
	 * @param child                    The child information
	 * @param service                  The service information
	 */
	@CustomProperty(name = "dataio.default", value = "true")
	public CalculationRequest(@BusinessName("fundingYear") int fundingYear,
			@BusinessName("referenceDate") java.time.LocalDate referenceDate,
			@BusinessName("eligibilityDetermination") FundingDetermination eligibilityDetermination,
			@BusinessName("child") Child child, @BusinessName("service") Service service) {
		this.fundingYear = fundingYear;
		this.referenceDate = referenceDate;
		this.eligibilityDetermination = eligibilityDetermination;
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

	public java.time.LocalDate getReferenceDate() {
		return referenceDate;
	}

	public void setReferenceDate(java.time.LocalDate referenceDate) {
		this.referenceDate = referenceDate;
	}

	public FundingDetermination getEligibilityDetermination() {
		return eligibilityDetermination;
	}

	public void setEligibilityDetermination(FundingDetermination eligibilityDetermination) {
		this.eligibilityDetermination = eligibilityDetermination;
	}

	public Child getChild() {
		return child;
	}

	public void setChild(Child child) {
		this.child = child;
	}

	public Service getService() {
		return service;
	}

	public void setService(Service service) {
		this.service = service;
	}

	public Map<String, Double> getRateOverrides() {
		return rateOverrides;
	}

	public void setRateOverrides(Map<String, Double> rateOverrides) {
		this.rateOverrides = rateOverrides;
	}

	public void addRateOverride(String componentCode, double rate) {
		this.rateOverrides.put(componentCode, rate);
	}

	/**
	 * Check if there's a rate override for a specific component
	 * 
	 * @param componentCode The component code to check
	 * @return true if a rate override exists
	 */
	public boolean hasRateOverride(String componentCode) {
		return rateOverrides.containsKey(componentCode);
	}

	/**
	 * Get the rate for a component, using override if available
	 * 
	 * @param componentCode The component code
	 * @param defaultRate   The default rate to use if no override exists
	 * @return The applicable rate
	 */
	public double getApplicableRate(String componentCode, double defaultRate) {
		return rateOverrides.getOrDefault(componentCode, defaultRate);
	}
}
