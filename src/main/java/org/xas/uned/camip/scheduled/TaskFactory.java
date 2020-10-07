package org.xas.uned.camip.scheduled;

import java.util.HashMap;
import java.util.Map;

public class TaskFactory {

	private final Map<String, Task> tasks = new HashMap<>();

	public void executeAll() {
		for (Task task : tasks.values()) {
			task.execute();
		}
	}

	public void addTask(final String name, final Task task) {
		tasks.put(name, task);
	}

}
