package thelm.yttrjei.recipe.category;

import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

import diy.y2k.yttr.Yttr;
import diy.y2k.yttr.init.content.YItems;
import diy.y2k.yttr.init.technical.YOpponents;
import diy.y2k.yttr.mechanics.rifle.RifleMode;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.builder.ITooltipBuilder;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.gui.widgets.IRecipeExtrasBuilder;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import thelm.jeidrawables.JEIDrawables;
import thelm.jeidrawables.gui.render.ITextureDrawable;
import thelm.jeidrawables.gui.render.ResourceDrawable;
import thelm.yttrjei.YttrJEI;
import thelm.yttrjei.recipe.FillingRecipe;

/**
 * Based on EmiFillingRecipe
 */
public class FillingRecipeCategory extends AbstractRecipeCategory<FillingRecipe> {

	public static final Text TITLE = Text.translatable("emi.category.yttr.filling");

	public static final Identifier BACKGROUND = Yttr.id("textures/gui/can_filler.png");
	public static final ResourceDrawable PROGRESS_0 = new ResourceDrawable(BACKGROUND, 28, 25, 36, 4);
	public static final ResourceDrawable PROGRESS_1 = new ResourceDrawable(BACKGROUND, 64, 7, 18, 18);
	public static final ResourceDrawable PROGRESS_2 = new ResourceDrawable(BACKGROUND, 82, 25, 23, 18);
	public static final Map<RifleMode, ITextureDrawable> ICONS = RifleMode.ALL_VALUES.stream().
			collect(Collectors.toUnmodifiableMap(
					Function.identity(),
					k -> new ResourceDrawable(Yttr.id("textures/gui/rifle_modes.png"), k.ordinal()*16, 0, 16, 16, RifleMode.ALL_VALUES.size()*16, 16).withColor(k.color)));

	public FillingRecipeCategory() {
		super(YttrJEI.FILLING, TITLE);
	}

	@Override
	public int getWidth() {
		return 104;
	}

	@Override
	public int getHeight() {
		return 40;
	}

	@Override
	public void setRecipe(IRecipeLayoutBuilder builder, FillingRecipe recipe, IFocusGroup focuses) {
		RifleMode mode = recipe.mode();
		addItem(builder, RecipeIngredientRole.INPUT, 1, 1, new ItemStack(mode.item.get()), JEIDrawables.SLOT);
		addItem(builder, RecipeIngredientRole.INPUT, 19, 1, new ItemStack(YItems.GLOWING_GAS), JEIDrawables.SLOT);
		int shots = mode.shotsPerItemInCanFiller();
		Optional<ItemStack> inputCanFocus = focuses.getItemStackFocuses(RecipeIngredientRole.INPUT).
				map(f -> f.getTypedValue().getIngredient()).
				filter(s -> s.getItem() == YItems.EMPTY_AMMO_CAN || s.getItem() == YItems.AMMO_CAN).
				findAny();
		if(inputCanFocus.isPresent()) {
			ItemStack inputCan = inputCanFocus.get();
			int inputShots = YOpponents.SHOTS.get(inputCan);
			addItem(builder, RecipeIngredientRole.INPUT, 37, 19, inputCan, JEIDrawables.SLOT);
			addItem(builder, RecipeIngredientRole.OUTPUT, 83, 19, createAmmoCan(mode, inputShots+shots), JEIDrawables.OUTPUT_SLOT);
		}
		else {
			List<ItemStack> canInputs = List.of(createAmmoCan(mode, 0), createAmmoCan(mode, shots), createAmmoCan(mode, 1024 - shots));
			List<ItemStack> canOutputs = List.of(createAmmoCan(mode, shots), createAmmoCan(mode, shots * 2), createAmmoCan(mode, 1024));
			builder.createFocusLink(
					addItem(builder, RecipeIngredientRole.INPUT, 37, 19, canInputs, JEIDrawables.SLOT),
					addItem(builder, RecipeIngredientRole.OUTPUT, 83, 19, canOutputs, JEIDrawables.OUTPUT_SLOT));
		}
	}

	@Override
	public void createRecipeExtras(IRecipeExtrasBuilder builder, FillingRecipe recipe, IFocusGroup focuses) {
		builder.addDrawable(PROGRESS_0, 0, 18);
		builder.addDrawable(PROGRESS_2, 54, 18);
		builder.addDrawable(ICONS.get(recipe.mode()), 1, 23);
	}

	@Override
	public void draw(FillingRecipe recipe, IRecipeSlotsView recipeSlotsView, DrawContext guiGraphics, double mouseX, double mouseY) {
		PROGRESS_1.draw(guiGraphics, 36, 0);
		TextRenderer font = font();
		RifleMode mode = recipe.mode();
		guiGraphics.drawText(font, "+" + mode.shotsPerItemInCanFiller(), 52, 8, 0x555555, false);
	}

	@Override
	public void getTooltip(ITooltipBuilder tooltip, FillingRecipe recipe, IRecipeSlotsView recipeSlotsView, double mouseX, double mouseY) {
		if(mouseX >= 1 && mouseX < 17 && mouseY >= 23 && mouseY < 39) {
			RifleMode mode = recipe.mode();
			tooltip.add(Text.translatable("yttr.rifle_mode." + mode.name().toLowerCase(Locale.ROOT)).formatted(mode.chatColor));
		}
	}

	@Override
	public Identifier getRegistryName(FillingRecipe recipe) {
		return Yttr.id(recipe.mode().name().toLowerCase(Locale.ROOT));
	}

	public ItemStack createAmmoCan(RifleMode mode, int qty) {
		if(qty <= 0) {
			return new ItemStack(YItems.EMPTY_AMMO_CAN);
		}
		else {
			if(qty > 1024) {
				qty = 1024;
			}
			ItemStack is = new ItemStack(YItems.AMMO_CAN);
			YOpponents.MODE.set(is, mode);
			YOpponents.SHOTS.set(is, qty);
			return is;
		}
	}
}
