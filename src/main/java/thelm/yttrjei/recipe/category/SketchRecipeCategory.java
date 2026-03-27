package thelm.yttrjei.recipe.category;

import java.util.List;

import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.widgets.IRecipeExtrasBuilder;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import thelm.jeidrawables.gui.render.IngredientDrawable;
import thelm.jeidrawables.gui.render.ResourceDrawable;
import thelm.yttrjei.YttrJEI;
import thelm.yttrjei.recipe.SketchRecipe;

/**
 * Based on EmiSketchRecipe
 */
public class SketchRecipeCategory extends AbstractRecipeCategory<SketchRecipe> {

	public static final Text TITLE = Text.translatable("emi.category.yttr.sketches");

	public static final IDrawable ICON = new IngredientDrawable<>(new ItemStack(Items.BRUSH));

	public SketchRecipeCategory() {
		super(YttrJEI.SKETCHES, TITLE);
	}

	@Override
	public int getWidth() {
		return 320;
	}

	@Override
	public int getHeight() {
		return 180;
	}

	@Override
	public IDrawable getIcon() {
		return ICON;
	}

	@Override
	public void setRecipe(IRecipeLayoutBuilder builder, SketchRecipe recipe, IFocusGroup focuses) {
		List<List<Object>> inputs = recipe.inputs();
		List<List<Object>> catalysts = recipe.catalysts();
		List<List<Object>> outputs = recipe.outputs();
		int sx = 0;
		for(List<Object> in : inputs) {
			builder.addSlot(RecipeIngredientRole.INPUT, sx+1, getHeight()-17).addIngredientsUnsafe(in);
			sx += 18;
		}
		sx = (getWidth() - catalysts.size()*18) / 2;
		for(List<Object> cat : catalysts) {
			builder.addSlot(RecipeIngredientRole.CATALYST, sx+1, getHeight()-17).addIngredientsUnsafe(cat);
			sx += 18;
		}
		sx = getWidth() - outputs.size()*18;
		for(List<Object> out : outputs) {
			builder.addSlot(RecipeIngredientRole.OUTPUT, sx+1, getHeight()-17).addIngredientsUnsafe(out);
			sx += 18;
		}
	}

	@Override
	public void createRecipeExtras(IRecipeExtrasBuilder builder, SketchRecipe recipe, IFocusGroup focuses) {
		Identifier id = recipe.id();
		IDrawable recipeImage = new ResourceDrawable(new Identifier(id.getNamespace(), "textures/gui/sketch_recipe/" + id.getPath() + ".png"), 0, 0, 320, 180, 320, 180);
		builder.addDrawable(recipeImage, 0, 0);
	}

	@Override
	public Identifier getRegistryName(SketchRecipe recipe) {
		return recipe.id();
	}
}
