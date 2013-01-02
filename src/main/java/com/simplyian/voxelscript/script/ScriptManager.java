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

public class ScriptManager {
	private final VoxelScriptPlugin plugin;

	private final ScriptLoader loader;

	private final ScriptFunction sf;

	private final Map<String, Script> scripts = new HashMap<String, Script>();

	private Scriptable mainScope;

	private List<File> searchPaths;

	public ScriptManager(VoxelScriptPlugin plugin) {
		this.plugin = plugin;
		this.loader = new ScriptLoader(plugin);
		this.sf = new ScriptFunction(this);

		this.searchPaths = Lists.newArrayList(new File(plugin.getDataFolder(), "scripts/"));

		setupSearchPaths();
	}

	private void setupSearchPaths() {
		for (File path : searchPaths) {
			if (!path.exists() && !path.mkdirs()) {
				plugin.getLogger().log(Level.SEVERE, "Could not create the following script directory: " + path.getPath());
			}
		}
	}

	public void setupScriptFunction(Scriptable scope) {
		scope.put("script", scope, sf);
	}

	/**
	 * Loads all scripts.
	 *
	 * @return
	 */
	public void loadScripts() {
		try {
			// Initialize the main scope
			Context cx = Context.enter();
			mainScope = cx.initStandardObjects();
			plugin.getModuleManager().setupModuleFunction(mainScope);
			plugin.getScriptManager().setupScriptFunction(mainScope);

			for (File path : searchPaths) {
				for (File file : path.listFiles()) {
					String name = getScriptName(file);
					if (name == null) {
						continue;
					}

					loadScript(cx, mainScope, name, file);
				}
			}
			mainScope = null;
		} finally {
			Context.exit();
		}
	}

	/**
	 * Gets the script with the given name.
	 *
	 * @param name
	 * @return
	 */
	public Script getScript(String name) {
		Script s = scripts.get(name.toLowerCase());
		if (s == null) {
			// TODO load script if it isn't already loaded, and ensure that script doesn't get loaded more than once
		}
		return s;
	}

	/**
	 * Loads a script.
	 *
	 * @param cx
	 * @param scope
	 * @param name
	 * @param file
	 * @return
	 */
	public Script loadScript(Context cx, Scriptable scope, String name, File file) {
		Scriptable scriptScope = cx.newObject(scope);
		scriptScope.setPrototype(scope);
		scriptScope.setParentScope(null);

		Script s = loader.loadScript(cx, scope, name, file);
		if (s != null) {
			scripts.put(name.toLowerCase(), s);
		}
		return s;
	}

	public String getScriptName(File file) {
		if (file.isFile()) {
			String fileName = file.getName();
			if (fileName.endsWith(".js")) {
				return fileName.substring(0, fileName.length() - 3);
			}
		}
		return null;
	}
}
