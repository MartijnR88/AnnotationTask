package nl.wisdelft.martijn;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import org.json.JSONArray;
import org.json.JSONObject;

public class StrategyTask {
	static ArrayList<String> ground_truth;
	
	public static void main(String[] args) throws IOException {
		ground_truth = loadGroundTruth();
		System.out.println(ground_truth.size());
		
//		String filename = "domains";
//		ArrayList<String> results = loadCSV(filename + ".csv");
		
		File file = new File("ground_truth2.csv");
		FileWriter writer = new FileWriter(file, true);
		writer.append("VideoId1, VideoId2, Ground_Truth_Score");
		writer.append('\n');
		
		for (String truth : ground_truth) {
			writer.append(truth);
			writer.append('\n');
		}
		
//		for (String result : results) {
//			String[] res = result.split(",");
//			String videoId1 = res[0];
//			String domain = res[1];
//			//String score = res[2];
//			String ground_truth_score = getGroundTruthScore(videoId1, videoId2);
//			if (ground_truth.contains(videoId1)) {
//				writer.append(videoId1 + "," + videoId2 + "," + score + "," + ground_truth_score);
//				//writer.append(videoId1 + "," + domain);
//				writer.append('\n');
//			}
//		}
			
		writer.flush();
		writer.close();
	}

	private static ArrayList<String> loadGroundTruth() throws IOException {
		ArrayList<String> result = new ArrayList<String>();

		ArrayList<String> files = new ArrayList<String>();
		//files.add("D:/Dropbox/Public/Master Thesis/Results/Annotation Task/Response strategies/ground_truth1.json");
		//files.add("D:/Dropbox/Public/Master Thesis/Results/Annotation Task/Response strategies/ground_truth2.json");
		files.add("D:/Dropbox/Public/Master Thesis/Results/Annotation Task/Response strategies/strategy3_response.json");
		
		for (String file : files) {
			FileReader fr = new FileReader(new File(file));
			BufferedReader br = new BufferedReader(fr);
			String data = "";
			data = br.readLine();
			JSONArray myJSONArray = new JSONArray(data);
			for (int i = 0; i < myJSONArray.length(); i++) {
				String videoId1 = new JSONObject(new JSONObject(myJSONArray
						.get(i).toString()).get("data").toString()).get(
						"videoId1").toString();
				String[] split = videoId1.split(":");
				videoId1 = split[2];
				String videoId2 = new JSONObject(new JSONObject(myJSONArray
						.get(i).toString()).get("data").toString()).get(
						"videoId2").toString();
				String[] split2 = videoId2.split(":");
				videoId2 = split2[2];
				String response = new JSONObject(myJSONArray
						.get(i).toString()).get("response").toString().toString();
				result.add(videoId1 + "," + videoId2 + "," + response);
//				if(!result.contains(videoId1))
//						result.add(videoId1);
//				if(!result.contains(videoId2))
//					result.add(videoId2);
			}		
			br.close();
		}		
		
		return result;
	}

	private static String getGroundTruthScore(String videoId1, String videoId2) {
		Iterator<String> it = ground_truth.iterator();
		String result = "";
		while(it.hasNext()) {
			String temp = it.next();
			if (temp.contains(videoId1 + "," + videoId2)) {
				String[] ids = temp.split(",");
				result = ids[2];
			}
		}
		return result;
	}

	private static ArrayList<String> loadCSV(String filename) throws IOException {
		ArrayList<String> result = new ArrayList<String>();
		FileReader fr = new FileReader(new File(filename));
		BufferedReader br = new BufferedReader(fr);
		String line = "";
		while ((line = br.readLine()) != null) { 
			result.add(line);
		}
		
		br.close();			
		return result;
	}
}
