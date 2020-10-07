package org.xas.uned.camip;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;
import org.xas.uned.camip.facade.DeviceFacade;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = Application.class, loader = AnnotationConfigContextLoader.class)
public class DevicesTest {

	@Autowired
	private DeviceFacade deviceFacade;

	@Test
	public void getDevidesTest() {

		deviceFacade.updateCurrentConnectedList();

	}
}
