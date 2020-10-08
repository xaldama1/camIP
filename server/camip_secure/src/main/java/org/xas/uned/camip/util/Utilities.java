package org.xas.uned.camip.util;

import org.joda.time.DateTime;
import org.xas.uned.camip.bean.TrafficData;

public class Utilities {

	public static TrafficData convertTrafficInfo(String info) {
		info = info.substring(6);
		String[] infoParts = info.split(" ");
		TrafficData data = new TrafficData();
		// for (String part : infoParts) {
		String from = infoParts[2];
		String to = infoParts[4];
		data.setFromHost(from.substring(0, from.lastIndexOf(".")));
		data.setFromPort(from.substring(from.lastIndexOf(".") + 1));
		data.setToHost(to.substring(0, to.lastIndexOf(".")));
		data.setToPort(to.substring(to.lastIndexOf(".") + 1));
		data.setTime(new DateTime());

		return data;
	}
}
