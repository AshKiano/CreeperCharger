package com.ashkiano.creepercharger;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Creeper;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Arrays;

public class CreeperCharger extends JavaPlugin implements Listener {

    // Define the material that will charge the Creeper (can be changed as desired)
    private final Material chargingItemMaterial = Material.STICK;
    private final String chargingItemLore = "Charging Creeper Stick";

    @Override
    public void onEnable() {
        Bukkit.getPluginManager().registerEvents(this, this);

        Metrics metrics = new Metrics(this, 19366);

        this.getLogger().info("Thank you for using the CreeperCharger plugin! If you enjoy using this plugin, please consider making a donation to support the development. You can donate at: https://paypal.me/josefvyskocil");

        checkForUpdates();
    }

    @EventHandler
    public void onPlayerInteractEntity(PlayerInteractEntityEvent event) {
        if (event.getRightClicked() instanceof Creeper) {
            Player player = event.getPlayer();
            ItemStack itemInHand = player.getInventory().getItemInMainHand();
            if (itemInHand.getType() == chargingItemMaterial && itemInHand.getItemMeta().hasLore() && itemInHand.getItemMeta().getLore().contains(chargingItemLore)) {
                Creeper creeper = (Creeper) event.getRightClicked();
                creeper.setPowered(true);
                player.sendMessage("Creeper has been charged!");
            }
        }
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (command.getName().equalsIgnoreCase("getcharger") && sender instanceof Player) {
            Player player = (Player) sender;
            ItemStack chargingItem = new ItemStack(chargingItemMaterial);
            ItemMeta meta = chargingItem.getItemMeta();
            meta.setLore(Arrays.asList(chargingItemLore));
            chargingItem.setItemMeta(meta);
            player.getInventory().addItem(chargingItem);
            player.sendMessage("You have received the Charging Creeper Stick!");
            return true;
        }
        return false;
    }

    private void checkForUpdates() {
        try {
            String pluginName = this.getDescription().getName();
            URL url = new URL("https://www.ashkiano.com/version_check.php?plugin=" + pluginName);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");

            int responseCode = con.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
                String inputLine;
                StringBuffer response = new StringBuffer();

                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();

                String jsonResponse = response.toString();
                JSONObject jsonObject = new JSONObject(jsonResponse);
                if (jsonObject.has("error")) {
                    this.getLogger().warning("Error when checking for updates: " + jsonObject.getString("error"));
                } else {
                    String latestVersion = jsonObject.getString("latest_version");

                    String currentVersion = this.getDescription().getVersion();
                    if (currentVersion.equals(latestVersion)) {
                        this.getLogger().info("This plugin is up to date!");
                    } else {
                        this.getLogger().warning("There is a newer version (" + latestVersion + ") available! Please update!");
                    }
                }
            } else {
                this.getLogger().warning("Failed to check for updates. Response code: " + responseCode);
            }
        } catch (Exception e) {
            this.getLogger().warning("Failed to check for updates. Error: " + e.getMessage());
        }
    }
}
