package com.christianrubiales.hyperpoly;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class HyperpolyglotHtmlToJson {

	public static void main(String[] args) throws IOException {
		FileWriter[] out = new FileWriter[args.length - 1];
		for (int i = 0; i < args.length - 1; i++) {
			out[i] = new FileWriter(args[i + 1]);
		}
    	try (
			Scanner in = new Scanner(new File(args[0]));
			) {

			// find the first header
			String line = "";
			while(!line.startsWith("<table")) {
				line = in.nextLine();
			}
			write(out, "{\n");
			
			boolean headerRead = false;
			
			while(!line.startsWith("</table>")) {
				line = in.nextLine();

				int tdIndex = 0;
				if (line.startsWith("<tr>")) {
					do {
						line = in.nextLine();
						
						if (line.startsWith("<th")) { // header
							if (line.contains("name=")) {
								String header = line.substring(line.indexOf("name=") + 6);
								header = header.substring(0, header.indexOf("\"") + 1);
								write(out, (headerRead ? "},\n" : "\n") + "\"" + header + ": {\n");
								headerRead = true;
							}
						} else if (line.startsWith("<td")) { // entry
							if (tdIndex == 0) {
								String entry = "";
								if (line.contains("name=")) {
									entry = line.substring(line.indexOf("name=") + 6);
									entry = entry.substring(0, entry.indexOf("\""));
								} else {
									entry = line.replace("<td>", "").replace("</td>", "").replace(" ", "-");
								}
								write(out, "  \"" + entry + "\": ");
								tdIndex++;
							} else { // contents
								String contents = line.replace("<td>", "");
								while (!line.endsWith("</td>")) {
									line = in.nextLine();
									contents += line;
								}
								write(out[tdIndex - 1], "\"" + contents.replace("</td>", "").replace("\"", "\\\"") + "\",\n");
								tdIndex++;
							}
						}
					} while (!line.startsWith("</tr>"));
				}
			}
			write(out, "}\n}");
    	}
	}

	public static void write(FileWriter[] out, String string) throws IOException {
		// System.out.print(string);
		for (FileWriter writer : out) {
			writer.write(string);
			writer.flush();
		}
	}

	public static void write(FileWriter out, String string) throws IOException {
		// System.out.print(string);
		out.write(string);
		out.flush();
	}
}
