package org.xas.uned.camip.scheduled.impl;

import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.xas.uned.camip.checker.DoubleCheckChecker;
import org.xas.uned.camip.scheduled.AbstractTask;
import org.xas.uned.camip.util.TaskConstants;

@Component
public class DoubleCheckTask extends AbstractTask {

	@Autowired
	private DoubleCheckChecker doubleCheckChecker;

	public DoubleCheckTask() {
		initialize("DoubleCheckTask", TaskConstants.TASK_DOUBLE_CHECK_PERIOD_SEC, TimeUnit.SECONDS);
	}

	@Override
	public void executeInternal() {
		doubleCheckChecker.checkWaitingList();
	}
}
