package io.github.rusthero.biomescompass;

import io.github.rusthero.biomescompass.gui.LocateBiomeMenu;
import io.github.rusthero.biomescompass.items.BiomesCompassItem;
import io.github.rusthero.biomescompass.listeners.ItemUseListener;
import io.github.rusthero.biomescompass.listeners.MenuClickListener;
import io.github.rusthero.biomescompass.listeners.MenuDragListener;
import io.github.rusthero.biomescompass.locate.LocateBiomeCache;
import io.github.rusthero.biomescompass.locate.PlayerBiomeLocatorRegistry;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class BiomesCompass extends JavaPlugin {
    private LocateBiomeCache locateBiomeCache;
    private PlayerBiomeLocatorRegistry playerBiomeLocators;
    private LocateBiomeMenu locateBiomeMenu;

    @Override
    public void onEnable() {
        // TODO Configuration

        locateBiomeCache = new LocateBiomeCache();
        playerBiomeLocators = new PlayerBiomeLocatorRegistry();
        locateBiomeMenu = new LocateBiomeMenu();

        PluginManager pluginManager = getServer().getPluginManager();
        pluginManager.registerEvents(new ItemUseListener(this), this);
        pluginManager.registerEvents(new MenuClickListener(this), this);
        pluginManager.registerEvents(new MenuDragListener(locateBiomeMenu), this);

        getServer().addRecipe(BiomesCompassItem.getRecipe(this));

        getLogger().info("BiomesCompass is enabled");
    }

    @Override
    public void onDisable() {
        getLogger().info("BiomesCompass is disabled");
    }

    public LocateBiomeCache getLocateBiomeCache() {
        return locateBiomeCache;
    }

    public PlayerBiomeLocatorRegistry getPlayerBiomeLocators() {
        return playerBiomeLocators;
    }

    public LocateBiomeMenu getLocateBiomeMenu() {
        return locateBiomeMenu;
    }
}