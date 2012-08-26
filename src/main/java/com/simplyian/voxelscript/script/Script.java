/*
 * This file is part of VoxelScript.
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

import org.mozilla.javascript.Scriptable;

public class Script {
	private final ScriptMeta meta;
	private final Scriptable exports;

	public Script(ScriptMeta meta, Scriptable exports) {
		this.meta = meta;
		this.exports = exports;
	}

	public ScriptMeta getMeta() {
		return meta;
	}

	public Scriptable getExports() {
		return exports;
	}

	public String getName() {
		return meta.getName();
	}

	@Override
	public String toString() {
		return "Script: " + getName();
	}
}