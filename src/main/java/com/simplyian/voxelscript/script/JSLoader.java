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
import java.io.FileInputStream;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Level;

import org.mozilla.javascript.Context;
import org.mozilla.javascript.EcmaError;
import org.mozilla.javascript.Scriptable;
import org.mozilla.javascript.ScriptableObject;

import com.simplyian.voxelscript.VoxelScriptPlugin;
import java.io.IOException;
import org.apache.commons.io.FileUtils;
import org.mozilla.javascript.*;

public class JSLoader {
	private final VoxelScriptPlugin plugin;

	// Quick and dirty way to check if something is already being loaded
	private Set<String> loading = new HashSet<String>();

	private Context cx;

	private Scriptable scope;

	public JSLoader(VoxelScriptPlugin plugin) {
		this.plugin = plugin;
	}

	/**
	 * Initializes the script loader, preparing it for loading scripts.
	 */
	public void begin() {
		cx = Context.enter();
		scope = cx.initStandardObjects();
		plugin.getModuleManager().setupModuleFunction(scope);
		plugin.getPackageManager().setupPackageFunction(scope);
		plugin.getScriptManager().setupScriptFunction(scope);
	}

	/**
	 * Ends the loading process.
	 */
	public void end() {
		Context.exit();
	}

	public Package loadPackage(String name, File dir) throws IOException {
		throw new UnsupportedOperationException("Not yet implemented");
	}

	/**
	 * Loads the given script.
	 *
	 * @param cx
	 * @param scope
	 * @param name
	 * @param file
	 * @return
	 */
	public Script loadScript(String name, File file) throws IOException {
		return loadScript(name, FileUtils.readFileToString(file));
	}

	/**
	 * Loads the given script.
	 *
	 * @param cx
	 * @param scope The scope to load the script into.
	 * @param name
	 * @param script
	 * @return The loaded script.
	 */
	private Script loadScript(String name, String script) {
		if (loading.contains(name.toLowerCase())) {
			plugin.getLogger().log(Level.WARNING, "Circular dependency detected for a script wanting '" + name + "'. Stopping...");
			return null;
		}

		try {
			Scriptable scriptScope = cx.newObject(scope);
			scriptScope.setPrototype(scope);
			scriptScope.setParentScope(null);

			// Execute the JS
			loading.add(name.toLowerCase());
			cx.evaluateString(scriptScope, "var exports = {};", name, 1, null);
			cx.evaluateString(scriptScope, script, name, 1, null);
			loading.remove(name.toLowerCase());

			// Get the exports
			Object exportsObj = ScriptableObject.getProperty(scriptScope, "exports");
			Scriptable exports = null;
			if (exportsObj instanceof Scriptable) {
				exports = (Scriptable) exportsObj;
			}

			// Create the script
			return new Script(name, exports);
		} catch (EcmaError ex) {
			plugin.getLogger().log(Level.SEVERE, "JavaScript error: could not finish running the script '" + name + "'.", ex);
			return null;
		} catch (Exception ex) {
			plugin.getLogger().log(Level.SEVERE, "An error occured while executing the script '" + name + "'.", ex);
			return null;
		}
	}
}
