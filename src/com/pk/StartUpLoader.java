/*
 * Created on Mar 14, 2004
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package com.pk;

import java.io.File;
import java.io.FilenameFilter;
import java.net.URL;
import java.net.URLClassLoader;

/**
 * @author Isabelle
 */
public class StartUpLoader {

	public static void main(String[] args) {
		File libDir = new File("./lib/");
		File currLib = new File("./");
		URLClassLoader loader = null; //initialized in trys
		StringBuffer classpath = new StringBuffer();
		try {
			String classPathSeparator = System.getProperty("path.separator");
			File[] libJars = libDir.listFiles(new FilenameFilter() {
				public boolean accept(File dir, String name) {
					boolean acceptFile = false;
					if (name.endsWith(".jar") || name.endsWith(".zip")) {
						acceptFile = true;
					}
					return acceptFile;
				}
			});
			URL tmp[] = new URL[libJars.length + 1];
			String origPath = null;
			boolean usesUNC = System.getProperty("os.name").startsWith(
					"Windows");
			for (int i = 0; i < libJars.length + 1; i++) {

				if (i < libJars.length) {
					origPath = libJars[i].getAbsolutePath();
				} else {
					origPath = currLib.getAbsolutePath();
				}
				// allow UNC URLs

				if (usesUNC) {
					if (origPath.startsWith("\\\\")
							&& !origPath.startsWith("\\\\\\")) {
						origPath = "\\\\" + origPath;
					} else if (origPath.startsWith("//")
							&& !origPath.startsWith("///")) {
						origPath = "//" + origPath;
					}
				}
				tmp[i] = new URL("file:" + origPath);
				classpath.append(origPath);
			}
			tmp[tmp.length - 1] = new URL("file:/" + origPath + "/");
			System.setProperty("java.class.path",
					System.getProperty("java.class.path") + classPathSeparator
							+ classpath.toString());
			loader = new URLClassLoader(tmp, null);
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(3);
		}

		Thread.currentThread().setContextClassLoader(loader);
		try {
			new PrettyKid();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
