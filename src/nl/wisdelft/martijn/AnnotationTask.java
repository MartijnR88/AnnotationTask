package nl.wisdelft.martijn;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class AnnotationTask {
	private static Document document;
	private static XPath xPath = XPathFactory.newInstance().newXPath();

	public static void main(String[] args) {
		try {
			ArrayList<String> files = new ArrayList<String>();
			files.add("D:/Dropbox/Public/Master Thesis/Results/Annotation Task/response1.json");
			files.add("D:/Dropbox/Public/Master Thesis/Results/Annotation Task/response2.json");
			files.add("D:/Dropbox/Public/Master Thesis/Results/Annotation Task/response3.json");

			List<List<String>> result = new ArrayList<List<String>>();
			// Add headers
			List<String> headers = new ArrayList<String>();
			headers.add("VideoId");
			headers.add("AnnotatorId");
			headers.add("timeSpent");
			headers.add("Types");
			headers.add("Other types");
			headers.add("Categories");
			headers.add("Other categories");
			result.add(headers);

			for (String file : files) {
				FileReader fr = new FileReader(new File(file));
				BufferedReader br = new BufferedReader(fr);
				String data = "";
				data = br.readLine();
				JSONArray myJSONArray = new JSONArray(data);				

				for (int i = 0; i < myJSONArray.length(); i++) {
					System.out.println(myJSONArray.length());
					List<String> row = new ArrayList<String>();
					String videoId = new JSONObject(new JSONObject(myJSONArray.get(i).toString()).get("data").toString()).get("videoId").toString();
					String[] split = videoId.split(":");
					videoId = split[2];
					row.add(videoId);
					row.add(new JSONObject(new JSONObject(new JSONArray(
							new JSONObject(myJSONArray.get(i).toString()).get(
									"response").toString()).get(0).toString())
							.toString()).get("id").toString());
					row.add(new JSONObject(myJSONArray.get(i).toString()).get(
							"timeTaken").toString());
					row.add(new JSONObject(new JSONObject(new JSONArray(
							new JSONObject(myJSONArray.get(i).toString()).get(
									"response").toString()).get(0).toString())
							.toString()).get("type").toString());
					row.add(new JSONObject(new JSONObject(new JSONArray(
							new JSONObject(myJSONArray.get(i).toString()).get(
									"response").toString()).get(0).toString())
							.toString()).get("otherType").toString());
					row.add(new JSONObject(new JSONObject(new JSONArray(
							new JSONObject(myJSONArray.get(i).toString()).get(
									"response").toString()).get(0).toString())
							.toString()).get("category").toString());
					row.add(new JSONObject(new JSONObject(new JSONArray(
							new JSONObject(myJSONArray.get(i).toString()).get(
									"response").toString()).get(0).toString())
							.toString()).get("otherCategory").toString());
					result.add(row);
				}
			}
			// Write the result to CSV
			writeToCSV(result, "annotations.csv");

		} catch (JSONException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/** Write a matrix to CSV */
	private static void writeToCSV(List<List<String>> results, String filename)
			throws IOException {
		boolean created = false;
		File file = new File(filename);

		// Create file if it doesn't exist
		if (!file.exists()) {
			file.createNewFile();
			created = true;
		}

		FileWriter writer = new FileWriter(file.getName(), true);
		if (results.size() == 0) {
			System.out.println("No results Found.");
		} else {
			if (created) {
				for (String header : results.get(0)) {
					writer.append(header.replaceAll(",", ";") + ",");
				}
				writer.append('\n');
			}

			// Append all results to the writer
			List<List<String>> rows = results;
			for (int i = 1; i < rows.size(); i++) {
				List<String> row = rows.get(i);
				for (String column : row) {
					writer.append(column.replaceAll(",", ";") + ",");
				}
				writer.append('\n');
			}
		}

		writer.flush();
		writer.close();
	}
}
