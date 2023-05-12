package me.monsterdropcraft;

import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.RecipeChoice;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.inventory.ItemStack;
import org.bukkit.event.EventHandler;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.Recipe;
import org.bukkit.event.Listener;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.NamespacedKey;
import org.bukkit.Material;

@SuppressWarnings("deprecation")
public class Main extends JavaPlugin implements Listener {
	private final double chancePass = 0.005D;
	private final double chanceSapphire = 0.05D;
	
	private final Material passMaterial = Material.PAPER;
	private final String passName = "§cPASS";
	
	private final Material sapphireMaterial = Material.EMERALD;
	private final String sapphireName = "§aSAPPHIRE";
	
	private final Material giftMateroal = Material.GHAST_TEAR;
	private final String giftName = "§d§lGIFT";
	
	@Override
	public void onEnable() {
		getServer().getPluginManager().registerEvents(this, this);
		
		registerCraft();
	}
	
	@Override
	public void onDisable() {
		unloadCraft();
	}
	
	private void registerCraft() {
		//CRAFT
		ItemStack gift = customItem(giftMateroal, giftName);
		ShapedRecipe shapedRecipeGift = new ShapedRecipe(new NamespacedKey(this, "gift"), gift);
		
		shapedRecipeGift.shape("123", "456", "789");
		shapedRecipeGift.setIngredient('4', new RecipeChoice.ExactChoice(customItem(sapphireMaterial, sapphireName)));
		shapedRecipeGift.setIngredient('5', new RecipeChoice.ExactChoice(customItem(passMaterial, passName)));
		shapedRecipeGift.setIngredient('6', new RecipeChoice.ExactChoice(customItem(sapphireMaterial, sapphireName)));
		
		addRecipe(shapedRecipeGift);
		
		//TRADE
		ItemStack diamond = new ItemStack(Material.DIAMOND, 16);
		ShapedRecipe shapedRecipeTrade = new ShapedRecipe(new NamespacedKey(this, "trade"), diamond);
		
		shapedRecipeTrade.shape("123", "456", "789");
		shapedRecipeTrade.setIngredient('5', new RecipeChoice.ExactChoice(gift));
		
		addRecipe(shapedRecipeTrade);
	}
	
	private void unloadCraft() {
		removeRecipe(new NamespacedKey(this, "gift"));
		removeRecipe(new NamespacedKey(this, "trade"));
	}
	
	@EventHandler
	public void onEntityDeath(EntityDeathEvent event) {
		EntityType entityType = event.getEntity().getType();
		Entity killer = event.getEntity().getKiller();
		
		switch (entityType) {
			case CREEPER: {
				if (killer instanceof Player
					&& Math.random() < chancePass) {
					ItemStack pass = customItem(passMaterial, passName);
					event.getDrops().add(pass);
					break;
				}
				break;
			}
			
			case ZOMBIE: {
				if (killer instanceof Player
					&& Math.random() < chanceSapphire) {
					ItemStack sapphire = customItem(sapphireMaterial, sapphireName);
					event.getDrops().add(sapphire);
					break;
				}
				break;
			}
			
			default: {
				break;
			}
		}
	}
	
	private void addRecipe(Recipe recipe) {
		getServer().addRecipe(recipe);
	}
	
	private void removeRecipe(NamespacedKey name) {
		Recipe recipe = getServer().getRecipe(name);
		
		if (recipe != null) {
			getServer().removeRecipe(name);
		}
	}
	
	private ItemStack customItem(Material material, String name) {
		ItemStack itemStack = new ItemStack(material);
		ItemMeta itemMeta = itemStack.getItemMeta();
		itemMeta.setDisplayName(name);
		itemStack.setItemMeta(itemMeta);
		return itemStack;
	}
}