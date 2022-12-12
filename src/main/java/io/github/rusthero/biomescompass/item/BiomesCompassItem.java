package io.github.rusthero.biomescompass.item;

import io.github.rusthero.biomescompass.gui.BiomeSelectMenu;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;

public class BiomesCompassItem extends ItemStack {
    public BiomesCompassItem() {
        setType(Material.COMPASS);

        ItemMeta meta = getItemMeta();
        meta.setDisplayName(ChatColor.DARK_GREEN + "Biomes Compass");
        setItemMeta(meta);
    }

    public static ShapedRecipe getRecipe(JavaPlugin plugin) {
        // TODO Add permission check for the recipe
        ShapedRecipe recipe = new ShapedRecipe(new NamespacedKey(plugin, "biomes_compass"), new BiomesCompassItem());

        recipe.shape("SLS", "LCL", "SLS");

        recipe.setIngredient('S', Material.OAK_SAPLING);
        recipe.setIngredient('L', Material.OAK_LOG);
        recipe.setIngredient('C', Material.COMPASS);

        return recipe;
    }

    public static class Listener implements org.bukkit.event.Listener {
        @EventHandler
        private void use(PlayerInteractEvent event) {
            if (event.getAction() == Action.LEFT_CLICK_AIR || event.getAction() == Action.LEFT_CLICK_BLOCK) return;
            if (event.getItem() == null || !event.getItem().equals(new BiomesCompassItem())) return;

            Player player = event.getPlayer();
            if (!player.hasPermission("biomescompass.use")) return;

            BiomeSelectMenu.singleton().open(player);
        }

        @EventHandler
        private void craft(CraftItemEvent event) {
            if (!event.getRecipe().getResult().equals(new BiomesCompassItem())) return;
            if (!(event.getWhoClicked() instanceof Player player)) return;

            event.setCancelled(!player.hasPermission("biomescompass.craft"));
        }
    }
}