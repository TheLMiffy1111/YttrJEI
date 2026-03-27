package thelm.yttrjei.recipe.category;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.List;

import diy.y2k.yttr.Yttr;
import diy.y2k.yttr.init.content.YItems;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.builder.ITooltipBuilder;
import mezz.jei.api.gui.ingredient.IRecipeSlotDrawable;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.gui.placement.HorizontalAlignment;
import mezz.jei.api.gui.placement.VerticalAlignment;
import mezz.jei.api.gui.widgets.IRecipeExtrasBuilder;
import mezz.jei.api.gui.widgets.IScrollGridWidget;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import thelm.jeidrawables.JEIDrawables;
import thelm.yttrjei.YttrJEI;
import thelm.yttrjei.recipe.ContinuityGiftRecipe;

/**
 * Based on EmiGiftRecipe and TagInfoRecipeCategory
 */
public class ContinuityGiftRecipeCategory extends AbstractRecipeCategory<ContinuityGiftRecipe> {

	public static final NumberFormat CHANCE_FORMAT = new DecimalFormat("#.##");

	public static final Identifier ID = Yttr.id("continuity_gifts");
	public static final Text TITLE = Text.translatable("emi.category.yttr.continuity_gifts");
	public static final Text TOOLTIP = Text.translatable("emi.category.yttr.continuity_gifts.chance.tooltip");

	public ContinuityGiftRecipeCategory() {
		super(YttrJEI.CONTINUITY_GIFTS, TITLE);
	}

	@Override
	public int getWidth() {
		return 142;
	}

	@Override
	public int getHeight() {
		return 110;
	}

	@Override
	public void setRecipe(IRecipeLayoutBuilder builder, ContinuityGiftRecipe recipe, IFocusGroup focuses) {
		addItem(builder, RecipeIngredientRole.INPUT, 1, 1, new ItemStack(YItems.DROP_OF_CONTINUITY), JEIDrawables.SLOT);
		builder.addInvisibleIngredients(RecipeIngredientRole.INPUT).addItemStack(new ItemStack(YItems.LOOTBOX_OF_CONTINUITY));
		for(Item item : recipe.results()) {
			builder.addSlot(RecipeIngredientRole.OUTPUT).addItemStack(new ItemStack(item));
		}
	}

	@Override
	public void createRecipeExtras(IRecipeExtrasBuilder builder, ContinuityGiftRecipe recipe, IFocusGroup focuses) {
		List<IRecipeSlotDrawable> outputSlots = builder.getRecipeSlots().getSlots(RecipeIngredientRole.OUTPUT);
		IScrollGridWidget scrollGridWidget = builder.addScrollGridWidget(outputSlots, 7, 5);
		scrollGridWidget.setPosition(0, 20, getWidth(), getHeight() - 20, HorizontalAlignment.CENTER, VerticalAlignment.BOTTOM);
	}

	@Override
	public void draw(ContinuityGiftRecipe recipe, IRecipeSlotsView recipeSlotsView, DrawContext guiGraphics, double mouseX, double mouseY) {
		TextRenderer font = font();
		Text chanceComponent = Text.translatable("emi.category.yttr.continuity_gifts.chance", CHANCE_FORMAT.format(recipe.chance()));
		guiGraphics.drawText(font, chanceComponent, 23, 5, 0x404040, false);
	}

	@Override
	public void getTooltip(ITooltipBuilder tooltip, ContinuityGiftRecipe recipe, IRecipeSlotsView recipeSlotsView, double mouseX, double mouseY) {
		if(mouseX >= 23 && mouseX < 60 && mouseY >= 5 && mouseY < 14) {
			tooltip.add(TOOLTIP);
		}
	}

	@Override
	public Identifier getRegistryName(ContinuityGiftRecipe recipe) {
		return ID;
	}
}
