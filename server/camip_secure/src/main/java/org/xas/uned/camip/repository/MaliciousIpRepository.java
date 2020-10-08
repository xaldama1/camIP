package org.xas.uned.camip.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.xas.uned.camip.bean.MaliciousIp;
import org.xas.uned.camip.bean.MaliciousIpOrigin;

public interface MaliciousIpRepository extends CrudRepository<MaliciousIp, Long> {

	List<MaliciousIp> findByOrigin(MaliciousIpOrigin origin);

}
