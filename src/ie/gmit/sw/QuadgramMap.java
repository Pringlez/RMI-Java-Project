package ie.gmit.sw;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

public class QuadgramMap {

	private Map<String, Integer> map = new HashMap<String, Integer>();
	
	public QuadgramMap(String fileName, int option) throws Exception {
		parse(fileName, option);
	}
	
	public float getScore(String text){
		
		float score = 0.0f, frequency, total;
		
		for (int i = 0; i < text.length(); i += 4) {
			if (i + 4 >= text.length()) break;
			
			String next = text.substring(i, i + 4);
			
			if (map.get(next) != null){
				frequency = (float)map.get(next);
				total = (float)map.size();
				score += Math.log10((frequency / total));
			}
		}
		
		return -score;
	}
	
	public void parse(String fileName, int option) throws Exception{
		
		switch(option){
			// Loading normal file as quadgrams, a large text files would be needed to build a good map
			case 0:
				try{
					BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream("./" + fileName + ".txt")));
					StringBuffer sb = new StringBuffer();
					int j, frequency = 0;
					char next;
					
					while((j = br.read()) != -1){
						next = (char)j;
						
						if (next >= 'A' && next <= 'z'){
							sb.append(next);
						}
						
						if(sb.length() == 4){
							String qGram = sb.toString().toUpperCase();
							sb = new StringBuffer();
							
							if(map.containsKey(qGram)){
								frequency = map.get(qGram);
							}
							
							frequency++;
							map.put(qGram, frequency);
						}
					}
					
					br.close();
					System.out.println("Loaded " + fileName + " Map Successfully!");	
				}
				catch (Exception error) {
					System.out.println("Loading " + fileName + " Map Failed!");
					System.out.println("Error: " + error);
				}
			break;
			case 1:
				// Loading supplied quadgrams file into hashmap
				try{
					BufferedReader br2 = new BufferedReader(new InputStreamReader(new FileInputStream("./" + fileName + ".txt")));
					String line, quadgram;
					Integer frequency;
					
					while ((line = br2.readLine()) != null) {
						quadgram = line.substring(0, 4);
						frequency = Integer.parseInt(line.substring(5, line.length()));
						map.put(quadgram, frequency);
					}
					
					br2.close();
					System.out.println("Loaded " + fileName + " Map Successfully!");	
				}
				catch (Exception error) {
					System.out.println("Loading " + fileName + " Map Failed!");
					System.out.println("Error: " + error);
				}
			break;
		}
	}
	
	/*public static void main(String[] args) throws Exception {
		new QuadgramMap("WarAndPeace");
	}*/
}