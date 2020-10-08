package org.xas.uned.camip.bean;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SyncData {

	private String userToken;

	private List<Device> devices;

}
