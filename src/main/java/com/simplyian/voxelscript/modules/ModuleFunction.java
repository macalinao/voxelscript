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
package com.simplyian.voxelscript.modules;

import org.mozilla.javascript.BaseFunction;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.Scriptable;

/**
 * Function to get a module.
 */
public class ModuleFunction extends BaseFunction {
	private final ModuleManager moduleManager;

	public ModuleFunction(ModuleManager moduleManager) {
		this.moduleManager = moduleManager;
	}

	@Override
	public Object call(Context cx, Scriptable scope, Scriptable thisObj, Object[] args) {
		if (args.length < 1) {
			return null;
		}

		String moduleName = args[0].toString();
		Object module = moduleManager.getModule(moduleName);

		if (module != null) {
			return Context.javaToJS(module, scope);
		}

		return null;
	}

	@Override
	public Scriptable construct(Context cx, Scriptable scope, Object[] args) {
		throw new RuntimeException("Cannot invoke module() as a constructor!");
	}
}
