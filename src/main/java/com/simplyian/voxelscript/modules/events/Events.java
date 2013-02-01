/*
 * This file is part of VoxelScript.
 *
 * Copyright (c) 2012-2013, THEDevTeam <http://thedevteam.org/>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.simplyian.voxelscript.modules.events;

import com.simplyian.voxelscript.util.GetClasses;
import com.google.common.base.Joiner;
import com.simplyian.voxelscript.VoxelScriptPlugin;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import org.spout.api.Spout;
import org.spout.api.event.Event;
import org.spout.api.event.EventExecutor;
import org.spout.api.event.Order;
import org.spout.api.plugin.Plugin;
import org.spout.api.plugin.PluginManager;

/**
 * The Events module.
 *
 * <p> Provides an easy way to register events. </p>
 */
public class Events {
	private final VoxelScriptPlugin plugin;

	private final Map<String, Class<? extends Event>> events = new HashMap<String, Class<? extends Event>>();

	public Events(VoxelScriptPlugin plugin) {
		this.plugin = plugin;

		initializeCoreEvents();
	}

	private void initializeCoreEvents() {
		defineAll("spout", plugin.getClassLoader(), "org.spout.api.event");

		PluginManager pm = Spout.getPluginManager();

		Plugin vanilla = pm.getPlugin("Vanilla");
		if (vanilla != null) {
			defineAll("vanilla", vanilla.getClass().getClassLoader(), "org.spout.vanilla.event");
		}
	}

	/**
	 * Registers a new EventExecutor.
	 *
	 * @param event
	 * @param executor
	 */
	public void on(String event, EventExecutor executor) {
		Class<? extends Event> clazz = events.get(event.toLowerCase());
		if (clazz == null) {
			plugin.getLogger().log(Level.WARNING, "Could not register event handler for event '" + event + "' because it does not exist.");
			return;
		}
		Spout.getEventManager().registerEvent(clazz, Order.DEFAULT, executor, plugin);
	}

	/**
	 * Defines an event from a plugin.
	 *
	 * @param plugin
	 * @param name
	 * @param clazz
	 */
	public void define(Plugin plugin, String name, Class<? extends Event> clazz) {
		define(plugin.getName() + ":" + name, clazz);
	}

	/**
	 * Defines an event.
	 *
	 * @param name
	 * @param clazz
	 */
	private void define(String name, Class<? extends Event> clazz) {
		if (events.put(name.toLowerCase(), clazz) != null) {
			plugin.getLogger().log(Level.WARNING, "Duplicate event registered: '" + name + "' for '" + clazz.getCanonicalName() + "'.");
		}
	}

	/**
	 * Defines all events in a given package recursively. Note: Event classes
	 * must end in "Event".
	 *
	 * @param pkg
	 */
	public void defineAll(Plugin plugin, String pkg) {
		defineAll(plugin.getName(), plugin.getClass().getClassLoader(), pkg);
	}

	/**
	 * Defines all events in a given package and all of its subpackages. Note:
	 * Event classes must end in "Event".
	 *
	 * @param prefix The prefix of the event.
	 * @param pkg
	 */
	private void defineAll(String prefix, ClassLoader loader, String pkg) {
		for (Class<?> evtClass : GetClasses.getClasses(pkg, loader, true)) {
			if (!evtClass.isAssignableFrom(Event.class)) {
				continue;
			}
			Class<Event> evtC = (Class<Event>) evtClass;
			String str = evtC.getSimpleName();

			String eventName = str.substring(0, str.length() - "Event".length());
			if (prefix != null) {
				eventName = prefix + ":" + eventName;
			}

			define(eventName, evtC);
		}
	}
}
