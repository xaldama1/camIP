package org.xas.uned.camip.ssl;

import java.io.IOException;
import java.io.InputStream;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.UnknownHostException;

import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CustomSSLSocketFactory extends SSLSocketFactory {

	private static final Logger LOGGER = LoggerFactory.getLogger(CustomSSLSocketFactory.class);

	private final SSLSocketFactory trustedFactory;

	public CustomSSLSocketFactory() {
		this.trustedFactory = SSLManager.INSTANCE.createTrustedSSLSocketFactory();
	}

	public String[] getDefaultCipherSuites() {
		return trustedFactory.getDefaultCipherSuites();
	}

	public String[] getSupportedCipherSuites() {
		return trustedFactory.getSupportedCipherSuites();
	}

	@Override
	public Socket createSocket(final Socket socket, final InputStream inputStream, final boolean autoClose)
			throws IOException {
		InetAddress inetAddress = socket.getInetAddress();
		if (inetAddress == null) {
			Socket trustedSocket = trustedFactory.createSocket(socket, inputStream, autoClose);
			return new CustomUnconnectedSSLSocket(trustedSocket);
		} else {
			SSLSocket result = (SSLSocket) getFactory(socket.getInetAddress()).createSocket(socket, inputStream,
					autoClose);
			return new CustomSSLSocket(result);
		}
	}

	public Socket createSocket(final Socket socket, final String host, int port, boolean autoClose) throws IOException {
		SSLSocket result = (SSLSocket) getFactory(host).createSocket(socket, host, port, autoClose);
		return new CustomSSLSocket(result);
	}

	public Socket createSocket(final String host, final int port) throws IOException, UnknownHostException {
		SSLSocket result = (SSLSocket) getFactory(host).createSocket(host, port);
		return new CustomSSLSocket(result);
	}

	public Socket createSocket(final String host, final int port, final InetAddress localAddress, final int localPort)
			throws IOException, UnknownHostException {
		SSLSocket result = (SSLSocket) getFactory(host).createSocket(host, port, localAddress, localPort);
		return new CustomSSLSocket(result);
	}

	public Socket createSocket(final InetAddress host, final int port) throws IOException {
		SSLSocket result = (SSLSocket) getFactory(host).createSocket(host, port);
		return new CustomSSLSocket(result);
	}

	public Socket createSocket(final InetAddress address, final int port, final InetAddress localHost,
			final int localPort) throws IOException {
		SSLSocket result = (SSLSocket) getFactory(address).createSocket(address, port, localHost, localPort);
		return new CustomSSLSocket(result);
	}

	@Override
	public Socket createSocket() throws IOException {
		return new CustomUnconnectedSSLSocket(trustedFactory.createSocket());
	}

	public boolean isTrusted(final SSLAddress address) {
		boolean trusted = true;
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Is {} trusted? {}", address, trusted);
		}
		return trusted;
	}

	public boolean isTrusted(final InetSocketAddress address) {
		return isTrusted(SSLAddress.fromInetSocketAddress(address));
	}

	private SSLSocketFactory getFactory(final SSLAddress address) {
		return trustedFactory;
	}

	private SSLSocketFactory getFactory(final String host) {
		return getFactory(SSLAddress.fromString(host));
	}

	private SSLSocketFactory getFactory(final InetAddress address) {
		return getFactory(SSLAddress.fromInetAddress(address));
	}
}
