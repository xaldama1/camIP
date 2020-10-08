package org.xas.uned.camip.scheduled;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javax.annotation.PreDestroy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public abstract class AbstractTask implements Task {

	private static final Logger LOGGER = LoggerFactory.getLogger(AbstractTask.class);

	private final ScheduledExecutorService scheduledExecutor = Executors.newScheduledThreadPool(5);

	private String taskName;

	private int initialDelay;

	private int period;

	private TimeUnit periodUnits;

	private boolean running = false;

	public void initialize(final String taskName, final int period, final TimeUnit periodUnits) {
		initialize(taskName, 10, period, periodUnits);
	}

	public void initialize(final String taskName, final int initialDelay, final int period,
			final TimeUnit periodUnits) {
		this.taskName = taskName;
		this.initialDelay = initialDelay;
		this.period = period;
		this.periodUnits = periodUnits;
	}

	@Override
	public final void execute() {
		if (!running) {
			running = true;

			if (taskName == null) {
				throw new IllegalStateException("Invoke initialize() from subclass");
			}

			scheduledExecutor.scheduleAtFixedRate(new Runnable() {
				@Override
				public void run() {
					try {
						executeInternal();
					} catch (Throwable e) {
						LOGGER.error("Error into {} task", taskName, e);
					}
				}
			}, initialDelay, period, periodUnits);

		}
	}

	@PreDestroy
	public void destroy() {
		if (scheduledExecutor != null) {
			scheduledExecutor.shutdown();
		}
	}

	public abstract void executeInternal();

	public String getTaskName() {
		return taskName;
	}

	public int getPeriod() {
		return period;
	}

	public TimeUnit getPeriodUnits() {
		return periodUnits;
	}

}
