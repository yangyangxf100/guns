package com.yy.plugin.file;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.StringWriter;
import java.io.Writer;


public class FileWriter {

	protected Writer writer;
	protected String charSet = Config.charSet;

	public FileWriter(Writer writer) {
		this.writer = writer;
	}

	public FileWriter() {
		writer = new StringWriter();
	}

	public FileWriter(String path, String file) {
		try {
			File f = new File(path);
			f.mkdirs();
			// java.io.FileWriter fi = new java.io.FileWriter(path + file);
			OutputStreamWriter out = new OutputStreamWriter(
					new FileOutputStream(path + file), charSet);
			writer = out;
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void closeWriter() {
		try {
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public int length() {
		return writer.toString().length();
	}

	public String toString() {
		return writer.toString();
	}

	protected void write(String text) {
		try {
			writer.write(text);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	protected void newLine() {
		write("\n");
	}

	protected void newLine(int i) {
		for (int j = 0; j < i; j++) {
			newLine();
		}
	}

	protected void close() {
		write(">");
	}

	protected void xclose() {
		write(" />");
	}
}
