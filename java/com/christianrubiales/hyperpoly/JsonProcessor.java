package com.christianrubiales.hyperpoly;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

/**
 * Make JSON file valid by removing trailing commas.
 */
public class JsonProcessor {
	
	static Map<String, String> headerMap = new HashMap<>();
	static {
		headerMap.put("\"general\": {", "\"version\": {");
		headerMap.put("\"grammar-invocation\": {", "\"grammar-execution\": {");
		headerMap.put("\"date-time\": {", "\"dates-time\": {");
		headerMap.put("\"regexes\": {", "\"regex\": {");
		headerMap.put("\"variables-expressions\": {", "\"var-expr\": {");
		headerMap.put("\"strings\": {", "\"str\": {");
	}

	/**
	 * @param args[0] - json file
	 * @throws FileNotFoundException 
	 */
	public static void main(String[] args) throws IOException {
		System.out.println("JsonProcessor " + args[0]);
		List<String> lines = readLines(new File(args[0]));
		
		for (int i = 1; i < lines.size() - 1; i++) {
			String prev = lines.get(i - 1);
			String curr = lines.get(i);
				
			if (headerMap.containsKey(curr)) {
				curr = headerMap.get(curr);
				lines.set(i, curr);
			}
			
			// header closing bracket encoutered, remove trailing  comma of previos line	
			if (curr.startsWith("}")) {
				if (!prev.endsWith("{")) {
					lines.set(i - 1, prev.substring(0, prev.length() - 1));
				}
			} else if (curr.startsWith("  ")) {
				if (prev.equals("{")) {
					lines.add(i, "\"version\": {");
					
					while (!curr.equals("")) {
						i++;
						prev = lines.get(i - 1);
						curr = lines.get(i);
					}
					prev = prev.substring(0, prev.length() - 1);
					curr = "},";
					lines.set(i - 1, prev);
					lines.set(i, curr);
					
					continue;
				}
				
				int index = curr.indexOf(':') + 2;
				String prefix = curr.substring(0, index);
				if (prefix.contains("<a")) {
					prefix = prefix.replaceAll("<a.*\">", "").replace("</a>", "");
				}
				String suffix = curr.substring(index);
				if (!suffix.equals("\"\"") && !suffix.equals("\"\",")) {
					try {
					suffix = "\"" + suffix.substring(1,  suffix.length() - 2)
					.replace("\\\"", "\"").replace("\\", "\\\\").replace("\"", "\\\"") + "\",";
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
				lines.set(i, prefix + suffix);
			}
		}

		try (FileWriter out = new FileWriter(new File(args[0]))) {
			for (String line : lines) {
//				System.out.println(line);
				out.write(line + "\n");
			}
		}
	}

	
	public static List<String> readLines(File file) throws FileNotFoundException {
		List<String> lines = new ArrayList<>();
		
		try (Scanner in = new Scanner(file)) {
			while (in.hasNextLine()) {
				lines.add(in.nextLine());
			}
		}
		
		return lines;
	}
}
