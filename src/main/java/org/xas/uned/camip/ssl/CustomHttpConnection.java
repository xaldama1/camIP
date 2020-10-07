package org.xas.uned.camip.ssl;

import java.io.IOException;
import java.net.ProtocolException;

public interface CustomHttpConnection extends CustomURLConnection {

	void setRequestMethod(String method) throws ProtocolException;

	int getResponseCode() throws IOException;

	String getResponseMessage() throws IOException;

	void disconnect();

	void setInstanceFollowRedirects(boolean followRedirects);

	long getLastModified();
}