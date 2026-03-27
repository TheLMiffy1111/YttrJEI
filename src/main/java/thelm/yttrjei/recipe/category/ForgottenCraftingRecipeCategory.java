package thelm.yttrjei.recipe.category;

import java.util.List;

import com.mojang.blaze3d.platform.GlStateManager.DstFactor;
import com.mojang.blaze3d.platform.GlStateManager.SrcFactor;
import com.unascribed.yttr.Yttr;
import com.unascribed.yttr.init.YItems;

import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import thelm.jeidrawables.gui.render.BlendFunction;
import thelm.jeidrawables.gui.render.IngredientDrawable;
import thelm.jeidrawables.gui.render.ResourceDrawable;
import thelm.jeidrawables.gui.render.ScaledDrawable;
import thelm.yttrjei.YttrJEI;
import thelm.yttrjei.recipe.ForgottenCraftingRecipe;

/**
 * Based on EmiForgottenRecipe
 */
public class ForgottenCraftingRecipeCategory extends AbstractRecipeCategory<ForgottenCraftingRecipe> {

	public static final Text TITLE = new TranslatableText("emi.category.yttr.forgotten_crafting");

	public static final BlendFunction MULTIPLY = new BlendFunction(SrcFactor.ZERO, DstFactor.SRC_COLOR, SrcFactor.ZERO, DstFactor.ONE);
	public static final IDrawable ICON = new IngredientDrawable<>(new ItemStack(YItems.WASTELAND_DIRT));
	public static final IDrawable OVERLAY = new ResourceDrawable(Yttr.id("textures/gui/ruined_recipe/overlay.png"), 0, 0, 116, 54, 116, 54, MULTIPLY);
	public static final IDrawable BORDER = new ScaledDrawable(new ResourceDrawable(Yttr.id("textures/gui/ruined_recipe/border.png"), 1, 1, 216, 123, 218, 125), 2/3F);

	public ForgottenCraftingRecipeCategory() {
		super(YttrJEI.FORGOTTEN_CRAFTING, TITLE);
	}

	@Override
	public int getWidth() {
		return 116;
	}

	@Override
	public int getHeight() {
		return 54;
	}

	@Override
	public IDrawable getIcon() {
		return ICON;
	}

	@Override
	public void setRecipe(IRecipeLayoutBuilder builder, ForgottenCraftingRecipe recipe, IFocusGroup focuses) {
		builder.addInvisibleIngredients(RecipeIngredientRole.OUTPUT).addItemStack(new ItemStack(recipe.result()));
	}

	@Override
	public void draw(ForgottenCraftingRecipe recipe, IRecipeSlotsView recipeSlotsView, MatrixStack poseStack, double mouseX, double mouseY) {
		Identifier id = recipe.id();
		IDrawable recipeImage = new ResourceDrawable(new Identifier(id.getNamespace(), "textures/gui/ruined_recipe/" + id.getPath() + ".png"), 0, 0, 116, 54, 116, 54);
		recipeImage.draw(poseStack, 0, 0);
		OVERLAY.draw(poseStack, 0, 0);
		BORDER.draw(poseStack, -14, -14);
		for(int i = 0; i < 9; ++i) {
			if(!recipe.emptySlots().contains(i)) {
				int x = i%3*18+1;
				int y = i/3*18+1;
				if(mouseX >= x && mouseX < x+16 && mouseY >= y && mouseY < y+16) {
					DrawableHelper.fill(poseStack, x, y, x+16, y+16, 0x80FFFFFF);
				}
			}
		}
		if(mouseX >= 95 && mouseX < 111 && mouseY >= 19 && mouseY < 35) {
			DrawableHelper.fill(poseStack, 95, 19, 111, 35, 0x80FFFFFF);
		}
	}

	@Override
	public List<Text> getTooltipStrings(ForgottenCraftingRecipe recipe, IRecipeSlotsView recipeSlotsView, double mouseX, double mouseY) {
		for(int i = 0; i < 9; ++i) {
			if(!recipe.emptySlots().contains(i)) {
				int x = i%3*18+1;
				int y = i/3*18+1;
				if(mouseX >= x && mouseX < x+16 && mouseY >= y && mouseY < y+16) {
					return List.of(new TranslatableText("container.enchant.clue").formatted(Formatting.ITALIC));
				}
			}
		}
		if(mouseX >= 95 && mouseX < 111 && mouseY >= 19 && mouseY < 35) {
			return List.of(new TranslatableText("container.enchant.clue", new TranslatableText(recipe.result().getTranslationKey() + ".alt")).formatted(Formatting.ITALIC));
		}
		return List.of();
	}

	@Override
	public Identifier getRegistryName(ForgottenCraftingRecipe recipe) {
		return recipe.id();
	}
}
