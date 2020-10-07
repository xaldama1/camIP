package org.xas.uned.camip.facade.impl;

import java.util.List;
import java.util.Set;

import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.xas.uned.camip.bean.MaliciousIp;
import org.xas.uned.camip.bean.MaliciousIpOrigin;
import org.xas.uned.camip.facade.MaliciousIPFacade;
import org.xas.uned.camip.provider.maliciousip.HoneypotMaliciousIpProvider;
import org.xas.uned.camip.provider.maliciousip.TorMaliciousIpProvider;
import org.xas.uned.camip.repository.MaliciousIpRepository;

@Service
public class MaliciousIPFacadeImpl implements MaliciousIPFacade {

	@Autowired
	private MaliciousIpRepository maliciousIpRepository;

	@Autowired
	private TorMaliciousIpProvider torMaliciousIpProvider;

	@Autowired
	private HoneypotMaliciousIpProvider honeypotMaliciousIpProvider;

	public void updateIPList() {
		try {
			Set<String> ips = torMaliciousIpProvider.getIpReputationList();
			ips.addAll(honeypotMaliciousIpProvider.getIpReputationList());

			if (!ips.isEmpty()) {
				updateDB(ips);

			}
		} catch (Exception e) {

		}
	}

	private boolean updateDB(final Set<String> ips) {

		List<MaliciousIp> stored = maliciousIpRepository.findByOrigin(MaliciousIpOrigin.IMPORTED);

		boolean addedNew = false;
		// Save new ips
		DateTime current = new DateTime();
		boolean found;
		for (String ip : ips) {
			found = false;
			for (MaliciousIp maliciousIp : stored) {
				if (maliciousIp.getIp().equals(ip)) {
					found = true;
					break;
				}
			}
			if (!found) {
				maliciousIpRepository.save(new MaliciousIp(ip, current, MaliciousIpOrigin.IMPORTED));
				addedNew = true;
			}
		}

		return addedNew;
	}
}
