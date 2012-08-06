/*
 * This file is part of SuperFlow.
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
package com.simplyian.superflow;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.logging.Level;

import org.mozilla.javascript.Context;
import org.mozilla.javascript.Scriptable;
import org.mozilla.javascript.ScriptableObject;
import org.spout.api.Spout;
import org.spout.api.plugin.CommonPlugin;

public class SuperFlowPlugin extends CommonPlugin {
	private File baseDir;

	private Events events;

	@Override
	public void onEnable() {
		baseDir = new File(getDataFolder(), "scripts/");

		if (!baseDir.exists()) {
			if (!baseDir.mkdirs()) {
				getLogger().log(Level.SEVERE, "Could not create the scripts directory!");
			}
		}

		events = new Events(this);

		setupRhino();

		getLogger().log(Level.INFO, "SpoutScript enabled!");
	}

	@Override
	public void onDisable() {
		getLogger().log(Level.INFO, "SpoutScript disabled!");
	}

	/**
	 * Load all scripts in the scripts folder.
	 */
	private void setupRhino() {
		Map<String, String> scripts = new HashMap<String, String>();

		for (File file : baseDir.listFiles()) {
			if (!file.getName().endsWith(".js")) {
				continue;
			}

			int ch;
			StringBuffer strContent = new StringBuffer("");
			FileInputStream fin = null;
			try {
				fin = new FileInputStream(file);
				while ((ch = fin.read()) != -1) {
					strContent.append((char) ch);
				}
				fin.close();
			} catch (Exception ex) {
				getLogger().log(Level.SEVERE, "Could not read file " + file.getName() + "!", ex);
			}

			String script = strContent.toString();

			scripts.put(file.getName(), script);
		}

		Context cx = Context.enter();
		try {
			Scriptable scope = cx.initStandardObjects();

			Object wrappedEngine = Context.javaToJS(Spout.getEngine(), scope);
			ScriptableObject.putProperty(scope, "engine", wrappedEngine);

			Object wrappedEvents = Context.javaToJS(events, scope);
			ScriptableObject.putProperty(scope, "events", wrappedEvents);

			for (Entry<String, String> entry : scripts.entrySet()) {
				cx.evaluateString(scope, entry.getValue(), entry.getKey(), 1, null);
			}
		} finally {
			Context.exit();
		}
	}
}
