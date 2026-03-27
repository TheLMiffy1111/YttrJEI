package thelm.yttrjei.recipe.category;

import java.util.List;

import com.unascribed.yttr.crafting.ingredient.FluidIngredient;

import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.builder.IRecipeSlotBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.helpers.IJeiHelpers;
import mezz.jei.api.helpers.IPlatformFluidHelper;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.fluid.Fluid;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.Ingredient;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import thelm.jeidrawables.gui.render.BlankDrawable;
import thelm.yttrjei.YttrJEI;

public abstract class AbstractRecipeCategory<R> implements IRecipeCategory<R> {

	public final RecipeType<R> recipeType;
	public final Text title;
	public final IDrawable background;

	public AbstractRecipeCategory(RecipeType<R> recipeType, Text title) {
		this.recipeType = recipeType;
		this.title = title;
		background = new BlankDrawable(getWidth(), getHeight());
	}

	@Override
	public Text getTitle() {
		return title;
	}

	@Override
	public Identifier getUid() {
		return recipeType.getUid();
	}

	@Override
	public Class<? extends R> getRecipeClass() {
		return recipeType.getRecipeClass();
	}

	@Override
	public RecipeType<R> getRecipeType() {
		return recipeType;
	}

	@Override
	public IDrawable getBackground() {
		return background;
	}

	public abstract int getWidth();

	public abstract int getHeight();

	@Override
	public IDrawable getIcon() {
		return null;
	}

	public TextRenderer font() {
		return MinecraftClient.getInstance().textRenderer;
	}

	public IJeiHelpers jeiHelpers() {
		return YttrJEI.jeiHelpers;
	}

	public IPlatformFluidHelper<?> fluidHelper() {
		return jeiHelpers().getPlatformFluidHelper();
	}

	public IRecipeSlotBuilder addSlot(IRecipeLayoutBuilder builder, RecipeIngredientRole ingredientRole, int x, int y, IDrawable background) {
		return builder.addSlot(ingredientRole, x, y).setBackground(background, 8 - background.getWidth() / 2, 8 - background.getHeight() / 2);
	}

	public IRecipeSlotBuilder addItem(IRecipeLayoutBuilder builder, RecipeIngredientRole ingredientRole, int x, int y, List<ItemStack> itemStacks, IDrawable background) {
		return addSlot(builder, ingredientRole, x, y, background).addItemStacks(itemStacks);
	}

	public IRecipeSlotBuilder addItem(IRecipeLayoutBuilder builder, RecipeIngredientRole ingredientRole, int x, int y, Ingredient ingredient, IDrawable background) {
		return addSlot(builder, ingredientRole, x, y, background).addIngredients(ingredient);
	}

	public IRecipeSlotBuilder addItem(IRecipeLayoutBuilder builder, RecipeIngredientRole ingredientRole, int x, int y, ItemStack itemStack, IDrawable background) {
		return addSlot(builder, ingredientRole, x, y, background).addItemStack(itemStack);
	}

	public IRecipeSlotBuilder addFluid(IRecipeLayoutBuilder builder, RecipeIngredientRole ingredientRole, int x, int y, int width, int height, FluidIngredient fluidIngredient) {
		IRecipeSlotBuilder slot = builder.addSlot(ingredientRole, x, y);
		for(Fluid fluid : fluidIngredient.getMatchingFluids()) {
			slot.addFluidStack(fluid, fluidHelper().bucketVolume());
		}
		slot.setFluidRenderer(fluidHelper().bucketVolume(), false, width, height);
		return slot;
	}
}
