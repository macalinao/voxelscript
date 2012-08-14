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

import java.util.logging.Level;
import java.util.logging.Logger;

import org.mozilla.javascript.Scriptable;

import com.simplyian.voxelscript.VoxelScriptPlugin;

/**
 * The script meta.
 */
public class ScriptMeta {
	private String name = "";
	private String description = "Yet another VoxelScript script";
	private String version = "Unknown";
	private String author = "Anonymous";

	private ScriptMeta() {
	}

	public String getName() {
		return name;
	}

	public String getDescription() {
		return description;
	}

	public String getVersion() {
		return version;
	}

	public String getAuthor() {
		return author;
	}

	/**
	 * Loads a given meta object and file name into a ScriptMeta.
	 * 
	 * @param fileName
	 * @param meta
	 * @return
	 */
	public static ScriptMeta loadMeta(String fileName, Scriptable meta) {
		ScriptMeta ret = new ScriptMeta();

		if (meta == null) {
			return ret;
		}

		// Name
		Object nameObj = meta.get("name", meta);
		if (nameObj == null || !(nameObj instanceof String)) {
			ret.name = fileName;
		} else {
			ret.name = nameObj.toString();
		}

		// Description
		Object descObj = meta.get("description", meta);
		if (descObj != null && descObj instanceof String) {
			ret.description = descObj.toString();
		}

		// Version
		Object versionObj = meta.get("version", meta);
		if (versionObj != null && versionObj instanceof String) {
			ret.version = versionObj.toString();
		}

		// Author
		Object authorObj = meta.get("author", meta);
		if (authorObj != null && authorObj instanceof String) {
			ret.author = authorObj.toString();
		}

		return ret;
	}
}
