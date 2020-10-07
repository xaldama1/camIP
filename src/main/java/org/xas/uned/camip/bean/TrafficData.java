package org.xas.uned.camip.bean;

import org.joda.time.DateTime;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TrafficData {

	private DateTime time;
	private String fromHost;
	private String fromPort;
	private String toHost;
	private String toPort;

}
