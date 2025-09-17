package funding;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * Service class - represents a service site in the Victorian ECE funding model.
 * Simple POJO design for IBM ODM as per project guidelines.
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
    
    /**
     * Delivery setting options for a service
     */
    public enum DeliverySetting {
        SESSIONAL_STANDALONE,
        LONG_DAY_CARE_INTEGRATED,
        SCHOOL_BASED_KINDER
    }
    
    /**
     * Default constructor
     */
    public Service() {
    }
    
    /**
     * Constructor with mandatory fields
     */
    public Service(String serviceId, String providerId) {
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
    
    public void setDeliversVicGovFundedKindergartenProgramInYear(boolean deliversVicGovFundedKindergartenProgramInYear) {
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
     * @param cohortKey The cohort key to look up
     * @param defaultHours Default hours to return if cohort not found
     * @return Hours per week for the cohort, or defaultHours if not found
     */
    public int getPrePrepHoursForCohort(String cohortKey, int defaultHours) {
        return prePrepHoursByCohort.getOrDefault(cohortKey, defaultHours);
    }
    
    /**
     * Resolve Pre-Prep hours using the cohort resolution order from the rules brief
     * 1) If specific cohort exists, use it
     * 2) Else use UNIVERSAL if defined
     * 3) If neither found, throw exception (no silent default)
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
            throw new IllegalStateException("No Pre-Prep hours configuration found for cohort " + 
                                           cohortKey + " and no UNIVERSAL fallback defined");
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
}
