package thelm.yttrjei.recipe.category;

import java.util.List;

import com.unascribed.yttr.Yttr;
import com.unascribed.yttr.crafting.PistonSmashingRecipe;

import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import net.minecraft.block.Block;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
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

	public static final Text TITLE = new TranslatableText("emi.category.yttr.piston_smashing");
	public static final Text CLOUD_HINT = new TranslatableText("emi.category.yttr.piston_smashing.cloud_output_hint");

	public static final IDrawable CURVED_ARROW = new ResourceDrawable(Yttr.id("textures/gui/curved_arrow.png"), 0, 0, 16, 16, 16, 16);
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
		ItemStack output = recipe.getOutput();
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
			addItem(builder, RecipeIngredientRole.OUTPUT, x+1, 1, cloudOutput, JEIDrawables.SLOT).addTooltipCallback((slots, tooltip) -> tooltip.add(CLOUD_HINT));
		}
	}

	@Override
	public void draw(PistonSmashingRecipe recipe, IRecipeSlotsView recipeSlotsView, MatrixStack poseStack, double mouseX, double mouseY) {
		ItemStack output = recipe.getOutput();
		ItemStack cloudOutput = recipe.getCloudOutput();
		PISTON_SIDE.draw(poseStack, 0, 22);
		CURVED_ARROW.draw(poseStack, 39, 3);
		int x = 59;
		if(!output.isEmpty()) {
			x += 22;
		}
		if(!cloudOutput.isEmpty()) {
			CLOUD.withColor(recipe.getCloudColor()).draw(poseStack, x, 6);
		}
	}
}
