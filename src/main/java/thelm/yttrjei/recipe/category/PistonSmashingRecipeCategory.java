package thelm.yttrjei.recipe.category;

import java.util.List;

import com.unascribed.lib39.machination.recipe.PistonSmashingRecipe;

import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.widgets.IRecipeExtrasBuilder;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import thelm.jeidrawables.JEIDrawables;
import thelm.jeidrawables.gui.render.IColorableDrawable;
import thelm.jeidrawables.gui.render.ResourceDrawable;
import thelm.jeidrawables.gui.render.RotatedDrawable;
import thelm.yttrjei.YttrJEI;
import thelm.yttrjei.gui.render.BlockIngredientRenderer;

/**
 * Based on EmiPistonSmashingRecipe
 */
public class PistonSmashingRecipeCategory extends AbstractRecipeCategory<PistonSmashingRecipe> {

	public static final Text TITLE = Text.translatable("emi.category.lib39.piston_smashing");
	public static final Text CLOUD_HINT = Text.translatable("emi.category.lib39.piston_smashing.cloud_output_hint");

	public static final IDrawable CURVED_ARROW = new ResourceDrawable(new Identifier("lib39-machination:textures/gui/curved_arrow.png"), 0, 0, 16, 16, 16, 16);
	public static final IDrawable PISTON_SIDE = new RotatedDrawable(new ResourceDrawable(new Identifier("textures/block/piston_side.png"), 0, 0, 16, 16, 16, 16), 90);
	public static final IColorableDrawable CLOUD = new ResourceDrawable(new Identifier("textures/particle/effect_4.png"), 0, 0, 8, 8, 8, 8);

	public PistonSmashingRecipeCategory() {
		super(YttrJEI.PISTON_SMASHING, TITLE);
	}

	@Override
	public int getWidth() {
		return 111;
	}

	@Override
	public int getHeight() {
		return 38;
	}

	@Override
	public void setRecipe(IRecipeLayoutBuilder builder, PistonSmashingRecipe recipe, IFocusGroup focuses) {
		List<Block> input = recipe.getInput().getMatchingBlocks();
		List<Block> catalysts = recipe.getCatalyst().getMatchingBlocks();
		ItemStack output = recipe.getOutput(registryAccess());
		ItemStack cloudOutput = recipe.getCloudOutput().copy();
		cloudOutput.setCount(cloudOutput.getCount() * recipe.getCloudSize());
		builder.addSlot(RecipeIngredientRole.INPUT, 32, 22).addItemStacks(input.stream().map(ItemStack::new).toList()).setCustomRenderer(VanillaTypes.ITEM_STACK, BlockIngredientRenderer.INSTANCE);
		builder.addSlot(RecipeIngredientRole.CATALYST, 16, 22).addItemStacks(catalysts.stream().map(ItemStack::new).toList()).setCustomRenderer(VanillaTypes.ITEM_STACK, BlockIngredientRenderer.INSTANCE);
		builder.addSlot(RecipeIngredientRole.CATALYST, 48, 22).addItemStacks(catalysts.stream().map(ItemStack::new).toList()).setCustomRenderer(VanillaTypes.ITEM_STACK, BlockIngredientRenderer.INSTANCE);
		int x = 59;
		if(!output.isEmpty()) {
			addItem(builder, RecipeIngredientRole.OUTPUT, x+1, 1, output, JEIDrawables.SLOT);
			x += 22;
		}
		if(!cloudOutput.isEmpty()) {
			x += 12;
			addItem(builder, RecipeIngredientRole.OUTPUT, x+1, 1, cloudOutput, JEIDrawables.SLOT).addRichTooltipCallback((slots, tooltip) -> tooltip.add(CLOUD_HINT));
		}
	}

	@Override
	public void createRecipeExtras(IRecipeExtrasBuilder builder, PistonSmashingRecipe recipe, IFocusGroup focuses) {
		ItemStack output = recipe.getOutput(registryAccess());
		ItemStack cloudOutput = recipe.getCloudOutput();
		builder.addDrawable(PISTON_SIDE, 0, 22);
		builder.addDrawable(CURVED_ARROW, 39, 3);
		int x = 59;
		if(!output.isEmpty()) {
			x += 22;
		}
		if(!cloudOutput.isEmpty()) {
			builder.addDrawable(CLOUD.withColor(recipe.getCloudColor()), x, 6);
		}
	}
}
