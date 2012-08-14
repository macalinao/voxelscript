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

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;

import org.mozilla.javascript.Scriptable;
import org.spout.api.Spout;

import com.simplyian.voxelscript.VoxelScriptPlugin;
import com.simplyian.voxelscript.modules.commands.Commands;
import com.simplyian.voxelscript.modules.events.Events;

public class ModuleManager {
	private Map<String, Object> modules = new HashMap<String, Object>();
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
		registerModule("commands", new Commands(plugin));
		registerModule("engine", Spout.getEngine());
		registerModule("events", new Events(plugin));
	}

	/**
	 * Registers a module with this ModuleManager.
	 * 
	 * @param name
	 * @param module
	 */
	public void registerModule(String name, Object module) {
		Object prev = modules.put(name, module);
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
	public Object getModule(String name) {
		return modules.get(name);
	}
}
