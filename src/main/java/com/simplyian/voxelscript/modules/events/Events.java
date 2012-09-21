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
import org.spout.api.event.player.PlayerInteractEvent;
import org.spout.api.event.player.PlayerJoinEvent;
import org.spout.api.event.player.PlayerKickEvent;
import org.spout.api.event.player.PlayerLeaveEvent;
import org.spout.api.event.player.PlayerLoginEvent;
import org.spout.api.event.player.PlayerPreLoginEvent;
import org.spout.api.event.server.*;
import org.spout.api.event.server.NodeBasedEvent;
import org.spout.api.event.server.PreCommandEvent;
import org.spout.api.event.server.ServerStartEvent;
import org.spout.api.event.server.ServerStopEvent;
import org.spout.api.event.server.access.BanChangeEvent;
import org.spout.api.event.server.permissions.PermissionGetAllWithNodeEvent;
import org.spout.api.event.server.permissions.PermissionNodeEvent;
import org.spout.api.event.server.plugin.PluginDisableEvent;
import org.spout.api.event.server.plugin.PluginEnableEvent;
import org.spout.api.event.server.protection.EntityCanBeHarmedEvent;
import org.spout.api.event.server.protection.EntityCanBreakEvent;
import org.spout.api.event.server.protection.EntityCanBuildEvent;
import org.spout.api.event.server.protection.EntityCanInteractBlockEvent;
import org.spout.api.event.server.protection.EntityCanInteractEntityEvent;
import org.spout.api.event.server.protection.EntityEnterProtection;
import org.spout.api.event.server.protection.EntityLeaveProtection;
import org.spout.api.event.server.service.ServiceRegisterEvent;
import org.spout.api.event.server.service.ServiceUnregisterEvent;
import org.spout.api.event.storage.PlayerLoadEvent;
import org.spout.api.event.storage.PlayerSaveEvent;
import org.spout.api.event.world.RegionLoadEvent;
import org.spout.api.event.world.RegionUnloadEvent;
import org.spout.api.event.world.WorldLoadEvent;
import org.spout.api.event.world.WorldUnloadEvent;
import org.spout.api.plugin.Plugin;

import org.spout.vanilla.event.block.BlockActionEvent;
import org.spout.vanilla.event.block.FurnaceBurnEvent;
import org.spout.vanilla.event.block.SignChangeEvent;
import org.spout.vanilla.event.block.SignUpdateEvent;
import org.spout.vanilla.event.entity.CraftingResultEvent;
import org.spout.vanilla.event.entity.EntityAnimationEvent;
import org.spout.vanilla.event.entity.EntityCollectItemEvent;
import org.spout.vanilla.event.entity.EntityCombustEvent;
import org.spout.vanilla.event.entity.EntityExplodeEvent;
import org.spout.vanilla.event.entity.EntityMetaChangeEvent;
import org.spout.vanilla.event.entity.EntityStatusEvent;
import org.spout.vanilla.event.entity.EntityTameEvent;
import org.spout.vanilla.event.entity.EntityTargetEvent;
import org.spout.vanilla.event.entity.PotionSplashEvent;
import org.spout.vanilla.event.entity.ProjectileHitEvent;
import org.spout.vanilla.event.entity.SlimeSplitEvent;
import org.spout.vanilla.event.entity.VanillaEntityDeathEvent;
import org.spout.vanilla.event.entity.VanillaEntityTeleportEvent;
import org.spout.vanilla.event.game.ServerListPingEvent;
import org.spout.vanilla.event.player.*;
import org.spout.vanilla.event.player.PlayerBedEvent;
import org.spout.vanilla.event.player.PlayerBucketEvent;
import org.spout.vanilla.event.player.PlayerDeathEvent;
import org.spout.vanilla.event.player.PlayerFishEvent;
import org.spout.vanilla.event.player.PlayerFoodSaturationChangeEvent;
import org.spout.vanilla.event.player.PlayerGameModeChangedEvent;
import org.spout.vanilla.event.player.PlayerHungerChangeEvent;
import org.spout.vanilla.event.player.PlayerLevelChangeEvent;
import org.spout.vanilla.event.player.PlayerRespawnEvent;
import org.spout.vanilla.event.player.PlayerSlotChangeEvent;
import org.spout.vanilla.event.player.network.PlayerGameStateEvent;
import org.spout.vanilla.event.player.network.PlayerKeepAliveEvent;
import org.spout.vanilla.event.player.network.PlayerPingChangedEvent;
import org.spout.vanilla.event.player.network.PlayerUpdateStatsEvent;
import org.spout.vanilla.event.player.network.PlayerUpdateUserListEvent;
import org.spout.vanilla.event.window.WindowCloseEvent;
import org.spout.vanilla.event.window.WindowOpenEvent;
import org.spout.vanilla.event.window.WindowPropertyEvent;
import org.spout.vanilla.event.window.WindowSetSlotEvent;
import org.spout.vanilla.event.window.WindowSetSlotsEvent;
import org.spout.vanilla.event.world.PlayExplosionEffectEvent;
import org.spout.vanilla.event.world.PlayParticleEffectEvent;
import org.spout.vanilla.event.world.PlaySoundEffectEvent;
import org.spout.vanilla.event.world.WeatherChangeEvent;

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
		define("PermissionNode", PermissionNodeEvent.class);

		// Region Events
		define("RegionLoad", RegionLoadEvent.class);
		define("RegionUnload", RegionUnloadEvent.class);

		// World events
		define("WorldLoad", WorldLoadEvent.class);
		define("WorldUnload", WorldUnloadEvent.class);

		// Vanilla events
		if (plugin.getEngine().getPluginManager().getPlugin("Vanilla") != null) {
			define("BlockAction", BlockActionEvent.class);
			define("FurnaceBurn", FurnaceBurnEvent.class);
			define("SignChange", SignChangeEvent.class);
			define("SignUpdate", SignUpdateEvent.class);
			define("CraftingResult", CraftingResultEvent.class);

			define("EntityAnimation", EntityAnimationEvent.class);
			define("EntityCollectItem", EntityCollectItemEvent.class);
			define("EntityCombust", EntityCombustEvent.class);
			define("EntityExplode", EntityExplodeEvent.class);
			define("EntityMetaChange", EntityMetaChangeEvent.class);
			define("EntityStatus", EntityStatusEvent.class);
			define("EntityTame", EntityTameEvent.class);
			define("EntityTarget", EntityTargetEvent.class);

			define("PotionSplash", PotionSplashEvent.class);
			define("ProjectileHit", ProjectileHitEvent.class);
			define("SlimeSplit", SlimeSplitEvent.class);
			define("VanillaEntityDeath", VanillaEntityDeathEvent.class);
			define("VanillaEntityTeleport", VanillaEntityTeleportEvent.class);
			define("ServerListPing", ServerListPingEvent.class);

			define("PlayerBed", PlayerBedEvent.class);
			define("PlayerBucket", PlayerBucketEvent.class);
			define("PlayerDeath", PlayerDeathEvent.class);
			define("PlayerFish", PlayerFishEvent.class);
			define("PlayerFoodSaturationChange", PlayerFoodSaturationChangeEvent.class);
			define("PlayerGameModeChanged", PlayerGameModeChangedEvent.class);
			define("PlayerHungerChange", PlayerHungerChangeEvent.class);
			define("PlayerLevelChange", PlayerLevelChangeEvent.class);
			define("PlayerRespawn", PlayerRespawnEvent.class);
			define("PlayerSlotChange", PlayerSlotChangeEvent.class);
			define("PlayerGameState", PlayerGameStateEvent.class);
			define("PlayerKeepAlive", PlayerKeepAliveEvent.class);
			define("PlayerPingChanged", PlayerPingChangedEvent.class);
			define("PlayerUpdateStats", PlayerUpdateStatsEvent.class);
			define("PlayerUpdateUserList", PlayerUpdateUserListEvent.class);

			define("WindowClose", WindowCloseEvent.class);
			define("WindowOpen", WindowOpenEvent.class);
			define("WindowProperty", WindowPropertyEvent.class);
			define("WindowSetSlot", WindowSetSlotEvent.class);
			define("WindowSetSlots", WindowSetSlotsEvent.class);

			define("PlayExplosionEffect", PlayExplosionEffectEvent.class);
			define("PlayParticleEffect", PlayParticleEffectEvent.class);
			define("PlaySoundEffect", PlaySoundEffectEvent.class);
			define("WeatherChange", WeatherChangeEvent.class);
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
}
