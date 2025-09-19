package funding.example;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

// JAXB annotations for XML binding

// ODM annotations
import ilog.rules.bom.annotations.*;

/**
 * Example CalculationRequest class with proper ODM annotations.
 * This shows how to integrate both Serializable and JAXB annotations for ODM 9.x.
 */
public class AnnotatedCalculationRequest implements Serializable {
    private static final long serialVersionUID = 1L;
    
    // Core request fields with XML annotations
    private int fundingYear;
    
    private String eligibilityDetermination;
    
    private String childId;
    
    private String serviceId;
    
    private Map<String, Double> rateOverrides = new HashMap<>();
    
    /**
     * Default constructor required by JAXB
     */
    public AnnotatedCalculationRequest() {
    }
    
    /**
     * Constructor with mandatory fields.
     * Using @CustomProperty to mark this as the default constructor for DVS.
     * Using @BusinessName to provide business-friendly parameter names.
     * 
     * @param fundingYear The funding year
     * @param eligibilityDetermination The eligibility determination ID
     * @param childId The child ID
     * @param serviceId The service ID
     */
    @CustomProperty(name = "dataio.default", value = "true")
    public AnnotatedCalculationRequest(
            @BusinessName("fundingYear") int fundingYear, 
            @BusinessName("eligibilityDetermination") String eligibilityDetermination,
            @BusinessName("childId") String childId, 
            @BusinessName("serviceId") String serviceId) {
        this.fundingYear = fundingYear;
        this.eligibilityDetermination = eligibilityDetermination;
        this.childId = childId;
        this.serviceId = serviceId;
    }
    
    // Getters and setters
    public int getFundingYear() {
        return fundingYear;
    }
    
    public void setFundingYear(int fundingYear) {
        this.fundingYear = fundingYear;
    }
    
    public String getEligibilityDetermination() {
        return eligibilityDetermination;
    }
    
    public void setEligibilityDetermination(String eligibilityDetermination) {
        this.eligibilityDetermination = eligibilityDetermination;
    }
    
    public String getChildId() {
        return childId;
    }
    
    public void setChildId(String childId) {
        this.childId = childId;
    }
    
    public String getServiceId() {
        return serviceId;
    }
    
    public void setServiceId(String serviceId) {
        this.serviceId = serviceId;
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
     * Get the rate for a component, using override if available.
     * 
     * @param componentCode The component code
     * @param defaultRate The default rate to use if no override exists
     * @return The applicable rate
     */
    public double getApplicableRate(String componentCode, double defaultRate) {
        return rateOverrides.getOrDefault(componentCode, defaultRate);
    }
}
