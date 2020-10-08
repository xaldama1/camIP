package org.xas.uned.camip.provider.maliciousip;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashSet;
import java.util.Set;

import org.springframework.stereotype.Service;

@Service
public class TorMaliciousIpProvider extends AbstractMaliciousIpProvider {

	private static final String TOR_URL = "https://check.torproject.org/cgi-bin/TorBulkExitList.py?ip=1.1.1.1";

	@Override
	protected String getUrl() {
		return TOR_URL;
	};

	@Override
	protected Set<String> readResult(final InputStream inputStream) throws Exception {

		Set<String> ips = new HashSet<>();
		BufferedReader bufreader = null;

		try {
			bufreader = new BufferedReader(new InputStreamReader(inputStream));
			String inputLine;
			while ((inputLine = bufreader.readLine()) != null) {
				if (isValidIP(inputLine)) {
					ips.add(inputLine);
				}
			}
		}
		finally {
			if (bufreader != null) {
				try {
					bufreader.close();
				}
				catch (IOException e) {
				}
			}
		}
		return ips;
	}

	private boolean isValidIP(final String ip) {
		return !ip.startsWith("#");
	}

}
