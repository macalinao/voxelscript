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
package com.simplyian.voxelscript.script;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.logging.Level;

import org.mozilla.javascript.Context;
import org.mozilla.javascript.Scriptable;

import com.google.common.collect.Lists;
import com.simplyian.voxelscript.VoxelScriptPlugin;
import java.io.IOException;
import java.util.logging.Logger;

public class ScriptManager {
	private final VoxelScriptPlugin plugin;

	private final JSLoader loader;

	private final Map<String, Script> scripts = new HashMap<String, Script>();

	private File scriptFolder;

	public ScriptManager(VoxelScriptPlugin plugin, JSLoader loader) {
		this.plugin = plugin;
		this.loader = loader;

		scriptFolder = new File(plugin.getDataFolder(), "scripts/");

		if (!scriptFolder.exists() && !scriptFolder.mkdirs()) {
			plugin.getLogger().log(Level.SEVERE, "Could not create the following script directory: " + scriptFolder.getPath());
		}
	}

	/**
	 * Loads all scripts.
	 *
	 * @return
	 */
	public void loadScripts() {
		for (File file : scriptFolder.listFiles()) {
			if (file.isDirectory()) {
				continue;
			}

			String name = getScriptName(file);
			if (name == null) {
				continue;
			}

			getScript(name);
		}
	}

	/**
	 * Gets the script with the given name.
	 *
	 * @param name
	 * @return
	 */
	public Script getScript(String name) {
		name = name.toLowerCase();
		Script s = scripts.get(name);
		if (s == null) {
			File file = getScriptFile(name);

			try {
				s = loader.loadScript(name, file);
			} catch (IOException ex) {
				plugin.getLogger().log(Level.SEVERE, "Could not read script file due to error!");
			}

			scripts.put(name, s);
		}
		return s;
	}

	/**
	 * Gets the file corresponding with the given script name.
	 *
	 * @param scriptName
	 * @return
	 */
	public File getScriptFile(String scriptName) {
		if (scriptName.endsWith(".js")) {
			return new File(scriptFolder.getPath(), scriptName);
		}
		return new File(scriptFolder.getPath(), scriptName + ".js");
	}

	/**
	 * Gets the name of the script from its file.
	 *
	 * @param file
	 * @return
	 */
	public static String getScriptName(File file) {
		if (!file.isFile()) {
			return null;

		}

		String fileName = file.getName();
		if (!fileName.endsWith(".js")) {
			return null;
		}

		return fileName.substring(0, fileName.length() - ".js".length());
	}
}
