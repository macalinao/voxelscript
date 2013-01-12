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

import java.util.Map;

/**
 * Describes a package.
 */
public class PackageDescription {
	private String name;

	private String desc;

	private String author;

	private String version;

	private String main;

	private PackageDescription() {
	}

	public String getAuthor() {
		return author;
	}

	public String getDesc() {
		return desc;
	}

	public String getMain() {
		return main;
	}

	public String getName() {
		return name;
	}

	public String getVersion() {
		return version;
	}

	static PackageDescription load(String name, Map<String, Object> args) {
		PackageDescription pd = new PackageDescription();
		if (args == null) {
			return pd;
		}

		Object pdName = args.get("name");
		if (pdName != null) {
			pd.name = name;
		} else {
			pd.name = pdName.toString();
		}

		Object desc = args.get("desc");
		if (desc != null) {
			pd.desc = desc.toString();
		}

		Object author = args.get("author");
		if (author != null) {
			pd.author = author.toString();
		}

		Object version = args.get("version");
		if (version != null) {
			pd.version = version.toString();
		}

		Object main = args.get("main");
		if (main != null) {
			pd.main = main.toString();
		}

		return pd;
	}
}
