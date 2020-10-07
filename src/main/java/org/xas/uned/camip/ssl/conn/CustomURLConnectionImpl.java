package org.xas.uned.camip.ssl.conn;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xas.uned.camip.ssl.CustomURLConnection;

public class CustomURLConnectionImpl<C extends URLConnection> implements CustomURLConnection {

	private static final Logger LOGGER = LoggerFactory.getLogger(CustomURLConnectionImpl.class);

	protected final URL url;

	protected C con;

	public CustomURLConnectionImpl(final URL url, final C con) {
		this.con = con;
		this.url = url;
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> T unwrap(final Class<T> target) {
		if (target.isInstance(this)) {
			return (T) this;
		}
		if (target.isInstance(con)) {
			return (T) con;
		}
		throw new IllegalArgumentException(
				"This connection " + getClass() + " wrapping " + con.getClass() + " is not assignable to " + target);
	}

	public InputStream getErrorStream() {
		if (con instanceof HttpURLConnection) {
			return ((HttpURLConnection) con).getErrorStream();
		}
		return null;
	}

	@Override
	public void setConnectTimeout(final int timeout) {
		con.setConnectTimeout(timeout);
	}

	@Override
	public void setReadTimeout(final int timeout) {
		con.setReadTimeout(timeout);
	}

	@Override
	public void setDoOutput(final boolean dooutput) {
		con.setDoOutput(dooutput);
	}

	@Override
	public void setDoInput(final boolean doinput) {
		con.setDoInput(doinput);
	}

	@Override
	public void setRequestProperty(final String key, final String value) {
		con.setRequestProperty(key, value);
	}

	@Override
	public void addRequestProperty(final String key, final String value) {
		con.addRequestProperty(key, value);
	}

	@Override
	public URL getURL() {
		return con.getURL();
	}

	@Override
	public InputStream getInputStream() throws IOException {
		return con.getInputStream();
	}

	@Override
	public OutputStream getOutputStream() throws IOException {
		return con.getOutputStream();
	}

	@Override
	public int getConnectTimeout() {
		return con.getConnectTimeout();
	}

	@Override
	public String getHeaderField(final String field) {
		return con.getHeaderField(field);
	}

	@Override
	public String getContentEncoding() {
		return con.getContentEncoding();
	}

	@Override
	public void connect() throws IOException {
		try {
			con.connect();
		} catch (IOException e) {
			LOGGER.error("Error connecting to " + con.getURL(), e);
		}
	}
}
