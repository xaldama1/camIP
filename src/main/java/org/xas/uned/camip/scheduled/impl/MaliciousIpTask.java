package org.xas.uned.camip.scheduled.impl;

import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.xas.uned.camip.facade.MaliciousIPFacade;
import org.xas.uned.camip.scheduled.AbstractTask;
import org.xas.uned.camip.util.TaskConstants;

@Component
public class MaliciousIpTask extends AbstractTask {

	@Autowired
	private MaliciousIPFacade maliciousIPFacade;

	public MaliciousIpTask() {
		initialize("MaliciousIpTask", TaskConstants.TASK_UPDATE_IP_LIST_PERIOD_HOURS, TimeUnit.HOURS);
	}

	@Override
	public void executeInternal() {
		maliciousIPFacade.updateIPList();
	}
}
