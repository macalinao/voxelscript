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
	private Set<String> loadingPackages = new HashSet<String>();

	private Set<String> loadingScripts = new HashSet<String>();

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

	/**
	 * Loads a package provided its directory.
	 *
	 * @param name
	 * @param dir
	 * @return
	 * @throws IOException
	 */
	public Package loadPackage(String name, File dir) throws IOException {
		Scriptable packageScope = cx.newObject(scope);
		packageScope.setPrototype(scope);
		packageScope.setParentScope(scope);
		packageScope.put("include", scope, new IncludeFunction(plugin, dir, this, packageScope));

		File mainFile = null;
		PackageDescription desc = null;

		File packageJson = new File(dir.getPath(), "package.json");
		if (!packageJson.exists()) {
			plugin.getLogger().log(Level.WARNING, "A package.json was not found for the package '" + name + "'.");
			mainFile = new File(dir.getPath(), "index.js");
			if (!mainFile.exists()) {
				plugin.getLogger().log(Level.WARNING, "An index.js was not found for the package '" + name + "'. This package was not loaded.");
				return null;
			}
			desc = PackageDescription.load(name, null);
		} else {
			// TODO load package.json
		}

		String script = FileUtils.readFileToString(mainFile);
		if (loadingPackages.contains(name.toLowerCase())) {
			plugin.getLogger().log(Level.WARNING, "Circular dependency detected for something wanting package '" + name + "'. Stopping...");
			return null;
		}

		loadingPackages.add(name.toLowerCase());
		Scriptable exports = runScript(name, script, packageScope);
		loadingPackages.remove(name.toLowerCase());
		if (exports == null) {
			return null;
		}
		return new Package(desc, exports);
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
		String script = FileUtils.readFileToString(file);
		if (loadingScripts.contains(name.toLowerCase())) {
			plugin.getLogger().log(Level.WARNING, "Circular dependency detected for something wanting script '" + name + "'. Stopping...");
			return null;
		}

		loadingScripts.add(name.toLowerCase());
		Scriptable exports = runScript(name, script);
		loadingScripts.remove(name.toLowerCase());
		if (exports == null) {
			return null;
		}
		return new Script(name, exports);
	}

	/**
	 * Runs a script under the default scope.
	 *
	 * @param name
	 * @param script
	 * @return
	 */
	Scriptable runScript(String name, String script) {
		return runScript(name, script, scope);
	}

	/**
	 * Runs a script.
	 *
	 * @param name
	 * @param script
	 * @param importFunction The import function.
	 * @return The exports of the script.
	 */
	Scriptable runScript(String name, String script, Scriptable parentScope) {
		try {
			Scriptable scriptScope = cx.newObject(parentScope);
			scriptScope.setPrototype(parentScope);
			scriptScope.setParentScope(parentScope);

			// Execute the JS
			cx.evaluateString(scriptScope, "var exports = {};", name, 1, null);
			cx.evaluateString(scriptScope, script, name, 1, null);

			// Get the exports
			Object exportsObj = ScriptableObject.getProperty(scriptScope, "exports");
			Scriptable exports = null;
			if (exportsObj instanceof Scriptable) {
				exports = (Scriptable) exportsObj;
			}

			// Create the script
			return exports;
		} catch (EcmaError ex) {
			plugin.getLogger().log(Level.SEVERE, "JavaScript error: could not finish running the script '" + name + "'.", ex);
			return null;
		} catch (Exception ex) {
			plugin.getLogger().log(Level.SEVERE, "An error occured while executing the script '" + name + "'.", ex);
			return null;
		}
	}
}
