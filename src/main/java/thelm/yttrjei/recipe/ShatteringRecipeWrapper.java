package thelm.yttrjei.recipe;

import diy.y2k.yttr.content.recipe.ShatteringRecipe;
import net.minecraft.client.MinecraftClient;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.Recipe;
import net.minecraft.recipe.StonecuttingRecipe;
import net.minecraft.registry.DynamicRegistryManager;
import net.minecraft.util.Identifier;

public record ShatteringRecipeWrapper(Identifier id, Ingredient input, ItemStack output, boolean exclusive) {

	public ShatteringRecipeWrapper(Recipe<?> recipe) {
		this(recipe.getId(), recipe.getIngredients().get(0), recipe.getOutput(registryAccess()), recipe instanceof ShatteringRecipe);
	}

	public ShatteringRecipeWrapper(StonecuttingRecipe recipe) {
		this(recipe.getId(), Ingredient.ofStacks(recipe.getOutput(registryAccess())), new ItemStack(Item.byRawId(recipe.getIngredients().get(0).getMatchingItemIds().getInt(0))), false);
	}

	public static DynamicRegistryManager registryAccess() {
		return MinecraftClient.getInstance().world.getRegistryManager();
	}
}
