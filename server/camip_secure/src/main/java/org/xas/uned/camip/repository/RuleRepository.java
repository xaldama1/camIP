package org.xas.uned.camip.repository;

import java.util.List;

import org.joda.time.DateTime;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.xas.uned.camip.bean.Rule;

public interface RuleRepository extends CrudRepository<Rule, Long> {
	@Query(" from Rule r where r.rule = ?1")
	List<Rule> findByRule(String rule);

	@Query(" from Rule r where r.activeuntil < ?1")
	List<Rule> findByTime(DateTime current);
}
