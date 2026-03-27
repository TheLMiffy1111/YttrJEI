package thelm.yttrjei.recipe.category;

import java.text.DecimalFormat;
import java.text.NumberFormat;

import com.unascribed.yttr.crafting.VoidFilteringRecipe;

import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;
import thelm.jeidrawables.JEIDrawables;
import thelm.yttrjei.YttrJEI;

/**
 * Based on EmiVoidFilteringRecipe
 */
public class VoidFilteringRecipeCategory extends AbstractRecipeCategory<VoidFilteringRecipe> {

	public static final NumberFormat CHANCE_FORMAT = new DecimalFormat("#.##");

	public static final Text TITLE = Text.translatable("emi.category.yttr.void_filtering");

	public VoidFilteringRecipeCategory() {
		super(YttrJEI.VOID_FILTERING, TITLE);
	}

	@Override
	public int getWidth() {
		return 100;
	}

	@Override
	public int getHeight() {
		return 26;
	}

	@Override
	public void setRecipe(IRecipeLayoutBuilder builder, VoidFilteringRecipe recipe, IFocusGroup focuses) {
		addItem(builder, RecipeIngredientRole.OUTPUT, 5, 5, recipe.getOutput(), JEIDrawables.OUTPUT_SLOT);
	}

	@Override
	public void draw(VoidFilteringRecipe recipe, IRecipeSlotsView recipeSlotsView, MatrixStack poseStack, double mouseX, double mouseY) {
		TextRenderer font = font();
		Text chanceComponent = Text.translatable("emi.category.yttr.void_filtering.chance", CHANCE_FORMAT.format(recipe.getChance()));
		font.draw(poseStack, chanceComponent, 30, 9, 0x404040);
	}
}
