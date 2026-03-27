package thelm.yttrjei.recipe.category;

import java.util.Map;

import diy.y2k.yttr.Yttr;
import diy.y2k.yttr.init.content.YEnchantments;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.widgets.IRecipeExtrasBuilder;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import thelm.jeidrawables.JEIDrawables;
import thelm.jeidrawables.gui.render.IngredientDrawable;
import thelm.jeidrawables.gui.render.ResourceDrawable;
import thelm.yttrjei.YttrJEI;
import thelm.yttrjei.recipe.ShatteringRecipeWrapper;

/**
 * Based on EmiShatteringRecipe
 */
public class ShatteringRecipeCategory extends AbstractRecipeCategory<ShatteringRecipeWrapper> {

	public static final Text TITLE = Text.translatable("emi.category.yttr.shattering");

	public static final IDrawable ICON = new IngredientDrawable<>(withShattering(new ItemStack(Items.DIAMOND_PICKAXE)));
	public static final IDrawable SHATTERING = new ResourceDrawable(Yttr.id("textures/gui/shattering.png"), 0, 0, 24, 17, 24, 34);

	public static ItemStack withShattering(ItemStack stack) {
		EnchantmentHelper.set(Map.of(YEnchantments.SHATTERING_CURSE, 1), stack);
		return stack;
	}

	public ShatteringRecipeCategory() {
		super(YttrJEI.SHATTERING, TITLE);
	}

	@Override
	public int getWidth() {
		return 84;
	}

	@Override
	public int getHeight() {
		return 26;
	}

	@Override
	public IDrawable getIcon() {
		return ICON;
	}

	@Override
	public void setRecipe(IRecipeLayoutBuilder builder, ShatteringRecipeWrapper recipe, IFocusGroup focuses) {
		addItem(builder, RecipeIngredientRole.INPUT, 1, 5, recipe.input(), JEIDrawables.SLOT);
		addItem(builder, RecipeIngredientRole.OUTPUT, 63, 5, recipe.output(), JEIDrawables.OUTPUT_SLOT);
	}

	@Override
	public void createRecipeExtras(IRecipeExtrasBuilder builder, ShatteringRecipeWrapper recipe, IFocusGroup focuses) {
		if(recipe.exclusive()) {
			builder.addDrawable(SHATTERING, 26, 5);
		}
		else {
			builder.addDrawable(JEIDrawables.RECIPE_ARROW, 27, 5);
		}
	}

	@Override
	public Identifier getRegistryName(ShatteringRecipeWrapper recipe) {
		return recipe.id();
	}
}
