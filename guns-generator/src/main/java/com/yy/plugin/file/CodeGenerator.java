package com.yy.plugin.file;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.yy.plugin.file.cls.ClassBean;
import com.yy.plugin.file.cls.ClassFile;
import com.yy.plugin.file.cls.FiledBean;
import com.yy.plugin.file.cls.MethodBean;
import com.yy.plugin.pdm.bean.Column;
import com.yy.plugin.pdm.bean.Table;

@SuppressWarnings("rawtypes")
public class CodeGenerator {
	public void generator(Map tables, PathConfig path) {
		initTable(tables, path);
		Iterator it = tables.keySet().iterator();
		while (it.hasNext()) {
			Table table = (Table) tables.get(it.next());

			if ((table != null) && (table.isGen())) {
				generateTable(table, path);
			}
		}
	}

	public void printUrl(Map tables, PathConfig path) {
		initTable(tables, path);
		Iterator it = tables.keySet().iterator();

		it = tables.keySet().iterator();
		while (it.hasNext()) {
			Table table = (Table) tables.get(it.next());
			if ((table != null) && (table.isGen())) {
				System.out.println(" <A HREF=\"${pageContext.request.contextPath}/admin/" + lowerFirstChar(table.getClassName()) + "/list\" target='content'>" + table.getComment() + "</A><br><br>");
			}
		}

		// 产生字段与字段描述
		it = tables.keySet().iterator();
		while (it.hasNext()) {
			Table table = (Table) tables.get(it.next());
			System.out.println("\n\n" + table.getComment());
			List<Column> list = table.getColumnsList();
			for (Column col : list) {
				System.out.println(col.getComment() + "," + col.getFildName());
			}
		}

		// 产生主键自增 sql
		it = tables.keySet().iterator();
		while (it.hasNext()) {
			Table table = (Table) tables.get(it.next());
			String id = table.getColumn(table.getPk().getKeyColumnId()).getFildName();
			System.out.println("alter table " + table.getCode() + " modify " + id + " INT not null AUTO_INCREMENT;".toLowerCase());
		}

		System.out.println("\n");
		// mapper
		it = tables.keySet().iterator();
		while (it.hasNext()) {
			Table table = (Table) tables.get(it.next());
			System.out.println("<mapper resource=\"mybatis/cus/" + table.getClassName() + "SQL.xml\"/>");
		}

		System.out.println("\ntypeAlias");
		// typeAlias
		it = tables.keySet().iterator();
		while (it.hasNext()) {
			Table table = (Table) tables.get(it.next());
			System.out.println("<typeAlias type=\"com.gochinatv.cms.po." + table.getClassName() + "\" alias=\"" + table.getClassName() + "\"/>");
		}

		System.out.println("\n sql 脚本");
		it = tables.keySet().iterator();
		while (it.hasNext()) {
			Table table = (Table) tables.get(it.next());
			System.out.println("INSERT INTO `t_sys_menu` VALUES ('cus_" + table.getClassNameField() + "', 'ms_sso', '" + table.getName().replaceAll("表", "管理") + "', 'cms/" + table.getClassNameField() + "/open', '32000000', '1',100);");
		}

		it = tables.keySet().iterator();
		while (it.hasNext()) {
			Table table = (Table) tables.get(it.next());
			System.out.println("INSERT INTO `t_sys_function` VALUES ('cus_" + table.getClassNameField() + "', 'open', '首页', '');");
			System.out.println("INSERT INTO `t_sys_function` VALUES ('cus_" + table.getClassNameField() + "', 'search', '查询', '');");
			System.out.println("INSERT INTO `t_sys_function` VALUES ('cus_" + table.getClassNameField() + "', 'saveOrUpdate', '添加或者修改', '');");
			System.out.println("INSERT INTO `t_sys_function` VALUES ('cus_" + table.getClassNameField() + "', 'deleteById', '根据Id 删除', '');");
			System.out.println("INSERT INTO `t_sys_function` VALUES ('cus_" + table.getClassNameField() + "', 'getById', '根据 ID 查询', '');");
		}

		System.out.println("\nRelatedItem");
		// cp_innerservice-provider
		it = tables.keySet().iterator();
		while (it.hasNext()) {
			Table table = (Table) tables.get(it.next());
			List<Column> list = table.getColumnsList();
			System.out.print(table.getClassName().toUpperCase() + ",");
		}

		System.out.println("/n as");
		it = tables.keySet().iterator();
		while (it.hasNext()) {
			Table table = (Table) tables.get(it.next());
			System.out.println("\n\n" + table.getComment());
			List<Column> list = table.getColumnsList();
			for (Column col : list) {
				System.out.print(col.getFildNameOriginal() + " as " + col.getFildName() + ",");
			}
		}
	}

	/**
	 * 打印 doc 说明文档
	 * 
	 * @param tables
	 * @param path
	 */
	public void printDoc(Map tables, PathConfig path) {
		initTable(tables, path);
		Iterator it = tables.keySet().iterator();
		// 产生字段与字段描述
		it = tables.keySet().iterator();
		while (it.hasNext()) {
			Table table = (Table) tables.get(it.next());
			System.out.println("\n\n" + table.getComment());
			String id = table.getColumn(table.getPk().getKeyColumnId()).getFildName();

			System.out.println("名称,代码,类型,长度,缺省值,可为空,主键,注释");
			List<Column> list = table.getColumnsList();
			for (Column col : list) {
				String type = col.getDatatypeSource();
				String length = "";
				if (type.indexOf("(") != -1 && type.indexOf("") != -1) {
					length = type.substring(type.indexOf("(") + 1, type.length() - 1);
					type = type.substring(0, type.indexOf("("));
				}
				String isCanNull = col.getIsCanNull().replaceAll(",", "，");
				String isCanNullVal = "";
				if (isCanNull.length() > 0) {
					isCanNullVal = "N";
				}

				String primary = "";
				if (id.equals(col.getFildName())) {
					primary = id;
				}

				System.out.println(col.getName().replaceAll(",", "，") + "," + col.getFildName() + "," + type + "," + length + "," + isCanNull + "," + isCanNullVal + "," + primary + "," + col.getComment().replaceAll(",", "，"));
			}
		}
	}

	public void generateTable(Table table, PathConfig path) {
		// System.out.println(table.getComment() + " example ing...");
		// example(table, path);

		System.out.println(table.getComment() + " bean ing...");
		generateBean(table, path);

		System.out.println(table.getComment() + " generateMapper ing...");
		generateMapper(table, path);

		System.out.println(table.getComment() + " generateDao ing...");
		generateDao(table, path);

		System.out.println(table.getComment() + " generateService ing...");
		generateService(table, path);

		System.out.println(table.getComment() + " generateServiceImp ing...");
		generateServiceImp(table, path);

		System.out.println(table.getComment() + " generateSqlFile ing...");
		generateSqlFile(table, path);

		System.out.println(table.getComment() + " generateAction ing...");
		generateAction(table, path);

		System.out.println(table.getComment() + " generateJsp ing...");
		generateJsp(table, path);

		System.out.println(table.getComment() + " generateAPIAction ing...");
		generateAPIAction(table, path);

		System.out.println(table.getComment() + " generateVo ing...");
		generateVo(table, path);

		System.out.println(table.getComment() + " generateVo ing...");
		generateTest(table, path);
		
		System.out.println(table.getComment() + " edit.jsp ing...");
		generateAceEditJsp(table, path);
		
		System.out.println(table.getComment() + " list.jsp ing...");
		generateAceListJsp(table, path);
		
		System.out.println(table.getComment() + " generateAceAction ing...");
		generateAceAction(table, path);
	}
	

	private void initTable(Map tables, PathConfig path) {
		Iterator it = tables.keySet().iterator();
		while (it.hasNext()) {
			Table table = (Table) tables.get(it.next());

			table.setPackageName(path.getEntityPackage());
			table.setClassName(parseTableName(table.getCode()));

			List<Column> list = table.getColumnsList();
			for (Column col : list) {
				col.setFildName(parseFieldName(col.getCode()));
				col.setFildNameOriginal(col.getCode());
			}
		}
	}

	private void gennerateClassComment(ClassFile file, String comment) {
		file.commentMethodBegin().newLine();
		file.commentMethod("<ul>").newLine();
		file.commentMethod("\t<li> <b>目的:</b> <br />").newLine();
		file.commentMethod(comment == null ? "\t\tTODO comment this class" : "\t\t" + comment).newLine();
		Date date = Calendar.getInstance().getTime();
		String d = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date);

		file.commentMethod("\t\t创建:" + d).newLine();
		file.commentMethod("\t\t作者:格调先生").newLine();
		file.commentMethod("\t</li>").newLine();
		file.commentMethod("</ul>").newLine();
		file.commentMethodEnd().newLine();
	}

	private void generateCommentMethod(ClassFile file, String params, String ret, String e, String comments) {
		file.tab().commentMethodBegin(comments).newLine();

		if ((params != null) && (!"".equals(params))) {
			String[] ps = params.split(",");
			for (int i = 0; i < ps.length; i++) {
				String[] param = ps[i].split(" ");
				if (param.length != 2)
					continue;
				file.tab().commentMethod("@param " + param[1] + "\n\t\t\t" + param[0]).newLine();
			}
		}

		file.tab().commentMethod(" @return \n\t\t\t" + ret).newLine();
		if (e != null && e.length() > 0) {
			file.tab().commentMethod(" @exception \n\t\t\t" + e).newLine();
		}
		file.tab().commentMethodEnd().newLine();
	}

	public void generateAnnotion(ClassFile file, String annotion) {
		if (annotion != null) {
			file.write("\t" + annotion);
		}
	}

	private String parseTableName(String str) {
		if ((str.startsWith("T_")) || (str.startsWith("t_"))) {
			str = str.substring(2);
		}
		StringBuffer sb = new StringBuffer();

		if ((str != null) && (str.length() > 0)) {
			String[] s = str.toLowerCase().split("_");
			for (int i = 0; i < s.length; i++) {
				sb.append(upCaseFirstChar(s[i]));
			}
		}

		return sb.toString();
	}

	private String parseFieldName(String str) {
		StringBuffer sb = new StringBuffer();

		if ((str != null) && (str.length() > 0)) {
			String[] s = str.toLowerCase().split("_");
			if (s.length > 1) {
				for (int i = 0; i < s.length; i++) {
					if ((s[i] != null) && (s[i].length() > 0)) {
						if (i != 0)
							sb.append(upCaseFirstChar(s[i]));
						else {
							sb.append(s[i]);
						}
					}
				}
			} else {
				sb.append(str);
			}
		}
		return sb.toString();
	}

	private String upCaseFirstChar(String str) {
		return str.substring(0, 1).toUpperCase() + str.substring(1);
	}

	private String lowerFirstChar(String str) {
		return str.substring(0, 1).toLowerCase() + str.substring(1);
	}

	public void generateBean(Table table, PathConfig path) {
		ClassBean cls = new ClassBean();
		cls.setPkgName(path.getBeanPackage());
		cls.setClassName(table.getBeanName());

		cls.addImps("java.io.Serializable");
		cls.addImps("com.wordnik.swagger.annotations.ApiModelProperty");

		cls.setClassImp("Serializable");

		System.out.println(table.getClassName());
		Column pkColumn = table.getColumn(table.getPk().getKeyColumnId());

		cls.addFiled(pkColumn.getFildName(), pkColumn.getCodeDatetype(), pkColumn.getComment());

		@SuppressWarnings("unchecked")
		List<Column> list = table.getColumnsList();
		for (Column col : list) {
			if (col.getId().equals(pkColumn.getId()))
				continue;
			Table parent = table.getParent(col.getId());

			if ((parent != null) && (col.isGenRefence())) {
				cls.addImps(parent.getFullName());
				cls.addFiled(parent.getFieldName(), parent.getDTOName(), parent.getFullName(), col.getCode());
			} else {
				cls.addFiled(col.getFildName(), col.getCodeDatetype(), col.getComment(), col.getCode());
			}

		}

		if (table.getChilds() != null) {
			Iterator it = table.getChilds().keySet().iterator();
			while (it.hasNext()) {
				String colId = (String) it.next();
				Table child = table.getChild(colId);

				cls.addFiled(child.getFieldName() + "List", "List", "关联的" + child.getComment());
			}
		}
		ClassFile entity = new ClassFile(path.getBeanPath(), table.getBeanName() + ".java");
		entity.pkg(cls.getPkgName()).newLine();

		entity.imps(cls.getImps()).newLine(3);
		gennerateClassComment(entity, "基础对象， 对应表:" + table.getCode() + "\t" + table.getComment());

		entity.cls(cls.getClassName()).ext("Base").impms(cls.getClassImp());

		entity.seck();
		entity.newLine();

		String id = table.getColumn(table.getPk().getKeyColumnId()).getFildName();
		Iterator proIt = cls.getFileds().iterator();
		while (proIt.hasNext()) {
			FiledBean p = (FiledBean) proIt.next();

			entity.tab().commentProperty(p.getComment()).newLine();
			entity.tab().write("@ApiModelProperty(value = \"" + p.getComment() + "-hidden\")\n");
			entity.tab().property(p.getType(), p.getName()).newLine();

		}

		entity.method(cls.getClassName(), "", "").newLine();
		entity.methodCode("").newLine();
		entity.methodEnd().newLine(3);

		entity.method(cls.getClassName(), "", pkColumn.getCodeDatetype() + " " + pkColumn.getFildName()).newLine();
		entity.methodCode("this.set" + upCaseFirstChar(pkColumn.getFildName()) + "(" + pkColumn.getFildName() + ");").newLine();
		entity.methodEnd().newLine(3);

		Iterator proItGs = cls.getFileds().iterator();
		while (proItGs.hasNext()) {
			FiledBean p = (FiledBean) proItGs.next();
			entity.getter(p.getName(), p.getType(), p.getComment()).newLine();
			entity.setter(p.getName(), p.getType(), p.getComment()).newLine();
		}

		Iterator xxx = cls.getFileds().iterator();
		StringBuffer sb = new StringBuffer("\tpublic String toString() {\n\t\treturn \"" + cls.getClassName() + " [\"+");
		while (xxx.hasNext()) {
			FiledBean p = (FiledBean) xxx.next();
			sb.append("\"" + p.getName() + "=\"+" + p.getName() + "+\",\"+");
		}
		entity.wirte(sb.toString() + "\" base=\"+super.toString()+\"]\";\n\t}");

		entity.seckEnd();
		entity.closeWriter();
	}

	public void generateVo(Table table, PathConfig path) {
		ClassBean cls = new ClassBean();
		cls.setPkgName(path.getVoPackage());
		cls.setClassName(table.getBeanName() + "Vo");

		cls.addImps("java.io.Serializable");

		cls.setClassImp("Serializable");

		Column pkColumn = table.getColumn(table.getPk().getKeyColumnId());

		cls.addFiled(pkColumn.getFildName(), pkColumn.getCodeDatetype(), pkColumn.getComment());

		@SuppressWarnings("unchecked")
		List<Column> list = table.getColumnsList();
		for (Column col : list) {
			if (col.getId().equals(pkColumn.getId()))
				continue;
			Table parent = table.getParent(col.getId());

			if ((parent != null) && (col.isGenRefence())) {
				cls.addImps(parent.getFullName());
				cls.addFiled(parent.getFieldName(), parent.getDTOName(), parent.getFullName(), col.getCode());
			} else {
				cls.addFiled(col.getFildName(), col.getCodeDatetype(), col.getComment(), col.getCode());
			}

		}

		if (table.getChilds() != null) {
			Iterator it = table.getChilds().keySet().iterator();
			while (it.hasNext()) {
				String colId = (String) it.next();
				Table child = table.getChild(colId);

				cls.addFiled(child.getFieldName() + "List", "List", "关联的" + child.getComment());
			}
		}
		ClassFile entity = new ClassFile(path.getVoPath(), table.getBeanName() + "Vo.java");
		entity.pkg(cls.getPkgName()).newLine();

		entity.imps(cls.getImps()).newLine(3);
		gennerateClassComment(entity, "基础对象， 对应表:" + table.getCode() + "\t" + table.getComment());

		entity.cls(cls.getClassName()).impms(cls.getClassImp());

		entity.seck();
		entity.newLine();

		String id = table.getColumn(table.getPk().getKeyColumnId()).getFildName();
		Iterator proIt = cls.getFileds().iterator();
		while (proIt.hasNext()) {
			FiledBean p = (FiledBean) proIt.next();

			entity.tab().commentProperty(p.getComment()).newLine();
			entity.tab().property(p.getType(), p.getName()).newLine();

		}

		entity.method(cls.getClassName(), "", "").newLine();
		entity.methodCode("").newLine();
		entity.methodEnd().newLine(3);

		entity.method(cls.getClassName(), "", pkColumn.getCodeDatetype() + " " + pkColumn.getFildName()).newLine();
		entity.methodCode("this.set" + upCaseFirstChar(pkColumn.getFildName()) + "(" + pkColumn.getFildName() + ");").newLine();
		entity.methodEnd().newLine(3);

		Iterator proItGs = cls.getFileds().iterator();
		while (proItGs.hasNext()) {
			FiledBean p = (FiledBean) proItGs.next();
			entity.getter(p.getName(), p.getType(), p.getComment()).newLine();
			entity.setter(p.getName(), p.getType(), p.getComment()).newLine();
		}

		Iterator xxx = cls.getFileds().iterator();
		StringBuffer sb = new StringBuffer("\tpublic String toString() {\n\t\treturn \"" + cls.getClassName() + " [\"+");
		while (xxx.hasNext()) {
			FiledBean p = (FiledBean) xxx.next();
			sb.append("\"" + p.getName() + "=\"+" + p.getName() + "+\",\"+");
		}
		entity.wirte(sb.toString() + "\" base=\"+super.toString()+\"]\";\n\t}");

		entity.seckEnd();
		entity.closeWriter();
	}

	public void generateMapper(Table table, PathConfig path) {
		ClassFile file = new ClassFile(path.getMapperPath(), table.getMapperName() + ".java");
		file.pkg(path.getMapperPackage());

		file.imp("java.util.List").newLine();

		file.imp(path.getBeanPackage() + "." + table.getClassName()).newLine();

		file.inter(table.getMapperName());
		file.seck();

		file.write("\t/**\n");
		file.write("\t * 添加\n");
		file.write("\t * \n");
		file.write("\t * @param bean\n");
		file.write("\t * @return\n");
		file.write("\t */\n");
		file.write("\tpublic Long insert(" + table.getClassName() + " bean);\n");
		file.newLine();

		file.write("\t/**\n");
		file.write("\t * 删除\n");
		file.write("\t * \n");
		file.write("\t * @param id\n");
		file.write("\t */\n");
		file.write("\tpublic void delete(Long id);\n");
		file.newLine();

		file.write("\t/**\n");
		file.write("\t * 修改\n");
		file.write("\t * \n");
		file.write("\t * @param bean\n");
		file.write("\t */\n");
		file.write("\tpublic void update(" + table.getClassName() + " bean);\n");
		file.newLine();

		file.write("\t/**\n");
		file.write("\t * 根据 id 查找\n");
		file.write("\t * \n");
		file.write("\t * @param id\n");
		file.write("\t * @return\n");
		file.write("\t */\n");
		file.write("\tpublic " + table.getClassName() + " getById(Long id);\n");
		file.newLine();

		file.write("\t/**\n");
		file.write("\t * 根据对象查找数据\n");
		file.write("\t * \n");
		file.write("\t * @param bean\n");
		file.write("\t * @return\n");
		file.write("\t */\n");
		file.write("\tpublic List<" + table.getClassName() + "> search4Page(" + table.getClassName() + " bean);\n");
		file.newLine();

		file.write("\t/**\n");
		file.write("\t * 根据对象查找条目\n");
		file.write("\t * \n");
		file.write("\t * @param bean\n");
		file.write("\t * @return\n");
		file.write("\t */\n");
		file.write("\tpublic Long searchCount4Page(" + table.getClassName() + " bean);\n");

		file.seckEnd();
		file.closeWriter();
	}

	public void generateDao(Table table, PathConfig path) {
		ClassFile file = new ClassFile(path.getDaoPath(), table.getDAOName() + ".java");
		file.pkg(path.getDaoPackage());

		file.imp("java.util.List").newLine();
		file.imp("javax.annotation.Resource").newLine();
		file.imp("org.springframework.stereotype.Component").newLine();

		file.imp(path.getBeanPackage() + "." + table.getClassName()).newLine();
		file.imp(path.getMapperPackage() + "." + table.getMapperName()).newLine();

		file.cls(table.getDAOName(), "\n@Component\n").impms(table.getMapperName());
		file.seck();

		file.write("\t @Resource\n");
		file.write("\t private " + table.getClassName() + "Mapper mapper;\n");

		file.write("\t @Override\n");
		file.write("\t public Long insert(" + table.getClassName() + " bean) {\n");
		file.write("\t\t return mapper.insert(bean);\n");
		file.write("\t }\n");

		file.write("\t @Override\n");
		file.write("\t public void delete(Long id) {\n");
		file.write("\t \tmapper.delete(id);\n");
		file.write("\t }\n");

		file.write("\t @Override\n");
		file.write("\t public void update(" + table.getClassName() + " bean) {\n");
		file.write("\t \tmapper.update(bean);\n");
		file.write("\t }\n");

		file.write("\t @Override\n");
		file.write("\t public " + table.getClassName() + " getById(Long id) {\n");
		file.write("\t\t return mapper.getById(id);\n");
		file.write("\t }\n");

		file.write("\t @Override\n");
		file.write("\t public List<" + table.getClassName() + "> search4Page(" + table.getClassName() + " bean) {\n");
		file.write("\t\t return mapper.search4Page(bean);\n");
		file.write("\t }\n");

		file.write("\t @Override\n");
		file.write("\t public Long searchCount4Page(" + table.getClassName() + " bean) {\n");
		file.write("\t \treturn mapper.searchCount4Page(bean);\n");
		file.write("\t }\n");

		file.seckEnd();
		file.closeWriter();
	}

	public void generateService(Table table, PathConfig path) {
		ClassFile file = new ClassFile(path.getServicePath(), table.getClassName() + "Service.java");
		file.pkg(path.getServicePackage());

		file.imp("java.util.List").newLine();
		file.imp("com.gochinatv.vo.PageInfo").newLine();
		file.imp("com.gochinatv.vo.PageResult").newLine();

		file.imp(path.getBeanPackage() + "." + table.getClassName()).newLine();

		file.inter(table.getClassName() + "Service");
		file.seck();

		file.write("\t/**\n");
		file.write("\t * 添加\n");
		file.write("\t * \n");
		file.write("\t * @param bean\n");
		file.write("\t * @return\n");
		file.write("\t */\n");
		file.write("\tpublic Long insert(" + table.getClassName() + " bean);\n");
		file.newLine();

		file.write("\t/**\n");
		file.write("\t * 删除\n");
		file.write("\t * \n");
		file.write("\t * @param id\n");
		file.write("\t */\n");
		file.write("\tpublic void delete(Long id);\n");
		file.newLine();

		file.write("\t/**\n");
		file.write("\t * 修改\n");
		file.write("\t * \n");
		file.write("\t * @param bean\n");
		file.write("\t */\n");
		file.write("\tpublic void update(" + table.getClassName() + " bean);\n");
		file.newLine();

		file.write("\t/**\n");
		file.write("\t * 根据 id 查找\n");
		file.write("\t * \n");
		file.write("\t * @param id\n");
		file.write("\t * @return\n");
		file.write("\t */\n");
		file.write("\tpublic " + table.getClassName() + " getById(Long id);\n");
		file.newLine();

		file.write("\t/**\n");
		file.write("\t * 根据对象查找数据\n");
		file.write("\t * \n");
		file.write("\t * @param bean\n");
		file.write("\t * @return\n");
		file.write("\t */\n");
		file.write("\tpublic List<" + table.getClassName() + "> search4Page(" + table.getClassName() + " bean);\n");
		file.newLine();

		file.write("\t/**\n");
		file.write("\t * 根据对象查找条目\n");
		file.write("\t * \n");
		file.write("\t * @param bean\n");
		file.write("\t * @return\n");
		file.write("\t */\n");
		file.write("\tpublic Long searchCount4Page(" + table.getClassName() + " bean);\n");

		file.write("\t/**\n");
		file.write("\t * 根据对象查找数据\n");
		file.write("\t * \n");
		file.write("\t* @param bean\n");
		file.write("\t* @param pageInfo\n");
		file.write("\t* @return\n");
		file.write("\t*/\n");
		file.write("\tpublic PageResult search4Page(boolean isToVo," + table.getClassName() + " bean,PageInfo pageInfo);\n");

		file.seckEnd();
		file.closeWriter();
	}

	public void generateServiceImp(Table table, PathConfig path) {
		ClassFile file = new ClassFile(path.getServiceImpPath(), table.getClassName() + "ServiceImpl.java");
		file.pkg(path.getServiceImpPackage());

		file.imp("java.util.List").newLine();
		file.imp("javax.annotation.Resource").newLine();
		file.imp("com.gochinatv.vo.PageInfo").newLine();
		file.imp("com.gochinatv.vo.PageResult").newLine();
		file.imp("org.springframework.stereotype.Component").newLine();

		file.imp("com.gochinatv.framework.annotation.SummerCached").newLine();
		file.imp("com.gochinatv.framework.annotation.SummerCached.OperatorType").newLine();
		file.imp("com.gochinatv.framework.annotation.SummerCached.RelatedItem").newLine();
		file.imp("com.gochinatv.util.ChangeRelection").newLine();
		file.imp("com.gochinatv.cms.vo." + table.getClassName() + "Vo").newLine();

		file.imp(path.getBeanPackage() + "." + table.getClassName()).newLine();
		file.imp(path.getDaoPackage() + "." + table.getDAOName()).newLine();
		file.imp(path.getServicePackage() + "." + table.getClassName() + "Service").newLine();

		file.cls(table.getClassName() + "ServiceImpl", "\n@Component\n").impms(table.getClassName() + "Service");
		file.seck();

		file.write("\t @Resource\n");
		file.write("\t private " + table.getClassName() + "Dao dao;\n");

		file.write("\t \n\t @SummerCached(action=OperatorType.DELETE,related=RelatedItem." + table.getClassName().toUpperCase() + ")\n");
		file.write("\t public Long insert(" + table.getClassName() + " bean) {\n");
		file.write("\t\t return dao.insert(bean);\n");
		file.write("\t }\n");

		file.write("\t \n\t @SummerCached(action=OperatorType.DELETE,related=RelatedItem." + table.getClassName().toUpperCase() + ")\n");
		file.write("\t public void delete(Long id) {\n");
		file.write("\t \tdao.delete(id);\n");
		file.write("\t }\n");

		file.write("\t \n\t @SummerCached(action=OperatorType.DELETE,related=RelatedItem." + table.getClassName().toUpperCase() + ")\n");
		file.write("\t public void update(" + table.getClassName() + " bean) {\n");
		file.write("\t \tdao.update(bean);\n");
		file.write("\t }\n");

		file.write("\t @Override\n");
		file.write("\t public " + table.getClassName() + " getById(Long id) {\n");
		file.write("\t\t return dao.getById(id);\n");
		file.write("\t }\n");

		file.write("\t \n\t @SummerCached(livingTime=300,keyPrefix=\"" + table.getClassName().toUpperCase() + "\" ,action=OperatorType.SELECT,related=RelatedItem." + table.getClassName().toUpperCase() + ")\n");
		file.write("\t public List<" + table.getClassName() + "> search4Page(" + table.getClassName() + " bean) {\n");
		file.write("\t\t return dao.search4Page(bean);\n");
		file.write("\t }\n");

		file.write("\t public Long searchCount4Page(" + table.getClassName() + " bean) {\n");
		file.write("\t \treturn dao.searchCount4Page(bean);\n");
		file.write("\t }\n");

		file.write("\t \n\t @SummerCached(livingTime=300,keyPrefix=\"" + table.getClassName().toUpperCase() + "\" ,action=OperatorType.SELECT,related=RelatedItem." + table.getClassName().toUpperCase() + ")\n");
		file.write("\tpublic PageResult search4Page(boolean isToVo," + table.getClassName() + " bean, PageInfo pageInfo) {\n");
		file.write("\t	\tPageResult pageResult = new PageResult();\n");
		file.write("\t	\tif(bean.getStart()==-1){//如果  bean。getStart == -1 ，说明是 easyUi 的页面，是分页的；否则是 start ，fetchSize 模式\n");
		file.write("\t	\t\tbean.setStart(pageInfo.getOffset());\n");
		file.write("\t	\t\tbean.setFetchsize(pageInfo.getRows());\n");
		file.write("\t	\t}\n");

		file.write("\t	\tList poRows = dao.search4Page(bean);\n");

		file.write("\t	\tLong total = dao.searchCount4Page(bean);\n");

		file.write("\t	\tif(isToVo){\n");
		file.write("\t	\t\tList rows = ChangeRelection.changeProValueList(" + table.getClassName() + ".class, " + table.getClassName() + "Vo.class, poRows);\n");
		file.write("\t	\t\tpageResult.setRows(rows);\n");
		file.write("\t	\t}else{\n");
		file.write("\t	\t\tpageResult.setRows(poRows);\n");
		file.write("\t	\t}\n");

		file.write("\t\t\tpageResult.setTotal(total);\n");
		file.write("\t\t\treturn pageResult;\n");
		file.write("\t}\n");

		file.seckEnd();
		file.closeWriter();
	}

	public void generateSqlFile(Table table, PathConfig path) {
		if (!table.isGen()) {
			return;
		}

		List<Column> list = table.getColumnsList();

		StringBuffer insertSql = new StringBuffer("<!-- 添加 -->\n<insert id=\"insert\" useGeneratedKeys=\"true\" keyProperty=\"id\">\n\t<![CDATA[\n \t\t\tinsert into `" + table.getCode() + "`  (");
		StringBuffer insertColumn = new StringBuffer("");
		StringBuffer insertValues = new StringBuffer("");

		StringBuffer search4Page = new StringBuffer("<!-- 得到分页数据 -->\n<select id=\"search4Page\" resultType=\"" + table.getClassName() + "\" parameterType=\"" + table.getClassName() + "\"> \n\tselect * from " + table.getCode() + " where 1=1\n");
		StringBuffer searchCount4Page = new StringBuffer("<!-- 得到条目 -->\n<select id=\"searchCount4Page\" resultType=\"java.lang.Long\"> \n\tselect count(1) FROM  " + table.getCode() + "  WHERE 1=1 \n");
		StringBuffer update = new StringBuffer("<!-- 根据 id 更新 -->\n<update id=\"update\" parameterType=\"" + table.getClassName() + "\">UPDATE  " + table.getCode() + " SET \n\tid=#{id}\n");

		String delete = "<!-- 主键删除 -->\n<delete id=\"delete\">\n\tdelete from " + table.getCode() + "   where id=#{id}\n</delete>\n";

		String getById = "<!-- 主键查询 -->\n <select id=\"getById\" resultType=\"" + table.getClassName() + "\">\n \tselect * from " + table.getCode() + "   where\n \tid=#{id}\n </select>\n";

		String getBySql = "<!-- 根据条件查询 -->\n <select id=\"getBySql\" resultType=\"" + table.getClassName() + "\">\n \tselect * from " + table.getCode() + "   ${where}\n </select>\n";

		for (int i = 0; i < list.size(); i++) {
			Column col = list.get(i);
			/*if (i < list.size() - 1) {
				insertColumn.append("`" + col.getFildNameOriginal() + "`,");
				insertValues.append(" #{" + col.getFildName() + "},");
			} else {
				insertColumn.append("`" + col.getFildNameOriginal() + "`");
				insertValues.append(" #{" + col.getFildName() + "}");
			}*/

			search4Page.append("\t<if test=\"" + col.getFildName() + " != null and " + col.getFildName() + " != ''  and isLike==0  \">\n");
			search4Page.append("\t\tand " + col.getFildNameOriginal() + " = #{" + col.getFildName() + "}\n");
			search4Page.append("\t</if>\n");

			searchCount4Page.append("\t<if test=\"" + col.getFildName() + " != null and " + col.getFildName() + " != ''  and isLike==0  \">\n");
			searchCount4Page.append("\t\tand " + col.getFildNameOriginal() + " = #{" + col.getFildName() + "}\n");
			searchCount4Page.append("\t</if>\n");

			update.append("\t<if test=\"" + col.getFildName() + " != null and " + col.getFildName() + " != ''\">\n");
			update.append("\t\t, `" + col.getFildNameOriginal() + "` = #{" + col.getFildName() + "}\n");
			update.append("\t</if>\n");
		}

		search4Page.append("\n\n\t<!-- like 查询 -->\n");
		searchCount4Page.append("\n\n\t<!-- like 查询-->\n");
		// ----
		for (int i = 0; i < list.size(); i++) {
			Column col = list.get(i);
			if (i < list.size() - 1) {
				insertColumn.append("`" + col.getFildNameOriginal() + "`,");
				insertValues.append(" #{" + col.getFildName() + "},");
			} else {
				insertColumn.append("`" + col.getFildNameOriginal() + "`");
				insertValues.append(" #{" + col.getFildName() + "}");
			}

			// and txtLoginName like CONCAT('%',#{txtLoginName},'%')
			search4Page.append("\t<if test=\"" + col.getFildName() + " != null and " + col.getFildName() + " != ''  and isLike==1  \">\n");
			if(col.getFildName().equals("id")){//and createTime = #{createTime}
				search4Page.append("\t\tand " + col.getFildNameOriginal() + " = #{" + col.getFildName() + "}\n");
			}else{
				search4Page.append("\t\tand " + col.getFildNameOriginal() + " like CONCAT('%',#{" + col.getFildName() + "},'%')\n");
			}
			search4Page.append("\t</if>\n");

			searchCount4Page.append("\t<if test=\"" + col.getFildName() + " != null and " + col.getFildName() + " != ''  and isLike==1  \">\n");
			if(col.getFildName().equals("id")){//and createTime = #{createTime}
				searchCount4Page.append("\t\tand " + col.getFildNameOriginal() + " = #{" + col.getFildName() + "}\n");
			}else{
				searchCount4Page.append("\t\tand " + col.getFildNameOriginal() + " like CONCAT('%',#{" + col.getFildName() + "},'%')\n");
			}
			searchCount4Page.append("\t</if>\n");
		}
		// --

		insertSql.append(insertColumn + ") values (" + insertValues + ") \n\t]]> \n</insert>");

		search4Page.append("\tlimit #{start},#{fetchsize}\n </select>\n");
		searchCount4Page.append("</select>\n");
		update.append("\tWHERE id = #{id}\n</update>\n");

		String ret = "<?xml version=\"1.0\" encoding=\"UTF-8\" ?> \n<!DOCTYPE mapper PUBLIC \"-//mybatis.org//DTD Mapper 3.0//EN\" \"http://mybatis.org/dtd/mybatis-3-mapper.dtd\" >\n<mapper namespace=\"com.gochinatv.mapper." + table.getClassName()
				+ "Mapper\">\n";
		ret += insertSql + "\n";
		ret += delete + "\n";
		ret += getById + "\n";
		ret += getBySql + "\n";
		ret += search4Page + "\n";
		ret += searchCount4Page + "\n";
		ret += update + "\n";
		ret += "\n</mapper>";

		System.out.println(ret);

		FileWriter fw = new FileWriter(path.getSqlPath(), table.getClassName() + "SQL.xml");
		fw.write(ret);
		fw.closeWriter();
	}

	public void generateAction(Table table, PathConfig path) {
		ClassFile file = new ClassFile(path.getActionPath(), table.getClassName() + "Controller.java");

		file.write("package com.gochinatv.cms.controller;\n");
		file.write("\n");
		file.write("import java.util.Date;\n");
		file.write("\n");
		file.write("import javax.servlet.http.HttpServletRequest;\n");
		file.write("import org.apache.commons.lang.StringUtils;\n");

		file.write("\n");
		file.write("import org.springframework.beans.factory.annotation.Autowired;\n");
		file.write("import org.springframework.stereotype.Controller;\n");
		file.write("import org.springframework.web.bind.annotation.PathVariable;\n");
		file.write("import org.springframework.web.bind.annotation.RequestMapping;\n");
		file.write("import org.springframework.web.bind.annotation.RequestMethod;\n");
		file.write("import org.springframework.web.bind.annotation.ResponseBody;\n");
		file.write("import org.springframework.web.servlet.ModelAndView;\n");
		file.write("\n");
		file.write("import com.alibaba.fastjson.JSON;\n");
		file.write("import com.alibaba.fastjson.serializer.SerializerFeature;\n");
		file.write("import com.gochinatv.cms.po." + table.getClassName() + ";\n");
		file.write("import com.gochinatv.cms.service." + table.getClassName() + "Service;\n");
		file.write("import com.gochinatv.exception.BusinessException;\n");
		file.write("import com.gochinatv.framework.controller.BaseController;\n");
		file.write("import com.gochinatv.framework.annotation.UserRightAnnotation;\n");
		file.write("import com.gochinatv.util.FastJsonUtil;\n");
		file.write("import com.gochinatv.util.InputCheckUtil;\n");
		file.write("import com.gochinatv.vo.PageInfo;\n");
		file.write("import com.gochinatv.vo.PageResult;\n");
		file.write("import com.gochinatv.util.ChangeRelection;\n");
		file.write("import com.gochinatv.framework.po.UserActionLog;\n");
		file.write("import com.gochinatv.framework.service.UserActionLogService;\n");

		file.write("\n");
		file.write("/**\n");
		file.write(" * @author 格调先生\n");
		file.write(" */\n");
		file.write("@Controller\n");
		file.write("@RequestMapping(value = \"/cms/" + table.getClassNameField() + "/*\")\n");
		file.write("public class " + table.getClassName() + "Controller extends BaseController {\n");
		file.write("	@Autowired\n");
		file.write("	private " + table.getClassName() + "Service " + table.getClassNameField() + "Service;\n");

		file.write("	@Autowired\n");
		file.write("	private UserActionLogService userActionLogService;\n");

		file.write("\n");
		file.write("	/**\n");
		file.write("	 * 打开首页\n");
		file.write("	 * \n");
		file.write("	 * @return\n");
		file.write("	 */\n");
		file.write("	@UserRightAnnotation(menuId=\"cus_" + table.getClassNameField() + "\",functionId=\"open\")\n");
		file.write("	@RequestMapping(value = \"/open\", method = RequestMethod.GET)\n");
		file.write("	public ModelAndView open() {\n");
		file.write("		ModelAndView modelView = new ModelAndView(\"cus/" + table.getClassNameField() + "Mng\");\n");
		file.write("		return modelView;\n");
		file.write("	}\n");
		file.write("\n");
		file.write("	/**\n");
		file.write("	 * 搜索\n");
		file.write("	 * \n");
		file.write("	 * @param bean\n");
		file.write("	 * @param pageInfo\n");
		file.write("	 * @return\n");
		file.write("	 * @throws BusinessException\n");
		file.write("	 */\n");
		file.write("	@UserRightAnnotation(menuId=\"cus_" + table.getClassNameField() + "\",functionId=\"search\")\n");
		file.write("	@RequestMapping(value = \"/search\", method = RequestMethod.GET)\n");
		file.write("	@ResponseBody\n");
		file.write("	public String search(" + table.getClassName() + " bean, PageInfo pageInfo) throws BusinessException {\n");

		file.write(" 		bean.setIsLike(\"1\");\n");
		file.write(" 		PageResult pageResult = " + table.getClassNameField() + "Service.search4Page(false,bean, pageInfo);\n");
		file.write("		return FastJsonUtil.obj2json(pageResult);\n");
		file.write("	}\n");
		file.write("\n");
		file.write("	/**\n");
		file.write("	 * 添加\n");
		file.write("	 * \n");
		file.write("	 * @param bean\n");
		file.write("	 * @param request\n");
		file.write("	 * @throws BusinessException\n");
		file.write("	 */\n");
		file.write("	@UserRightAnnotation(menuId=\"cus_" + table.getClassNameField() + "\",functionId=\"saveOrUpdate\")\n");
		file.write("	@RequestMapping(value = \"/saveOrUpdate\", method = RequestMethod.POST)\n");
		file.write("	@ResponseBody\n");
		file.write("	public void saveOrUpdate(" + table.getClassName() + " bean, HttpServletRequest request) throws BusinessException {\n");
		file.write("		//String userId = super.getLoginUserId(request);\n");
		file.write("		//bean.setLastModifyUserId(userId);\n");
		file.write("		if (bean.getId()==null || StringUtils.isBlank(bean.getId()+\"\")  || bean.getId()==0) {\n");
		file.write("				String userId = super.getLoginUserId(request);\n");
		file.write("				String userIp = super.getClientIp(request);\n");
		file.write("				UserActionLog userActionLog = new UserActionLog(userId, userIp, \"60010301\",FastJsonUtil.obj2jsonDateFormat(bean));\n");
		file.write("			    userActionLogService.insert(userActionLog);\n");

		file.write("			\t" + table.getClassNameField() + "Service.insert(bean);\n");
		file.write("		} else {\n");

		file.write("		\ttry {\n");
		file.write("		\t\t" + table.getClassName() + " oldDto = " + table.getClassNameField() + "Service.getById(Long.valueOf(bean.getId()));\n");

		file.write("				String userId = super.getLoginUserId(request);\n");
		file.write("				String userIp = super.getClientIp(request);\n");
		file.write("				" + table.getClassName() + " oldData = " + table.getClassNameField() + "Service.getById(Long.valueOf(bean.getId()));\n");
		file.write("				UserActionLog userActionLog = new UserActionLog(userId, userIp, \"60010302\",FastJsonUtil.obj2jsonDateFormat(oldData));\n");
		file.write("			    userActionLogService.insert(userActionLog);\n");

		file.write("			\tChangeRelection.changeObj(oldDto, bean);\n");
		file.write("		\t} catch (Exception e) {\n");
		file.write("			\te.printStackTrace();\n");
		file.write("		\t}\n");

		file.write("			" + table.getClassNameField() + "Service.update(bean);\n");
		file.write("		}\n");
		file.write("	}\n");
		file.write("\n");
		file.write("	/**\n");
		file.write("	 * 根据 id 删除\n");
		file.write("	 * \n");
		file.write("	 * @param id\n");
		file.write("	 * @param request\n");
		file.write("	 * @throws BusinessException\n");
		file.write("	 */\n");
		file.write("	@UserRightAnnotation(menuId=\"cus_" + table.getClassNameField() + "\",functionId=\"deleteById\")\n");
		file.write("	@RequestMapping(value = \"/delete\", method = RequestMethod.GET)\n");
		file.write("	@ResponseBody\n");
		file.write("	public void deleteById( HttpServletRequest request,String ids) throws BusinessException {\n");
		file.write("		InputCheckUtil.checkNotEmpty(String.valueOf(ids), \"ids\");\n");
		file.write("		String userId = super.getLoginUserId(request);\n");
		file.write("		String userIp = super.getClientIp(request);\n");
		file.write("		for(String id:ids.split(\",\")){\n");
		file.write("		\t" + table.getClassName() + " oldData = " + table.getClassNameField() + "Service.getById(Long.valueOf(id));\n");
		file.write("		\tUserActionLog userActionLog = new UserActionLog(userId, userIp, \"60010303\",FastJsonUtil.obj2jsonDateFormat(oldData));\n");
		file.write("		\tuserActionLogService.insert(userActionLog);\n");
		file.write("		\t" + table.getClassNameField() + "Service.delete(Long.valueOf(id));\n");
		file.write("        }\n");
		file.write("	}\n");
		file.write("\n");
		file.write("	/**\n");
		file.write("	 * 获取信息，用于前台回显\n");
		file.write("	 * \n");
		file.write("	 * @param id\n");
		file.write("	 * @return\n");
		file.write("	 */\n");
		file.write("	@UserRightAnnotation(menuId=\"cus_" + table.getClassNameField() + "\",functionId=\"getById\")\n");
		file.write("	@RequestMapping(value = \"/{id}\", method = RequestMethod.GET)\n");
		file.write("	@ResponseBody\n");
		file.write("	public ModelAndView getById(@PathVariable Long id) {\n");
		file.write("		return getSimpleJsonView(JSON.toJSONStringWithDateFormat(" + table.getClassNameField() + "Service.getById(id), \"yyyy-MM-dd HH:mm:ss\",SerializerFeature.WriteMapNullValue, SerializerFeature.WriteDateUseDateFormat));\n");
		file.write("	}\n");
		file.write("}\n");

		file.closeWriter();
	}

	public void generateJsp(Table table, PathConfig path) {
		ClassFile file = new ClassFile(path.getJspPath(), table.getClassNameField() + "Mng.jsp");
		file.write("<%@ page language=\"java\" contentType=\"text/html; charset=UTF-8\"\n");
		file.write("         pageEncoding=\"UTF-8\" %>\n");
		file.write("<%@ include file=\"../../common/include.jsp\" %>\n");
		file.write("<%@ include file=\"../../common/commonFileUpload.jsp\" %>\n");
		file.write("<!doctype html>\n");
		file.write("<html lang=\"en\">\n");
		
		
		file.write("<head>\n");
		file.write("    <title>" + table.getComment() + "管理</title>\n");
		file.write("    <script>\n");
		file.write("        $(function () {\n");
		file.write("            createDataGrid();\n");
		file.write("        });\n");
		file.write("\n"); 
		file.write("        function createDataGrid() {\n");
		file.write("            $('#dgd_result').datagrid({\n");
		file.write("                title: \"" + table.getComment() + "管理\",\n");
		file.write("                pagination: true,\n");
		file.write("                rownumbers: true,\n");
		file.write("                singleSelect: false,\n");
		file.write("                method: 'get',\n");
		file.write("                 fit: true,\n");
		file.write("                //height: 440,\n");
		file.write("                url: '<%=basePath%>cms/" + table.getClassNameField() + "/search',\n");
		file.write("                queryParams: {\n");
		file.write("                },\n");
		file.write("                idField: 'id',\n");
		file.write("                toolbar: [\n");
		file.write("                    {\n");
		file.write("                        text: '添加',\n");
		file.write("                        id: 'btn_add',\n");
		file.write("                        iconCls: 'icon-add',\n");
		file.write("                        handler: open4Add\n");
		file.write("                    },{\n");
		file.write("                        text: '编辑',\n");
		file.write("                        id: 'btn_edit',\n");
		file.write("                        iconCls: 'icon-edit',\n");
		file.write("                        disabled: true,\n");
		file.write("                        handler: open4Edit\n");
		file.write("                    },{\n");
		file.write("                        text: '删除',\n");
		file.write("                        id: 'btn_delete',\n");
		file.write("                        iconCls: 'icon-no',\n");
		file.write("                        disabled: true,\n");
		file.write("                        handler: deleteObject\n");
		file.write("                    }\n");
		file.write("                ],\n");
		file.write("                onLoadSuccess: function () {\n");
		file.write("                },\n");
		file.write("                onDblClickRow: open4Edit,\n");
		file.write("                onSelect: function (rowIndex, rowData) {\n");
		file.write("                    $('#btn_delete').linkbutton('enable');\n");
		file.write("                    $('#btn_edit').linkbutton('enable');\n");
		file.write("                }\n");
		file.write("            });\n");
		file.write("        }\n");
		file.write("\n");
		file.write("        //打开添加 对话框\n");
		file.write("        function open4Add() {\n");
		file.write("\n");
		file.write("            var s = $(document.body).height();\n");
		file.write("            $('#dlg_" + table.getClassNameField() + "').dialog({\n");
		file.write("            	height: s*0.7\n");
		file.write("            }); \n");
		file.write("\n");
		file.write("            $('#frm_" + table.getClassNameField() + "').form('clear');\n");
		file.write("            $('#dlg_" + table.getClassNameField() + "').dialog('open');\n");
		file.write("            $('#dlg_" + table.getClassNameField() + "').form('load', {\n");
		file.write("                status: 1\n");
		file.write("            });\n");
		file.write("            $('#id').val(\"0\");\n");
		file.write("        }\n");
		file.write("\n");
		file.write("\n");
		file.write("         //打开编辑对话框 \n");
		file.write("        function open4Edit(rowIndex, rowData) {\n");
		file.write("            var id;\n");
		file.write("            if (rowData) {\n");
		file.write("                id = rowData.id;\n");
		file.write("            } else {\n");
		file.write("                var row = $(\"#dgd_result\").datagrid(\"getSelected\");\n");
		file.write("                if (!row) {\n");
		file.write("                    $.messager.alert(\"提示\", \"请至少选择一行！\", \"error\");\n");
		file.write("                    return;\n");
		file.write("                }\n");
		file.write("                id = row.id;\n");
		file.write("            }\n");
		file.write("\n");
		file.write("            var s = $(document.body).height();\n");
		file.write("            $('#dlg_" + table.getClassNameField() + "').dialog({\n");
		file.write("            	height: s*0.7\n");
		file.write("            }); \n");
		file.write("\n");

		file.write("            $('#frm_" + table.getClassNameField() + "').form('clear');\n");
		file.write("            $('#dlg_" + table.getClassNameField() + "').dialog('open');\n");
		file.write("            $.ajax({\n");
		file.write("                url: '<%=basePath%>cms/" + table.getClassNameField() + "/' + id,\n");
		file.write("                type: \"GET\",\n");
		file.write("                success: function (data) {\n");
		file.write("                    if (typeof(data.errMsg) != 'undefined') {\n");
		file.write("                        $.messager.alert(\"提示\", data.errMsg, \"error\");\n");
		file.write("                        return;\n");
		file.write("                    }\n");
		file.write("                    dealRetData(data);\n");
		file.write("                }\n");
		file.write("            });\n");
		file.write("        }\n");
		file.write("\n");
		file.write("        //处理编辑数据\n");
		file.write("        function dealRetData(data) {\n");
		file.write("            var data = JSON.parse(data);\n");
		file.write("            $('#frm_" + table.getClassNameField() + "').form('load', data);\n");
		file.write("            if(typeof(data.profiles) != 'undefined'){\n");
		file.write("            	$('#profiles').combobox('setValues', data.profiles.split(\",\"));\n");
		file.write("            }\n");
		file.write("            if(typeof(data.logo) != 'undefined'){\n");
		file.write("            	var jsonList = eval('(' + data.logo + ')');\n");
		file.write("                for (var key in jsonList) {\n");
		file.write("                    $(\"#\" + key + \"\").val(jsonList[key]);\n");
		file.write("                }	\n");
		file.write("            }\n");
		file.write("        }\n");
		file.write("\n");
		file.write("         //保存对象\n");
		file.write("        function saveObject() {\n");
		file.write("            $.messager.progress();\n");
		file.write("            $('#frm_" + table.getClassNameField() + "').form('submit', {\n");
		file.write("                url: '<%=basePath%>cms/" + table.getClassNameField() + "/saveOrUpdate',\n");
		file.write("                onSubmit: function (param) {\n");
		file.write("                    var isValid = $(this).form('validate');\n");
		file.write("                    if (!isValid) {\n");
		file.write("                        $.messager.progress('close');\n");
		file.write("                        return false;\n");
		file.write("                    }\n");
		file.write("                    return true;\n");
		file.write("                },\n");
		file.write("                success: function (data) {\n");
		file.write("                    $.messager.progress('close');\n");
		file.write("                    if (data) {\n");
		file.write("                        var result = eval('(' + data + ')');\n");
		file.write("                        $.messager.alert(\"提示\", result.errMsg, \"error\");\n");
		file.write("                        return;\n");
		file.write("                    }\n");
		file.write("                    $('#dgd_result').datagrid(\"clearSelections\");\n");
		file.write("                    $('#dgd_result').datagrid(\"reload\");\n");
		file.write("                    $('#dlg_" + table.getClassNameField() + "').dialog('close');\n");
		file.write("                }\n");
		file.write("            });\n");
		file.write("        }\n");
		file.write("\n");
		file.write("         //删除\n");
		file.write("        function deleteObject() {\n");
		file.write("            var rows = $(\"#dgd_result\").datagrid(\"getSelections\");\n");
		file.write("            if (rows.length==0) {\n");
		file.write("                $.messager.alert(\"提示\", \"请至少选择一行！\", \"error\");\n");
		file.write("                return;\n");
		file.write("            }\n");

		file.write("            var ids= '';\n");
		file.write("            for(var i=0; i<rows.length; i++){\n");
		file.write("            	ids+=rows[i].id+',';\n");
		file.write("            }\n");

		file.write("            $.messager.confirm('确认', '确认删除？', function (r) {\n");
		file.write("                if (r) {\n");
		file.write("                    $.ajax({\n");
		file.write("                        url: '<%=basePath%>cms/" + table.getClassNameField() + "/delete?ids=' + ids,\n");
		file.write("                        type: 'GET',\n");
		file.write("                        success: function (data) {\n");
		file.write("                            if (data) {\n");
		file.write("                                $.messager.alert(\"提示\", data.errMsg, \"error\");\n");
		file.write("                                return;\n");
		file.write("                            }\n");
		file.write("                            $('#dgd_result').datagrid(\"clearSelections\");\n");
		file.write("                            $('#dgd_result').datagrid(\"reload\");\n");
		file.write("                        }\n");
		file.write("                    });\n");
		file.write("                }\n");
		file.write("            });\n");
		file.write("        }\n");
		file.write("\n");
		file.write("        // 搜索\n");
		file.write("        function reSearch() {\n");
		file.write("            // 重新加载数据\n");
		file.write("            $('#dgd_result').datagrid('load', {\n");

		List<Column> list = table.getColumnsList();
		StringBuffer sb = new StringBuffer("");
		for (Column col : list) {
			sb.append("\t\t\t\t" + col.getFildName() + ": $('#" + col.getFildName() + "').val(),\n");
		}
		if (sb.toString().lastIndexOf(",") != -1) {
			file.write(sb.toString().substring(0, sb.toString().toString().lastIndexOf(",")) + "\n");
		}

		file.write("            });\n");
		file.write("        }\n");
		file.write("\n");
		file.write("        function reset() {\n");
		file.write("            $('#searchCondtion input').val(\"\");\n");
		file.write("            $('#condition_status').combobox('setValue', '');\n");
		file.write("        }\n\n");

		file.write("        document.onkeydown = function (event) {\n");
		file.write("        	var e = event || window.event || arguments.callee.caller.arguments[0];\n");
		file.write("        	if (e && e.keyCode == 13) {\n");
		file.write("        		reSearch();\n");
		file.write("        	}\n");
		file.write("         };\n");
		file.write("    </script>\n\n");
		file.write("</head>\n");
		file.write("<body class=\"easyui-layout\">\n");
		file.write("<div id=\"searchCondtion\" class=\"easyui-panel ms-panel-outer\" title=\"查询条件\" data-options=\"region:'north',collapsible:true\" style=\"height:auto;overflow:hidden\">\n");

		file.write("	     <header>\n");
		file.write("			<div class=\"m-toolbar\">\n");
		file.write("				<div class=\"m-left\">" + table.getName() + "管理</div>");
		file.write("				<div class=\"m-right\">\n");
		file.write("					<a href=\"javascript:void(0)\" class=\"easyui-linkbutton\" onClick=\"reSearch()\" data-options=\"iconCls:'icon-search'\">搜索</a>\n");
		file.write("                	<a href=\"javascript:void(0)\" class=\"easyui-linkbutton\" onClick=\"reset()\" data-options=\"iconCls:'icon-reset'\">重置</a>\n");
		file.write("				</div>\n");
		file.write("			</div>\n");
		file.write("		</header>\n");
		file.write("		<form id=\"ff\">\n");

		int showNum = 4;
		int ii = 0;
		for (Column col : list) {
			if (showNum == ii) {
				file.write("<!--");
			}
			file.write("			<div style=\"margin-bottom:10px\">\n");
			if (col.getDatatype().toUpperCase().indexOf("DATE") != -1) {
				file.write("				<input id=\"" + col.getFildName() + "\" name=\"" + col.getFildName() + "\"  class=\"easyui-datetimebox\" label=\"" + col.getComment() + ":\" prompt=\"请输入" + col.getComment() + "\" style=\"width:100%\">\n");
			} else if (col.getFildName().toLowerCase().equals("status")) {
				file.write("					<select id=\"" + col.getFildName() + "\" name=\"" + col.getFildName() + "\"  class=\"easyui-combobox\" label=\"状态\" style=\"width:100%\" editable=\"false\">\n");
				file.write("					<option value=\"1\">有效</option>\n");
				file.write("					<option value=\"0\">无效</option>\n");
				file.write("				</select>\n");
			} else if (col.getFildName().toLowerCase().equals("state")) {
				file.write("					<select id=\"" + col.getFildName() + "\" name=\"" + col.getFildName() + "\"  class=\"easyui-combobox\" label=\"状态\" style=\"width:100%\" editable=\"false\">\n");
				file.write("					<option value=\"1\">有效</option>\n");
				file.write("					<option value=\"0\">无效</option>\n");
				file.write("				</select>\n");
			} else if (col.getFildName().toLowerCase().equals("createTime".toLowerCase())) {
				file.write("				<input id=\"" + col.getFildName() + "\" name=\"" + col.getFildName() + "\"  class=\"easyui-datetimebox\" label=\"" + col.getComment() + ":\" prompt=\"请输入" + col.getComment() + "\" style=\"width:100%\">\n");
			} else {
				file.write("				<input id=\"" + col.getFildName() + "\" name=\"" + col.getFildName() + "\"  class=\"easyui-textbox\" label=\"" + col.getComment() + ":\" prompt=\"请输入" + col.getComment() + "\" style=\"width:100%\">\n");
			}
			file.write("			</div> \n");
			ii++;
		}
		file.write("-->\n");

		file.write("		</form>\n");
		file.write("	</div>\n");

		file.write("\n\n<div data-options=\"region:'center'\" style=\"border: 0;\">\n");
		file.write("	<table id=\"dgd_result\"  data-options=\"header:'#hh',singleSelect:true,border:false,fit:true,fitColumns:true,scrollbarSize:0\">\n");

		file.write("    	<thead>\n");
		file.write("    	<tr>\n");
		file.write("    	\t<th data-options=\"field:'ck',checkbox:true\"></th>\n");
		int topNum = 5;
		if (list.size() < topNum) {
			topNum = list.size();
		}

		// list page start
		for (int i = 0; i < list.size(); i++) {
			if (showNum == i) {
				file.write("<!--\n");
			}
			Column col = list.get(i);
			if (col.getFildName().toLowerCase().equals("id")) {
				file.write("        	<th field=\"" + col.getFildName() + "\" width=\"20\" align=\"center\">" + col.getName() + "</th>\n");
			}else if (col.getDatatype().toUpperCase().indexOf("DATE") != -1 || col.getFildName().toLowerCase().equals("createTime".toLowerCase())) {
				file.write("        	<th field=\"" + col.getFildName() + "\" width=\"100\" align=\"center\" formatter=\"formatDateTime\">" + col.getName() + "</th>\n");
			} else if (col.getFildName().toLowerCase().equals("state") || col.getFildName().toLowerCase().equals("status")) {
				file.write("        	<th field=\"" + col.getFildName() + "\" width=\"100\" align=\"center\" formatter=\"formattStatus\">" + col.getName() + "</th>\n");
			} else {
				file.write("        	<th field=\"" + col.getFildName() + "\" width=\"100\" align=\"center\">" + col.getName() + "</th>\n");
			}
		}
		file.write("-->\n");

		file.write("    	</tr>\n");
		file.write("    	</thead>\n");

		file.write("	</table>\n");
		file.write("</div>\n");
		file.write("</body>\n");
		file.write("\n\n\n");
		file.write("<!-- add start -->\n");
		file.write("<div style=\"dispaly:none\">\n");
		file.write("    <div id=\"dlg_" + table.getClassNameField() + "\" class=\"easyui-dialog\" title=\"" + table.getComment() + "添加\" style=\"width:80%;height:60%;padding:10px;top:100px;height:auto;\"\n");
		file.write("         data-options=\"closed:true,modal:true,shadow:true,buttons:'#tb2'\">\n");
		file.write("        <form id=\"frm_" + table.getClassNameField() + "\" method=\"post\">\n");

		for (Column col : list) {
			file.write("			<div style=\"margin-bottom:10px\">\n");
			if (col.getDatatype().toUpperCase().indexOf("DATE") != -1) {
				file.write("				<input id=\"id_" + col.getFildName() + "\" name=\"" + col.getFildName() + "\" data-options=\"required:true\" class=\"easyui-datetimebox\" label=\"" + col.getComment() + ":\" editable=\"false\" prompt=\"请输入" + col.getComment()
						+ "\" style=\"width:100%\">\n");
			} else if (col.getFildName().toLowerCase().equals("status")) {
				file.write("				<select id=\"id_" + col.getFildName() + "\" name=\"" + col.getFildName() + "\"  class=\"easyui-combobox\" label=\"状态\" style=\"width:100%\" editable=\"false\">\n");
				file.write("					<option value=\"1\">有效</option>\n");
				file.write("					<option value=\"0\">无效</option>\n");
				file.write("				</select>\n");
			} else if (col.getFildName().toLowerCase().equals("state")) {
				file.write("				<select id=\"id_" + col.getFildName() + "\" name=\"" + col.getFildName() + "\"  class=\"easyui-combobox\" label=\"状态\" style=\"width:100%\" editable=\"false\">\n");
				file.write("					<option value=\"1\">有效</option>\n");
				file.write("					<option value=\"0\">无效</option>\n");
				file.write("				</select>\n");
			} else if (col.getDatatype().toUpperCase().indexOf("DATE") != -1 || col.getFildName().toLowerCase().equals("createTime".toLowerCase())) {
				file.write("				<input id=\"id_" + col.getFildName() + "\" name=\"" + col.getFildName() + "\" data-options=\"required:true\" class=\"easyui-datetimebox\" label=\"" + col.getComment() + ":\" editable=\"false\" prompt=\"请输入" + col.getComment()
						+ "\" style=\"width:100%\">\n");
			}  else if (col.getFildName().toLowerCase().equals("id".toLowerCase())) {
				file.write("				<input id=\"id_" + col.getFildName() + "\" name=\"" + col.getFildName() + "\"   label=\"" + col.getComment() + "\" editable=\"false\"  class=\"easyui-textbox\" style=\"width:100%\">\n");
			}else {
				file.write("				<input id=\"id_" + col.getFildName() + "\" name=\"" + col.getFildName() + "\" data-options=\"required:true\" class=\"easyui-textbox\" label=\"" + col.getComment() + ":\" prompt=\"请输入" + col.getComment()
						+ "\" style=\"width:100%\">\n");
			}
			file.write("			</div> \n");
		}

		file.write("        </form>\n");
		file.write("        <div id=\"tb2\">\n");
		file.write("            <a id=\"btn_saveObject\" href=\"javascript:void(0)\" class=\"easyui-linkbutton\" onclick=\"saveObject()\"\n");
		file.write("               data-options=\"iconCls:'icon-save'\">保存</a>\n");
		file.write("            <a href=\"javascript:void(0)\" class=\"easyui-linkbutton\" onClick=\"$('#dlg_" + table.getClassNameField() + "').dialog('close');\"\n");
		file.write("               data-options=\"iconCls:'icon-cancel'\">取消</a>\n");
		file.write("        </div>\n");
		file.write("    </div>\n");
		file.write("</div>\n");
		file.write("<!-- add end -->\n\n");
		file.write("</html>\n");

		file.closeWriter();
	}

	public void generateAPIAction(Table table, PathConfig path) {
		ClassFile file = new ClassFile(path.getApiActionPath(), table.getClassName() + "Controller_V2.java");

		file.write("package com.gochinatv.cms.api;\n");
		file.write("\n");
		file.write("import java.util.List;\n");
		file.write("import java.util.ArrayList;\n");
		file.write("\n");
		file.write("import org.slf4j.Logger;\n");
		file.write("import org.slf4j.LoggerFactory;\n");
		file.write("import org.springframework.beans.factory.annotation.Autowired;\n");
		file.write("import org.springframework.stereotype.Controller;\n");
		file.write("import org.springframework.web.bind.annotation.RequestBody;\n");
		file.write("import org.springframework.web.bind.annotation.RequestMapping;\n");
		file.write("import org.springframework.web.bind.annotation.ResponseBody;\n");
		file.write("\n");
		file.write("import com.alibaba.fastjson.JSON;\n");
		file.write("import com.alibaba.fastjson.serializer.SerializerFeature;\n");
		file.write("import com.gochinatv.cms.po." + table.getClassName() + ";\n");

		file.write("import com.gochinatv.cms.serviceImpl." + table.getClassName() + "ServiceImpl;\n");
		file.write("import com.gochinatv.exception.BusinessException;\n");
		file.write("import com.gochinatv.framework.controller.BaseAceController;\n");
		file.write("import com.gochinatv.util.ChangeRelection;\n");
		file.write("import com.gochinatv.vo.PageInfo;\n");
		file.write("import com.gochinatv.vo.PageResult;\n");

		file.write("import java.text.MessageFormat;\n");
		file.write("import com.gochinatv.exception.MessageHelper;\n");
		file.write("import org.slf4j.MDC;\n");
		file.write("import com.owlike.genson.Genson;\n");
		file.write("import com.wordnik.swagger.annotations.ApiOperation;\n");

		file.write("\n");
		file.write("/**\n");
		file.write(" * @author 格调先生\n");
		file.write(" */\n");
		file.write("@Controller\n");
		file.write("@RequestMapping(value = \"/api/v2/" + table.getClassName() + "/*\")\n");
		file.write("public class " + table.getClassName() + "Controller_V2 extends BaseAceController {\n");
		file.write("	private Logger logger = LoggerFactory.getLogger(" + table.getClassName() + "Controller_V2.class);\n");
		file.write("\n");
		file.write("	@Autowired\n");
		file.write("	private " + table.getClassName() + "ServiceImpl " + table.getClassName() + "Service;\n");
		file.write("\n");
		file.write("	/**\n");
		file.write("	 * 搜索\n");
		file.write("	 * \n");
		file.write("	 * @param bean\n");
		file.write("	 * @param pageInfo\n");
		file.write("	 * @return\n");
		file.write("	 * @throws BusinessException\n");
		file.write("	 */\n");
		file.write("	@RequestMapping(value = \"/search\", produces = \"application/json;charset=UTF-8\")\n");
		file.write("	@ResponseBody\n");
		file.write("	@ApiOperation(value = \"搜索" + table.getName() + "\", httpMethod = \"POST\", response = PageResult.class, notes = \"搜索" + table.getName() + "\")\n");
		file.write("	public String search(@RequestBody " + table.getClassName() + " bean) throws BusinessException {\n");
		file.write("		// 参数校验--start\n");
		file.write("		String[] verifyField = { };\n");
		file.write("		List<String> verifyList = ChangeRelection.findEmptyFields(" + table.getClassName() + ".class, bean, verifyField);\n");
		file.write("		PageResult pageResult = new PageResult();\n");
		file.write("		// 参数校验--end\n");
		file.write("\n");
		file.write("		if (verifyList.size() > 0) {\n");

		file.write("\t\t\tString code = \"E01001\";\n");
		file.write("\t\t\tString message = MessageFormat.format(MessageHelper.getInstance().getMessage(code),verifyList.toString().replaceAll(\"\\\\[\", \"(\").replaceAll(\"\\\\]\", \")\").replaceAll(\"\\\\,\", \"，\"));\n");
		file.write("\tpageResult = new PageResult();\n");
		file.write("\t\tpageResult.setMessage(message);\n");
		file.write("\t\tpageResult.setCode(code);\n");

		file.write("		} else {\n");
		file.write("			PageInfo pageInfo = new PageInfo();\n");
		file.write("			pageResult = " + table.getClassName() + "Service.search4Page(true,bean, pageInfo);\n");
		file.write("		}\n");
		file.write("		String res = JSON.toJSONStringWithDateFormat(pageResult, \"yyyy-MM-dd HH:mm:ss\", SerializerFeature.WriteMapNullValue, SerializerFeature.WriteDateUseDateFormat,SerializerFeature.WriteNullStringAsEmpty);\n");
		file.write("		MDC.put(\"resData\", new Genson().serialize(res));\n");
		file.write("		return res;\n");
		file.write("	}\n");
		file.write("\n");
		file.write("	/**\n");
		file.write("	 * 插入\n");
		file.write("	 * \n");
		file.write("	 * @param demo\n");
		file.write("	 * @return\n");
		file.write("	 * @throws BusinessException\n");
		file.write("	 */\n");
		file.write("	@RequestMapping(value = \"/insert\", produces = \"application/json;charset=UTF-8\")\n");
		file.write("	@ResponseBody\n");
		file.write("	@ApiOperation(value = \"添加" + table.getName() + "\", httpMethod = \"POST\", response = PageResult.class, notes = \"添加" + table.getName() + "\")\n");
		file.write("	public PageResult insert(@RequestBody " + table.getClassName() + " demo) throws BusinessException {\n");
		file.write("		// 默认项\n");
		file.write("		//demo.setComment(demo.getCommentSource());\n");
		file.write("		//demo.setIsRealData(\"1\");\n");
		file.write("		//demo.setLanguage(\"1\");\n");
		file.write("		//demo.setStatus(\"1\");\n");
		file.write("		//demo.setCreateTime(new Date());\n");
		file.write("		// 默认项\n");
		file.write("\n");
		file.write("		" + table.getClassName() + "Service.insert(demo);\n");
		file.write("		PageResult pageResult = new PageResult();\n");
		file.write("		pageResult.setRows(new ArrayList());\n");
		file.write("		MDC.put(\"resData\", new Genson().serialize(pageResult));\n");
		file.write("		return pageResult;\n");
		file.write("	}\n");
		file.write("\n");
		file.write("	/**\n");
		file.write("	 * 修改\n");
		file.write("	 * \n");
		file.write("	 * @param newDto\n");
		file.write("	 * @return\n");
		file.write("	 * @throws BusinessException\n");
		file.write("	 */\n");
		file.write("	@RequestMapping(value = \"/edit\", produces = \"application/json;charset=UTF-8\")\n");
		file.write("	@ResponseBody\n");
		file.write("	@ApiOperation(value = \"编辑" + table.getName() + "\", httpMethod = \"POST\", response = PageResult.class, notes = \"编辑" + table.getName() + "\")\n");
		file.write("	public PageResult edit(@RequestBody " + table.getClassName() + " newDto) throws BusinessException {\n");
		file.write("		// 参数校验--start\n");
		file.write("		String[] verifyField = { \"id\" };\n");
		file.write("		List<String> verifyList = ChangeRelection.findEmptyFields(" + table.getClassName() + ".class, newDto, verifyField);\n");
		file.write("		// 参数校验--end\n");
		file.write("		PageResult pageResult = new PageResult();\n");
		file.write("\n");
		file.write("		if (verifyList.size() > 0) {\n");
		file.write("\t\t\tString code = \"E01001\";\n");
		file.write("\t\t\tString message = MessageFormat.format(MessageHelper.getInstance().getMessage(code),verifyList.toString().replaceAll(\"\\\\[\", \"(\").replaceAll(\"\\\\]\", \")\").replaceAll(\"\\\\,\", \"，\"));");
		file.write("\tpageResult = new PageResult();");
		file.write("\t\tpageResult.setMessage(message);");
		file.write("\t\tpageResult.setCode(code);");
		file.write("		} else {\n");
		file.write("			" + table.getClassName() + " oldDto = " + table.getClassName() + "Service.getById(Long.valueOf(newDto.getId()));\n");
		file.write("			try {\n");
		file.write("				ChangeRelection.changeObj(oldDto, newDto);\n");
		file.write("			} catch (Exception e) {\n");
		file.write("				e.printStackTrace();\n");
		file.write("			}\n");
		file.write("			" + table.getClassName() + "Service.update(newDto);\n");
		file.write("		}\n");
		file.write("\n");
		file.write("		pageResult.setRows(new ArrayList());\n");
		file.write("		MDC.put(\"resData\", new Genson().serialize(pageResult));\n");
		file.write("		return pageResult;\n");
		file.write("	}\n");
		file.write("\n");
		file.write("	/**\n");
		file.write("	 * 删除\n");
		file.write("	 * \n");
		file.write("	 * @param bean\n");
		file.write("	 * @return\n");
		file.write("	 * @throws BusinessException\n");
		file.write("	 */\n");
		file.write("	@RequestMapping(value = \"/delete\", produces = \"application/json;charset=UTF-8\")\n");
		file.write("	@ResponseBody\n");
		file.write("	@ApiOperation(value = \"删除" + table.getName() + "\", httpMethod = \"POST\", response = PageResult.class, notes = \"删除" + table.getName() + "\")\n");
		file.write("	public PageResult delete(@RequestBody " + table.getClassName() + " bean) throws BusinessException {\n");
		file.write("		// 参数校验--start\n");
		file.write("		String[] verifyField = { \"id\" };\n");
		file.write("		List<String> verifyList = ChangeRelection.findEmptyFields(" + table.getClassName() + ".class, bean, verifyField);\n");
		file.write("		// 参数校验--end\n");
		file.write("		PageResult pageResult = new PageResult();\n");
		file.write("\n");
		file.write("		if (verifyList.size() > 0) {\n");
		file.write("\t\t\tString code = \"E01001\";\n");
		file.write("\t\t\tString message = MessageFormat.format(MessageHelper.getInstance().getMessage(code),verifyList.toString().replaceAll(\"\\\\[\", \"(\").replaceAll(\"\\\\]\", \")\").replaceAll(\"\\\\,\", \"，\"));");
		file.write("\tpageResult = new PageResult();");
		file.write("\t\tpageResult.setMessage(message);");
		file.write("\t\tpageResult.setCode(code);");
		file.write("		} else {\n");
		file.write("			" + table.getClassName() + "Service.delete(Long.valueOf(bean.getId()));\n");
		file.write("		}\n");
		file.write("		pageResult.setRows(new ArrayList());\n");
		file.write("		MDC.put(\"resData\", new Genson().serialize(pageResult));\n");
		file.write("		return pageResult;\n");
		file.write("	}\n");
		file.write("}\n");

		file.closeWriter();
	}

	public void generateTest(Table table, PathConfig path) {
		ClassFile file = new ClassFile(path.getTestPath(), table.getTestName() + ".java");
		file.write("\n");
		file.write("package com.gochinatv.api;\n");
		file.write("import java.util.HashMap;\n");
		file.write("import java.util.Map;\n");
		file.write("\n");
		file.write("import org.junit.FixMethodOrder;\n");
		file.write("import org.junit.Test;\n");
		file.write("import org.junit.runners.MethodSorters;\n");
		file.write("\n");
		file.write("import com.gochinatv.api.base.Url;\n");
		file.write("import com.gochinatv.util.ForMatJSONStr;\n");
		file.write("import com.gochinatv.util.HttpAPI;\n");
		file.write("import com.google.gson.Gson;\n");
		file.write("\n");
		file.write("@FixMethodOrder(MethodSorters.NAME_ASCENDING)\n");
		file.write("public class " + table.getTestName() + " {\n");
		file.write("	@Test\n");
		file.write("	public void test001_search() throws Exception {\n");
		file.write("		String url = Url.baseUrl + \"api/v2/" + table.getClassName() + "/search\";\n");
		file.write("		Map<String, String> map = new HashMap<String, String>();\n");
		file.write("\n");
		file.write("		// 分页查询\n");
		file.write("		// map.put(\"page\", \"1\");// 页码\n");
		file.write("		// map.put(\"rows\", \"10\");// 每页显示的数目\n");
		file.write("\n");
		file.write("		// limit 1 ，1\n");
		file.write("		map.put(\"start\", \"0\");// 开始位置\n");
		file.write("		map.put(\"fetchsize\", \"10\");// 每页显示的数目");
		file.write("\n");
		file.write("\n");

		List<Column> list = table.getColumnsList();
		for (Column col : list) {
			file.write("		map.put(\"" + col.getFildName() + "\", \"0\");//" + col.getComment() + "\n");
		}

		file.write("\n");
		file.write("\n");
		file.write("		String ret = HttpAPI.postJson(url, map);\n");
		file.write("		System.out.println(\"-----------------\");\n");
		file.write("		System.out.println(url);\n");
		file.write("		System.out.println(ForMatJSONStr.format(new Gson().toJson(map)));\n");
		file.write("		System.out.println(ForMatJSONStr.format(ret));\n");
		file.write("	}\n");
		file.write("\n");
		file.write("	@Test\n");
		file.write("	public void test002_insert() throws Exception {\n");
		file.write("		String url = Url.baseUrl + \"api/v2/" + table.getClassName() + "/insert\";\n");
		file.write("		Map<String, String> map = new HashMap<String, String>();\n");

		for (Column col : list) {
			file.write("		map.put(\"" + col.getFildName() + "\", \"0\");//" + col.getComment() + "\n");
		}

		file.write("\n");
		file.write("		String ret = HttpAPI.postJson(url, map);\n");
		file.write("		System.out.println(\"-----------------\");\n");
		file.write("		System.out.println(url);\n");
		file.write("		System.out.println(ForMatJSONStr.format(new Gson().toJson(map)));\n");
		file.write("		System.out.println(ForMatJSONStr.format(ret));\n");
		file.write("	}\n");
		file.write("\n");
		file.write("	@Test\n");
		file.write("	public void test003_edit() throws Exception {\n");
		file.write("		String url = Url.baseUrl + \"api/v2/" + table.getClassName() + "/edit\";\n");
		file.write("		Map<String, String> map = new HashMap<String, String>();\n");
		for (Column col : list) {
			file.write("		map.put(\"" + col.getFildName() + "\", \"0\");//" + col.getComment() + "\n");
		}
		file.write("\n");
		file.write("		String ret = HttpAPI.postJson(url, map);\n");
		file.write("		System.out.println(\"-----------------\");\n");
		file.write("		System.out.println(url);\n");
		file.write("		System.out.println(ForMatJSONStr.format(new Gson().toJson(map)));\n");
		file.write("		System.out.println(ForMatJSONStr.format(ret));\n");
		file.write("	}\n");
		file.write("\n");
		file.write("	@Test\n");
		file.write("	public void test004_delete() throws Exception {\n");
		file.write("		String url = Url.baseUrl + \"api/v2/" + table.getClassName() + "/delete\";\n");
		file.write("		Map<String, String> map = new HashMap<String, String>();\n");
		file.write("		map.put(\"id\",\"0\");\n");
		file.write("\n");
		file.write("		String ret = HttpAPI.postJson(url, map);\n");
		file.write("		System.out.println(\"-----------------\");\n");
		file.write("		System.out.println(url);\n");
		file.write("		System.out.println(ForMatJSONStr.format(new Gson().toJson(map)));\n");
		file.write("		System.out.println(ForMatJSONStr.format(ret));\n");
		file.write("	}\n");
		file.write("}\n");

		file.closeWriter();

		file.seckEnd();
		file.closeWriter();
	}

	/**
	 * 例子
	 * 
	 * @param table
	 * @param path
	 */
	public void example(Table table, PathConfig path) {
		if (!table.isGen()) {
			return;
		}

		ClassBean cls = new ClassBean();
		// 1、包名
		cls.setPkgName(path.getExamplePackage());

		// 2、import
		cls.addImps("java.util.ArrayList");
		cls.addImps("java.util.Date");
		cls.addImps("java.util.List");

		// 3、类名
		cls.setClassName(table.getExampleName());
		// 继承
		cls.setClassExt("UserDao");
		// 实现 xx 接口
		cls.setClassImp("Runnable");

		// 4、属性名
		cls.addFiled("my" + table.getService(), table.getService(), "我是属性", "@Autowired");
		cls.addFiled("age", "int", "年龄");

		// 5、方法--start
		MethodBean jsonAdd = new MethodBean();
		// 方法的注解
		jsonAdd.setAnnotion("@RequestMapping(value = \"/jsonAdd\")\n\t@ResponseBody");
		// 方法的名字
		jsonAdd.setName("jsonAdd");
		// 注释
		jsonAdd.setComments("添加");
		// 方法的返回类型
		jsonAdd.setRet("JsonBean");
		// 方法的入参
		jsonAdd.setParams("HttpServletRequest request,@RequestBody " + table.getClassName() + "Query user");

		jsonAdd.addCodeLine("int a = 0;");
		jsonAdd.addCodeLine("System.out.println(a);");
		jsonAdd.addCodeLine("return null;");

		cls.addMethod(jsonAdd);
		// 5、方法--end

		// 6、路径，类名
		ClassFile file = new ClassFile(path.getExamplePath(), table.getExampleName() + ".java");
		file.pkg(cls.getPkgName()).newLine();
		file.imps(cls.getImps()).newLine(1);

		gennerateClassComment(file, "维护表 " + table.getDTOName() + "的Action");

		// 7、类的注解
		String annotion = "@Controller\n@RequestMapping(\"/json/user/" + lowerFirstChar(table.getClassName()) + "Json\")\n";
		file.cls(cls.getClassName(), annotion).ext(cls.getClassExt()).impms(cls.getClassImp());
		file.seck();
		file.newLine();

		// 8、生成属性 nothing to do
		Iterator daos = cls.getFileds().iterator();
		while (daos.hasNext()) {
			FiledBean filed = (FiledBean) daos.next();
			if (filed.getComment() != null && filed.getComment().length() > 0) {
				file.tab().commentProperty(filed.getComment()).newLine();
			}
			if (filed.getAnnotion() != null && filed.getAnnotion().length() > 0) {
				file.tab().annotion(filed.getAnnotion()).newLine();
			}
			file.tab().property(filed.getType(), filed.getName()).newLine();
		}

		file.newLine();

		// 9、生成方法 nothing to do
		Iterator mths = cls.getMethods().iterator();
		while (mths.hasNext()) {
			MethodBean mth = (MethodBean) mths.next();
			generateCommentMethod(file, mth.getParams(), mth.getRet(), mth.getExceptions(), mth.getComments());
			generateAnnotion(file, mth.getAnnotion());
			file.newLine();
			file.method(mth.getName(), mth.getRet(), mth.getParams(), mth.getExceptions()).newLine();

			Iterator codeLines = mth.getCodeLines().iterator();
			while (codeLines.hasNext()) {
				file.methodCode((String) codeLines.next()).newLine();
			}
			file.methodEnd().newLine(2);
		}

		file.seckEnd();
		file.closeWriter();
	}
	
	public void generateAceEditJsp(Table table, PathConfig path) {
		ClassFile file = new ClassFile(path.getAceEditJspPath(), table.getClassNameField() + "Edit.jsp");
		file.write("<%@ page language=\"java\" contentType=\"text/html; charset=UTF-8\"  %>\n"); 
		file.write("<%@ include file=\"../../common/includeACE.jsp\" %>\n");
		file.write("<!DOCTYPE html>\n");
		file.write("<html lang=\"en\">\n");
		file.write("<head>\n");
		file.write("    <meta http-equiv=\"X-UA-Compatible\" content=\"IE=edge,chrome=1\" />\n");
		file.write("    <meta charset=\"utf-8\" />\n");
		file.write("    <title>编辑"+table.getComment()+"</title>\n");
		file.write("</head>\n");
		file.write("\n");
		file.write("<body class=\"no-skin\">\n");
		file.write("<div class=\"main-container ace-save-state\" id=\"main-container\">\n");
		file.write("    <script type=\"text/javascript\">\n");
		file.write("        try{ace.settings.loadState('main-container')}catch(e){}\n");
		file.write("    </script>\n");
		file.write("\n");
		file.write("    <div class=\"page-content\">\n");
		file.write("        <div class=\"col-sm-5 widget-box widget-color-blue2\">\n");
		file.write("            <div class=\"widget-header\">\n");
		file.write("                <h4 class=\"widget-title lighter smaller\">编辑"+table.getComment()+"</h4>\n");
		file.write("            </div>\n");
		file.write("	        <form class=\"form-horizontal\" id=\""+table.getClassNameField()+"Form\" user=\"form\" style=\"margin-top: 20px;\" method=\"POST\">\n");
		file.write("	            <input type=\"hidden\" name=\"id\" id=\"id\" value=\"${bean.id }\" />\n");
		
		
		List<Column> list = table.getColumnsList();
		for (Column col : list) {
			if(col.getCode().equals("status")){
				file.write("	\n");
				file.write("	            <div class=\"form-group\">\n");
				file.write("	                <label class=\"col-sm-3 control-label no-padding-right\" > 状态 </label>\n");
				file.write("	                <div class=\"col-sm-9\">\n");
				file.write("	                    <select id=\"status\" name=\"status\" class=\"input-sm\">\n");
				file.write("	                    	    <option value=\"1\" <c:if test=\"${bean.status eq '1' }\">selected</c:if>>启用</option>\n");
				file.write("	                    		<option value=\"0\" <c:if test=\"${bean.status eq '0' }\">selected</c:if>>禁用</option>\n");
				file.write("	                	 </select>\n");
				file.write("	                </div>\n");
				file.write("	            </div> \n");
			}else if (col.getFildName().toUpperCase().indexOf("TIME") != -1) {
				file.write("	\n");
				file.write("	            <div class=\"form-group\">\n");
				file.write("	                <label class=\"col-sm-3 control-label no-padding-right\" > "+col.getComment()+"</label>\n");
				file.write("	                <div class=\"col-sm-9\">\n");
				file.write("	                    <input class=\"date-timepicker1\" type=\"text\" class=\"form-control\" name=\""+col.getFildName()+"\" value=\"${bean."+col.getFildName()+" }\"/>\n");
				file.write("	                </div>\n");
				file.write("	            </div> \n");
			}else{
				file.write("	\n");
				file.write("	            <div class=\"form-group\">\n");
				file.write("	                <label class=\"col-sm-3 control-label no-padding-right\" > "+col.getComment()+"</label>\n");
				file.write("	                <div class=\"col-sm-9\">\n");
				file.write("	                    <input type=\"text\" id=\""+col.getFildName()+"\" name=\""+col.getFildName()+"\" value=\"${bean."+col.getFildName()+" }\" placeholder=\""+col.getComment()+"\" class=\"col-xs-10 col-sm-5\">\n");
				file.write("	                </div>\n");
				file.write("	            </div> \n");
			}
		}
		 
		file.write("	          	<button class=\"btn btn-info\" type=\"button\" style=\"margin-left: 20px;\" onclick=\"add()\">\n");
		file.write("	                <i class=\"ace-icon fa fa-check bigger-110\"></i>    保存\n");
		file.write("	            </button>\n");
		file.write("	          	<button class=\"btn btn-danger btn-lg\" onclick=\"window.history.go(-1);\">\n");
		file.write("	          		<i class=\"ace-icon fa fa-reply bigger-110\"></i>\n");
		file.write("	    		</button\n");
		
		file.write("	        </form>\n");
		file.write("        </div>\n");
		file.write("    </div>\n");
		file.write("</div> \n");
		file.write("<script>\n");
		file.write("    function add() {\n");
		file.write("    	 $('#"+table.getClassNameField()+"Form').form('submit', {\n");
		file.write("             url: '<%=basePath%>cms/"+table.getClassNameField()+"/ace/saveOrUpdate',\n");
		file.write("             onSubmit: function (param) {\n");
		file.write("                 return true;\n");
		file.write("             },\n");
		file.write("             success: function (data) {\n");
		file.write("                 if (data) {\n");
		file.write("                     var result = eval('(' + data + ')');\n");
		file.write("                     $.messager.alert(\"提示\", result.errMsg, \"error\");\n");
		file.write("                     return;\n");
		file.write("                 }else{\n");
		file.write("                	  self.location=document.referrer;\n");
		file.write("                 }\n");
		file.write("             }\n");
		file.write("           });\n");
		file.write("    	}\n");
		file.write("             \n");
		file.write("        $('.date-timepicker1').datetimepicker().next().on(ace.click_event, function(){\n");
		file.write("            $(this).prev().focus();\n");
		file.write("        });\n");
		file.write("</script>\n");
		file.write("</body>\n");
		file.write("</html>\n");

		file.closeWriter();
	}
	
	public void generateAceListJsp(Table table, PathConfig path) {
		ClassFile file = new ClassFile(path.getAceListJspPath(), table.getClassNameField() + "List.jsp");
		file.write("<%@ page language=\"java\" contentType=\"text/html; charset=UTF-8\" %>\n"); 
		file.write("<%@ include file=\"../../common/includeACE.jsp\" %>\n");
		file.write("<!DOCTYPE html>\n");
		file.write("<html lang=\"en\">\n");
		file.write("<head>\n");
		file.write("    <title>"+table.getComment()+"管理</title>\n");
		file.write("</head>\n");
		file.write("<body class=\"no-skin\">\n");
		file.write("	<div class=\"main-container ace-save-state\" id=\"main-container\">\n");
		file.write("	    <script type=\"text/javascript\">\n");
		file.write("	        try{ace.settings.loadState('main-container')}catch(e){}\n");
		file.write("	    </script>\n");
		file.write("	\n");
		file.write("	    <div class=\"page-content\">\n");
		file.write("	        <div class=\"col-xs-12\">\n");
		file.write("		        <a href=\"<%=basePath%>/cms/"+table.getClassNameField()+"/ace/edit\">\n");
		file.write("		            <button class=\"btn btn-white btn-info btn-bold\">\n");
		file.write("		                <i class=\"ace-icon fa fa-pencil-square-o bigger-120 blue\"></i>\n");
		file.write("		               		新增\n");
		file.write("		            </button>\n");
		file.write("		        </a>\n");
		file.write("<form method=\"get\" action=\"<%=basePath%>/cms/"+table.getClassNameField()+"/ace/open\" id=\""+table.getClassNameField()+"Form\">\n");
		file.write("	\n");
		file.write("	            <div class=\"table-header\" style=\"margin-top: 10px;\"> "+table.getComment()+"列表  </div>\n");
		file.write("	\n");
		file.write("	            <!-- div.table-responsive -->\n");
		file.write("	            <!-- div.dataTables_borderWrap -->\n");
		file.write("	            <div>\n");
		file.write("	                <div id=\"dynamic-table_wrapper\" class=\"dataTables_wrapper form-inline no-footer\">\n");
		file.write("	                	\n");
		file.write("	                	<!-- search Form start -->\n");
		file.write("		                    <div class=\"row\">\n");
		file.write("			                    <div class=\"col-xs-6\">\n");
		file.write("			                        <div class=\"dataTables_length\" id=\"dynamic-table_length\">\n");
		file.write("			                        </div>\n");
		file.write("			                    </div>\n");
		file.write("		                        <div>\n");
		file.write("		                            <div id=\"dynamic-table_filter\" class=\"dataTables_filter\"><label>搜索:\n");
		
		file.write("		                            	<select id=\"searchKey\" name=\"searchKey\"  >\n");
		List<Column> list = table.getColumnsList();
		for (Column col : list) {
			file.write("				                                <option	value=\""+col.getCode()+"\" <c:if test=\"${ searchKey eq \""+col.getCode()+"\"}\">selected</c:if>>"+col.getComment()+"</option>\n");
		}
		file.write("			                            </select>\n");
		file.write("		                                <input type=\"search\" id=\"searchValue\" name=\"searchValue\" value=\"${searchValue}\" placeholder=\"搜索关键字\" />\n");
		file.write("		                                <button class=\"btn btn-info\" type=\"button\" onclick=\"sub()\">\n");
		file.write("											<i class=\"ace-icon fa fa-check bigger-110\"></i>\n");
		file.write("											Submit\n");
		file.write("										</button>\n");
		file.write("										<button class=\"btn\" type=\"button\" onclick=\"rese()\">\n");
		file.write("											<i class=\"ace-icon fa fa-undo bigger-110\"></i>\n");
		file.write("											Reset\n");
		file.write("										</button>\n");
		
		file.write("		                            </div>\n");
		file.write("		                        </div>\n");
		file.write("		                    </div>\n");
		file.write("	                	<!-- search Form end -->\n");
		file.write("	                	\n");
		file.write("	                	\n");
		file.write("	                	<!-- table start -->\n");
		file.write("	                    <table id=\"dynamic-table\" class=\"table table-striped table-bordered table-hover dataTable no-footer\" role=\"grid\" aria-describedby=\"dynamic-table_info\">\n");
		file.write("	                        <thead>\n");
		file.write("		                        <tr role=\"row\">\n");
		file.write("		                            <th class=\"center sorting_disabled\" rowspan=\"1\" colspan=\"1\" aria-label=\"\">\n");
		file.write("		                                <label class=\"pos-rel\">\n");
		file.write("		                                    <input type=\"checkbox\" class=\"ace\" id=\"check_all\">\n");
		file.write("		                                    <span class=\"lbl\"></span>\n");
		file.write("		                                </label>\n");
		file.write("		                            </th>\n");
		
		int maxShowNum = 3;
		int i =0;
		for (Column col : list) {
			String s = "		                            <th class=\"sorting_disabled\" tabindex=\"0\" rowspan=\"1\" colspan=\"1\" >"+col.getComment()+"</th>\n";
			if(i>=maxShowNum){
				s = s.replaceAll("sorting_disabled", "hidden-480");
			}
			file.write(s);
			i++;
		} 
		file.write("		                            <th class=\"sorting_disabled\" rowspan=\"1\" colspan=\"1\" aria-label=\"\">操作</th>\n");
		file.write("		                        </tr>\n");
		
		file.write("	                        </thead>\n");
		file.write("	\n");
		file.write("	                        <tbody>\n");
		file.write("		                        <c:forEach items=\"${dataList }\" var=\"user\">\n");
		file.write("			                        <tr role=\"row\" class=\"odd\">\n");
		file.write("			                            <td class=\"center\">\n");
		file.write("			                                <label class=\"pos-rel\">\n");
		file.write("			                                    <input type=\"checkbox\" name=\"checkbox\" value=\"${user.id}\" class=\"ace\">\n");
		file.write("			                                    <span class=\"lbl\"></span>\n");
		file.write("			                                </label>\n");
		file.write("			                            </td>\n");
		file.write("			\n");
		i = 0;
		for (Column col : list) {
			String ss = "class=\"hidden-480\"";
			if(col.getCode().toLowerCase().equals("id")){
				file.write("			                            <td "+(i>=maxShowNum?ss:"")+"><a href=\"<%=basePath%>/cms/"+table.getClassNameField()+"/ace/edit?id=${user.id}\">${user.id}</a> </td>\n");
			}else if(col.getCode().toLowerCase().equals("status")){
				file.write("			                            <td "+(i>=maxShowNum?ss:"")+">\n");
				file.write("			                            	<c:if test=\"${user.status eq '0'}\">禁用</c:if>\n");
				file.write("			                            	<c:if test=\"${user.status eq '1'}\">启用</c:if>\n");
				file.write("			                            </td>");
			}else if(col.getDatatype().toLowerCase().indexOf("date")!=-1){
				file.write("			                            <td "+(i>=maxShowNum?ss:"")+"><fmt:formatDate value=\"${user."+col.getCode()+"}\" pattern=\"yyyy-MM-dd HH:mm:ss\" /></td>\n");
			}else{
				file.write("			                            <td "+(i>=maxShowNum?ss:"")+">${user."+col.getCode()+"}</td>\n");
			}
			i++;
		} 
		file.write("			                            \n");
		file.write("			                            <td>\n");
		file.write("			                                <div>\n");
		file.write("			                                		<a class=\"green\" href=\"<%=basePath%>/cms/"+table.getClassNameField()+"/ace/edit?id=${user.id}\" attr=\"编辑"+table.getComment()+"信息\">\n");
		file.write("				                                        <i class=\"ace-icon fa fa-pencil bigger-130\"></i>\n");
		file.write("				                                    </a>\n");
		file.write("			                                		<a class=\"red\" onclick=\"del(${user.id})\">\n");
		file.write("				                                        <i class=\"ace-icon fa fa-trash-o bigger-130\"></i>\n");
		file.write("				                                    </a>\n");
		file.write("			                                </div>\n");
		file.write("			                            </td>\n");
		file.write("			                        </tr>\n");
		file.write("		                         </c:forEach>\n");
		file.write("	                        </tbody>\n");
		file.write("	                    </table>\n");
		file.write("	                    <div class=\"row\">\n");
		file.write("	                        <div class=\"col-xs-6\">\n");
		file.write("	                            <div class=\"dataTables_info\" id=\"dynamic-table_info\" role=\"status\" aria-live=\"polite\">\n");
		file.write("	                            	 第${nowBegin}-${nowEnd}条，共${count}条  \n");
		file.write("	                            	 <label>\n");
		file.write("			                            	<select name=\"rows\" aria-controls=\"dynamic-table\" class=\"form-control input-sm\">\n");
		file.write("				                                <option	<c:if test=\"${ rows eq 5}\">selected</c:if>  value=\"5\">5</option>\n");
		file.write("				                                <option	<c:if test=\"${ rows eq 10}\">selected</c:if>  value=\"10\">10</option>\n");
		file.write("				                                <option <c:if test=\"${ rows eq 20}\">selected</c:if>  value=\"25\">25</option>\n");
		file.write("				                                <option <c:if test=\"${ rows eq 50}\">selected</c:if>  value=\"50\">50</option>\n");
		file.write("				                                <option <c:if test=\"${ rows eq 100}\">selected</c:if> value=\"100\">100</option>\n");
		file.write("			                                </select>\n");
		file.write("			                          </label>\n");
		file.write("	                            </div>\n");
		file.write("	                        </div>\n");
		file.write("	                        <div class=\"col-xs-6\">\n");
		file.write("	                        	<div class=\"dataTables_paginate paging_simple_numbers\" id=\"dynamic-table_paginate\">\n");
		file.write("	                            	<ul class=\"pagination\" id=\"pagination\"></ul>\n");
		file.write("	                        	</div>\n");
		file.write("	                        </div>\n");
		file.write("	                    </div>\n");
		file.write("	                    <!-- table end -->\n");
		file.write("	                </div>\n");
		file.write("	            </div>\n");
		file.write("	        </div>\n");
		file.write("	    </div><!-- /.page-content -->\n");
		file.write("	</div><!-- /.main-container -->\n");
		file.write("\n");
		file.write("<!-- inline scripts related to this page -->\n");
		file.write("<script type=\"text/javascript\">\n");
		file.write("    jQuery(function($) { \n");
		file.write("        $(\"#check_all\").change(function () {\n");
		file.write("            if($(\"#check_all\").is(':checked')){\n");
		file.write("                $(\"input[name=checkbox]\").prop('checked', true);\n");
		file.write("            }else{\n");
		file.write("                $(\"input[name=checkbox]\").prop('checked', false);\n");
		file.write("            }\n");
		file.write("        });\n");
		file.write("        \n");
		file.write("        $(\"input[name=checkbox]\").change(function () {\n");
		file.write("            var isAll = true;\n");
		file.write("            $(\"input[name=checkbox]\").each(function () {\n");
		file.write("                if(!$(this).is(':checked')){\n");
		file.write("                    isAll = false;\n");
		file.write("                    $(\"#check_all\").prop('checked', false);\n");
		file.write("                    return;\n");
		file.write("                }\n");
		file.write("            });\n");
		file.write("            if(isAll){\n");
		file.write("                $(\"#check_all\").prop('checked', true);\n");
		file.write("            }\n");
		file.write("        });\n");
		file.write("        new page({pageMain:\"pagination\",nowPage:${page},count:${count},rows:${rows},\n");
		file.write("            url:\"\",params:\"?rows=${rows}&searchKey=${searchKey}&searchValue=${searchValue}\",pakey:\"page\"});\n");
		file.write("    });\n");
		file.write("    \n");
		file.write("    //搜索\n");
		file.write("    document.onkeydown = function (event) {\n");
		file.write("    	var e = event || window.event || arguments.callee.caller.arguments[0];\n");
		file.write("    	if (e && e.keyCode == 13) {\n");
		file.write("    		$(\"#"+table.getClassNameField()+"Form\").submit();\n");
		file.write("    	}\n");
		file.write("     };\n");
		file.write("     \n");
		file.write("	 //切换每页显示的书码\n");
		file.write("     $(\"select[name=rows]\").change(function () {\n");
		file.write("         $(\"#"+table.getClassNameField()+"Form\").submit();\n");
		file.write("     });\n");
		file.write("     \n");
		file.write("     \n");
		file.write("	 function sub(){\n");
		file.write("		 $(\"#"+table.getClassNameField()+"Form\").submit();\n");
		file.write("	 }\n");
		file.write("	 \n");
		file.write("	 function rese(){\n");
		file.write("		$(\"#searchValue\").val('');\n");
		file.write("	 }\n");
		file.write("     \n");
		file.write("     \n");
		file.write("    /**\n");
		file.write("     * 删除角色\n");
		file.write("     */\n");
		file.write("    function del(id) {\n");
		file.write("        bootbox.confirm({\n");
		file.write("                    message: \"是否删除?\",\n");
		file.write("                    buttons: {\n");
		file.write("                        confirm: {\n");
		file.write("                            label: \"删除\",\n");
		file.write("                            className: \"btn-sm btn-primary\",\n");
		file.write("                        },\n");
		file.write("                        cancel: {\n");
		file.write("                            label: \"取消\",\n");
		file.write("                            className: \"btn-sm\",\n");
		file.write("                        }\n");
		file.write("                    },\n");
		file.write("                    callback: function(result) {\n");
		file.write("                        if(result) {\n");
		file.write("                            $.ajax({\n");
		file.write("                                url: '<%=basePath%>cms/"+table.getClassNameField()+"/ace/delete?ids=' + id,\n");
		file.write("                                type: 'GET',\n");
		file.write("                                success: function (data) {\n");
		file.write("                                    if (data) {\n");
		file.write("                                        $.messager.alert(\"提示\", data.errMsg, \"error\");\n");
		file.write("                                        return;\n");
		file.write("                                    }else{\n");
		file.write("                                    	location.reload();\n");
		file.write("                                    }\n");
		file.write("                                }\n");
		file.write("                            });\n");
		file.write("                            \n");
		file.write("                        }\n");
		file.write("                    }\n");
		file.write("                }\n");
		file.write("        );\n");
		file.write("    }\n");
		file.write("    \n");
		file.write("    /**\n");
		file.write("     * 批量删除\n");
		file.write("     */\n");
		file.write("    function delSelected(){\n");
		file.write("        var id = \"\";\n");
		file.write("        $(\"input[name=checkbox]\").each(function () {\n");
		file.write("            if($(this).is(':checked')){\n");
		file.write("               id +=  $(this).val()+\",\";\n");
		file.write("            }\n");
		file.write("        });\n");
		file.write("        if(id != \"\"){\n");
		file.write("            del(id);\n");
		file.write("        }\n");
		file.write("    }\n");
		file.write("</script>\n");
		file.write("</form>\n");
		file.write("</body>\n");
		file.write("</html>\n");
		file.write("\n");

		file.closeWriter();
	}
	
	public void generateAceAction(Table table, PathConfig path) {
		ClassFile file = new ClassFile(path.getAceActionPath(), "Ace"+table.getClassName() + "Controller.java");
		file.write("package com.gochinatv.cms.controller.ace;\n");
		file.write("\n");
		file.write("import javax.servlet.http.HttpServletRequest;\n");
		file.write("\n");
		file.write("import org.apache.commons.lang.StringUtils;\n");
		file.write("import org.springframework.beans.factory.annotation.Autowired;\n");
		file.write("import org.springframework.stereotype.Controller;\n");
		file.write("import org.springframework.web.bind.annotation.RequestMapping;\n");
		file.write("import org.springframework.web.bind.annotation.RequestMethod;\n");
		file.write("import org.springframework.web.bind.annotation.ResponseBody;\n");
		file.write("import org.springframework.web.servlet.ModelAndView;\n");
		file.write("\n");
		file.write("import com.gochinatv.cms.po."+table.getBeanName()+";\n");
		file.write("import com.gochinatv.cms.service."+table.getBeanName()+"Service;\n");
		file.write("import com.gochinatv.exception.BusinessException;\n");
		file.write("import com.gochinatv.framework.annotation.UserRightAnnotation;\n");
		file.write("import com.gochinatv.framework.controller.BaseAceController;\n");
		file.write("import com.gochinatv.framework.po.UserActionLog;\n");
		file.write("import com.gochinatv.framework.service.UserActionLogService;\n");
		file.write("import com.gochinatv.util.ChangeRelection;\n");
		file.write("import com.gochinatv.util.FastJsonUtil;\n");
		file.write("import com.gochinatv.util.InputCheckUtil;\n");
		file.write("import com.gochinatv.vo.PageInfo;\n");
		file.write("import com.gochinatv.vo.PageResult;\n");
		file.write("import java.lang.reflect.Field;\n");
		file.write("\n");
		file.write("/**\n");
		file.write(" * @author 格调先生\n");
		file.write(" */\n");
		file.write("@Controller\n");
		file.write("@RequestMapping(value = \"/cms/"+table.getClassNameField()+"/ace/*\")\n");
		file.write("public class Ace"+table.getClassName() + "Controller extends BaseAceController {\n");
		file.write("	@Autowired\n");
		file.write("	private "+table.getBeanName()+"Service "+table.getClassNameField()+"Service;\n");
		file.write("	@Autowired\n");
		file.write("	private UserActionLogService userActionLogService;\n");
		file.write("\n");
		file.write("	/**\n");
		file.write("	 * 打开首页\n");
		file.write("	 * \n");
		file.write("	 * @return\n");
		file.write("	 */\n");
		file.write("	@UserRightAnnotation(menuId = \"cus_"+table.getClassNameField()+"\", functionId = \"open\")\n");
		file.write("	@RequestMapping(value = \"/open\", method = RequestMethod.GET)\n");
		file.write("	public ModelAndView open(HttpServletRequest request, "+table.getBeanName()+" bean, PageInfo pageInfo,String searchKey ,String searchValue) {\n");
		file.write("		ModelAndView modelView = new ModelAndView(\"ace/"+table.getClassNameField()+"List\");\n");
		file.write("\n");
		file.write("		\n");
		file.write("		Field[] fs = bean.getClass().getDeclaredFields();\n");
		file.write("		if(StringUtils.isNotBlank(searchValue) && StringUtils.isNotBlank(searchKey)){\n");
		file.write("			for(Field f:fs){\n");
		file.write("				f.setAccessible(true);\n");
		file.write("				if(f.getName().equals(searchKey)){\n");
		file.write("					try {\n");
		file.write("						if (f.getType().equals(String.class)) {\n");
		file.write("							f.set(bean, searchValue);\n");
		file.write("						}else if (f.getType().equals(int.class) || f.getType().equals(Integer.class) ) {\n");
		file.write("							f.set(bean, new Integer(searchValue));\n");
		file.write("						}\n");
		file.write("					} catch (Exception e) {\n");
		file.write("						e.printStackTrace();\n");
		file.write("					}\n");
		file.write("				}\n");
		file.write("			}\n");
		file.write("		}\n");
		file.write("		request.setAttribute(\"searchKey\", searchKey);\n");
		file.write("		request.setAttribute(\"searchValue\", searchValue);\n");
		file.write("		\n");
		file.write("		\n");
		file.write("		bean.setIsLike(\"1\");\n");
		file.write("		PageResult pageResult = "+table.getClassNameField()+"Service.search4Page(false, bean, pageInfo);\n");
		file.write("\n");
		file.write("		long count = pageResult.getTotal();\n");
		file.write("		long maxPage = pageResult.getTotal() % pageInfo.getRows() == 0 ? pageResult.getTotal() / pageInfo.getRows() : pageResult.getTotal() % pageInfo.getRows() + 1;\n");
		file.write("		int page = pageInfo.getPage();\n");
		file.write("\n");
		file.write("		request.setAttribute(\"dataList\", pageResult.getRows());\n");
		file.write("		request.setAttribute(\"count\", count);\n");
		file.write("		request.setAttribute(\"maxPage\", maxPage);\n");
		file.write("		request.setAttribute(\"page\", page);\n");
		file.write("		request.setAttribute(\"rows\", pageInfo.getRows());\n");
		file.write("		request.setAttribute(\"nowBegin\", pageInfo.getRows() * (page - 1) + 1);\n");
		file.write("		request.setAttribute(\"nowEnd\", pageInfo.getRows() * (page - 1) + pageResult.getRows().size());\n");
		file.write("\n");
		file.write("		// 返回查询条件\n");
		file.write("		request.setAttribute(\"bean\", bean);\n");
		file.write("\n");
		file.write("		return modelView;\n");
		file.write("	}\n");
		file.write("\n");
		file.write("	/**\n");
		file.write("	 * 跳转到修改页面\n");
		file.write("	 * \n");
		file.write("	 * @return\n");
		file.write("	 */\n");
		file.write("	@UserRightAnnotation(menuId = \"cus_"+table.getClassNameField()+"\", functionId = \"open\")\n");
		file.write("	@RequestMapping(value = \"/edit\", method = RequestMethod.GET)\n");
		file.write("	public ModelAndView edit(HttpServletRequest request,String id) {\n");
		file.write("		ModelAndView modelView = new ModelAndView(\"ace/" +table.getClassNameField()+ "Edit\");\n");
		file.write("		if(StringUtils.isNotBlank(id)){\n");
		file.write("			"+table.getBeanName()+" bean = "+table.getClassNameField()+"Service.getById(Long.valueOf(id));\n");
		file.write("			request.setAttribute(\"bean\", bean);\n");
		file.write("		}\n");
		file.write("\n");
		file.write("		return modelView;\n");
		file.write("	}\n");
		file.write("	\n");
		file.write("	/**\n");
		file.write("	 * 添加\n");
		file.write("	 * \n");
		file.write("	 * @param bean\n");
		file.write("	 * @param request\n");
		file.write("	 * @throws BusinessException\n");
		file.write("	 */\n");
		file.write("	@UserRightAnnotation(menuId = \"cus_"+table.getClassNameField()+"\", functionId = \"saveOrUpdate\")\n");
		file.write("	@RequestMapping(value = \"/saveOrUpdate\", method = RequestMethod.POST)\n");
		file.write("	@ResponseBody\n");
		file.write("	public void saveOrUpdate("+table.getBeanName()+" bean, HttpServletRequest request) throws BusinessException {\n");
		file.write("		String userId = super.getLoginUserId(request);\n");
		file.write("		String userIp = super.getClientIp(request);\n");
		file.write("		if (bean.getId() == null || StringUtils.isBlank(bean.getId() + \"\") || bean.getId() == 0) {\n");
		file.write("			UserActionLog userActionLog = new UserActionLog(userId, userIp, \"60010301\", FastJsonUtil.obj2jsonDateFormat(bean));\n");
		file.write("			userActionLogService.insert(userActionLog);\n");
		file.write("			"+table.getClassNameField()+"Service.insert(bean);\n");
		file.write("		} else {\n");
		file.write("			try {\n");
		file.write("				"+table.getBeanName()+" oldData = "+table.getClassNameField()+"Service.getById(Long.valueOf(bean.getId()));\n");
		file.write("				UserActionLog userActionLog = new UserActionLog(userId, userIp, \"60010302\", FastJsonUtil.obj2jsonDateFormat(oldData));\n");
		file.write("				userActionLogService.insert(userActionLog);\n");
		file.write("				ChangeRelection.changeObj(oldData, bean);\n");
		file.write("			} catch (Exception e) {\n");
		file.write("				e.printStackTrace();\n");
		file.write("			}\n");
		file.write("			"+table.getClassNameField()+"Service.update(bean);\n");
		file.write("		}\n");
		file.write("	}\n");
		file.write("\n");
		file.write("	/**\n");
		file.write("	 * 根据 id 删除\n");
		file.write("	 * \n");
		file.write("	 * @param id\n");
		file.write("	 * @param request\n");
		file.write("	 * @throws BusinessException\n");
		file.write("	 */\n");
		file.write("	@UserRightAnnotation(menuId = \"cus_"+table.getClassNameField()+"\", functionId = \"deleteById\")\n");
		file.write("	@RequestMapping(value = \"/delete\", method = RequestMethod.GET)\n");
		file.write("	@ResponseBody\n");
		file.write("	public void deleteById(HttpServletRequest request, String ids) throws BusinessException {\n");
		file.write("		InputCheckUtil.checkNotEmpty(String.valueOf(ids), \"ids\");\n");
		file.write("		String userId = super.getLoginUserId(request);\n");
		file.write("		String userIp = super.getClientIp(request);\n");
		file.write("		for (String id : ids.split(\",\")) {\n");
		file.write("			"+table.getBeanName()+" oldData = "+table.getClassNameField()+"Service.getById(Long.valueOf(id));\n");
		file.write("			UserActionLog userActionLog = new UserActionLog(userId, userIp, \"60010303\", FastJsonUtil.obj2jsonDateFormat(oldData));\n");
		file.write("			userActionLogService.insert(userActionLog);\n");
		file.write("			"+table.getClassNameField()+"Service.delete(Long.valueOf(id));\n");
		file.write("		}\n");
		file.write("	}\n");
		file.write("}\n");

		file.closeWriter();


		
	}
}