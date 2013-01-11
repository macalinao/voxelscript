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

import com.simplyian.voxelscript.VoxelScriptPlugin;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import org.mozilla.javascript.Scriptable;

/**
 * Handles packages.
 */
public class PackageManager {
	private final VoxelScriptPlugin plugin;

	private final JSLoader loader;

	private final PackageFunction pf;

	private final Map<String, Package> packages = new HashMap<String, Package>();

	private File packageFolder;

	public PackageManager(VoxelScriptPlugin plugin, JSLoader loader) {
		this.plugin = plugin;
		this.loader = loader;

		pf = new PackageFunction(this);
		packageFolder = new File(plugin.getDataFolder(), "packages/");
	}

	public void setupPackageFunction(Scriptable scope) {
		scope.put("package", scope, pf);
	}

	/**
	 * Loads all packages.
	 *
	 * @return
	 */
	public void loadPackages() {
		for (File file : packageFolder.listFiles()) {
			if (!file.isDirectory()) {
				continue;
			}

			String name = file.getName();
			if (name == null) {
				continue;
			}

			getPackage(name);
		}
	}

	public Package getPackage(String name) {
		name = name.toLowerCase();
		Package p = packages.get(name);
		if (p == null) {
			File dir = getPackageDir(name);

			try {
				p = loader.loadPackage(name, dir);
			} catch (IOException ex) {
				plugin.getLogger().log(Level.SEVERE, "Could not read package due to error!");
			}

			packages.put(name, p);
		}
		return p;
	}

	/**
	 * Gets the directory corresponding with the given package name.
	 *
	 * @param packageName
	 * @return
	 */
	public File getPackageDir(String packageName) {
		return new File(packageFolder.getPath(), packageName + "/");
	}
}
