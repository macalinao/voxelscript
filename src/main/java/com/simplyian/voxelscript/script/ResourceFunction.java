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
import org.mozilla.javascript.BaseFunction;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.Scriptable;

/**
 * Returns a File corresponding to the filename provided.
 */
public class ResourceFunction extends BaseFunction {
	private final VoxelScriptPlugin plugin;

	private final File packageFolder;

	public ResourceFunction(VoxelScriptPlugin plugin, File packageFolder) {
		this.plugin = plugin;
		this.packageFolder = packageFolder;
	}

	@Override
	public Object call(Context cx, Scriptable scope, Scriptable thisObj, Object[] args) {
		if (args.length < 1) {
			return null;
		}

		return new File(packageFolder, args[0].toString());
	}

	@Override
	public Scriptable construct(Context cx, Scriptable scope, Object[] args) {
		throw new RuntimeException("Cannot invoke import() as a constructor!");
	}
}
