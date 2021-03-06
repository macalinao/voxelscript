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

import org.mozilla.javascript.Scriptable;

/**
 * A single, executable JavaScript file.
 */
public class Script {
	private final String name;
	private final Scriptable exports;

	Script(String name, Scriptable exports) {
		this.name = name;
		this.exports = exports;
	}

	public Scriptable getExports() {
		return exports;
	}

	public String getName() {
		return name;
	}

	@Override
	public String toString() {
		return "Script: " + getName();
	}
}
