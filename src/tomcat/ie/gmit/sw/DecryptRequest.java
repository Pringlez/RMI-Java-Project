package ie.gmit.sw;

public class DecryptRequest {

	private long jobNumber;
	private String cypherText;
	private int maxKeyLength;
	
	public DecryptRequest(long jobNumber, String cypherText, int maxKeyLength){
		setJobNumber(jobNumber);
		setCypherText(cypherText);
		setMaxKeyLength(maxKeyLength);
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
}
