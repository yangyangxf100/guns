package com.yy.plugin.pdm.xml;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.Node;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;

public class XmlUtil {
	private Document doc;

	public Document read(String filename) {
		SAXReader reader = new SAXReader();
		try {
			this.doc = reader.read(filename);
		} catch (DocumentException e) {
			e.printStackTrace();
		}

		return this.doc;
	}

	public List selectNodes(String path) {
		return this.doc.selectNodes(path);
	}

	public Node selectNode(String path) {
		return this.doc.selectSingleNode(path);
	}

	public Element selectElement(String path) {
		return (Element) selectNode(path);
	}

	public Attribute selectAttribute(String path) {
		return (Attribute) selectNode(path);
	}

	public Document create() {
		this.doc = DocumentHelper.createDocument();
		return this.doc;
	}

	public void write() {
		write(null, null);
	}

	public void write(String fileName, String encoding) {
		XMLWriter writer = null;

		OutputFormat format = OutputFormat.createPrettyPrint();

		if (encoding == null) {
			encoding = "UTF-8";
		}
		format.setEncoding(encoding);

		if (this.doc != null) {
			try {
				if (fileName != null) {
					writer = new XMLWriter(new FileWriter(new File(fileName)),
							format);
				} else {
					writer = new XMLWriter(format);
				}
				writer.write(this.doc);
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				try {
					writer.close();
				} catch (IOException e) {
					e.printStackTrace();
				}

			}
		}

		System.out.println("Please read the xml document before write.");
	}
}