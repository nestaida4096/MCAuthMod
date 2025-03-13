package nest.auth.authmod;

import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import java.net.HttpURLConnection;
import java.net.URL;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.HashSet;
import java.util.Set;

import java.util.Timer;
import java.util.TimerTask;

@Mod.EventBusSubscriber
public class AuthChecker {

    private static final Set<String> authenticatedPlayers = new HashSet<>();
    private static int tickCount = 0;

    @SubscribeEvent
    public static void onServerTick(TickEvent.ServerTickEvent event) {
        tickCount++;

        if (tickCount >= 200) { // 10秒ごと (1tick = 50ms, 20tick = 1秒)
            fetchAuthenticatedPlayers();
            tickCount = 0;
        }
    }

    private static void fetchAuthenticatedPlayers() {
        try {
            URL url = new URL("http://127.0.0.1:8080/authenticatedPlayers");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String inputLine;
            StringBuilder response = new StringBuilder();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

            updateAuthenticatedPlayers(response.toString());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void updateAuthenticatedPlayers(String json) {
        authenticatedPlayers.clear();
        String[] players = json.replace("[", "").replace("]", "").replace("\"", "").split(",");
        for (String player : players) {
            authenticatedPlayers.add(player.trim());
        }
    }

    public static boolean isAuthenticated(String playerName) {
        return authenticatedPlayers.contains(playerName);
    }

    public static void startChecking() {
        Timer timer = new Timer(true);
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                System.out.println("認証リストを取得...");
                fetchAuthenticatedPlayers();
            }
        }, 0, 10 * 1000); // 10秒ごとに実行
    }
}