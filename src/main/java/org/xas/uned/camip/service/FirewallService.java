package org.xas.uned.camip.service;

public interface FirewallService {

	void allow2Ip(String ip);

	void allowFromIp(String ip);

	void block2Ip(String ip, Integer ttl);

	void blockFromIp(String ip, Integer ttl);

	void checkRules();

}
