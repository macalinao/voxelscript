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
package com.simplyian.superflow;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;

import org.spout.api.Spout;
import org.spout.api.event.Event;
import org.spout.api.event.EventExecutor;
import org.spout.api.event.Order;
import org.spout.api.event.block.BlockChangeEvent;
import org.spout.api.event.block.CuboidChangeEvent;
import org.spout.api.event.chunk.ChunkLoadEvent;
import org.spout.api.event.chunk.ChunkPopulateEvent;
import org.spout.api.event.chunk.ChunkUnloadEvent;
import org.spout.api.event.chunk.ChunkUpdatedEvent;
import org.spout.api.event.entity.EntityChangeWorldEvent;
import org.spout.api.event.entity.EntityChangedWorldEvent;
import org.spout.api.event.entity.EntityControllerChangeEvent;
import org.spout.api.event.entity.EntityDeathEvent;
import org.spout.api.event.entity.EntityDespawnEvent;
import org.spout.api.event.entity.EntityHealthChangeEvent;
import org.spout.api.event.entity.EntityInteractEvent;
import org.spout.api.event.entity.EntityMoveEvent;
import org.spout.api.event.entity.EntitySpawnEvent;
import org.spout.api.event.entity.EntityTeleportEvent;
import org.spout.api.event.player.ClientPlayerConnectedEvent;
import org.spout.api.event.player.PlayerBanKickEvent;
import org.spout.api.event.player.PlayerChatEvent;
import org.spout.api.event.player.PlayerConnectEvent;
import org.spout.api.event.player.PlayerHeldItemChangeEvent;
import org.spout.api.event.player.PlayerInteractEvent;
import org.spout.api.event.player.PlayerJoinEvent;
import org.spout.api.event.player.PlayerKickEvent;
import org.spout.api.event.player.PlayerLeaveEvent;
import org.spout.api.event.player.PlayerLoginEvent;
import org.spout.api.event.player.PlayerPreLoginEvent;
import org.spout.api.event.server.BanChangeEvent;
import org.spout.api.event.server.NodeBasedEvent;
import org.spout.api.event.server.PluginDisableEvent;
import org.spout.api.event.server.PluginEnableEvent;
import org.spout.api.event.server.PreCommandEvent;
import org.spout.api.event.server.ServerStartEvent;
import org.spout.api.event.server.ServerStopEvent;
import org.spout.api.event.server.ServiceRegisterEvent;
import org.spout.api.event.server.ServiceUnregisterEvent;
import org.spout.api.event.server.data.RetrieveDataEvent;
import org.spout.api.event.server.permissions.PermissionGetAllWithNodeEvent;
import org.spout.api.event.server.permissions.PermissionGetGroupsEvent;
import org.spout.api.event.server.permissions.PermissionGroupEvent;
import org.spout.api.event.server.permissions.PermissionNodeEvent;
import org.spout.api.event.server.protection.EntityCanBeHarmedEvent;
import org.spout.api.event.server.protection.EntityCanBreakEvent;
import org.spout.api.event.server.protection.EntityCanBuildEvent;
import org.spout.api.event.server.protection.EntityCanInteractBlockEvent;
import org.spout.api.event.server.protection.EntityCanInteractEntityEvent;
import org.spout.api.event.server.protection.EntityEnterProtection;
import org.spout.api.event.server.protection.EntityLeaveProtection;
import org.spout.api.event.storage.PlayerLoadEvent;
import org.spout.api.event.storage.PlayerSaveEvent;
import org.spout.api.event.world.RegionLoadEvent;
import org.spout.api.event.world.RegionUnloadEvent;
import org.spout.api.event.world.WorldLoadEvent;
import org.spout.api.event.world.WorldUnloadEvent;

public class Events {
	private final SuperFlowPlugin plugin;
	private final Map<String, Class<? extends Event>> events = new HashMap<String, Class<? extends Event>>();

	public Events(SuperFlowPlugin plugin) {
		this.plugin = plugin;

		initializeCoreEvents();
	}

	private void initializeCoreEvents() {
		// Block events
		define("BlockChange", BlockChangeEvent.class);
		define("CuboidChange", CuboidChangeEvent.class);

		// Chunk events
		define("ChunkLoad", ChunkLoadEvent.class);
		define("ChunkPopulate", ChunkPopulateEvent.class);
		define("ChunkUnload", ChunkUnloadEvent.class);
		define("ChunkUpdated", ChunkUpdatedEvent.class);

		// Entity Events

		// Protection
		define("EntityCanBeHarmed", EntityCanBeHarmedEvent.class);
		define("EntityCanBreak", EntityCanBreakEvent.class);
		define("EntityCanBuild", EntityCanBuildEvent.class);
		define("EntityCanInteractBlock", EntityCanInteractBlockEvent.class);
		define("EntityCanInteractEntity", EntityCanInteractEntityEvent.class);
		define("EntityEnterProtection", EntityEnterProtection.class);
		define("EntityLeaveProtection", EntityLeaveProtection.class);

		// general
		define("EntityChangedWorld", EntityChangedWorldEvent.class);
		define("EntityChangeWorld", EntityChangeWorldEvent.class);
		define("EntityControllerChange", EntityControllerChangeEvent.class);
		define("EntityDeath", EntityDeathEvent.class);
		define("EntityDespawn", EntityDespawnEvent.class);
		define("EntityHealthChange", EntityHealthChangeEvent.class);
		define("EntityInteract", EntityInteractEvent.class);
		define("EntityMove", EntityMoveEvent.class);
		define("EntitySpawn", EntitySpawnEvent.class);
		define("EntityTeleport", EntityTeleportEvent.class);

		// Player events
		define("ClientPlayerConnected", ClientPlayerConnectedEvent.class);
		define("PlayerBanKick", PlayerBanKickEvent.class);
		define("PlayerChat", PlayerChatEvent.class);
		define("PlayerConnect", PlayerConnectEvent.class);
		define("PlayerHeldItemChange", PlayerHeldItemChangeEvent.class);
		define("PlayerInteract", PlayerInteractEvent.class);
		define("PlayerJoin", PlayerJoinEvent.class);
		define("PlayerKick", PlayerKickEvent.class);
		define("PlayerLeave", PlayerLeaveEvent.class);
		define("PlayerLogin", PlayerLoginEvent.class);
		define("PlayerPreLogin", PlayerPreLoginEvent.class);
		define("PlayerLoad", PlayerLoadEvent.class);
		define("PlayerSave", PlayerSaveEvent.class);

		// Server events
		define("BanChange", BanChangeEvent.class);
		define("NodeBased", NodeBasedEvent.class);
		define("PluginDisable", PluginDisableEvent.class);
		define("PluginEnable", PluginEnableEvent.class);
		define("PreCommand", PreCommandEvent.class);
		define("ServerStart", ServerStartEvent.class);
		define("ServerStop", ServerStopEvent.class);
		define("ServiceRegister", ServiceRegisterEvent.class);
		define("ServiceUnregister", ServiceUnregisterEvent.class);
		define("RetrieveData", RetrieveDataEvent.class);

		// Permission Events
		define("PermissionGetAllWithNode", PermissionGetAllWithNodeEvent.class);
		define("PermissionGetGroups", PermissionGetGroupsEvent.class);
		define("PermissionGroup", PermissionGroupEvent.class);
		define("PermissionNode", PermissionNodeEvent.class);

		// Region Events
		define("RegionLoad", RegionLoadEvent.class);
		define("RegionUnload", RegionUnloadEvent.class);

		// World events
		define("WorldLoad", WorldLoadEvent.class);
		define("WorldUnload", WorldUnloadEvent.class);
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
	 * Defines an event.
	 * 
	 * @param name
	 * @param clazz
	 */
	public void define(String name, Class<? extends Event> clazz) {
		if (events.put(name.toLowerCase(), clazz) != null) {
			plugin.getLogger().log(Level.WARNING, "Duplicate event registered: '" + name + "' for '" + clazz.getCanonicalName() + "'.");
		}
	}
}
