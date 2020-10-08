package org.xas.uned.camip.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.xas.uned.camip.bean.Devicechecker;

public interface DeviceCheckerRepository extends CrudRepository<Devicechecker, Long> {

	@Query(" from Devicechecker dc where dc.device.id = ?1 and dc.checker = ?2")
	Devicechecker findByDeviceIdAndChecker(long deviceId, String checker);
}
