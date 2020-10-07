package org.xas.uned.camip.provider.maliciousip;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashSet;
import java.util.Set;

//import org.hdiv.ee.ssl.ConnectionSettings;
//import org.hdiv.ee.ssl.HdivHttpConnection;
//import org.hdiv.ee.ssl.HdivURLConnection;
//import org.hdiv.ee.ssl.SSLManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpMethod;
import org.xas.uned.camip.ssl.CustomHttpConnection;
import org.xas.uned.camip.ssl.CustomURLConnection;
import org.xas.uned.camip.ssl.SSLManager;

public abstract class AbstractMaliciousIpProvider {

	private static final Logger LOGGER = LoggerFactory.getLogger(AbstractMaliciousIpProvider.class);

	protected abstract String getUrl();

	protected abstract Set<String> readResult(final InputStream inputStream) throws Exception;

	public Set<String> getIpReputationList() {

		Set<String> ips = new HashSet<>();

		CustomHttpConnection con = null;

		InputStream in = null;
		try {

			CustomURLConnection urlCon = SSLManager.INSTANCE.opentrustedConnection(getUrl());
			con = urlCon.unwrap(CustomHttpConnection.class);
			con.setRequestMethod(HttpMethod.GET.name());
			con.setConnectTimeout(1000);
			con.setReadTimeout(4000);
			in = con.getInputStream();
			ips = readResult(in);

		} catch (Exception e) {
			LOGGER.error("Error getting IP Reputation list.", e);
		} finally {
			if (in != null) {
				try {
					in.close();
				} catch (IOException e) {
				}
			}
			if (con != null) {
				con.disconnect();
			}
		}

		return ips;
	}

}
