package org.xas.uned.camip.checker;

import org.xas.uned.camip.bean.Action;
import org.xas.uned.camip.bean.CheckResult;
import org.xas.uned.camip.bean.CheckerStatus;
import org.xas.uned.camip.bean.Device;
import org.xas.uned.camip.bean.Devicechecker;
import org.xas.uned.camip.bean.TrafficData;
import org.xas.uned.camip.repository.DeviceCheckerRepository;

public abstract class AbstractChecker implements Checker {

	protected Checker next = null;

	@Override
	public CheckResult doCheck(boolean incoming, TrafficData trafficData, Device device) {

		CheckResult result = new CheckResult();
		CheckerStatus status = getDeviceCheckerStatus(device);
		if (status == CheckerStatus.DISABLED || doInternalCheck(incoming, trafficData)) {
			result.setValid(true);

			Checker next = getNext(result);
			if (next != null) {
				CheckResult nextResult = next.doCheck(incoming, trafficData, device);
				if (!nextResult.isValid()) {
					result.setValid(false);
					result.getMsg().addAll(nextResult.getMsg());
					if (result.getAction() != Action.BLOCK || result.getAction() == null) {
						result.setAction(nextResult.getAction());
					}
				}
			}

		} else {
			result.setValid(false);
			result.getMsg().add(getMessage());
			result.setAction(status == CheckerStatus.MONITORING ? Action.NOTIFY : Action.BLOCK);
		}

		return result;
	}

	private CheckerStatus getDeviceCheckerStatus(Device device) {

		Devicechecker deviceChecker = getRepository().findByDeviceIdAndChecker(device.getId(), getCheckerName());
		return deviceChecker == null ? CheckerStatus.DISABLED : deviceChecker.getStatus();
	}

	public void setNext(Checker next) {
		this.next = next;
	}

	protected abstract boolean doInternalCheck(boolean incoming, TrafficData trafficData);

	protected abstract String getMessage();

	protected abstract Checker getNext(CheckResult result);

	protected abstract Integer bannedSeconds();

	protected abstract DeviceCheckerRepository getRepository();

	protected abstract String getCheckerName();

}
