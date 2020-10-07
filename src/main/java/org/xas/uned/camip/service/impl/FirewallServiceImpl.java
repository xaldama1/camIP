package org.xas.uned.camip.service.impl;

import java.io.IOException;
import java.util.List;

import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.xas.uned.camip.bean.Rule;
import org.xas.uned.camip.repository.RuleRepository;
import org.xas.uned.camip.service.FirewallService;

@Service
public class FirewallServiceImpl implements FirewallService {

	private static final String ADD_RULE = "iptables -A ";
	private static final String DELETE_RULE = "iptables -D ";
	private static final String DENY_OUTGOING = " OUTPUT -d %s -j DROP";
	private static final String DENY_INCOMING = " INPUT -s %s -j DROP";

	@Autowired
	private RuleRepository ruleRepository;

	@Override
	public void allow2Ip(String ip) {
		allow(String.format(DENY_OUTGOING, ip));
	}

	@Override
	public void allowFromIp(String ip) {
		allow(String.format(DENY_INCOMING, ip));

	}

	private void allow(String ruleFormated) {
		List<Rule> rules = ruleRepository.findByRule(ruleFormated);

		for (Rule rule : rules) {
			try {
				Runtime.getRuntime().exec("sudo " + DELETE_RULE + ruleFormated);
				Runtime.getRuntime().exec("iptables save");
			} catch (IOException e) {

			}
			ruleRepository.delete(rule);
		}
	}

	@Override
	public void block2Ip(String ip, Integer ttl) {

		block(String.format(DENY_OUTGOING, ip), ttl);
	}

	@Override
	public void blockFromIp(String ip, Integer ttl) {
		block(String.format(DENY_INCOMING, ip), ttl);

	}

	private void block(String ruleFormated, Integer ttl) {

		DateTime dt = new DateTime();

		Rule rule = new Rule();
		rule.setRule(ruleFormated);
		rule.setActiveuntil(dt);

		ruleRepository.save(rule);

		try {
			Runtime.getRuntime().exec("sudo " + ADD_RULE + ruleFormated);
			Runtime.getRuntime().exec("iptables save");
		} catch (IOException e) {

		}

	}

	public void checkRules() {

		List<Rule> rules = ruleRepository.findByTime(new DateTime());

		for (Rule rule : rules) {
			try {
				Runtime.getRuntime().exec("sudo " + DELETE_RULE + rule.getRule());
				Runtime.getRuntime().exec("iptables save");
			} catch (IOException e) {

			}
			ruleRepository.delete(rule);
		}
	}
}
