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
package com.simplyian.voxelscript.modules.commands;

import java.util.logging.Level;

import org.spout.api.command.Command;
import org.spout.api.command.CommandContext;
import org.spout.api.command.CommandExecutor;
import org.spout.api.command.CommandSource;
import org.spout.api.exception.CommandException;

import com.simplyian.voxelscript.VoxelScriptPlugin;

/**
 * The commands module, enabling commands to be created idiomatically.
 */
public class Commands {
	private final VoxelScriptPlugin plugin;

	public Commands(VoxelScriptPlugin plugin) {
		this.plugin = plugin;
	}

	/**
	 * Defines a command with the given executor.
	 * 
	 * @param name
	 * @param executor
	 */
	public void define(String name, final VSCommandExecutor executor) {
		Command cmd = plugin.getEngine().getRootCommand().getChild(name);
		if (cmd != null) {
			plugin.getLogger().log(Level.WARNING, "The command '" + name + "' already exists; skipping registration.");
			return;
		}

		cmd = plugin.getEngine().getRootCommand().addSubCommand(plugin, name);
		cmd.setExecutor(new CommandExecutor() {

			@Override
			public void processCommand(CommandSource source, Command command, CommandContext args) throws CommandException {
				executor.execute(source, args);
			}
		});
	}
}
