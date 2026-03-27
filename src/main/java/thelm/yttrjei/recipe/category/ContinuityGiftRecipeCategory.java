package thelm.yttrjei.recipe.category;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.List;

import com.unascribed.yttr.init.content.YItems;

import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import thelm.jeidrawables.JEIDrawables;
import thelm.yttrjei.YttrJEI;
import thelm.yttrjei.recipe.ContinuityGiftRecipe;

/**
 * Based loosely on EmiGiftRecipe
 */
public class ContinuityGiftRecipeCategory extends AbstractRecipeCategory<ContinuityGiftRecipe> {

	public static final NumberFormat CHANCE_FORMAT = new DecimalFormat("#.##");

	public static final Text TITLE = Text.translatable("emi.category.yttr.continuity_gifts");
	public static final Text TOOLTIP = Text.translatable("emi.category.yttr.continuity_gifts.chance.tooltip");

	public ContinuityGiftRecipeCategory() {
		super(YttrJEI.CONTINUITY_GIFTS, TITLE);
	}

	@Override
	public int getWidth() {
		return 59;
	}

	@Override
	public int getHeight() {
		return 26;
	}

	@Override
	public void setRecipe(IRecipeLayoutBuilder builder, ContinuityGiftRecipe recipe, IFocusGroup focuses) {
		builder.addInvisibleIngredients(RecipeIngredientRole.INPUT).addItemStacks(List.of(
				new ItemStack(YItems.DROP_OF_CONTINUITY),
				new ItemStack(YItems.LOOTBOX_OF_CONTINUITY)));
		addItem(builder, RecipeIngredientRole.OUTPUT, 5, 5, new ItemStack(recipe.result()), JEIDrawables.OUTPUT_SLOT);
	}

	@Override
	public void draw(ContinuityGiftRecipe recipe, IRecipeSlotsView recipeSlotsView, MatrixStack poseStack, double mouseX, double mouseY) {
		TextRenderer font = font();
		Text chanceComponent = Text.translatable("emi.category.yttr.continuity_gifts.chance", CHANCE_FORMAT.format(recipe.chance()));
		font.draw(poseStack, chanceComponent, 30, 9, 0x404040);
	}

	@Override
	public List<Text> getTooltipStrings(ContinuityGiftRecipe recipe, IRecipeSlotsView recipeSlotsView, double mouseX, double mouseY) {
		if(mouseX >= 30 && mouseY >= 9 && mouseY < 18) {
			return List.of(TOOLTIP);
		}
		return List.of();
	}

	@Override
	public Identifier getRegistryName(ContinuityGiftRecipe recipe) {
		return Registry.ITEM.getId(recipe.result());
	}
}
