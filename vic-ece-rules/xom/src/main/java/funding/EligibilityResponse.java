package funding;

import java.io.Serializable;

/**
 * EligibilityResponse class - represents the output from eligibility determination rules.
 * Simple POJO design for IBM ODM as per project guidelines.
 */
public class EligibilityResponse implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private FundingDetermination fundingDetermination;
    private String primaryProgram;
    private String requestId;
    private String timestamp;
    private String processingStatus;
    private String errorMessage;
    
    /**
     * Default constructor
     */
    public EligibilityResponse() {
    }
    
    /**
     * Constructor with FundingDetermination
     */
    public EligibilityResponse(FundingDetermination fundingDetermination) {
        this.fundingDetermination = fundingDetermination;
    }
    
    // Getters and setters
    public FundingDetermination getFundingDetermination() {
        return fundingDetermination;
    }
    
    public void setFundingDetermination(FundingDetermination fundingDetermination) {
        this.fundingDetermination = fundingDetermination;
    }
    
    public String getPrimaryProgram() {
        return primaryProgram;
    }
    
    public void setPrimaryProgram(String primaryProgram) {
        this.primaryProgram = primaryProgram;
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
}
