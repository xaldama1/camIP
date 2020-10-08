package org.xas.uned.camip.ssl.conn;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.ProtocolException;
import java.net.URL;

import org.xas.uned.camip.ssl.CustomHttpConnection;

public class CustomHttpConnectionImpl<C extends HttpURLConnection> extends CustomURLConnectionImpl<C>
		implements CustomHttpConnection {

	public CustomHttpConnectionImpl(final URL url, final C con) {
		super(url, con);
	}

	@Override
	public void disconnect() {
		con.disconnect();
	}

	@Override
	public void setRequestMethod(final String method) throws ProtocolException {
		con.setRequestMethod(method);
	}

	@Override
	public void setInstanceFollowRedirects(boolean followRedirects) {
		con.setInstanceFollowRedirects(followRedirects);
	}

	@Override
	public int getResponseCode() throws IOException {
		return con.getResponseCode();
	}

	@Override
	public String getResponseMessage() throws IOException {
		return con.getResponseMessage();
	}

	@Override
	public long getLastModified() {
		return con.getLastModified();
	}
}