package com.christianrubiales.hyperpoly;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class SkeletonHtmlProcessor {

	/**
	 * @param args [0] - skeleton source file, [1] - skeleton destination file
	 */
	public static void main(String[] args) throws IOException {
    	try (
			Scanner in = new Scanner(new File(args[0]));
			FileWriter out = new FileWriter(args[1]);
			) {

			// find the table
			String line = "";
			while(!line.startsWith("<table")) {
				line = in.nextLine();
			}
			write(out, line);
			
			while(!line.startsWith("</table>")) {
				line = in.nextLine();
			
				String contents = "";
				if (line.startsWith("<tr>")) {
					contents += line;
					line = in.nextLine();
					
					if (line.contains(">CORE<")) {
						in.nextLine();
						contents = "";
					} else if (line.startsWith("<th colspan=\"3")) {
						contents = contents.replace("<tr>", "<tr class='header'>");
						contents += "<th class='header'>";
						contents += line.substring(line.lastIndexOf("\">") + 2).replace("</a>", "");
						contents += in.nextLine();
						write(out, contents);
					} else if (line.startsWith("<th>title </th>"))  {
						in.nextLine();
						in.nextLine();
						in.nextLine();
					} else if (line.startsWith("<td><img"))  {
						contents += line + in.nextLine();
						String name = in.nextLine().replace("<td>#", "").replace("</td>", "");
						contents = contents.replaceAll("src=\".*\" />", "src='./img/" + name + ".jpg' class='image' />");
						in.nextLine();
						
						String clazz = contents.substring(contents.indexOf("<a name=") + 9);
						clazz = clazz.substring(0, clazz.indexOf('"'));
						contents = contents.replaceFirst("<tr>", "<tr class='" + clazz + "'>");
						
						write(out, contents + in.nextLine());
					} else if (line.startsWith("<th></th>"))  {
						in.nextLine();
						in.nextLine();
						in.nextLine();
					} else {
						contents += line;
						while (!line.contains("</td>") && in.hasNextLine()) {
							line = in.nextLine();
						}
						while (!line.startsWith("</tr>") && in.hasNextLine()) {
							line = in.nextLine();
						}
						String clazz = contents.substring(contents.indexOf("<a name=") + 9);
						clazz = clazz.substring(0, clazz.indexOf('"'));
						contents = contents.replaceFirst("<tr>", "<tr class='" + clazz + "'>");
						write(out, contents + "</td></tr>");
					}
				} else if (line.startsWith("<th colspan=\"3")) {
					write(out, "<td colspan='1'>");
				} else {
					//write(out, line);
				}
			}
			write(out, "</table>");
    	}
	}
	
	public static void write(FileWriter out, String string) throws IOException {
//		System.out.println(string);
		out.write(string + "\n");
		out.flush();
	}
}