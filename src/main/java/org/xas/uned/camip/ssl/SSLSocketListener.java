package org.xas.uned.camip.ssl;

import javax.net.ssl.SSLSocket;

public interface SSLSocketListener {

	void onConnect(final String hostName, final SSLSocket socket);

	void onClose(final SSLSocket socket);
}
