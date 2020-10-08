package org.xas.uned.camip.bean;

import org.joda.time.DateTime;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class DeviceCheckData {

	private String ip;

	private String fromIp;

	private String mac;

	private String name;

	private DateTime requestTime;

	private Devicetoken requestToken;

	private boolean received;

	private String responseToken;

}
