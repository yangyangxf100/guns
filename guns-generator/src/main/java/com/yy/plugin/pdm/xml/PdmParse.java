package com.yy.plugin.pdm.xml;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import com.yy.plugin.pdm.bean.Column;
import com.yy.plugin.pdm.bean.Key;
import com.yy.plugin.pdm.bean.Table;

@SuppressWarnings("rawtypes")
public class PdmParse {

	public PdmParse() {
	}

	@SuppressWarnings("unchecked")
	public Map parse(String file) throws DocumentException {
		Map tableMap = new HashMap();
		SAXReader reader = new SAXReader();
		Document xml = reader.read(new File(file));
		List tables = xml
				.selectNodes("/Model/o:RootObject/c:Children/o:Model/c:Tables/o:Table");
		if (tables != null) {
			Table table;
			for (Iterator it = tables.iterator(); it.hasNext(); tableMap.put(
					table.getId(), table)) {
				table = parseTable((Element) it.next());
			}

		}
		List references = xml
				.selectNodes("/Model/o:RootObject/c:Children/o:Model/c:References/o:Reference");
		if (references != null){
			parseReferences(references, tableMap);
		}
		return tableMap;
	}

	private Table parseTable(Element e) {
		Table table = new Table();
		Element ename = (Element) e.selectSingleNode("a:Name");
		Element ecode = (Element) e.selectSingleNode("a:Code");
		Element ecomment = (Element) e.selectSingleNode("a:Comment");
		table.setId(e.attributeValue("Id"));
		table.setCode(ecode.getTextTrim());
		if (ecomment != null) {
			table.setComment(ecomment.getTextTrim());
		} else if (ename != null) {
			table.setComment(ename.getTextTrim());
		}
		
		table.setName(ename.getTextTrim());
		List cols = e.selectNodes("c:Columns/o:Column");
		List columnsList = new ArrayList();
		Column column;
		System.out.println("Table:=" + table.getId() + "\t" + table.getCode()
				+ "\t" + table.getComment()+"\n");
		for (Iterator it = cols.iterator(); it.hasNext(); table
				.setColumn(column)) {
			column = parseColumn((Element) it.next());
			column.setTable(table);
			columnsList.add(column);
		}

		table.setColumnsList(columnsList);
		Key key = parseKey(e);
		table.setPk(key);

		return table;
	}

	private Column parseColumn(Element e) {
		Column col = new Column();
		col.setId(e.attributeValue("Id"));
		Element ename = (Element) e.selectSingleNode("a:Name");
		Element ecode = (Element) e.selectSingleNode("a:Code");
		Element ecomment = (Element) e.selectSingleNode("a:Comment");
		Element edatetype = (Element) e.selectSingleNode("a:DataType");
		Element length = (Element) e.selectSingleNode("a:Length");
		Element precision = (Element) e.selectSingleNode("a:Precision");
		Element isCanNull = (Element) e.selectSingleNode("a:DefaultValue");
		
		col.setCode(ecode.getTextTrim());
		if(isCanNull!=null){
			col.setIsCanNull(isCanNull.getTextTrim());
		}else{
			col.setIsCanNull("");
		}
		col.setName(ename.getText());
		System.out.println("code is："+ename.getText()+"\t"+ecode.getTextTrim());
		if (ecomment != null) {
			col.setComment(ecomment.getTextTrim());
		} else if (ename != null) {
			col.setComment(ename.getTextTrim());
		}
		if (length != null) {
			col.setLength(length.getTextTrim());
		}
		if (precision != null) {
			col.setPrecision(Integer.valueOf(precision.getTextTrim())
					.intValue());
		}
		String datatype = edatetype.getTextTrim();
		col.setDatatypeSource(datatype);
		if (datatype.toUpperCase().indexOf("VARCHAR") != -1) {
			col.setDatatype("java.lang.String");
		} else if (datatype.toUpperCase().indexOf("NUMBER") != -1) {
			if (datatype.toUpperCase().indexOf(",") != -1) {
				col.setDatatype("java.lang.Double");
			} else {
				col.setDatatype("java.lang.Long");
			}
		} else if (datatype.toUpperCase().indexOf("DATE") != -1) {
			col.setDatatype("java.util.Date");
		} else if (datatype.toUpperCase().indexOf("BIGINT") != -1) {
			col.setDatatype("Long");
		} else if (datatype.toUpperCase().indexOf("DECIMAL") != -1) {
			col.setDatatype("java.math.BigDecimal");
		} else if (datatype.toUpperCase().indexOf("CLOB") != -1) {
			col.setDatatype("org.springframework.orm.hibernate3.support.ClobStringType");
		} else if (datatype.toUpperCase().indexOf("BLOB") != -1) {
			col.setDatatype("org.springframework.orm.hibernate3.support.BlobByteArrayType");
		} else if (datatype.toUpperCase().indexOf("INT") != -1) {
			col.setDatatype("Integer");
		}else if (datatype.toUpperCase().indexOf("SMALLINT") != -1) {
			col.setDatatype("Integer");
		} else if (datatype.toUpperCase().indexOf("CHAR") != -1) {
			col.setDatatype("java.lang.String");
		} else if (datatype.toUpperCase().indexOf("DOUBLE") != -1) {
			col.setDatatype("double");
		} else if (datatype.toUpperCase().indexOf("LONG") != -1) {
			col.setDatatype("Long");
		} else {
			col.setDatatype("java.lang.String");
		}
		System.out.println(col.getId() + "\t" + col.getCode() + "\t"
				+ col.getComment() + "\t" + col.getDatatype() + "\t"
				+ col.getLength() + "\t" + col.getPrecision());
		return col;
	}

	private Key parseKey(Element table) {
		Element key = (Element) table
				.selectSingleNode("c:Keys/o:Key/c:Key.Columns/o:Column");
		Element pk = (Element) table.selectSingleNode("c:PrimaryKey/o:Key");
		if (key == null || pk == null) {
			return null;
		} else {
			Key k = new Key();
			k.setKeyColumnId(key.attributeValue("Ref"));
			k.setKeyid(pk.attributeValue("Ref"));
			return k;
		}
	}

	private void parseReferences(List references, Map tables) {
		for (Iterator it = references.iterator(); it.hasNext();)
			try {
				parseReference((Element) it.next(), tables);
			} catch (Exception e) {
				e.printStackTrace();
			}
	}

	private void parseReference(Element ref, Map tables) {
		Element etabId1 = (Element) ref.selectSingleNode("c:Object1/o:Table");
		Element etabId2 = (Element) ref.selectSingleNode("c:Object2/o:Table");
		Element eparentKey = (Element) ref
				.selectSingleNode("c:ParentKey/o:Key");
		Element eparentJoin = (Element) ref
				.selectSingleNode("c:Joins/o:ReferenceJoin/c:Object1/o:Column");
		Element echildJoin = (Element) ref
				.selectSingleNode("c:Joins/o:ReferenceJoin/c:Object2/o:Column");
		System.out.println(ref.asXML());
		System.out.println((etabId1 == null) + "_\t_" + (etabId2 == null)
				+ "_\t_" + (eparentJoin == null) + "_\t_"
				+ (echildJoin == null) + "_\t_" + (eparentKey == null));
		if (etabId1 == null || etabId2 == null || eparentJoin == null
				|| echildJoin == null || eparentKey == null)
			return;
		String tabId1 = etabId1.attributeValue("Ref");
		String tabId2 = etabId2.attributeValue("Ref");
		String parentKey = eparentKey.attributeValue("Ref");
		String parentJoin = eparentJoin.attributeValue("Ref");
		String childJoin = echildJoin.attributeValue("Ref");
		Table parent = (Table) tables.get(tabId1);
		Table child = (Table) tables.get(tabId2);
		if (child.getPk().getKeyid().equals(parentKey)) {
			Table temp = parent;
			parent = child;
			child = temp;
		}
		if (child.getColumn(parentJoin) != null) {
			String temp = parentJoin;
			parentJoin = childJoin;
			childJoin = temp;
		}
		parent.setChild(childJoin, child);
		child.setParent(childJoin, parent);
		System.out.println(child.getId() + "\t" + child.getCode() + "\t"
				+ child.getComment() + "=======>>>>" + parent.getId() + "\t"
				+ parent.getCode() + "\t" + parent.getComment());
	}
}
