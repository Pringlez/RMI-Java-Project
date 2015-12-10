package ie.gmit.sw;

public class DecryptRequest {
	
	private long jobNumber;
	private String cypherText;
	private int maxKeyLength;
	private long timeToComplete;
	private boolean isComplete;

	public DecryptRequest(){
	}
	
	public DecryptRequest(long jobNumber, String cypherText, int maxKeyLength){
		setJobNumber(jobNumber);
		setCypherText(cypherText);
		setMaxKeyLength(maxKeyLength);
		setTimeToComplete(0);
		setComplete(false);
	}

	public long getJobNumber() {
		return jobNumber;
	}

	public void setJobNumber(long jobNumber) {
		this.jobNumber = jobNumber;
	}

	public String getCypherText() {
		return cypherText;
	}

	public void setCypherText(String cypherText) {
		this.cypherText = cypherText;
	}

	public int getMaxKeyLength() {
		return maxKeyLength;
	}

	public void setMaxKeyLength(int maxKeyLength) {
		this.maxKeyLength = maxKeyLength;
	}

	public long getTimeToComplete() {
		return timeToComplete;
	}

	public void setTimeToComplete(long timeToComplete) {
		this.timeToComplete = timeToComplete;
	}

	public boolean isComplete() {
		return isComplete;
	}

	public void setComplete(boolean isComplete) {
		this.isComplete = isComplete;
	}
}