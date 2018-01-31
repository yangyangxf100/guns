package com.yy.plugin.file.cls;

import java.io.Writer;
import java.util.Iterator;
import java.util.List;

import com.yy.plugin.file.Config;
import com.yy.plugin.file.FileWriter;

/**
 * 
 * @author kuan
 *
 */
public class ClassFile extends FileWriter {

	public ClassFile() {
	}

	public ClassFile(Writer writer) {
		super(writer);
	}

	public ClassFile(String path, String file) {
		super(path, file);
	}

	public ClassFile pkg(String pkg) {
		write("package " + pkg + ";");
		newLine(2);
		return this;
	}

	public ClassFile imp(String className) {
		write("import " + className + ";");
		return this;
	}

	public ClassFile imps(List imps) {
		for (Iterator it = imps.iterator(); it.hasNext(); newLine())
			imp((String) it.next());

		return this;
	}

	public ClassFile cls(String className) {
		write("public class " + className);
		return this;
	}

	public ClassFile cls(String className,String annation) {
		write(annation);
		write("public class " + className);
		return this;
	}
	public ClassFile abstractCls(String className) {
		write("public abstract class " + className);
		return this;
	}
	
	public ClassFile inter(String className) {
		write("public interface " + className);
		return this;
	}

	public ClassFile ext(String className) {
		write(" extends " + className);
		return this;
	}

	public ClassFile excption(String exception) {
		write(" throws" + exception);
		return this;
	}

	public ClassFile impms(String interfaceName) {
		if (interfaceName == "" || interfaceName == null) {
			return this;
		} else {
			write(" implements " + interfaceName);
			return this;
		}
	}

	public ClassFile seck() {
		write("{");
		return this;
	}

	public ClassFile seckEnd() {
		write("}");
		return this;
	}

	public ClassFile property(String type, String name) {
		write("private " + type + " " + name + ";");
		newLine();
		newLine();
		
		return this;
	}
	
	public ClassFile propertyAnnotionIdentity(String type, String name) {
		write("@GenericGenerator(name = \"generator\", strategy = \"native\")");newLine();
		write("\t@Id");newLine();
		write("\t@GeneratedValue(generator = \"generator\")");newLine();
		write("\t@Column(name = \""+name+"\", unique = true, nullable = false, length = 32)");
		newLine();
		write("\tprivate " + type + " " + name + ";");
		 
		newLine();
		
		return this;
	}

	public ClassFile commentAnnotion(String comment) {
		write(comment);
		return this;
	}
	
	public ClassFile commentProperty(String comment) {
		if(comment!=null && comment.length()>0){
			write("/**\t" + comment+"\t**/");
		}
		return this;
	}

	public ClassFile annotion(String anno) {
		if(anno!=null){
			write(anno);
		}
		return this;
	}
	public ClassFile commentMethodBegin() {
		write("/**");
		return this;
	}
	
	public ClassFile commentMethodBegin(String comm) {
		write("/**\n\t\t\t"+comm);
		return this;
	}

	public ClassFile commentMethod(String comment) {
		write("*" + comment);
		return this;
	}

	public ClassFile wirte(String msg) {
		write(msg+"\n");
		return this;
	}
	
	public ClassFile commentMethodEnd() {
		write("*/");
		return this;
	}

	public ClassFile method(String name, String ret, String params) {
		tab().write("public " + ret + " " + name);
		if (params != null)
			write("(" + params + ")");
		else
			write("()");
		tab().seck();
		return this;
	}

	public ClassFile method(String name, String ret, String params,
			String exception) {
		tab().write("public " + ret + " " + name);
		if (params != null)
			write("(" + params + ")");
		else
			write("()");
		if (exception != null && !"".equals(exception))
			write("throws " + exception);
		tab().seck();
		return this;
	}

	public ClassFile methodEnd() {
		tab().seckEnd();
		return this;
	}

	public ClassFile getter(String fildName, String fildType, String comments) {
		tab().commentMethodBegin().newLine();
		tab().commentMethod("get" + comments).newLine();
		tab().commentMethod("").newLine();
		tab().commentMethod("@return " + comments).newLine();
		tab().commentMethodEnd().newLine();
		String name = "get" + fildName.substring(0, 1).toUpperCase()
				+ fildName.substring(1);
		method(name, fildType, null).newLine();
		methodCode("return " + fildName + ";").newLine();
		methodEnd().newLine();
		return this;
	}

	public ClassFile methodCode(String code) {
		tab(2).write(code);
		return this;
	}

	public ClassFile setter(String fildName, String fildType, String comments) {
		tab().commentMethodBegin().newLine();
		tab().commentMethod("set" + comments).newLine();
		tab().commentMethod("").newLine();
		tab().commentMethod("@param " + comments).newLine();
		tab().commentMethod("@return void ").newLine();
		tab().commentMethodEnd().newLine();
		String name = "set" + fildName.substring(0, 1).toUpperCase()
				+ fildName.substring(1);
		method(name, "void", fildType + " " + fildName).newLine();
		methodCode("this." + fildName + "=" + fildName + ";").newLine();
		methodEnd().newLine();
		return this;
	}

	public ClassFile tab() {
		write("\t");
		return this;
	}

	public ClassFile tab(int i) {
		for (int j = 0; j < i; j++)
			tab();

		return this;
	}
}
