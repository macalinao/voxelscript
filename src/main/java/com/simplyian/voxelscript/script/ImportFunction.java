package com.simplyian.voxelscript.script;

import com.simplyian.voxelscript.VoxelScriptPlugin;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.io.FileUtils;
import org.mozilla.javascript.*;

/**
 * Imports a script within a package.
 */
public class ImportFunction extends BaseFunction {
	private final VoxelScriptPlugin plugin;

	private final File packageFolder;

	private final JSLoader loader;

	private final Scriptable scope;

	public ImportFunction(VoxelScriptPlugin plugin, File packageFolder, JSLoader loader, Scriptable scope) {
		this.plugin = plugin;
		this.packageFolder = packageFolder;
		this.loader = loader;
		this.scope = scope;
	}

	@Override
	public Object call(Context cx, Scriptable scope, Scriptable thisObj, Object[] args) {
		if (args.length < 1) {
			return null;
		}

		String scriptName = args[0].toString();
		File file = new File(packageFolder.getPath(), scriptName);
		if (!file.exists()) {
			file = new File(packageFolder.getPath(), scriptName + ".js");
			if (!file.exists()) {
				return null;
			}
		}

		if (file.isDirectory()) {
			file = new File(file.getPath(), "index.js");
			if (!file.exists()) {
				return null;
			}
		}

		String script = null;
		try {
			script = FileUtils.readFileToString(file);
		} catch (IOException ex) {
			plugin.getLogger().log(Level.SEVERE, "Could not read imported file '" + file.getPath() + "'!");
			return null;
		}

		return loader.runScript(file.getPath(), script, scope);
	}

	@Override
	public Scriptable construct(Context cx, Scriptable scope, Object[] args) {
		throw new RuntimeException("Cannot invoke import() as a constructor!");
	}
}
