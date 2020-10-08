package org.xas.uned.camip.ssl;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.Socket;
import java.net.URL;
import java.net.URLConnection;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLEngine;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509ExtendedTrustManager;
import javax.net.ssl.X509TrustManager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xas.uned.camip.ssl.conn.CustomHttpConnectionImpl;
import org.xas.uned.camip.ssl.conn.CustomURLConnectionImpl;

public enum SSLManager {

	INSTANCE;

	private static final Logger LOGGER = LoggerFactory.getLogger(SSLManager.class);

	private static final List<String> PREFERRED_PROTOCOLS = Arrays.asList("TLSv1.3", "TLSv1.2", "TLSv1.1", "TLSv1");

	public CustomURLConnection opentrustedConnection(final String url) throws IOException {
		return openTrustedConnection(new URL(url));
	}

	public CustomURLConnection openTrustedConnection(final URL url) throws IOException {
		OpenConnection impl = defaultOpenConnection();

		try {
			return impl.openConnection(url);
		} catch (Throwable e) {
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug("Error handling connection to " + url, e);
			}
			return null;
		}
	}

	private OpenConnection defaultOpenConnection() {
		return new OpenConnection() {
			public CustomURLConnection openConnection(final URL url) throws IOException {
				URLConnection conn = defaultOpenConnection(url);
				if (conn instanceof HttpsURLConnection) {
					HttpsURLConnection httpsConnection = (HttpsURLConnection) conn;
					httpsConnection.setSSLSocketFactory(wrap(httpsConnection.getSSLSocketFactory()));
					// httpsConnection.setHostnameVerifier(wrap(httpsConnection.getHostnameVerifier()));
				}
				if (conn instanceof HttpURLConnection) {
					return new CustomHttpConnectionImpl<HttpURLConnection>(url, (HttpURLConnection) conn);
				} else {
					return new CustomURLConnectionImpl<URLConnection>(url, conn);
				}
			}

			private URLConnection defaultOpenConnection(final URL url) throws IOException {
				return url.openConnection();
			}
		};
	}

	protected SSLSocketFactory createTrustedSSLSocketFactory() {
		return createSSLSocketFactory(createTrustedManager());
	}

	protected SSLSocketFactory createSSLSocketFactory(final TrustManager trustManager) {
		List<String> protocols = Arrays.asList("TLS", "SSL");
		for (String protocol : protocols) {
			CreateSSLContext impl = defaultCreateSSSLContext();
			SSLContext context = impl.createSSLContext(protocol, trustManager);
			if (context != null) {
				return context.getSocketFactory();
			}
		}
		throw new IllegalStateException("No provider found for any of the supported protocols " + protocols);
	}

	private CreateSSLContext defaultCreateSSSLContext() {
		return new CreateSSLContext() {
			public SSLContext createSSLContext(final String protocol, final TrustManager manager) {
				try {
					SSLContext context = SSLContext.getInstance(protocol);
					TrustManager[] trustManagers = null;
					if (manager != null) {
						trustManagers = new TrustManager[] { manager };
					}
					context.init(null, trustManagers, null);
					return context;
				} catch (NoSuchAlgorithmException e) {
					return null;
				} catch (Exception e) {
					throw new RuntimeException("Can't initialize the SSLContext", e);
				}
			}
		};
	}

	public SSLSocketListener createSocketListener() {
		return defaultSocketListener();
	}

	private SSLSocketListener defaultSocketListener() {
		return new SSLSocketListener() {
			@Override
			public void onConnect(final String hostName, final SSLSocket socket) {
				Set<String> supportedProtocols = new HashSet<String>(Arrays.asList(socket.getSupportedProtocols()));
				List<String> enabledProtocols = new LinkedList<String>();
				for (String protocol : SSLManager.PREFERRED_PROTOCOLS) {
					if (supportedProtocols.contains(protocol)) {
						enabledProtocols.add(protocol);
					}
				}
				if (!enabledProtocols.isEmpty()) {
					socket.setEnabledProtocols(enabledProtocols.toArray(new String[0]));
				}
			}

			@Override
			public void onClose(final SSLSocket socket) {

			}
		};
	}

	protected static TrustManager createTrustedManager() {
		TrustManager trustedManager;
		try {
			trustedManager = new TrustedExtendedTrustManager();
		} catch (Throwable e) {
			trustedManager = new TrustedTrustManager();
		}
		return trustedManager;
	}

	protected static SSLSocketFactory wrap(final SSLSocketFactory socketFactory) {
		if (socketFactory == null) {
			return new CustomSSLSocketFactory();
		} else if (socketFactory instanceof CustomSSLSocketFactory) {
			return socketFactory;
		} else {
			return new CustomSSLSocketFactory();
		}
	}

	protected interface OpenConnection {
		CustomURLConnection openConnection(final URL url) throws IOException;
	}

	protected interface CreateSSLContext {
		SSLContext createSSLContext(final String protocol, final TrustManager manager);
	}

	protected static class TrustedTrustManager implements X509TrustManager {
		public void checkClientTrusted(final X509Certificate[] x509Certificates, final String s)
				throws CertificateException {

		}

		public void checkServerTrusted(final X509Certificate[] x509Certificates, final String s)
				throws CertificateException {

		}

		public X509Certificate[] getAcceptedIssuers() {
			return null;
		}
	}

	protected static class TrustedExtendedTrustManager extends X509ExtendedTrustManager {

		public void checkClientTrusted(final X509Certificate[] x509Certificates, final String s, final Socket socket)
				throws CertificateException {

		}

		public void checkServerTrusted(final X509Certificate[] x509Certificates, final String s, final Socket socket)
				throws CertificateException {

		}

		public void checkClientTrusted(final X509Certificate[] x509Certificates, final String s,
				final SSLEngine sslEngine) throws CertificateException {

		}

		public void checkServerTrusted(final X509Certificate[] x509Certificates, final String s,
				final SSLEngine sslEngine) throws CertificateException {

		}

		public void checkClientTrusted(final X509Certificate[] x509Certificates, final String s)
				throws CertificateException {

		}

		public void checkServerTrusted(final X509Certificate[] x509Certificates, final String s)
				throws CertificateException {

		}

		public X509Certificate[] getAcceptedIssuers() {
			return null;
		}
	}
}
