package ie.gmit.sw;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.*;

public class QuadgramMap {

	private Map<String, Integer> map = new HashMap<String, Integer>();
	
	public QuadgramMap(String fileName, int option) throws Exception {
		parse(fileName, option);
	}
	
	public float getScore(String text){
		float score = 0.00f;
		for (int i = 0; i < text.length(); i += 4) {
			if (i + 4 >= text.length()) break;
			
			String next = text.substring(i, i + 4);
			
			if (map.get(next) != null){
				float frequency = (float)map.get(next);
				float total = (float)map.size();
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
					int j;
					
					while((j = br.read()) != -1){
						char next = (char)j;
						
						if (next >= 'A' && next <= 'z'){
							sb.append(next);
						}
						
						if(sb.length() == 4){
							String qGram = sb.toString().toUpperCase();
							sb = new StringBuffer();
							int frequency = 0;
							
							if(map.containsKey(qGram)){
								frequency = map.get(qGram);
							}
							
							frequency++;
							map.put(qGram, frequency);
						}
					}
					
					br.close();
					System.out.println("Quadgram Map Loaded!");
				}catch(Exception e){
					System.out.println("Error Loading Quadgram Map!");
				}
			break;
			case 1:
				// Loading supplied quadgrams file into hashmap
				try{
					BufferedReader br2 = new BufferedReader(new InputStreamReader(new FileInputStream("./" + fileName + ".txt")));
					String line, quad;
					Integer freq;
					
					while ((line = br2.readLine()) != null) {
						quad = line.substring(0, 4);
						freq = Integer.parseInt(line.substring(5, line.length()));
						//System.out.println("Quad: " + quad + " Freq: " + freq);
						map.put(quad, freq);
					}
					
					br2.close();
					System.out.println("Quadgram Map Loaded!");
					
				}catch(Exception e){
					System.out.println("Error Loading Quadgram Map!");
				}
			break;
		}
	}
	
	/*public static void main(String[] args) throws Exception {
		new QuadgramMap("WarAndPeace");
	}*/
}