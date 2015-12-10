package ie.gmit.sw;

public class KeyEnumerator implements Runnable {
	
	private QuadgramMap map = null;
	private float bestScore;
	private String bestKey;
	private String cypherText;
	private int maxKeyLength;
	private String cypherResult;
	private float cypherScore;
	private int keysEnumerated;
	
	private boolean debugging = false;
	
	/* Loading quadgram file into map
	 * Select either one (File extension not required)
	 */
	public KeyEnumerator() throws Exception {
		// Passing 0 as the integer will parse a normal text file
		// Passing 1 will parse a specific 2 column text file like 'Quadgrams.txt' 
		map = new QuadgramMap("WarAndPeace", 0);
		//map.parse("Quadgrams", 1);
	}
	
	public KeyEnumerator(String cypherText, int maxKeyLength) {
		setCypherText(cypherText);
		setMaxKeyLength(maxKeyLength);
	}

	private char[] getNextKey(char[] key) {
		for (int i = key.length - 1; i >=0; i--){
			if (key[i] =='Z'){
				if (i == 0) return null;
				key[i] = 'A';
			}
			else{
				key[i]++;
				break;
			}
		}
		return key;
	}
	
	@Override
	public void run() {
		setCypherResult(crackCypher(getCypherText(), getMaxKeyLength()));
	}
	
	public String crackCypher(String cypherText, int maxKeyLength) {
		
		System.out.println("Received Decypher Request!");
		System.out.println("Cypher Text: " + cypherText);
		System.out.println("Max Key Length: " + maxKeyLength);
		
		char[] key = null;
		int keyCounter = 0;
		String deCypherResult, keyStr;
		float cypherScore;
		
		for (int j = 3; j <= maxKeyLength; j++){
			key = new char[j];
			for (int k = 0; k < key.length; k++) key[k] = 'A';
			
			while ((key = getNextKey(key)) != null){
				keyCounter++;
				
				try {
					keyStr = new String(key);
					
					if(keyStr != null)
					{
						deCypherResult = new Vigenere(keyStr).doCypher(cypherText, false);
						cypherScore = map.getScore(deCypherResult);
						
						if(cypherScore > getBestScore()){
							setBestScore(cypherScore);
							setBestKey(keyStr);
							if(debugging)
								System.out.println("New Best Key: " + getBestKey());
						}
					}
				} 
				catch (Exception error) {
					System.out.println("Problem found with method 'crackCypher'!");
					System.out.println("Error: " + error);
				}
			}
		}
		
		setKeysEnumerated(keyCounter);
		setCypherResult(new Vigenere(getBestKey()).doCypher(cypherText, false));
		setCypherScore(map.getScore(getCypherResult()));
		
		if(debugging){
			System.out.println("Enumerated " + getKeysEnumerated() + " Keys");
			System.out.println(getCypherResult());
		}
		
		System.out.println("Best Key: " + getBestKey() + " - Score: " + getCypherScore());
		
		// Resetting for next job
		setBestScore(0.0f);
		
		if(getCypherResult() == null){
			setCypherResult("Decryption Failed!");
		}
		
		return getCypherResult();
	}
	
	public float getBestScore() {
		return bestScore;
	}

	public void setBestScore(float bestScore) {
		this.bestScore = bestScore;
	}

	public String getBestKey() {
		return bestKey;
	}

	public void setBestKey(String bestKey) {
		this.bestKey = bestKey;
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

	public String getCypherResult() {
		return cypherResult;
	}

	public void setCypherResult(String cypherResult) {
		this.cypherResult = cypherResult;
	}

	public float getCypherScore() {
		return cypherScore;
	}

	public void setCypherScore(float cypherScore) {
		this.cypherScore = cypherScore;
	}
	
	public int getKeysEnumerated() {
		return keysEnumerated;
	}

	public void setKeysEnumerated(int keysEnumerated) {
		this.keysEnumerated = keysEnumerated;
	}
	
	/*public static void main(String[] args) throws Exception {
		new KeyEnumerator().crackCypher("CWVAPTELA", 4);
	}*/
}