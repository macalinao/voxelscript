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
package com.simplyian.voxelscript;

import java.util.logging.Level;

import org.spout.api.plugin.CommonPlugin;

import com.simplyian.voxelscript.modules.ModuleManager;
import com.simplyian.voxelscript.script.ScriptManager;

public class VoxelScriptPlugin extends CommonPlugin {
	private static VoxelScriptPlugin _instance;
	private ModuleManager moduleManager;
	private ScriptManager scriptManager;

	@Override
	public void onEnable() {
		_instance = this;

		moduleManager = new ModuleManager(this);
		
		getLogger().log(Level.INFO, "Loading scripts...");
		scriptManager = new ScriptManager(this);
		scriptManager.loadScripts();

		getLogger().log(Level.INFO, "VoxelScript enabled!");
	}

	@Override
	public void onDisable() {
		getLogger().log(Level.INFO, "VoxelScript disabled!");
	}

	public ModuleManager getModuleManager() {
		return moduleManager;
	}

	public ScriptManager getScriptManager() {
		return scriptManager;
	}

	public static VoxelScriptPlugin getInstance() {
		return _instance;
	}
}
