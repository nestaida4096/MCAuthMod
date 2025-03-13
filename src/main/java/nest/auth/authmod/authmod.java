package nest.auth.authmod;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

@Mod(authmod.MOD_ID)
@EventBusSubscriber
public class authmod {
    public static final String MOD_ID = "authmod";

    public authmod() {
        System.out.println(MOD_ID + " が読み込まれました！");
    }

    @SubscribeEvent
    public static void onServerStarting(ServerStartingEvent event) {
        System.out.println("サーバーが開始されました。認証チェックを開始します...");
        AuthChecker.startChecking();
    }
}