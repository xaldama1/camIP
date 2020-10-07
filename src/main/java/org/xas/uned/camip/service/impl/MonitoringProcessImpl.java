package org.xas.uned.camip.service.impl;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xas.uned.camip.service.MonitoringProcess;
import org.xas.uned.camip.util.NetworkFlowListener;

public class MonitoringProcessImpl implements MonitoringProcess {

	private static final Logger LOGGER = LoggerFactory.getLogger(MonitoringProcessImpl.class);

	private static final int FREE_TIME = 5000;

	private static MonitoringProcessImpl MONITORINGPROCESS = null;

	private Process process;

	private NetworkFlowListener listener;

	private MonitoringProcessImpl(NetworkFlowListener listener) {
		this.listener = listener;
		start();
	};

	public static MonitoringProcessImpl getInstance(NetworkFlowListener listener) {
		if (MONITORINGPROCESS == null) {
			MONITORINGPROCESS = new MonitoringProcessImpl(listener);
		}
		return MONITORINGPROCESS;
	}

	private Process createProcess() {
		Process process = null;
		try {
			process = Runtime.getRuntime().exec("sudo tcpdump -n host 192.168.0.21 ");
			// process = Runtime.getRuntime().exec("sudo tcpdump -i wlan0 less 61 ");

			StringBuilder input = new StringBuilder();
			try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {

				String line;
				while ((line = reader.readLine()) != null) {
					input.append(line + "\n");
					listener.onTraffic(line);
				}
			} catch (Exception e) {
				LOGGER.error("Error reading network monitoring input", e);
			}
		} catch (Exception e) {
			LOGGER.error("Error runnin network monitoring command", e);
		}

		return process;

	}

	private void start() {

		try {
			new Thread() {
				@Override
				public void run() {
					try {
						process = createProcess();
						int exitVal = process.waitFor();
						if (exitVal == 0) {
							LOGGER.info("Process finished with code 0");
						}
					} catch (Exception e) {
						LOGGER.error("Error running network monitoring process", e);
					}
				}
			}.start();
		} catch (Exception e) {
			LOGGER.error("Error creating network monitoring thread", e);
		}
	}

	@Override
	public void freeProcess() {
		if (process.isAlive()) {

			process.destroyForcibly();
			try {
				Thread.sleep(FREE_TIME);
			} catch (Exception e) {
			}
		}

		start();
	}

}
