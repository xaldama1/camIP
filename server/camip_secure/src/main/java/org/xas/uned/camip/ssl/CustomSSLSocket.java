package org.xas.uned.camip.ssl;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.SocketException;
import java.nio.channels.SocketChannel;

import javax.net.ssl.HandshakeCompletedListener;
import javax.net.ssl.SSLParameters;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocket;

public class CustomSSLSocket extends SSLSocket {

	protected final SSLSocketListener listener;

	protected SSLSocket socket;

	public CustomSSLSocket(final Socket socket) {
		this.socket = (SSLSocket) socket;
		this.listener = SSLManager.INSTANCE.createSocketListener();
		if (socket.isConnected()) {
			String address = getHostName(socket.getRemoteSocketAddress());
			listener.onConnect(address, this.socket);
		}
	}

	private String getHostName(final SocketAddress endpoint) {
		if (endpoint == null) {
			return null;
		}
		String hostName = null;
		if (endpoint instanceof InetSocketAddress) {
			InetSocketAddress inetAddress = (InetSocketAddress) endpoint;
			hostName = inetAddress.getHostName();
		}
		return hostName;
	}

	@Override
	public void connect(final SocketAddress endpoint) throws IOException {
		String hostName = getHostName(endpoint);
		listener.onConnect(hostName, socket);
		socket.connect(endpoint);
	}

	@Override
	public void connect(final SocketAddress endpoint, final int timeout) throws IOException {
		String hostName = getHostName(endpoint);
		listener.onConnect(hostName, socket);
		socket.connect(endpoint, timeout);
	}

	@Override
	public void close() throws IOException {
		listener.onClose(socket);
		socket.close();
	}

	@Override
	public void bind(final SocketAddress bindpoint) throws IOException {
		socket.bind(bindpoint);
	}

	@Override
	public void startHandshake() throws IOException {
		socket.startHandshake();
	}

	@Override
	public String[] getSupportedCipherSuites() {
		return socket.getSupportedCipherSuites();
	}

	@Override
	public String[] getEnabledCipherSuites() {
		return socket.getEnabledCipherSuites();
	}

	@Override
	public void setEnabledCipherSuites(final String[] strings) {
		socket.setEnabledCipherSuites(strings);
	}

	@Override
	public String[] getSupportedProtocols() {
		return socket.getSupportedProtocols();
	}

	@Override
	public String[] getEnabledProtocols() {
		return socket.getEnabledProtocols();
	}

	@Override
	public void setEnabledProtocols(final String[] strings) {
		socket.setEnabledProtocols(strings);
	}

	@Override
	public SSLSession getSession() {
		return socket.getSession();
	}

	@Override
	public SSLSession getHandshakeSession() {
		return socket.getHandshakeSession();
	}

	@Override
	public void addHandshakeCompletedListener(final HandshakeCompletedListener handshakeCompletedListener) {
		socket.addHandshakeCompletedListener(handshakeCompletedListener);
	}

	@Override
	public void removeHandshakeCompletedListener(final HandshakeCompletedListener handshakeCompletedListener) {
		socket.removeHandshakeCompletedListener(handshakeCompletedListener);
	}

	@Override
	public void setUseClientMode(final boolean b) {
		socket.setUseClientMode(b);
	}

	@Override
	public boolean getUseClientMode() {
		return socket.getUseClientMode();
	}

	@Override
	public void setNeedClientAuth(final boolean b) {
		socket.setNeedClientAuth(b);
	}

	@Override
	public boolean getNeedClientAuth() {
		return socket.getNeedClientAuth();
	}

	@Override
	public void setWantClientAuth(final boolean b) {
		socket.setWantClientAuth(b);
	}

	@Override
	public boolean getWantClientAuth() {
		return socket.getWantClientAuth();
	}

	@Override
	public void setEnableSessionCreation(final boolean b) {
		socket.setEnableSessionCreation(b);
	}

	@Override
	public boolean getEnableSessionCreation() {
		return socket.getEnableSessionCreation();
	}

	@Override
	public SSLParameters getSSLParameters() {
		return socket.getSSLParameters();
	}

	@Override
	public void setSSLParameters(SSLParameters sslParameters) {
		socket.setSSLParameters(sslParameters);
	}

	@Override
	public InetAddress getInetAddress() {
		return socket.getInetAddress();
	}

	@Override
	public InetAddress getLocalAddress() {
		return socket.getLocalAddress();
	}

	@Override
	public int getPort() {
		return socket.getPort();
	}

	@Override
	public int getLocalPort() {
		return socket.getLocalPort();
	}

	@Override
	public SocketAddress getRemoteSocketAddress() {
		return socket.getRemoteSocketAddress();
	}

	@Override
	public SocketAddress getLocalSocketAddress() {
		return socket.getLocalSocketAddress();
	}

	@Override
	public SocketChannel getChannel() {
		return socket.getChannel();
	}

	@Override
	public InputStream getInputStream() throws IOException {
		return socket.getInputStream();
	}

	@Override
	public OutputStream getOutputStream() throws IOException {
		return socket.getOutputStream();
	}

	@Override
	public void setTcpNoDelay(final boolean on) throws SocketException {
		socket.setTcpNoDelay(on);
	}

	@Override
	public boolean getTcpNoDelay() throws SocketException {
		return socket.getTcpNoDelay();
	}

	@Override
	public void setSoLinger(final boolean on, final int linger) throws SocketException {
		socket.setSoLinger(on, linger);
	}

	@Override
	public int getSoLinger() throws SocketException {
		return socket.getSoLinger();
	}

	@Override
	public void sendUrgentData(final int data) throws IOException {
		socket.sendUrgentData(data);
	}

	@Override
	public void setOOBInline(final boolean on) throws SocketException {
		socket.setOOBInline(on);
	}

	@Override
	public boolean getOOBInline() throws SocketException {
		return socket.getOOBInline();
	}

	@Override
	public void setSoTimeout(final int timeout) throws SocketException {
		socket.setSoTimeout(timeout);
	}

	@Override
	public int getSoTimeout() throws SocketException {
		return socket.getSoTimeout();
	}

	@Override
	public void setSendBufferSize(final int size) throws SocketException {
		socket.setSendBufferSize(size);
	}

	@Override
	public int getSendBufferSize() throws SocketException {
		return socket.getSendBufferSize();
	}

	@Override
	public void setReceiveBufferSize(final int size) throws SocketException {
		socket.setReceiveBufferSize(size);
	}

	@Override
	public int getReceiveBufferSize() throws SocketException {
		return socket.getReceiveBufferSize();
	}

	@Override
	public void setKeepAlive(final boolean on) throws SocketException {
		socket.setKeepAlive(on);
	}

	@Override
	public boolean getKeepAlive() throws SocketException {
		return socket.getKeepAlive();
	}

	@Override
	public void setTrafficClass(final int tc) throws SocketException {
		socket.setTrafficClass(tc);
	}

	@Override
	public int getTrafficClass() throws SocketException {
		return socket.getTrafficClass();
	}

	@Override
	public void setReuseAddress(final boolean on) throws SocketException {
		socket.setReuseAddress(on);
	}

	@Override
	public boolean getReuseAddress() throws SocketException {
		return socket.getReuseAddress();
	}

	@Override
	public void shutdownInput() throws IOException {
		socket.shutdownInput();
	}

	@Override
	public void shutdownOutput() throws IOException {
		socket.shutdownOutput();
	}

	@Override
	public String toString() {
		return socket.toString();
	}

	@Override
	public boolean isConnected() {
		return socket.isConnected();
	}

	@Override
	public boolean isBound() {
		return socket.isBound();
	}

	@Override
	public boolean isClosed() {
		return socket.isClosed();
	}

	@Override
	public boolean isInputShutdown() {
		return socket.isInputShutdown();
	}

	@Override
	public boolean isOutputShutdown() {
		return socket.isOutputShutdown();
	}

	@Override
	public void setPerformancePreferences(final int connectionTime, final int latency, final int bandwidth) {
		socket.setPerformancePreferences(connectionTime, latency, bandwidth);
	}
}
