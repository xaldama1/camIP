package org.xas.uned.camip.bean;

import javax.persistence.Column;
import javax.persistence.Entity;
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
@Table(name = "RULE")
@Getter
@Setter
@NoArgsConstructor
public class Rule {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "RULESEQUENCE")
	private long id;

	private String rule;

	@Type(type = "org.jadira.usertype.dateandtime.joda.PersistentDateTime")
	@Column(name = "activeuntil", columnDefinition = "timestamp")
	private DateTime activeuntil;

}
