/*
 * This file is part of VoxelScript.
 *
 * Copyright (c) 2012-2012, THEDevTeam <http://thedevteam.org/>
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
package com.simplyian.voxelscript.modules;

import java.lang.Object;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;

import org.mozilla.javascript.Scriptable;
import org.spout.api.Spout;

import com.simplyian.voxelscript.VoxelScriptPlugin;
import com.simplyian.voxelscript.modules.commands.Commands;
import com.simplyian.voxelscript.modules.events.Events;
import org.spout.api.plugin.Plugin;

public class ModuleManager {
	private Map<String, Module> modules = new HashMap<String, Module>();
	private final VoxelScriptPlugin plugin;
	private final ModuleFunction mf;

	public ModuleManager(VoxelScriptPlugin plugin) {
		this.plugin = plugin;
		this.mf = new ModuleFunction(this);

		registerDefaults();
	}

	/**
	 * Sets up the module function in the given scope.
	 * 
	 * @param scope
	 */
	public void setupModuleFunction(Scriptable scope) {
		scope.put("module", scope, mf);
	}

	/**
	 * Registers the default modules.
	 */
	private void registerDefaults() {
		registerModule(plugin, "commands", new Commands(plugin));
		registerModule(plugin, "engine", Spout.getEngine());
		registerModule(plugin, "plugins", Spout.getEngine().getPluginManager());
		registerModule(plugin, "events", new Events(plugin));
	}


	/**
	 * Registers a module with this ModuleManager.
	 * 
	 * @param plugin
	 * @param name
	 * @param object
	 */
	public void registerModule(Plugin plugin, String name, Object object) {
		Module module = new Module(plugin, name, object);
		Module prev = modules.put(module.getFullName(), module);
		if (prev != null) {
			plugin.getLogger().log(Level.WARNING, "Reregistering of module '" + name + "' from '" + prev.getClass().getCanonicalName() + "' to '" + module.getClass().getCanonicalName() + "'.");
		}
	}

	/**
	 * Gets a module by its name.
	 * 
	 * @param name
	 * @return
	 */
	public Module getModule(String name) {
		return modules.get(name);
	}
}
