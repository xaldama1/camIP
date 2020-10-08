package org.xas.uned.camip.facade.impl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;

import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.transaction.support.TransactionTemplate;
import org.xas.uned.camip.bean.CheckerNames;
import org.xas.uned.camip.bean.CheckerStatus;
import org.xas.uned.camip.bean.Device;
import org.xas.uned.camip.bean.Devicechecker;
import org.xas.uned.camip.bean.Devicetoken;
import org.xas.uned.camip.checker.DoubleCheckChecker;
import org.xas.uned.camip.facade.DeviceFacade;
import org.xas.uned.camip.repository.DeviceCheckerRepository;
import org.xas.uned.camip.repository.DeviceRepository;
import org.xas.uned.camip.repository.DeviceTokenRepository;
import org.xas.uned.camip.service.impl.TrafficCheckerServiceImpl;
import org.xas.uned.camip.util.TaskConstants;

@Service
public class DeviceFacadeImpl extends Observable implements DeviceFacade {

	private static final Logger LOGGER = LoggerFactory.getLogger(DeviceFacadeImpl.class);

	private final DeviceRepository deviceRepository;

	private final TransactionTemplate transactionTemplate;

	private final DeviceTokenRepository deviceTokenRepository;
	private final DeviceCheckerRepository deviceCheckerRepository;

	@Autowired
	public DeviceFacadeImpl(final DeviceRepository deviceRepository, final DoubleCheckChecker doubleCheckChecker,
			final TrafficCheckerServiceImpl trafficCheckerServiceImpl,
			final PlatformTransactionManager transactionManager, final DeviceTokenRepository deviceTokenRepository,
			final DeviceCheckerRepository deviceCheckerRepository) {
		this.deviceRepository = deviceRepository;
		this.deviceTokenRepository = deviceTokenRepository;
		this.deviceCheckerRepository = deviceCheckerRepository;

		addObserver(doubleCheckChecker);
		addObserver(trafficCheckerServiceImpl);

		transactionTemplate = new TransactionTemplate(transactionManager);
	}

	public void updateCurrentConnectedList() {

		String baseIP = getBaseIP();
		List<Device> detected = new ArrayList<>();

		boolean changed = false;

		for (int i = 2; i < 255; i++) {
			String currentIP = baseIP + i;
			try {
				if (InetAddress.getByName(currentIP).isReachable(200)) {
					Device device = saveOrUpdateDevice(currentIP, getIpMacAddress(currentIP));
					if (device != null) {
						detected.add(device);
						changed = true;
					}
				}
			} catch (IOException e) {
				LOGGER.error("Error detecting device with ip " + currentIP);
			}
		}

		cleanOldDevices(detected);

		if (changed) {
			notifyObservers(detected);
		}

	}

	private String getBaseIP() {
		String ip;
		try {
			ip = Inet4Address.getLocalHost().getHostAddress();
			ip = ip.substring(0, ip.lastIndexOf(".") + 1);
		} catch (UnknownHostException e) {
			ip = "";
		}

		return ip;
	}

	private String getIpMacAddress(String ip) {

		String mac = null;

		try {

			ProcessBuilder processBuilder = new ProcessBuilder();
			processBuilder.command("arp", ip);

			Process process = processBuilder.start();

			StringBuilder output = new StringBuilder();
			try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {

				String line;
				while ((line = reader.readLine()) != null) {
					output.append(line + "\n");
				}
			} catch (Exception e) {
				LOGGER.error("Error getting mac address from ip " + ip, e);
			}

			int exitVal = process.waitFor();
			if (exitVal == 0) {
				System.out.println(output);
				mac = output.toString().split(" ")[3];
			}

		} catch (Exception e) {
			LOGGER.error("Error executing command to get mac address from ip " + ip);
		}

		return mac;
	}

	private Device saveOrUpdateDevice(String ip, String mac) {
		Device device = null;
		if (mac != null) {
			List<Device> devices = deviceRepository.findByMac(mac);
			if (!devices.isEmpty()) {
				device = devices.get(0);
				if (!ip.equals(device.getIp())) {
					device.setIp(ip);
					device.setLastUpdate(new DateTime());
					deviceRepository.save(device);
				}
			} else {
				DateTime current = new DateTime();
				device = new Device();
				device.setIp(ip);
				device.setDetected(current);
				device.setMac(mac);
				device.setLastUpdate(current);
				device = deviceRepository.save(device);
				device.setTokens(createDeviceTokens(device));
				device.setChecker(createDeviceCheckers(device));
			}

		}

		return device;
	}

	private void cleanOldDevices(List<Device> devices) {

		List<String> currentIps = new ArrayList<>();
		devices.forEach((final Device device) -> currentIps.add(device.getIp()));
		transactionTemplate.execute(new TransactionCallbackWithoutResult() {
			@Override
			protected void doInTransactionWithoutResult(TransactionStatus status) {
				deviceRepository.deleteByIpNotIn(currentIps);
			}
		});

	}

	private List<Devicetoken> createDeviceTokens(Device device) {
		List<Devicetoken> tokens = new ArrayList<>();

		int leftLimit = 97; // letter 'a'
		int rightLimit = 122; // letter 'z'
		int targetStringLength = 16;
		SecureRandom random = new SecureRandom();
		StringBuilder buffer;

		for (int x = 0; x <= TaskConstants.MAX_TOKENS; x++) {

			buffer = new StringBuilder(targetStringLength);

			for (int i = 0; i < targetStringLength; i++) {
				int randomLimitedInt = leftLimit + (int) (random.nextFloat() * (rightLimit - leftLimit + 1));
				buffer.append((char) randomLimitedInt);
			}

			Devicetoken deviceToken = new Devicetoken();
			deviceToken.setDevice(device);
			deviceToken.setToken(buffer.toString());
			tokens.add(deviceTokenRepository.save(deviceToken));
		}

		return tokens;
	}

	private List<Devicechecker> createDeviceCheckers(Device device) {
		List<Devicechecker> deviceCheckers = new ArrayList<>();

		for (CheckerNames checkerNames : CheckerNames.values()) {

			Devicechecker deviceChecker = new Devicechecker();
			deviceChecker.setDevice(device);
			deviceChecker.setChecker(checkerNames.name());
			deviceChecker.setStatus(CheckerStatus.BLOCKING);
			deviceCheckers.add(deviceCheckerRepository.save(deviceChecker));
		}

		return deviceCheckers;
	}

}
