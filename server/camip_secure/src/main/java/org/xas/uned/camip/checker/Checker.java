package org.xas.uned.camip.checker;

import org.xas.uned.camip.bean.CheckResult;
import org.xas.uned.camip.bean.Device;
import org.xas.uned.camip.bean.TrafficData;

public interface Checker {

	public CheckResult doCheck(boolean incoming, TrafficData trafficData, Device device);

}
