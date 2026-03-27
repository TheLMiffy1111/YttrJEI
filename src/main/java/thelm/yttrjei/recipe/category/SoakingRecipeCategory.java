package thelm.yttrjei.recipe.category;

import java.util.List;

import com.unascribed.lib39.machination.recipe.SoakingRecipe;

import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import thelm.jeidrawables.JEIDrawables;
import thelm.jeidrawables.gui.render.IngredientDrawable;
import thelm.jeidrawables.gui.render.ResourceDrawable;
import thelm.yttrjei.YttrJEI;

/**
 * Based on EmiSoakingRecipe
 */
public class SoakingRecipeCategory extends AbstractRecipeCategory<SoakingRecipe> {

	public static final Text TITLE = Text.translatable("emi.category.lib39.soaking");
	public static final Text CONSUME_HINT = Text.translatable("emi.category.lib39.soaking.consumes_catalyst");

	public static final IDrawable ICON = new IngredientDrawable<>(YttrJEI.jeiHelpers.getPlatformFluidHelper().create(Fluids.WATER, YttrJEI.jeiHelpers.getPlatformFluidHelper().bucketVolume()));
	public static final IDrawable CURVED_ARROW_DOWN = new ResourceDrawable(new Identifier("lib39-machination:textures/gui/curved_arrow_down.png"), 0, 0, 16, 16, 16, 16);
	public static final IDrawable CURVED_ARROW = new ResourceDrawable(new Identifier("lib39-machination:textures/gui/curved_arrow.png"), 0, 0, 16, 16, 16, 16);

	public SoakingRecipeCategory() {
		super(YttrJEI.SOAKING, TITLE);
	}

	@Override
	public int getWidth() {
		return 124;
	}

	@Override
	public int getHeight() {
		return 40;
	}

	@Override
	public IDrawable getIcon() {
		return ICON;
	}

	@Override
	public void setRecipe(IRecipeLayoutBuilder builder, SoakingRecipe recipe, IFocusGroup focuses) {
		List<List<ItemStack>> inputs = recipe.getSoakingIngredients().
				map(stack -> List.of(List.of(stack)),
						ings -> ings.stream().
						map(ing -> List.of(ing.getMatchingStacks())).
						toList());
		boolean fluidUsed = recipe.getResult().right().isPresent();
		int inputCount = inputs.size();
		int xi = 9 * (3 - Math.min(inputCount, 3));
		int xo = 9 * (3 + Math.min(inputCount, 3));
		int y = 4;
		// we ignore inputs beyond 6 for now
		for(int i = 0; i < inputCount && i < 6; ++i) {
			addItem(builder, RecipeIngredientRole.INPUT, xi+i%3*18+1, y+i/3*18+1, inputs.get(i), JEIDrawables.SLOT);
		}
		addFluid(builder, fluidUsed ? RecipeIngredientRole.INPUT : RecipeIngredientRole.CATALYST, xo+5, y+19, 32, 16, recipe.getCatalyst()).addTooltipCallback((slot, tooltip) -> {
			if(fluidUsed) {
				tooltip.add(CONSUME_HINT);
			}
		});
		addItem(builder, RecipeIngredientRole.OUTPUT, xo+49, y+1, recipe.getOutput(), JEIDrawables.OUTPUT_SLOT);
	}

	@Override
	public void draw(SoakingRecipe recipe, IRecipeSlotsView recipeSlotsView, MatrixStack poseStack, double mouseX, double mouseY) {
		List<List<ItemStack>> inputs = recipe.getSoakingIngredients().
				map(stack -> List.of(List.of(stack)),
						ings -> ings.stream().
						map(ing -> List.of(ing.getMatchingStacks())).
						toList());
		int inputCount = inputs.size();
		int xo = 9 * (3 + Math.min(inputCount, 3));
		int y = 4;
		// we ignore inputs beyond 6 for now
		CURVED_ARROW_DOWN.draw(poseStack, xo+4, y+2);
		CURVED_ARROW.draw(poseStack, xo+24, y+2);
	}
}
