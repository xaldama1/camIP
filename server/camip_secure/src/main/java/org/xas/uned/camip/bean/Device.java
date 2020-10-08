package org.xas.uned.camip.bean;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.Type;
import org.joda.time.DateTime;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "DEVICE")
@Getter
@Setter
@NoArgsConstructor
public class Device {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "DEVICESEQUENCE")
	private long id;

	private String ip;

	private String mac;

	private String name;

	@Type(type = "org.jadira.usertype.dateandtime.joda.PersistentDateTime")
	@Column(name = "detected", columnDefinition = "timestamp")
	private DateTime detected;

	@Type(type = "org.jadira.usertype.dateandtime.joda.PersistentDateTime")
	@Column(name = "lastupdate", columnDefinition = "timestamp")
	private DateTime lastUpdate;

	@OneToMany
	private List<Devicetoken> tokens;

	@OneToMany
	private List<Devicechecker> checker;

}
