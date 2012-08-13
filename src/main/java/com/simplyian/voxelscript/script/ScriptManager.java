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
package com.simplyian.voxelscript.script;

import java.util.List;

import com.simplyian.voxelscript.VoxelScriptPlugin;

public class ScriptManager {
	private final VoxelScriptPlugin plugin;
	private final ScriptLoader loader;
	
	public ScriptManager(VoxelScriptPlugin plugin) {
		this.plugin = plugin;
		this.loader = new ScriptLoader(plugin);
	}
	
	public void loadScripts() {
		List<Script> scripts = loader.loadScripts();
		System.out.println(scripts);
	}
}
