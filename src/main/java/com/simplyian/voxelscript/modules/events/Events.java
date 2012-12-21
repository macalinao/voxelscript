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
package com.simplyian.voxelscript.modules.events;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.logging.Level;

import org.spout.api.Spout;
import org.spout.api.event.Event;
import org.spout.api.event.EventExecutor;
import org.spout.api.event.Order;
import org.spout.api.plugin.Plugin;
import org.spout.api.plugin.PluginManager;

import com.simplyian.voxelscript.VoxelScriptPlugin;

/**
 * The Events module.
 *
 * <p> Provides an easy way to register events. </p>
 */
public class Events {
    private final VoxelScriptPlugin plugin;

    private final Map<String, Class<? extends Event>> events = new HashMap<String, Class<? extends Event>>();

    public Events(VoxelScriptPlugin plugin) {
        this.plugin = plugin;

        initializeCoreEvents();
    }

    private void initializeCoreEvents() {
        loadEventsFromPackage("org.spout.api.event");

        PluginManager pm = Spout.getPluginManager();
        if (pm.getPlugin("Vanilla") != null) {
            loadEventsFromPackage("org.spout.vanilla.event");
        }
    }

    @SuppressWarnings("unchecked")
    public void loadEventsFromPackage(String pck) {
        for (Class<?> evtClass : getClasses(Package.getPackage(pck))) {
            if (evtClass.isAssignableFrom(Event.class)) {
                Class<Event> evtC = (Class<Event>) evtClass;
                String str = evtC.getSimpleName();
                define(str.substring(0, str.length() - "Event".length()), evtC);
            }
        }
    }

    /**
     * Registers a new EventExecutor.
     *
     * @param event
     * @param executor
     */
    public void on(String event, EventExecutor executor) {
        Class<? extends Event> clazz = events.get(event.toLowerCase());
        if (clazz == null) {
            plugin.getLogger().log(Level.WARNING, "Could not register event '" + event + "' because it does not exist.");
            return;
        }
        Spout.getEventManager().registerEvent(clazz, Order.LATEST, executor, plugin);
    }

    /**
     * Defines an event from a plugin.
     *
     * @param plugin
     * @param name
     * @param clazz
     */
    public void define(Plugin plugin, String name, Class<? extends Event> clazz) {
        define(plugin.getName() + ":" + name, clazz);
    }

    /**
     * Defines an event.
     *
     * @param name
     * @param clazz
     */
    private void define(String name, Class<? extends Event> clazz) {
        if (events.put(name.toLowerCase(), clazz) != null) {
            plugin.getLogger().log(Level.WARNING, "Duplicate event registered: '" + name + "' for '" + clazz.getCanonicalName() + "'.");
        }
    }

    private static List<Class<?>> getClasses(Package pkg) {
        List<Class<?>> classes = new ArrayList<Class<?>>();

        String pkgname = pkg.getName();
        String relPath = pkgname.replace('.', '/');

        // Get a File object for the package
        URL resource = ClassLoader.getSystemClassLoader().getResource(relPath);
        if (resource == null) {
            throw new RuntimeException("Unexpected problem: No resource for " + relPath);
        }

        resource.getPath();
        if (resource.toString().startsWith("jar:")) {
            processJarfile(resource, pkgname, classes);
        } else {
            processDirectory(new File(resource.getPath()), pkgname, classes);
        }

        List<Package> ps = new ArrayList<Package>();
        for (Package pack : Package.getPackages()) {
            if (pack.getName().startsWith(pkg.getName())) {
                ps.add(pack);
            }
        }

        return classes;
    }

    private static void processDirectory(File directory, String pkgname, List<Class<?>> classes) {
        // Get the list of the files contained in the package
        String[] files = directory.list();
        for (String fileName : files) {
            String className = null;
            // we are only interested in .class files
            if (fileName.endsWith(".class")) {
                // removes the .class extension
                className = pkgname + '.' + fileName.substring(0, fileName.length() - 6);
            }
            if (className != null) {
                classes.add(loadClass(className));
            }
            File subdir = new File(directory, fileName);
            if (subdir.isDirectory()) {
                processDirectory(subdir, pkgname + '.' + fileName, classes);
            }
        }
    }

    private static void processJarfile(URL resource, String pkgname, List<Class<?>> classes) {
        String relPath = pkgname.replace('.', '/');
        String resPath = resource.getPath();
        String jarPath = resPath.replaceFirst("[.]jar[!].*", ".jar").replaceFirst("file:", "");
        JarFile jarFile;
        try {
            jarFile = new JarFile(jarPath);
        } catch (IOException e) {
            throw new RuntimeException("Unexpected IOException reading JAR File '" + jarPath + "'", e);
        }
        Enumeration<JarEntry> entries = jarFile.entries();
        while (entries.hasMoreElements()) {
            JarEntry entry = entries.nextElement();
            String entryName = entry.getName();
            String className = null;
            if (entryName.endsWith(".class") && entryName.startsWith(relPath) && entryName.length() > relPath.length() + "/".length()) {
                className = entryName.replace('/', '.').replace('\\', '.').replace(".class", "");
            }
            if (className != null) {
                classes.add(loadClass(className));
            }
        }

        try {
            jarFile.close();
        } catch (IOException e) {
        }
    }

    private static Class<?> loadClass(String className) {
        try {
            return Class.forName(className);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("Unexpected ClassNotFoundException loading class '" + className + "'");
        }
    }
}
