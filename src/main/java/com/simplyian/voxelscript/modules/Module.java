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

import org.spout.api.plugin.Plugin;

/**
 * An interface to easily provide a Java object to VoxelScript.
 */
public final class Module {
	private final Plugin plugin;

	private final String name;

	private final Object object;

	Module(Plugin plugin, String name, Object object) {
		this.plugin = plugin;
		this.name = name;
		this.object = object;
	}

	public Plugin getPlugin() {
		return plugin;
	}

	public String getName() {
		return name;
	}

	/**
	 * Gets the full name of this module.
	 *
	 * @return
	 */
	public String getFullName() {
		String ln = name.toLowerCase();
		if (plugin.getName().equalsIgnoreCase("VoxelScript") || ln != "main") {
			return ln;
		}
		return plugin.getName().toLowerCase() + ":" + ln;
	}

	public Object getObject() {
		return object;
	}
}
