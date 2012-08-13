/*
 * This file is part of ScriptEngine.
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
package com.simplyian.voxelscript;

import java.io.File;
import java.io.FileInputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.logging.Level;

import org.mozilla.javascript.Context;
import org.mozilla.javascript.Scriptable;
import org.mozilla.javascript.ScriptableObject;
import org.spout.api.Spout;
import org.spout.api.plugin.CommonPlugin;

import com.simplyian.voxelscript.modules.Commands;
import com.simplyian.voxelscript.modules.Events;

public class VoxelScriptPlugin extends CommonPlugin {
	private File baseDir;

	private Commands commands;
	private Events events;

	@Override
	public void onEnable() {
		baseDir = new File(getDataFolder(), "scripts/");

		if (!baseDir.exists()) {
			if (!baseDir.mkdirs()) {
				getLogger().log(Level.SEVERE, "Could not create the scripts directory!");
			}
		}

		commands = new Commands(this);
		events = new Events(this);

		setupRhino();

		getLogger().log(Level.INFO, "SpoutScript enabled!");
	}

	@Override
	public void onDisable() {
		getLogger().log(Level.INFO, "SpoutScript disabled!");
	}

	/**
	 * Load all scripts in the scripts folder.
	 */
	private void setupRhino() {
		Map<String, String> scripts = new HashMap<String, String>();

		for (File file : baseDir.listFiles()) {
			if (!file.getName().endsWith(".js")) {
				continue;
			}

			int ch;
			StringBuffer strContent = new StringBuffer("");
			FileInputStream fin = null;
			try {
				fin = new FileInputStream(file);
				while ((ch = fin.read()) != -1) {
					strContent.append((char) ch);
				}
				fin.close();
			} catch (Exception ex) {
				getLogger().log(Level.SEVERE, "Could not read file " + file.getName() + "!", ex);
			}

			String script = strContent.toString();

			scripts.put(file.getName(), script);
		}

		try {
			// Initialize the top scope
			Context cx = Context.enter();
			Scriptable mainScope = cx.initStandardObjects();

			Object wrappedEngine = Context.javaToJS(Spout.getEngine(), mainScope);
			ScriptableObject.putProperty(mainScope, "engine", wrappedEngine);

			Object wrappedCommands = Context.javaToJS(commands, mainScope);
			ScriptableObject.putProperty(mainScope, "commands", wrappedCommands);

			Object wrappedEvents = Context.javaToJS(events, mainScope);
			ScriptableObject.putProperty(mainScope, "events", wrappedEvents);

			for (Entry<String, String> entry : scripts.entrySet()) {
				Scriptable scriptScope = cx.newObject(mainScope);
				scriptScope.setPrototype(mainScope);
				scriptScope.setParentScope(null);

				ScriptableObject.putProperty(scriptScope, "meta", null);
				ScriptableObject.putProperty(scriptScope, "exports", null);

				cx.evaluateString(scriptScope, entry.getValue(), entry.getKey(), 1, null);

				Scriptable meta = (Scriptable) ScriptableObject.getProperty(scriptScope, "meta");
				Object nameObj = meta.get("name", meta);
				if (!(nameObj instanceof String)) {
					getLogger().log(Level.WARNING, "Could not load the script '" + entry.getValue() + "' due to there not being a name!");
				}
				String name = (String) nameObj;

				Scriptable exports = (Scriptable) ScriptableObject.getProperty(scriptScope, "exports");
				System.out.println("Loaded '" + name + "'");
			}
		} finally {
			Context.exit();
		}
	}
}
