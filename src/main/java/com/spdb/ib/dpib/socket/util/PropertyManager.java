package com.spdb.ib.dpib.socket.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Enumeration;
import java.util.Properties;

public class PropertyManager {

	private static final int MAJOR_VERSION = 2;

	/**
	 * The Minor version number of i.e. x.1.x.
	 */
	private static final int MINOR_VERSION = 0;

	/**
	 * The revision version number of i.e. x.x.1.
	 */
	private static final int REVISION_VERSION = 0;

	private static PropertyManager manager = null;

	private static final Object managerLock = new Object();

	// default property file name "config.propertie"
	private static String propsName = "config.properties";
	private static Properties confiProperties;
	/**
	 * 
	 * reset propsName value
	 * 
	 */
	public static void setPropsName(final String newPropsName) {
		propsName = newPropsName;
	}

	/**
	 * Returns a property.
	 * 
	 * @param name
	 *            the name of the property to return.
	 * @return the property value specified by name.
	 */
	public static String getProperty(final String name) {
		if (manager == null) {
			synchronized (managerLock) {
				if (manager == null) {
					manager = new PropertyManager(propsName);
				}
			}
		}
		return manager.getProp(name);
	}

	/**
	 * 获取配置文件下特定路径
	 */
	public static String getPath(String property) {
		if (confiProperties == null) {
			synchronized (PropertyManager.class) {
				if (confiProperties == null) {
					confiProperties = new Properties();
					InputStream is = null;
					try {
						is=Thread.currentThread().getContextClassLoader().getResourceAsStream("config.properties");
						confiProperties.load(is);
					} catch (IOException e) {
					} finally {
						if (is != null) {
							try {
								is.close();
							} catch (IOException e1) {
								e1.printStackTrace();
							}
						}
					}
				}
			}
		}
		return confiProperties.getProperty(property);
	}

	
	public static String clearConfiProperties() {
//		LcDebug.logError("-------清除 confiProperties 配置缓存开始！--------");
		try {
//			LcDebug.logError("------- 清除前confiProperties confiProperties.isEmpty()="+ confiProperties.isEmpty());
			PropertyManager.confiProperties.clear();
//			LcDebug.logError("-------清除 confiProperties 配置缓存开始！--------");
//			LcDebug.logError("------- 清除后confiProperties confiProperties.isEmpty()="+ confiProperties.isEmpty());
		} catch (Exception e) {
//			LcDebug.logError("清除 confiProperties 配置缓存遇到问题：" + e);
			e.printStackTrace();
		}
//		LcDebug.logError("-------清除 confiProperties 配置缓存结束！--------");
		return "清除 confiProperties 配置缓存成功！";
	}
	/**
	 * Sets a enet property. If the property doesn't already exists, a new one
	 * will be created.
	 * 
	 * @param name
	 *            the name of the property being set.
	 * @param value
	 *            the value of the property being set.
	 */
	public static void setProperty(final String name, final String value) {
		if (manager == null) {
			synchronized (managerLock) {
				if (manager == null) {
					manager = new PropertyManager(propsName);
				}
			}
		}
		manager.setProp(name, value);
	}

	/**
	 * Deletes a enet property. If the property doesn't exist, the method does
	 * nothing.
	 * 
	 * @param name
	 *            the name of the property to delete.
	 */
	public static void deleteProperty(final String name) {
		if (manager == null) {
			synchronized (managerLock) {
				if (manager == null) {
					manager = new PropertyManager(propsName);
				}
			}
		}
		manager.deleteProp(name);
	}

	// new clear
	public static void clearProperty() {
		if (manager != null) {
			synchronized (managerLock) {
				if (manager != null) {
					manager = null;
				}
			}
		}
	}

	/**
	 * Returns the names of the enet properties.
	 * 
	 * @return an Enumeration of the enet property names.
	 */
	public static Enumeration propertyNames() {
		if (manager == null) {
			synchronized (managerLock) {
				if (manager == null) {
					manager = new PropertyManager(propsName);
				}
			}
		}
		return manager.propNames();
	}

	/**
	 * Returns true if the properties are readable. This method is mainly
	 * valuable at setup time to ensure that the properties file is setup
	 * correctly.
	 */
	public static boolean propertyFileIsReadable() {
		if (manager == null) {
			synchronized (managerLock) {
				if (manager == null) {
					manager = new PropertyManager(propsName);
				}
			}
		}
		return manager.propFileIsReadable();
	}

	/**
	 * Returns true if the properties are writable. This method is mainly
	 * valuable at setup time to ensure that the properties file is setup
	 * correctly.
	 */
	public static boolean propertyFileIsWritable() {
		if (manager == null) {
			synchronized (managerLock) {
				if (manager == null) {
					manager = new PropertyManager(propsName);
				}
			}
		}
		return manager.propFileIsWritable();
	}

	/**
	 * Returns true if the enet.properties file exists where the path property
	 * purports that it does.
	 */
	public static boolean propertyFileExists() {
		if (manager == null) {
			synchronized (managerLock) {
				if (manager == null) {
					manager = new PropertyManager(propsName);
				}
			}
		}
		return manager.propFileExists();
	}

	/**
	 * Returns the version number of enet as a String. i.e. --
	 * major.minor.revision
	 */
	public static String getIflowVersion() {
		return MAJOR_VERSION + "." + MINOR_VERSION + "." + REVISION_VERSION;
	}

	/**
	 * Returns the major version number of enet. i.e. -- 1.x.x
	 */
	public static int getIflowVersionMajor() {
		return MAJOR_VERSION;
	}

	/**
	 * Returns the minor version number of enet. i.e. -- x.1.x
	 */
	public static int getIflowVersionMinor() {
		return MINOR_VERSION;
	}

	/**
	 * Returns the revision version number of enet. i.e. -- x.x.1
	 */
	public static int getIflowVersionRevision() {
		return REVISION_VERSION;
	}

	private Properties properties = null;

	private final Object propertiesLock = new Object();

	private String resourceURI;

	/**
	 * Creates a new PropertyManager. Singleton access only.
	 */
	private PropertyManager(final String resourceURI) {
		this.resourceURI = resourceURI;
	}

	/**
	 * Gets a enet property. enet properties are stored in enet.properties. The
	 * properties file should be accesible from the classpath. Additionally, it
	 * should have a path field that gives the full path to where the file is
	 * located. Getting properties is a fast operation.
	 * 
	 * @param name
	 *            the name of the property to get.
	 * @return the property specified by name.
	 */
	protected String getProp(final String name) {
		// If properties aren't loaded yet. We also need to make this thread
		// safe, so synchronize...
		if (properties == null) {
			synchronized (propertiesLock) {
				// Need an additional check
				if (properties == null) {
					loadProps();
				}
			}
		}
		final String property = properties.getProperty(name);
		if (property == null) {
			return null;
		} else {
			return property.trim();
		}
	}

	/**
	 * Sets a enet property. Because the properties must be saved to disk every
	 * time a property is set, property setting is relatively slow.
	 */
	protected void setProp(final String name, final String value) {
		// Only one thread should be writing to the file system at once.
		synchronized (propertiesLock) {
			// Create the properties object if necessary.
			if (properties == null) {
				loadProps();
			}
			properties.setProperty(name, value);
			saveProps();
		}
	}

	protected void deleteProp(final String name) {
		// Only one thread should be writing to the file system at once.
		synchronized (propertiesLock) {
			// Create the properties object if necessary.
			if (properties == null) {
				loadProps();
			}
			properties.remove(name);
			saveProps();
		}
	}

	protected Enumeration propNames() {
		// If properties aren't loaded yet. We also need to make this thread
		// safe, so synchronize...
		if (properties == null) {
			synchronized (propertiesLock) {
				// Need an additional check
				if (properties == null) {
					loadProps();
				}
			}
		}
		return properties.propertyNames();
	}

	/**
	 * Loads enet properties from the disk.
	 */
	private void loadProps() {
		properties = new Properties();
		InputStream in = null;
		// System.out.println(resourceURI);
		try {
			in = getClass().getResourceAsStream(resourceURI);
			properties.load(in);
		} catch (final Exception e) {
			System.err
					.println("Error reading properties in PropertyManager.loadProps() "
							+ e);
			e.printStackTrace();
		} finally {
			try {
				in.close();
			} catch (final Exception e) {
			}
		}
	}

	/**
	 * Saves enet properties to disk.
	 */
	private void saveProps() {
		// Now, save the properties to disk. In order for this to work, the user
		// needs to have set the path field in the properties file. Trim
		// the String to make sure there are no extra spaces.
		final String path = properties.getProperty("path").trim();
		OutputStream out = null;
		try {
			out = new FileOutputStream(path);
			properties.store(out, "properties -- " + (new java.util.Date()));
		} catch (final Exception ioe) {
			System.err
					.println("There was an error writing enet.properties to "
							+ path
							+ ". "
							+ "Ensure that the path exists and that the enet process has permission "
							+ "to write to it -- " + ioe);
			ioe.printStackTrace();
		} finally {
			try {
				out.close();
			} catch (final Exception e) {
			}
		}
	}

	/**
	 * Returns true if the properties are readable. This method is mainly
	 * valuable at setup time to ensure that the properties file is setup
	 * correctly.
	 */
	public boolean propFileIsReadable() {
		try {
			final InputStream in = getClass().getResourceAsStream(resourceURI);
			return true;
		} catch (final Exception e) {
			return false;
		}
	}

	/**
	 * Returns true if the enet.properties file exists where the path property
	 * purports that it does.
	 */
	public boolean propFileExists() {
		final String path = getProp("path");
		if (path == null) {
			return false;
		}
		final File file = new File(path);
		if (file.isFile()) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * Returns true if the properties are writable. This method is mainly
	 * valuable at setup time to ensure that the properties file is setup
	 * correctly.
	 */
	public boolean propFileIsWritable() {
		final String path = getProp("path");
		final File file = new File(path);
		if (file.isFile()) {
			// See if we can write to the file
			if (file.canWrite()) {
				return true;
			} else {
				return false;
			}
		} else {
			return false;
		}
	}
	
//	public static void main(String[] args){
//		System.out.println(PropertyManager.getPath("serverPt"));
//	}
}
