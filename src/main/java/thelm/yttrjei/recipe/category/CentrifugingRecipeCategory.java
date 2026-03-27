package thelm.yttrjei.recipe.category;

import diy.y2k.yttr.Yttr;
import diy.y2k.yttr.content.recipe.CentrifugingRecipe;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.widgets.IRecipeExtrasBuilder;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import thelm.jeidrawables.gui.render.ResourceDrawable;
import thelm.yttrjei.YttrJEI;

/**
 * Based on EmiCentrifugingRecipe
 */
public class CentrifugingRecipeCategory extends AbstractRecipeCategory<CentrifugingRecipe> {

	public static final Text TITLE = Text.translatable("emi.category.yttr.centrifuging");

	public static final Identifier BACKGROUND = Yttr.id("textures/gui/centrifuge.png");
	public static final ResourceDrawable CENTRIFUGE_X = new ResourceDrawable(BACKGROUND, 79, 12, 56, 84);
	public static final ResourceDrawable CENTRIFUGE_Y = new ResourceDrawable(BACKGROUND, 41, 24, 56, 83);

	public CentrifugingRecipeCategory() {
		super(YttrJEI.CENTRIFUGING, TITLE);
	}

	@Override
	public int getWidth() {
		return 94;
	}

	@Override
	public int getHeight() {
		return 95;
	}

	@Override
	public void setRecipe(IRecipeLayoutBuilder builder, CentrifugingRecipe recipe, IFocusGroup focuses) {
		builder.addSlot(RecipeIngredientRole.INPUT, 39, 40).addIngredients(recipe.getInput());
		builder.addSlot(RecipeIngredientRole.OUTPUT, 5, 36).addItemStack(getOutput(recipe, 0));
		builder.addSlot(RecipeIngredientRole.OUTPUT, 43, 5).addItemStack(getOutput(recipe, 1));
		builder.addSlot(RecipeIngredientRole.OUTPUT, 73, 44).addItemStack(getOutput(recipe, 2));
		builder.addSlot(RecipeIngredientRole.OUTPUT, 35, 74).addItemStack(getOutput(recipe, 3));
	}

	@Override
	public void createRecipeExtras(IRecipeExtrasBuilder builder, CentrifugingRecipe recipe, IFocusGroup focuses) {
		builder.addDrawable(CENTRIFUGE_X, 38, 0);
		builder.addDrawable(CENTRIFUGE_Y, 0, 12);
	}

	public ItemStack getOutput(CentrifugingRecipe recipe, int index) {
		if(index >= 0 && index < recipe.getOutputs().size()) {
			return recipe.getOutputs().get(index);
		}
		return ItemStack.EMPTY;
	}
}
