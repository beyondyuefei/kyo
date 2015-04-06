package com.ruijie.framework.kyo;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.ruijie.framework.kyo.common.utils.JavassistCompiler;

public class Test implements java.io.Serializable {
	private static final long serialVersionUID = -4198586076035674490L;
	private Integer a;

	public Test(Integer a) {
		this.a = a;
	}

	public Integer getA() {
		return a;
	}

	public void setA(Integer a) {
		this.a = a;
	}

	public static void main(String[] args) throws Exception {
		String source = "package com.ruijie.framework.kyo.session.serialization;"
				+ "\nimport com.ruijie.framework.kyo.common.extension.Extensions;"
				+ "\npublic class Serialization$Adapative implements com.ruijie.framework.kyo.session.serialization.Serialization{\t\n"
				+ "public javax.servlet.http.HttpSession deserialize(byte[] arg0) {\t\n"
				+ "String extName = \"java\";\t\n"
				+ "com.ruijie.framework.kyo.session.serialization.Serialization extension = Extensions.getExtensionLoader(com.ruijie.framework.kyo.session.serialization.Serialization.class).getExtension(extName);\t\n"
				+ "return extension.deserialize(arg0); }\t\n"
				+ "public byte[] serialize(javax.servlet.http.HttpSession arg0) {\t\n"
				+ "String extName = \"java\";\t\n"
				+ "com.ruijie.framework.kyo.session.serialization.Serialization extension = Extensions.getExtensionLoader(com.ruijie.framework.kyo.session.serialization.Serialization.class).getExtension(extName);\t\n"
				+ "return extension.serialize(arg0); }\t\n"
				+"}";
		
		Pattern IMPORT_PATTERN = Pattern.compile("import\\s+([\\w\\.\\*]+);\n");

		Matcher matcher = IMPORT_PATTERN.matcher(source);
        List<String> importPackages = new ArrayList<String>();
        Map<String, String> fullNames = new HashMap<String, String>();
        while (matcher.find()) {
            String pkg = matcher.group(1);
            if (pkg.endsWith(".*")) {
                String pkgName = pkg.substring(0, pkg.length() - 2);
                importPackages.add(pkgName);
                System.out.println("11  nme:" + pkgName);
            } else {
                int pi = pkg.lastIndexOf('.');
                if (pi > 0) {
	                String pkgName = pkg.substring(0, pi);
	                importPackages.add(pkgName);
	                System.out.println("22 nme:" + pkgName);
	                fullNames.put(pkg.substring(pi + 1), pkg);
                }
            }
            
        }
	}

}
