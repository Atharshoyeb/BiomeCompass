package io.github.rusthero.biomescompass.listeners;

import io.github.rusthero.biomescompass.BiomesCompass;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class MenuClickListener implements Listener {
    private final BiomesCompass biomesCompass;

    public MenuClickListener(final BiomesCompass biomesCompass) {
        this.biomesCompass = biomesCompass;
    }

    @EventHandler
    private void onMenuClick(final InventoryClickEvent event) {
        Inventory inventory = event.getClickedInventory();
        if (inventory == null || !biomesCompass.getLocateBiomeMenu().contains(event.getClickedInventory())) return;
        event.setCancelled(true);

        final ItemStack clickedItem = event.getCurrentItem();
        if (clickedItem == null || clickedItem.getType() == Material.AIR) return;

        final Player player = (Player) event.getWhoClicked();

        player.closeInventory();
        player.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(ChatColor.YELLOW + "Locating"));
        player.playSound(player.getLocation(), Sound.BLOCK_CONDUIT_ACTIVATE, 1.0f, 1.0f);

        biomesCompass.getLocateBiomeMenu().itemToBiome(clickedItem).ifPresent(biome -> biomesCompass.getPlayerBiomeLocators().get(player).asyncLocateBiome(biome, biomesCompass, optLocation -> optLocation.ifPresentOrElse(location -> {
            player.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(ChatColor.GREEN + "Located"));
            player.playSound(player.getLocation(), Sound.BLOCK_CONDUIT_ACTIVATE, 1.0f, 1.0f);
            player.setCompassTarget(location);
        }, () -> {
            player.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(ChatColor.RED + "Not found within search range"));
            player.playSound(player.getLocation(), Sound.BLOCK_CONDUIT_AMBIENT, 1.0f, 4.0f);
        })));
    }
}
