package ie.gmit.sw;

public class KeyEnumerator {
	
	private QuadgramMap map = null;
	private float bestScore;
	private String bestKey;
	private String me;
	
	/* Loading quadgram file into map
	 * Select either one (File extension not required)
	 */
	public KeyEnumerator() throws Exception{
		//map = new QuadgramMap("WarAndPeace", 0);
		map = new QuadgramMap("Quadgrams", 1);
	}
	
	private char[] getNextKey(char[] key){
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
	
	public String crackCypher(String cypherText, int maxKeyLength){
		System.out.println("Cypher Text: " + cypherText);
		char[] key = null;
		int counter = 0;
		
		for (int j = 3; j <= maxKeyLength; j++){
			key = new char[j];
			for (int k = 0; k < key.length; k++) key[k] = 'A';
			
			while ((key = getNextKey(key)) != null){
				counter++;
				String result;
				
				try {
					String keyStr = new String(key);
					
					if(keyStr != null)
					{
						result = new Vigenere(keyStr).doCypher(cypherText, false);
						float score = map.getScore(result);
						
						if(result.equals("JOHNWALSH")){
							me = "John was there - Score: " + score;
						}
						
						if(score > bestScore){
							bestKey = keyStr;
							//System.out.println(bestKey);
						}
					}
				} catch (Exception e) {
					System.out.println("Problem Found 1!");
				}
			}
		}
		System.out.println("Enumerated " + counter + " Keys");
		String cypherResult = new Vigenere(bestKey).doCypher(cypherText, false);
		System.out.println(cypherResult);
		String result = new Vigenere(bestKey).doCypher(cypherText, false);
		float score = map.getScore(result);
		System.out.println("Best: " + bestKey + " - Score: " + score);
		System.out.println("Test: " + me);
		
		if(cypherResult == null){
			System.out.println("Cypher is Null!");
			cypherResult = "Failed!";
		}
		
		return cypherResult;
	}
	
	/*public static void main(String[] args) throws Exception {
		new KeyEnumerator().crackCypher("CWVAPTELA", 4);
	}*/
}