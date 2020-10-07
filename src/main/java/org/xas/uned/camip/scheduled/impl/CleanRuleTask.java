package org.xas.uned.camip.scheduled.impl;

import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.xas.uned.camip.scheduled.AbstractTask;
import org.xas.uned.camip.service.FirewallService;
import org.xas.uned.camip.util.TaskConstants;

@Component
public class CleanRuleTask extends AbstractTask {

	@Autowired
	private FirewallService firewallService;

	public CleanRuleTask() {
		initialize("CleanRuleTask", TaskConstants.TASK_CHECK_RULES_PERIOD_MINS, TimeUnit.MINUTES);
	}

	@Override
	public void executeInternal() {
		firewallService.checkRules();
	}
}
