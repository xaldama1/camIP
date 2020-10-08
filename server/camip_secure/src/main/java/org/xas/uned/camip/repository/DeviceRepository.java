package org.xas.uned.camip.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.xas.uned.camip.bean.Device;

public interface DeviceRepository extends CrudRepository<Device, Long> {

	List<Device> findByMac(String mac);

	@Modifying
	@Query("delete from Device d where d.ip not in ?1")
	void deleteByIpNotIn(List<String> ips);
}
