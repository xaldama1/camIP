package org.xas.uned.camip.bean;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.Type;
import org.joda.time.DateTime;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "MALICIOUS_IP")
@Getter
@Setter
@NoArgsConstructor
public class MaliciousIp {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "IPSEQUENCE")
	private long id;

	private String ip;

	@Type(type = "org.jadira.usertype.dateandtime.joda.PersistentDateTime")
	@Column(name = "lastupdate", columnDefinition = "timestamp")
	private DateTime lastUpdate;

	@Enumerated(EnumType.STRING)
	private MaliciousIpOrigin origin;

	public MaliciousIp(final String ip, final DateTime lastUpdate, final MaliciousIpOrigin origin) {
		this.ip = ip;
		this.lastUpdate = lastUpdate;
		this.origin = origin;
	}

}
