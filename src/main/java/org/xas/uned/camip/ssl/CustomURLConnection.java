package org.xas.uned.camip.ssl;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;

public interface CustomURLConnection {

	<T> T unwrap(Class<T> target);

	void setRequestProperty(String key, String value);

	void addRequestProperty(String key, String value);

	InputStream getInputStream() throws IOException;

	OutputStream getOutputStream() throws IOException;

	void setConnectTimeout(int timeout);

	int getConnectTimeout();

	void setReadTimeout(int timeout);

	URL getURL();

	void setDoOutput(boolean dooutput);

	void setDoInput(boolean doinput);

	String getHeaderField(String field);

	String getContentEncoding();

	void connect() throws IOException;

	InputStream getErrorStream();
}
