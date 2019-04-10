/**
 * @author Nathan Jones
 */
package net;

import java.util.Iterator;
import java.util.List;

public class Library {
	public static final String key = "sadf789/*-";

	public static String serialize(List<String> aList) {
		if (aList == null) {
			return "N;";
		}
		StringBuffer buf = new StringBuffer();
		buf.append("a:").append(aList.size()).append(":{");
		int offset = 0;
		for (Iterator<String> it = aList.iterator(); it.hasNext();) {
			buf.append(serialize(new Integer(offset++)));
			String value = (String) it.next();
			buf.append(serialize(value));
		}
		buf.append("};");
		return buf.toString();
	}

	public static String serialize(Integer javaInt) {
		if (javaInt == null) {
			return "N;";
		}
		return "i:" + javaInt.toString() + ";";
	}

	public static String serialize(String javaString) {
		if (javaString == null) {
			return "N;";
		}
		return "s:" + javaString.length() + ":\"" + javaString + "\";";
	}

}
