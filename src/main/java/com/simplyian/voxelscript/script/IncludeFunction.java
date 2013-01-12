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
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.io.FileUtils;
import org.mozilla.javascript.*;

/**
 * Includes a script within a package.
 */
public class IncludeFunction extends BaseFunction {
	private final VoxelScriptPlugin plugin;

	private final File packageFolder;

	private final JSLoader loader;

	private final Scriptable scope;

	public IncludeFunction(VoxelScriptPlugin plugin, File packageFolder, JSLoader loader, Scriptable scope) {
		this.plugin = plugin;
		this.packageFolder = packageFolder;
		this.loader = loader;
		this.scope = scope;
	}

	@Override
	public Object call(Context cx, Scriptable scope, Scriptable thisObj, Object[] args) {
		if (args.length < 1) {
			return null;
		}

		String scriptName = args[0].toString();
		File file = new File(packageFolder.getPath(), scriptName);
		if (!file.exists()) {
			file = new File(packageFolder.getPath(), scriptName + ".js");
			if (!file.exists()) {
				return null;
			}
		}

		if (file.isDirectory()) {
			file = new File(file.getPath(), "index.js");
			if (!file.exists()) {
				return null;
			}
		}

		String script = null;
		try {
			script = FileUtils.readFileToString(file);
		} catch (IOException ex) {
			plugin.getLogger().log(Level.SEVERE, "Could not read included file '" + file.getPath() + "'!");
			return null;
		}

		return loader.runScript(file.getPath(), script, scope);
	}

	@Override
	public Scriptable construct(Context cx, Scriptable scope, Object[] args) {
		throw new RuntimeException("Cannot invoke import() as a constructor!");
	}
}
