package org.xas.uned.camip.ssl;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.UndeclaredThrowableException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.SocketException;
import java.nio.channels.SocketChannel;

import javax.net.ssl.HandshakeCompletedListener;
import javax.net.ssl.SSLParameters;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocket;

public class CustomUnconnectedSSLSocket extends SSLSocket {

	private final CustomSSLSocket trustedSocket;

	private SSLSocket connectedSocket;

	private boolean bindRequested = false;

	private SocketAddress bindAddress = null;

	public CustomUnconnectedSSLSocket(final Socket trustedSocket) {
		this.trustedSocket = new CustomSSLSocket(trustedSocket);
	}

	private <E> E withSocket(final SocketOperation<E> op) {
		try {
			if (connectedSocket != null) {
				return op.execute(connectedSocket);
			}
			return op.execute(trustedSocket);
		} catch (final Exception e) {
			throw new UndeclaredThrowableException(e);
		}
	}

	private <E> E withSocketIOException(final SocketOperation<E> op) throws IOException {
		try {
			return withSocket(op);
		} catch (UndeclaredThrowableException e) {
			if (e.getUndeclaredThrowable() instanceof IOException) {
				throw (IOException) e.getUndeclaredThrowable();
			} else {
				throw e;
			}
		}
	}

	private <E> E withSocketSocketException(final SocketOperation<E> op) throws SocketException {
		try {
			return withSocket(op);
		} catch (UndeclaredThrowableException e) {
			if (e.getUndeclaredThrowable() instanceof SocketException) {
				throw (SocketException) e.getUndeclaredThrowable();
			} else {
				throw e;
			}
		}
	}

	private SSLSocket pickSocket(final SocketAddress address) throws IOException {
		SSLSocket result = trustedSocket;
		if (bindRequested) {
			result.bind(bindAddress);
		}
		return result;
	}

	@Override
	public void connect(final SocketAddress endpoint) throws IOException {
		connectedSocket = pickSocket(endpoint);
		connectedSocket.connect(endpoint);

	}

	@Override
	public void connect(final SocketAddress endpoint, final int timeout) throws IOException {
		connectedSocket = pickSocket(endpoint);
		connectedSocket.connect(endpoint, timeout);
	}

	@Override
	public void bind(final SocketAddress bindpoint) throws IOException {
		bindRequested = true;
		bindAddress = bindpoint;
	}

	@Override
	public SSLSession getHandshakeSession() {
		return withSocket(new SocketOperation<SSLSession>() {
			public SSLSession execute(final SSLSocket socket) throws Exception {
				return socket.getHandshakeSession();
			}
		});
	}

	@Override
	public SSLParameters getSSLParameters() {
		return withSocket(new SocketOperation<SSLParameters>() {
			public SSLParameters execute(final SSLSocket socket) throws Exception {
				return socket.getSSLParameters();
			}
		});
	}

	@Override
	public void setSSLParameters(final SSLParameters sslParameters) {
		withSocket(new NoResultSocketOperation() {
			void executeNoResult(final SSLSocket socket) throws Exception {
				socket.setSSLParameters(sslParameters);
			}
		});
	}

	@Override
	public InetAddress getInetAddress() {
		return withSocket(new SocketOperation<InetAddress>() {
			public InetAddress execute(final SSLSocket socket) throws Exception {
				return socket.getInetAddress();
			}
		});
	}

	@Override
	public InetAddress getLocalAddress() {
		return withSocket(new SocketOperation<InetAddress>() {
			public InetAddress execute(final SSLSocket socket) throws Exception {
				return socket.getLocalAddress();
			}
		});
	}

	@Override
	public int getPort() {
		return withSocket(new SocketOperation<Integer>() {
			public Integer execute(final SSLSocket socket) throws Exception {
				return socket.getPort();
			}
		});
	}

	@Override
	public int getLocalPort() {
		return withSocket(new SocketOperation<Integer>() {
			public Integer execute(final SSLSocket socket) throws Exception {
				return socket.getLocalPort();
			}
		});
	}

	@Override
	public SocketAddress getRemoteSocketAddress() {
		return withSocket(new SocketOperation<SocketAddress>() {
			public SocketAddress execute(final SSLSocket socket) throws Exception {
				return socket.getRemoteSocketAddress();
			}
		});
	}

	@Override
	public SocketAddress getLocalSocketAddress() {
		return withSocket(new SocketOperation<SocketAddress>() {
			public SocketAddress execute(final SSLSocket socket) throws Exception {
				return socket.getLocalSocketAddress();
			}
		});
	}

	@Override
	public SocketChannel getChannel() {
		return withSocket(new SocketOperation<SocketChannel>() {
			public SocketChannel execute(final SSLSocket socket) throws Exception {
				return socket.getChannel();
			}
		});
	}

	@Override
	public InputStream getInputStream() throws IOException {
		return withSocketIOException(new SocketOperation<InputStream>() {
			public InputStream execute(final SSLSocket socket) throws Exception {
				return socket.getInputStream();
			}
		});
	}

	@Override
	public OutputStream getOutputStream() throws IOException {
		return withSocketIOException(new SocketOperation<OutputStream>() {
			public OutputStream execute(final SSLSocket socket) throws Exception {
				return socket.getOutputStream();
			}
		});
	}

	@Override
	public boolean getTcpNoDelay() throws SocketException {
		return withSocketSocketException(new SocketOperation<Boolean>() {
			public Boolean execute(final SSLSocket socket) throws Exception {
				return socket.getTcpNoDelay();
			}
		});
	}

	@Override
	public void sendUrgentData(final int data) throws IOException {
		withSocketIOException(new NoResultSocketOperation() {
			public void executeNoResult(final SSLSocket socket) throws Exception {
				socket.sendUrgentData(data);
			}
		});
	}

	@Override
	public void setTcpNoDelay(final boolean on) throws SocketException {
		withSocketSocketException(new NoResultSocketOperation() {
			public void executeNoResult(final SSLSocket socket) throws Exception {
				socket.setTcpNoDelay(on);
			}
		});
	}

	@Override
	public void setSoLinger(final boolean on, final int linger) throws SocketException {
		withSocketSocketException(new NoResultSocketOperation() {
			public void executeNoResult(final SSLSocket socket) throws Exception {
				socket.setSoLinger(on, linger);
			}
		});
	}

	@Override
	public int getSoLinger() throws SocketException {
		return withSocketSocketException(new SocketOperation<Integer>() {
			public Integer execute(final SSLSocket socket) throws Exception {
				return socket.getSoLinger();
			}
		});
	}

	@Override
	public void setOOBInline(final boolean on) throws SocketException {
		withSocketSocketException(new NoResultSocketOperation() {
			public void executeNoResult(final SSLSocket socket) throws Exception {
				socket.setOOBInline(on);
			}
		});
	}

	@Override
	public boolean getOOBInline() throws SocketException {
		return withSocketSocketException(new SocketOperation<Boolean>() {
			public Boolean execute(final SSLSocket socket) throws Exception {
				return socket.getOOBInline();
			}
		});
	}

	@Override
	public synchronized void setSoTimeout(final int timeout) throws SocketException {
		withSocketSocketException(new NoResultSocketOperation() {
			public void executeNoResult(final SSLSocket socket) throws Exception {
				socket.setSoTimeout(timeout);
			}
		});
	}

	@Override
	public synchronized int getSoTimeout() throws SocketException {
		return withSocketSocketException(new SocketOperation<Integer>() {
			public Integer execute(final SSLSocket socket) throws Exception {
				return socket.getSoTimeout();
			}
		});
	}

	@Override
	public synchronized void setSendBufferSize(final int size) throws SocketException {
		withSocketSocketException(new NoResultSocketOperation() {
			public void executeNoResult(final SSLSocket socket) throws Exception {
				socket.setSendBufferSize(size);
			}
		});
	}

	@Override
	public synchronized int getSendBufferSize() throws SocketException {
		return withSocketSocketException(new SocketOperation<Integer>() {
			public Integer execute(final SSLSocket socket) throws Exception {
				return socket.getSendBufferSize();
			}
		});
	}

	@Override
	public synchronized void setReceiveBufferSize(final int size) throws SocketException {
		withSocketSocketException(new NoResultSocketOperation() {
			public void executeNoResult(final SSLSocket socket) throws Exception {
				socket.setReceiveBufferSize(size);
			}
		});
	}

	@Override
	public synchronized int getReceiveBufferSize() throws SocketException {
		return withSocketSocketException(new SocketOperation<Integer>() {
			public Integer execute(final SSLSocket socket) throws Exception {
				return socket.getReceiveBufferSize();
			}
		});
	}

	@Override
	public void setKeepAlive(final boolean on) throws SocketException {
		withSocketSocketException(new NoResultSocketOperation() {
			public void executeNoResult(final SSLSocket socket) throws Exception {
				socket.setKeepAlive(on);
			}
		});
	}

	@Override
	public boolean getKeepAlive() throws SocketException {
		return withSocketSocketException(new SocketOperation<Boolean>() {
			public Boolean execute(final SSLSocket socket) throws Exception {
				return socket.getKeepAlive();
			}
		});
	}

	@Override
	public void setTrafficClass(final int tc) throws SocketException {
		withSocketSocketException(new NoResultSocketOperation() {
			public void executeNoResult(final SSLSocket socket) throws Exception {
				socket.setTrafficClass(tc);
			}
		});
	}

	@Override
	public int getTrafficClass() throws SocketException {
		return withSocketSocketException(new SocketOperation<Integer>() {
			public Integer execute(final SSLSocket socket) throws Exception {
				return socket.getTrafficClass();
			}
		});
	}

	@Override
	public void setReuseAddress(final boolean on) throws SocketException {
		withSocketSocketException(new NoResultSocketOperation() {
			public void executeNoResult(final SSLSocket socket) throws Exception {
				socket.setReuseAddress(on);
			}
		});
	}

	@Override
	public boolean getReuseAddress() throws SocketException {
		return withSocketSocketException(new SocketOperation<Boolean>() {
			public Boolean execute(final SSLSocket socket) throws Exception {
				return socket.getReuseAddress();
			}
		});
	}

	@Override
	public synchronized void close() throws IOException {
		withSocketIOException(new NoResultSocketOperation() {
			public void executeNoResult(final SSLSocket socket) throws Exception {
				socket.close();
			}
		});
	}

	@Override
	public void shutdownInput() throws IOException {
		withSocketIOException(new NoResultSocketOperation() {
			public void executeNoResult(final SSLSocket socket) throws Exception {
				socket.shutdownInput();
			}
		});
	}

	@Override
	public void shutdownOutput() throws IOException {
		withSocketIOException(new NoResultSocketOperation() {
			public void executeNoResult(final SSLSocket socket) throws Exception {
				socket.shutdownOutput();
			}
		});
	}

	@Override
	public boolean isConnected() {
		return withSocket(new SocketOperation<Boolean>() {
			public Boolean execute(final SSLSocket socket) throws Exception {
				return socket.isConnected();
			}
		});
	}

	@Override
	public boolean isBound() {
		return withSocket(new SocketOperation<Boolean>() {
			public Boolean execute(final SSLSocket socket) throws Exception {
				return socket.isBound();
			}
		});
	}

	@Override
	public boolean isClosed() {
		return withSocket(new SocketOperation<Boolean>() {
			public Boolean execute(final SSLSocket socket) throws Exception {
				return socket.isClosed();
			}
		});
	}

	@Override
	public boolean isInputShutdown() {
		return withSocket(new SocketOperation<Boolean>() {
			public Boolean execute(final SSLSocket socket) throws Exception {
				return socket.isInputShutdown();
			}
		});
	}

	@Override
	public boolean isOutputShutdown() {
		return withSocket(new SocketOperation<Boolean>() {
			public Boolean execute(final SSLSocket socket) throws Exception {
				return socket.isOutputShutdown();
			}
		});
	}

	@Override
	public void setPerformancePreferences(final int connectionTime, final int latency, final int bandwidth) {
		withSocket(new NoResultSocketOperation() {
			public void executeNoResult(final SSLSocket socket) throws Exception {
				socket.setPerformancePreferences(connectionTime, latency, bandwidth);
			}
		});
	}

	@Override
	public String[] getSupportedCipherSuites() {
		return withSocket(new SocketOperation<String[]>() {
			public String[] execute(final SSLSocket socket) throws Exception {
				return socket.getSupportedCipherSuites();
			}
		});
	}

	@Override
	public String[] getEnabledCipherSuites() {
		return withSocket(new SocketOperation<String[]>() {
			public String[] execute(final SSLSocket socket) throws Exception {
				return socket.getEnabledCipherSuites();
			}
		});
	}

	@Override
	public void setEnabledCipherSuites(final String[] strings) {
		withSocket(new NoResultSocketOperation() {
			public void executeNoResult(final SSLSocket socket) throws Exception {
				socket.setEnabledCipherSuites(strings);
			}
		});
	}

	@Override
	public String[] getSupportedProtocols() {
		return withSocket(new SocketOperation<String[]>() {
			public String[] execute(final SSLSocket socket) throws Exception {
				return socket.getSupportedProtocols();
			}
		});
	}

	@Override
	public String[] getEnabledProtocols() {
		return withSocket(new SocketOperation<String[]>() {
			public String[] execute(final SSLSocket socket) throws Exception {
				return socket.getEnabledProtocols();
			}
		});
	}

	@Override
	public void setEnabledProtocols(final String[] strings) {
		withSocket(new NoResultSocketOperation() {
			public void executeNoResult(final SSLSocket socket) throws Exception {
				socket.setEnabledProtocols(strings);
			}
		});
	}

	@Override
	public SSLSession getSession() {
		return withSocket(new SocketOperation<SSLSession>() {
			public SSLSession execute(final SSLSocket socket) throws Exception {
				return socket.getSession();
			}
		});
	}

	@Override
	public void addHandshakeCompletedListener(final HandshakeCompletedListener handshakeCompletedListener) {
		withSocket(new NoResultSocketOperation() {
			public void executeNoResult(final SSLSocket socket) throws Exception {
				socket.addHandshakeCompletedListener(handshakeCompletedListener);
			}
		});
	}

	@Override
	public void removeHandshakeCompletedListener(final HandshakeCompletedListener handshakeCompletedListener) {
		withSocket(new NoResultSocketOperation() {
			public void executeNoResult(final SSLSocket socket) throws Exception {
				socket.removeHandshakeCompletedListener(handshakeCompletedListener);
			}
		});
	}

	@Override
	public void startHandshake() throws IOException {
		withSocketIOException(new NoResultSocketOperation() {
			public void executeNoResult(final SSLSocket socket) throws Exception {
				socket.startHandshake();
			}
		});
	}

	@Override
	public void setUseClientMode(final boolean b) {
		withSocket(new NoResultSocketOperation() {
			public void executeNoResult(final SSLSocket socket) throws Exception {
				socket.setUseClientMode(b);
			}
		});
	}

	@Override
	public boolean getUseClientMode() {
		return withSocket(new SocketOperation<Boolean>() {
			public Boolean execute(final SSLSocket socket) throws Exception {
				return socket.getUseClientMode();
			}
		});
	}

	@Override
	public void setNeedClientAuth(final boolean b) {
		withSocket(new NoResultSocketOperation() {
			public void executeNoResult(final SSLSocket socket) throws Exception {
				socket.setNeedClientAuth(b);
			}
		});
	}

	@Override
	public boolean getNeedClientAuth() {
		return withSocket(new SocketOperation<Boolean>() {
			public Boolean execute(final SSLSocket socket) throws Exception {
				return socket.getNeedClientAuth();
			}
		});
	}

	@Override
	public void setWantClientAuth(final boolean b) {
		withSocket(new NoResultSocketOperation() {
			public void executeNoResult(final SSLSocket socket) throws Exception {
				socket.setWantClientAuth(b);
			}
		});
	}

	@Override
	public boolean getWantClientAuth() {
		return withSocket(new SocketOperation<Boolean>() {
			public Boolean execute(final SSLSocket socket) throws Exception {
				return socket.getWantClientAuth();
			}
		});
	}

	@Override
	public void setEnableSessionCreation(final boolean b) {
		withSocket(new NoResultSocketOperation() {
			public void executeNoResult(final SSLSocket socket) throws Exception {
				socket.setEnableSessionCreation(b);
			}
		});
	}

	@Override
	public boolean getEnableSessionCreation() {
		return withSocket(new SocketOperation<Boolean>() {
			public Boolean execute(final SSLSocket socket) throws Exception {
				return socket.getEnableSessionCreation();
			}
		});
	}

	private interface SocketOperation<E> {

		E execute(final SSLSocket socket) throws Exception;
	}

	private abstract class NoResultSocketOperation implements SocketOperation<Void> {

		abstract void executeNoResult(final SSLSocket socket) throws Exception;

		public Void execute(SSLSocket socket) throws Exception {
			this.executeNoResult(socket);
			return null;
		}
	}
}
