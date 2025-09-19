package funding;

import java.io.Serializable;

// ODM annotations
import ilog.rules.bom.annotations.*;

/**
 * CalculationResponse class - represents the output from funding calculation
 * rules. Simple POJO design for IBM ODM as per project guidelines.
 */

public class CalculationResponse implements Serializable {
	private static final long serialVersionUID = 1L;

	private FundingDetermination fundingCalculation;

	private String requestId;

	private String timestamp;

	private String processingStatus;

	private String errorMessage;

	/**
	 * Default constructor
	 */
	public CalculationResponse() {
	}

	/**
	 * Constructor with FundingDetermination
	 * 
	 * @param fundingCalculation The funding calculation result
	 */
	@CustomProperty(name = "dataio.default", value = "true")
	public CalculationResponse(@BusinessName("fundingCalculation") FundingDetermination fundingCalculation) {
		this.fundingCalculation = fundingCalculation;
	}

	// Getters and setters
	public FundingDetermination getFundingCalculation() {
		return fundingCalculation;
	}

	public void setFundingCalculation(FundingDetermination fundingCalculation) {
		this.fundingCalculation = fundingCalculation;
	}

	public String getRequestId() {
		return requestId;
	}

	public void setRequestId(String requestId) {
		this.requestId = requestId;
	}

	public String getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}

	public String getProcessingStatus() {
		return processingStatus;
	}

	public void setProcessingStatus(String processingStatus) {
		this.processingStatus = processingStatus;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}

	/**
	 * Check if the response indicates successful processing
	 * 
	 * @return true if processing was successful
	 */
	public boolean isSuccess() {
		return "SUCCESS".equals(processingStatus);
	}

	/**
	 * Check if the response indicates an error
	 * 
	 * @return true if there was an error
	 */
	public boolean isError() {
		return "ERROR".equals(processingStatus);
	}

	/**
	 * Get the total funding amount calculated
	 * 
	 * @return The total funding amount
	 */
	public double getTotalFundingAmount() {
		return fundingCalculation != null ? fundingCalculation.getTotalAmount() : 0.0;
	}
}
