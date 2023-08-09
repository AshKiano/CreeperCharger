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
}
