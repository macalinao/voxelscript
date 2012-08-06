/*
 * This file is part of SpoutScript.
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
package com.simplyian.spoutscript;

import java.util.logging.Level;

import org.spout.api.plugin.CommonPlugin;

public class SpoutScriptPlugin extends CommonPlugin {

	@Override
	public void onEnable() {
		getLogger().log(Level.INFO, "SpoutScript enabled!");
	}

	@Override
	public void onDisable() {
		getLogger().log(Level.INFO, "SpoutScript disabled!");
	}

}
