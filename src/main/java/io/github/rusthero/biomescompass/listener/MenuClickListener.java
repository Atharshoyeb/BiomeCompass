package io.github.rusthero.biomescompass.listener;

import io.github.rusthero.biomescompass.BiomesCompass;
import io.github.rusthero.biomescompass.locate.LocateBiomeCallback;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Optional;

public class MenuClickListener implements Listener {
    private final BiomesCompass biomesCompass;

    public MenuClickListener(final BiomesCompass biomesCompass) {
        this.biomesCompass = biomesCompass;
    }

    @EventHandler
    private void onMenuClick(final InventoryClickEvent event) {
        if (biomesCompass.getLocateBiomeMenu().contains(event.getClickedInventory())) return;
        event.setCancelled(true);

        final ItemStack clickedItem = event.getCurrentItem();
        if (clickedItem == null || clickedItem.getType() == Material.AIR) return;

        final Player player = (Player) event.getWhoClicked();

        player.closeInventory();

        biomesCompass.getLocateBiomeMenu().itemToBiome(clickedItem).ifPresent(biome ->
                biomesCompass.getPlayerBiomeLocators().get(player).asyncLocateBiome(biome, biomesCompass, new LocateBiomeCallback() {
                    @Override
                    public void onQueryDone(Optional<Location> optLocation) {
                        optLocation.ifPresentOrElse(location -> {
                            player.sendMessage("Closest biome you selected is at: " + location.toVector());
                            player.setCompassTarget(location);
                        }, () -> player.sendMessage("Could not find the biome in max search area"));
                    }

                    @Override
                    public void onRunning() {
                        player.sendMessage("You are already searching a biome!");
                    }

                    @Override
                    public void onCooldown() {
                        player.sendMessage("Please wait, you are on cooldown!");
                    }
                })
        );
    }
}
