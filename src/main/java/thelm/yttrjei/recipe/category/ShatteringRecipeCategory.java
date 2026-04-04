package thelm.yttrjei.recipe.category;

import java.util.Map;

import com.unascribed.yttr.Yttr;
import com.unascribed.yttr.crafting.ShatteringRecipe;
import com.unascribed.yttr.init.YEnchantments;

import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.Recipe;
import net.minecraft.recipe.StonecuttingRecipe;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import thelm.jeidrawables.JEIDrawables;
import thelm.jeidrawables.gui.render.IngredientDrawable;
import thelm.jeidrawables.gui.render.ResourceDrawable;
import thelm.yttrjei.YttrJEI;

/**
 * Based on EmiShatteringRecipe
 */
public class ShatteringRecipeCategory extends AbstractRecipeCategory<Recipe<?>> {

	public static final Text TITLE = new TranslatableText("emi.category.yttr.shattering");

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
	public void setRecipe(IRecipeLayoutBuilder builder, Recipe<?> recipe, IFocusGroup focuses) {
		Ingredient input = recipe instanceof StonecuttingRecipe ?
				Ingredient.ofStacks(recipe.getOutput()) :
					recipe.getIngredients().get(0);
		ItemStack output = recipe instanceof StonecuttingRecipe ?
				new ItemStack(Item.byRawId(recipe.getIngredients().get(0).getMatchingItemIds().getInt(0))) :
					recipe.getOutput();
		addItem(builder, RecipeIngredientRole.INPUT, 1, 5, input, JEIDrawables.SLOT);
		addItem(builder, RecipeIngredientRole.OUTPUT, 63, 5, output, JEIDrawables.OUTPUT_SLOT);
	}

	@Override
	public void draw(Recipe<?> recipe, IRecipeSlotsView recipeSlotsView, MatrixStack poseStack, double mouseX, double mouseY) {
		if(recipe instanceof ShatteringRecipe) {
			SHATTERING.draw(poseStack, 26, 5);
		}
		else {
			JEIDrawables.RECIPE_ARROW.draw(poseStack, 27, 5);
		}
	}
}
