package thelm.yttrjei.recipe;

import com.unascribed.yttr.crafting.ShatteringRecipe;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.Recipe;
import net.minecraft.recipe.StonecuttingRecipe;
import net.minecraft.util.Identifier;

public record ShatteringRecipeWrapper(Identifier id, Ingredient input, ItemStack output, boolean exclusive) {

	public ShatteringRecipeWrapper(Recipe<?> recipe) {
		this(recipe.getId(), recipe.getIngredients().get(0), recipe.getOutput(), recipe instanceof ShatteringRecipe);
	}

	public ShatteringRecipeWrapper(StonecuttingRecipe recipe) {
		this(recipe.getId(), Ingredient.ofStacks(recipe.getOutput()), new ItemStack(Item.byRawId(recipe.getIngredients().get(0).getMatchingItemIds().getInt(0))), false);
	}
}
