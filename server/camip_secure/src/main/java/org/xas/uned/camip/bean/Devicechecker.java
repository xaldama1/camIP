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
@Table(name = "DEVICECHECKER")
@Getter
@Setter
@NoArgsConstructor
public class Devicechecker {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "DEVICECHECKERSEQUENCE")
	private long id;

	private String checker;

	private CheckerStatus status;

	@ManyToOne(targetEntity = Device.class)
	private Device device;

}
