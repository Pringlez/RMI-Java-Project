package ie.gmit.sw;

public class Vigenere {
	// Storing the cypher as char array
	private char[] key;
	
	// 26x26 array of characters
	private char[][] tabulaRecta = { 
		{'A','B','C','D','E','F','G','H','I','J','K','L','M','N','O','P','Q','R','S','T','U','V','W','X','Y','Z'},
		{'B','C','D','E','F','G','H','I','J','K','L','M','N','O','P','Q','R','S','T','U','V','W','X','Y','Z','A'},
		{'C','D','E','F','G','H','I','J','K','L','M','N','O','P','Q','R','S','T','U','V','W','X','Y','Z','A','B'},
		{'D','E','F','G','H','I','J','K','L','M','N','O','P','Q','R','S','T','U','V','W','X','Y','Z','A','B','C'},
		{'E','F','G','H','I','J','K','L','M','N','O','P','Q','R','S','T','U','V','W','X','Y','Z','A','B','C','D'},
		{'F','G','H','I','J','K','L','M','N','O','P','Q','R','S','T','U','V','W','X','Y','Z','A','B','C','D','E'},
		{'G','H','I','J','K','L','M','N','O','P','Q','R','S','T','U','V','W','X','Y','Z','A','B','C','D','E','F'},
		{'H','I','J','K','L','M','N','O','P','Q','R','S','T','U','V','W','X','Y','Z','A','B','C','D','E','F','G'},
		{'I','J','K','L','M','N','O','P','Q','R','S','T','U','V','W','X','Y','Z','A','B','C','D','E','F','G','H'},
		{'J','K','L','M','N','O','P','Q','R','S','T','U','V','W','X','Y','Z','A','B','C','D','E','F','G','H','I'},
		{'K','L','M','N','O','P','Q','R','S','T','U','V','W','X','Y','Z','A','B','C','D','E','F','G','H','I','J'},
		{'L','M','N','O','P','Q','R','S','T','U','V','W','X','Y','Z','A','B','C','D','E','F','G','H','I','J','K'},
		{'M','N','O','P','Q','R','S','T','U','V','W','X','Y','Z','A','B','C','D','E','F','G','H','I','J','K','L'},
		{'N','O','P','Q','R','S','T','U','V','W','X','Y','Z','A','B','C','D','E','F','G','H','I','J','K','L','M'},
		{'O','P','Q','R','S','T','U','V','W','X','Y','Z','A','B','C','D','E','F','G','H','I','J','K','L','M','N'},
		{'P','Q','R','S','T','U','V','W','X','Y','Z','A','B','C','D','E','F','G','H','I','J','K','L','M','N','O'},
		{'Q','R','S','T','U','V','W','X','Y','Z','A','B','C','D','E','F','G','H','I','J','K','L','M','N','O','P'},
		{'R','S','T','U','V','W','X','Y','Z','A','B','C','D','E','F','G','H','I','J','K','L','M','N','O','P','Q'},
		{'S','T','U','V','W','X','Y','Z','A','B','C','D','E','F','G','H','I','J','K','L','M','N','O','P','Q','R'},
		{'T','U','V','W','X','Y','Z','A','B','C','D','E','F','G','H','I','J','K','L','M','N','O','P','Q','R','S'},
		{'U','V','W','X','Y','Z','A','B','C','D','E','F','G','H','I','J','K','L','M','N','O','P','Q','R','S','T'},
		{'V','W','X','Y','Z','A','B','C','D','E','F','G','H','I','J','K','L','M','N','O','P','Q','R','S','T','U'},
		{'W','X','Y','Z','A','B','C','D','E','F','G','H','I','J','K','L','M','N','O','P','Q','R','S','T','U','V'},
		{'X','Y','Z','A','B','C','D','E','F','G','H','I','J','K','L','M','N','O','P','Q','R','S','T','U','V','W'},
		{'Y','Z','A','B','C','D','E','F','G','H','I','J','K','L','M','N','O','P','Q','R','S','T','U','V','W','X'},
		{'Z','A','B','C','D','E','F','G','H','I','J','K','L','M','N','O','P','Q','R','S','T','U','V','W','X','Y'}
	};
	
	public Vigenere(){
	}
	
	public Vigenere(String key){
		setKey(key);
	}
	
	/* Striping the white space from the string and converting it uppercase & to char array, 
	 * then assigning it the instance variable
	 */
	public void setKey(String s){
		try {
			if(s != null){
				this.key = s.trim().toUpperCase().toCharArray();
			}
			else{
				this.key = "FAIL".toCharArray();
			}
		} catch (Exception e) {
			System.out.println("Problem Setting Key - Area 1");
		}  
	}
	
	/* Over-loaded setKey, passing & setting the passed char array 
	 * to the local instance of key
	 */
	public void setKey(char[] key){
		try {
			this.key = key;
		} catch (Exception e) {
			System.out.println("Problem Setting Key - Area 2");
		}  
	}
	
	/* The cypher character is produced by the passed char variables, the intersection between
	 * the two variables is returned as a char from the 26x26 array
	 */
	private char getEncryptedCharacter(char key, char plain){
		for (int rows = 0; rows < tabulaRecta.length; rows++){
			if (tabulaRecta[rows][0] == key){
				for (int cols = 0; cols < tabulaRecta[rows].length; cols++){
					if (tabulaRecta[0][cols] == plain){
						return tabulaRecta[rows][cols];
					}
				}
			}
		}
		return plain;
	}
	
	/* The plain text character is produced by the passed char variables, the first column intersection between
	 * the two variables is returned as a char from the 26x26 array
	 */
	private char getDecryptedCharacter(char key, char cypher){
		for (int cols = 0; cols < tabulaRecta[0].length; cols++){
			if (tabulaRecta[0][cols] == key){
				for (int rows = 0; rows < tabulaRecta.length; rows++){
					if (tabulaRecta[rows][cols] == cypher){
						return tabulaRecta[rows][0];
					}
				}
			}
		}
		return cypher;
	}

	public String doCypher(char[] text, boolean encrypt) {
		// Variable buffer & key index j
		StringBuffer buffer = new StringBuffer();
		int j = 0;
		
		// Looping over length of the text
		for (int i = 0; i < text.length; i++) {
			if (text[i] < 'A' || text[i] > 'Z') continue;
			
			// Resetting j variable if equals key length
			if (j == key.length) j = 0;
			
			// If true then encrypt
			if(encrypt){
				buffer.append(getEncryptedCharacter(key[j], text[i]));
			}else{
				buffer.append(getDecryptedCharacter(key[j], text[i]));
			}
			
			++j;
		}
		return buffer.toString();
	}
	
	public String doCypher(String s, boolean encrypt) {
		char[] text = s.trim().toUpperCase().toCharArray();
		return doCypher(text, encrypt);
	}
	
	/*public static void main(String[] args) {
		Vigenere v = new Vigenere("ABAB");
		String cypherTxt = v.doCypher("JOHNWALSH", true);
		System.out.println(cypherTxt);
		
		String plainTxt = v.doCypher(cypherTxt, false);
		System.out.println(plainTxt);
	}*/
}