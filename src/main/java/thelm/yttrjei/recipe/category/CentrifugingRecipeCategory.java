package thelm.yttrjei.recipe.category;

import com.unascribed.yttr.Yttr;
import com.unascribed.yttr.crafting.CentrifugingRecipe;

import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import thelm.jeidrawables.gui.render.ResourceDrawable;
import thelm.yttrjei.YttrJEI;

/**
 * Based on EmiCentrifugingRecipe
 */
public class CentrifugingRecipeCategory extends AbstractRecipeCategory<CentrifugingRecipe> {

	public static final Text TITLE = Text.translatable("emi.category.yttr.centrifuging");

	public static final ResourceDrawable CENTRIFUGE = new ResourceDrawable(Yttr.id("textures/gui/centrifuge.png"), 41, 8, 94, 95);

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
	public void draw(CentrifugingRecipe recipe, IRecipeSlotsView recipeSlotsView, MatrixStack poseStack, double mouseX, double mouseY) {
		CENTRIFUGE.draw(poseStack);
	}

	public ItemStack getOutput(CentrifugingRecipe recipe, int index) {
		if(index >= 0 && index < recipe.getOutputs().size()) {
			return recipe.getOutputs().get(index);
		}
		return ItemStack.EMPTY;
	}
}
