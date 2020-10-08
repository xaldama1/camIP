package org.xas.uned.camip;

import java.util.Map;
import java.util.Map.Entry;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.xas.uned.camip.scheduled.Task;
import org.xas.uned.camip.scheduled.TaskFactory;

@Configuration
public class ApplicationConfig {

	private TaskFactory taskFactory = null;

	@Autowired
	private ApplicationContext applicationContext;

	@EventListener
	public void handleContextStarted(final ContextRefreshedEvent event) {
		if (taskFactory != null) {
			taskFactory.executeAll();
		}
	}

	@Bean
	public TaskFactory taskFactory() {
		taskFactory = new TaskFactory();

		Map<String, Task> taskMap = applicationContext.getBeansOfType(Task.class);
		if (taskMap != null) {
			for (Entry<String, Task> taskEntry : taskMap.entrySet()) {
				taskFactory.addTask(taskEntry.getKey(), taskEntry.getValue());
			}
		}

		return taskFactory;
	}

}
