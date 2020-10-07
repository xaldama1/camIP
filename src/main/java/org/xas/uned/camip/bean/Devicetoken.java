package org.xas.uned.camip.bean;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "DEVICETOKEN")
@Getter
@Setter
@NoArgsConstructor
public class Devicetoken {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "DEVICETOKENSEQUENCE")
	private long id;

	private String token;

	@ManyToOne(targetEntity = Device.class)
	private Device device;

}
