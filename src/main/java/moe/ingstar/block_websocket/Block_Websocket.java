package moe.ingstar.block_websocket;

import moe.ingstar.block_websocket.server.WebSocketResponseHandler;
import net.fabricmc.api.ModInitializer;

import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.World;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Block_Websocket implements ModInitializer {
    public static final Logger LOGGER = LoggerFactory.getLogger("block_websocket");
	private static WebSocketResponseHandler webSocketResponseHandler;

	@Override
	public void onInitialize() {
		ServerLifecycleEvents.SERVER_STARTED.register(this::onServerStarted);
	}

	private void onServerStarted(MinecraftServer server) {
		World world = server.getWorld(World.OVERWORLD); // 获取主世界对象
		if (world != null && server.isRunning()) {
			startWebSocketServer();
		} else {
			stopWebSocketServer();
		}
	}

	static void startWebSocketServer() {
		try {
			webSocketResponseHandler = new WebSocketResponseHandler();
			webSocketResponseHandler.startWebSocketServer();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void stopWebSocketServer() {
		if (webSocketResponseHandler != null) {
			try {
				webSocketResponseHandler.stopWebSocketServer();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}