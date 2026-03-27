package thelm.yttrjei.recipe.category;

import diy.y2k.yttr.content.recipe.PinchingRecipe;
import diy.y2k.yttr.init.content.YItems;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.widgets.IRecipeExtrasBuilder;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import net.minecraft.client.MinecraftClient;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.Ingredient;
import net.minecraft.text.Text;
import net.minecraft.util.Arm;
import net.minecraft.util.Identifier;
import thelm.jeidrawables.JEIDrawables;
import thelm.jeidrawables.gui.render.ResourceDrawable;
import thelm.yttrjei.YttrJEI;

/**
 * Based on EmiPinchingRecipe
 */
public class PinchingRecipeCategory extends AbstractRecipeCategory<PinchingRecipe> {

	public static final Text TITLE = Text.translatable("emi.category.yttr.pinching");

	// Yttr's pinching texture is read incorrectly
	//public static final Identifier BACKGROUND = Yttr.id("textures/gui/pinching.png");
	public static final Identifier BACKGROUND = new Identifier("yttrjei:textures/gui/pinching.png");
	public static final ResourceDrawable LEFT_HAND = new ResourceDrawable(BACKGROUND, 0, 0, 54, 29, 54, 72);
	public static final ResourceDrawable RIGHT_HAND = new ResourceDrawable(BACKGROUND, 0, 36, 54, 29, 54, 72);

	public PinchingRecipeCategory() {
		super(YttrJEI.PINCHING, TITLE);
	}

	@Override
	public int getWidth() {
		return 90;
	}

	@Override
	public int getHeight() {
		return 55;
	}

	@Override
	public void setRecipe(IRecipeLayoutBuilder builder, PinchingRecipe recipe, IFocusGroup focuses) {
		boolean leftHand = MinecraftClient.getInstance().options.getMainArm().getValue() == Arm.LEFT;
		boolean usesPinch = recipe.destroysPinchPoint();
		addItem(builder, RecipeIngredientRole.INPUT, leftHand ? 1 : 73, 1, recipe.mainhand(), JEIDrawables.SLOT);
		addItem(builder, RecipeIngredientRole.INPUT, leftHand ? 73 : 1, 1, recipe.offhand().orElse(Ingredient.EMPTY), JEIDrawables.SLOT);
		builder.addSlot(usesPinch ? RecipeIngredientRole.INPUT : RecipeIngredientRole.CATALYST, 37, 1).addItemStack(new ItemStack(YItems.PINCH_POINT));
		addItem(builder, RecipeIngredientRole.OUTPUT, 37, 34, recipe.output(), JEIDrawables.OUTPUT_SLOT);
		if(usesPinch) {
			builder.addSlot(RecipeIngredientRole.OUTPUT, 60, 34).addItemStack(new ItemStack(YItems.BEDROCK_SHARD, 6));
		}
	}

	@Override
	public void createRecipeExtras(IRecipeExtrasBuilder builder, PinchingRecipe recipe, IFocusGroup focuses) {
		IDrawable drawable;
		int offset = 0;
		if(MinecraftClient.getInstance().options.getMainArm().getValue() == Arm.LEFT) {
			drawable = recipe.offhand().isPresent() ? LEFT_HAND : LEFT_HAND.trim(0, 0, 0, 18);
			offset = 0;
		}
		else {
			drawable = recipe.offhand().isPresent() ? RIGHT_HAND : RIGHT_HAND.trim(0, 0, 18, 0);
			offset = recipe.offhand().isPresent() ? 0 : 18;
		}
		builder.addDrawable(drawable, 18+offset, 0);
	}
}
