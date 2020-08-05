package me.Stellrow.WormHole;

import me.Stellrow.WormHole.commands.WormHoleCommands;
import me.Stellrow.WormHole.events.WormHoleEvents;
import me.Stellrow.WormHole.inventory.InventoryManager;
import me.Stellrow.WormHole.teleport.TeleportManager;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapelessRecipe;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionData;
import org.bukkit.potion.PotionType;


public class WormHole extends JavaPlugin {
    public ItemStack potion;
    public NamespacedKey potionkey = new NamespacedKey(this,"potionkey");
    private TeleportManager teleportManager;
    private InventoryManager inventoryManager;
    public void onEnable(){
        loadConfig();
        buildPotion();
        teleportManager = new TeleportManager(this);
        inventoryManager = new InventoryManager(this);
        getServer().getPluginManager().registerEvents(inventoryManager,this);
        getServer().getPluginManager().registerEvents(new WormHoleEvents(this),this);
        getCommand("wormhole").setExecutor(new WormHoleCommands(this));
        setupRecipe();

    }
    public void onDisable(){

    }
    ///inventory manager
    public InventoryManager getInventoryManager(){
        return inventoryManager;
    }

    //inventory manager
    ///teleport manager
    public TeleportManager getTeleportManager(){
        return teleportManager;
    }
    //teleport manager end


    ///config
    public void reload(){
        reloadConfig();
        buildPotion();
    }

    private void loadConfig(){
        getConfig().options().copyDefaults(true);
        saveConfig();
    }
    //config end


    ///build item
    private void buildPotion(){
        potion = new ItemStack(Material.valueOf(getConfig().getString("ItemConfig.type")));
        ItemMeta im = potion.getItemMeta();
        im.setDisplayName(Utils.asColor(getConfig().getString("ItemConfig.name")));
        im.setLore(Utils.loreAsColor(getConfig().getStringList("ItemConfig.lore")));
        im.getPersistentDataContainer().set(potionkey, PersistentDataType.STRING,"wormhole");
        PotionMeta pmeta = (PotionMeta) im;
        pmeta.setBasePotionData(new PotionData(PotionType.WATER));
        potion.setItemMeta(im);
    }
    //build item end
    ///setup recipe
    NamespacedKey recipeKey = new NamespacedKey(this,"recipekey");
    private void setupRecipe(){
        if(getConfig().getBoolean("General.allow-crafting")) {
            ShapelessRecipe recipe = new ShapelessRecipe(potionkey, potion);
            recipe.addIngredient(1, Material.ENDER_EYE);
            recipe.addIngredient(1, Material.POTION);
            getServer().addRecipe(recipe);
        }
    }
    //setup recipe
}
