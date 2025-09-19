package funding;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

// ODM annotations
import ilog.rules.bom.annotations.*;

/**
 * ServiceProvider class - represents a provider of ECE services Simple POJO
 * design for IBM ODM as per project guidelines.
 */
public class ServiceProvider implements Serializable {
	private static final long serialVersionUID = 1L;

	// Core attributes
	private String providerId;

	private String name;

	private String abn;

	private boolean isNQFApprovedProvider;

	private boolean isVicKinderFundingApprovedProvider;

	private ComplianceStatus complianceStatus = ComplianceStatus.COMPLIANT;

	private List<Service> serviceSites = new ArrayList<>();

	/**
	 * Compliance status options for a service provider
	 */
	public enum ComplianceStatus {
		COMPLIANT, UNDER_REVIEW, NON_COMPLIANT, EXEMPTION_GRANTED
	}

	/**
	 * Default constructor
	 */
	public ServiceProvider() {
	}

	/**
	 * Constructor with mandatory fields
	 * 
	 * @param providerId The provider identifier
	 * @param name       The provider name
	 */
	@CustomProperty(name = "dataio.default", value = "true")
	public ServiceProvider(@BusinessName("providerId") String providerId, @BusinessName("name") String name) {
		this.providerId = providerId;
		this.name = name;
	}

	// Getters and setters
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

	public String getAbn() {
		return abn;
	}

	public void setAbn(String abn) {
		this.abn = abn;
	}

	public boolean isNQFApprovedProvider() {
		return isNQFApprovedProvider;
	}

	public void setNQFApprovedProvider(boolean isNQFApprovedProvider) {
		this.isNQFApprovedProvider = isNQFApprovedProvider;
	}

	public boolean isVicKinderFundingApprovedProvider() {
		return isVicKinderFundingApprovedProvider;
	}

	public void setVicKinderFundingApprovedProvider(boolean isVicKinderFundingApprovedProvider) {
		this.isVicKinderFundingApprovedProvider = isVicKinderFundingApprovedProvider;
	}

	public ComplianceStatus getComplianceStatus() {
		return complianceStatus;
	}

	public void setComplianceStatus(ComplianceStatus complianceStatus) {
		this.complianceStatus = complianceStatus;
	}

	public List<Service> getServiceSites() {
		return serviceSites;
	}

	public void setServiceSites(List<Service> serviceSites) {
		this.serviceSites = serviceSites;
	}

	public void addServiceSite(Service serviceSite) {
		this.serviceSites.add(serviceSite);
	}

	/**
	 * Check if provider is eligible to deliver funded programs based on approval
	 * status and compliance
	 * 
	 * @return true if provider can deliver funded programs
	 */
	public boolean isEligibleToDeliverFundedPrograms() {
		return isVicKinderFundingApprovedProvider && (complianceStatus == ComplianceStatus.COMPLIANT
				|| complianceStatus == ComplianceStatus.EXEMPTION_GRANTED);
	}

	/**
	 * Find a service site by ID
	 * 
	 * @param serviceId The service ID to search for
	 * @return The service if found, null otherwise
	 */
	public Service findServiceById(String serviceId) {
		for (Service service : serviceSites) {
			if (service.getServiceId().equals(serviceId)) {
				return service;
			}
		}
		return null;
	}
}
