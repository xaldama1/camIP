package org.xas.uned.camip.ssl;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.List;

public class SSLAddress {

	private final String hostname;

	private final String address;

	SSLAddress(final String hostname, final String address) {
		this.hostname = hostname;
		this.address = address;
	}

	public String getHostname() {
		return hostname;
	}

	public String getAddress() {
		return address;
	}

	@Override
	public String toString() {
		return "{" + "hostname='" + hostname + '\'' + ", address='" + address + '\'' + '}';
	}

	public boolean matches(final SSLAddress other) {
		if (this.address != null && this.address.equals(other.address)) {
			return true;
		}
		return this.hostname != null && this.hostname.equalsIgnoreCase(other.hostname);
	}

	public static SSLAddress fromString(final String url) {
		String hostname = getHostname(url);
		try {
			InetAddress address = InetAddress.getByName(hostname);
			return fromInetAddress(address);
		} catch (UnknownHostException e) {
			return new SSLAddress(hostname, null);
		}
	}

	public static SSLAddress fromUrl(final URL url) {
		return fromString(url.getHost());
	}

	public static SSLAddress fromInetAddress(final InetAddress inetAddress) {
		return new SSLAddress(inetAddress.getHostName(), inetAddress.getHostAddress());
	}

	public static SSLAddress fromInetSocketAddress(final InetSocketAddress inetSocketAddress) {
		InetAddress inetAddress = inetSocketAddress.getAddress();
		if (inetAddress != null) {
			return fromInetAddress(inetSocketAddress.getAddress());
		} else {
			return fromString(inetSocketAddress.getHostName()); // last change to resolve the address again
		}
	}

	public static String getHostname(final String url) {
		String host = url;
		try {
			String lower = host.toLowerCase();
			if (lower.startsWith("http://") || lower.startsWith("https://") || lower.startsWith("ftp://")) {
				return new URL(host).getHost();
			}
		} catch (MalformedURLException e) {
			// ignore
		}

		int protocolIndex = host.indexOf("://");
		if (protocolIndex >= 0) {
			host = host.substring(protocolIndex + 3);
		}

		List<String> suffixTokens = Arrays.asList(":", "/");
		for (String token : suffixTokens) {
			int index = host.indexOf(token);
			if (index > 0) {
				host = host.substring(0, index);
			}
		}
		return host;
	}
}
