package thelm.yttrjei.recipe.category;

import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

import com.unascribed.yttr.Yttr;
import com.unascribed.yttr.init.content.YItems;
import com.unascribed.yttr.mechanics.rifle.RifleMode;

import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
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
	public static final ResourceDrawable PROGRESS_0 = new ResourceDrawable(BACKGROUND, 32, 39, 47, 18);
	public static final ResourceDrawable PROGRESS_1 = new ResourceDrawable(BACKGROUND, 97, 39, 47, 18);
	public static final ResourceDrawable PROGRESS_2 = new ResourceDrawable(BACKGROUND, 79, 57, 18, 22);
	public static final Map<RifleMode, ITextureDrawable> ICONS = RifleMode.ALL_VALUES.stream().
			collect(Collectors.toUnmodifiableMap(
					Function.identity(),
					k -> new ResourceDrawable(Yttr.id("textures/gui/rifle_modes.png"), k.ordinal()*16, 0, 16, 16, RifleMode.ALL_VALUES.size()*16, 16).withColor(k.color)));

	public FillingRecipeCategory() {
		super(YttrJEI.FILLING, TITLE);
	}

	@Override
	public int getWidth() {
		return 112;
	}

	@Override
	public int getHeight() {
		return 88;
	}

	@Override
	public void setRecipe(IRecipeLayoutBuilder builder, FillingRecipe recipe, IFocusGroup focuses) {
		RifleMode mode = recipe.mode();
		addItem(builder, RecipeIngredientRole.INPUT, 1, 1, new ItemStack(mode.item.get()), JEIDrawables.SLOT);
		addItem(builder, RecipeIngredientRole.INPUT, 95, 1, new ItemStack(YItems.GLOWING_GAS), JEIDrawables.SLOT);
		int shots = mode.shotsPerItem;
		Optional<ItemStack> inputCanFocus = focuses.getItemStackFocuses(RecipeIngredientRole.INPUT).
				map(f -> f.getTypedValue().getIngredient()).
				filter(s -> s.getItem() == YItems.EMPTY_AMMO_CAN || s.getItem() == YItems.AMMO_CAN).
				findAny();
		if(inputCanFocus.isPresent()) {
			ItemStack inputCan = inputCanFocus.get();
			NbtCompound nbt = inputCan.getNbt();
			int inputShots = nbt == null ? 0 : nbt.getInt("Shots");
			addItem(builder, RecipeIngredientRole.INPUT, 48, 21, inputCan, JEIDrawables.SLOT);
			addItem(builder, RecipeIngredientRole.OUTPUT, 48, 67, createAmmoCan(mode, inputShots+shots), JEIDrawables.OUTPUT_SLOT);
		}
		else {
			List<ItemStack> canInputs = List.of(createAmmoCan(mode, 0), createAmmoCan(mode, shots), createAmmoCan(mode, 1024 - shots));
			List<ItemStack> canOutputs = List.of(createAmmoCan(mode, shots), createAmmoCan(mode, shots * 2), createAmmoCan(mode, 1024));
			builder.createFocusLink(
					addItem(builder, RecipeIngredientRole.INPUT, 48, 21, canInputs, JEIDrawables.SLOT),
					addItem(builder, RecipeIngredientRole.OUTPUT, 48, 67, canOutputs, JEIDrawables.OUTPUT_SLOT));
		}
	}

	@Override
	public void draw(FillingRecipe recipe, IRecipeSlotsView recipeSlotsView, MatrixStack poseStack, double mouseX, double mouseY) {
		PROGRESS_0.draw(poseStack, 0, 20);
		PROGRESS_1.draw(poseStack, 65, 20);
		PROGRESS_2.draw(poseStack, 47, 38);
		ICONS.get(recipe.mode()).draw(poseStack, 48, 1);
		TextRenderer font = font();
		RifleMode mode = recipe.mode();
		font.draw(poseStack, "+" + mode.shotsPerItem, 68, 5, 0x555555);
	}

	@Override
	public List<Text> getTooltipStrings(FillingRecipe recipe, IRecipeSlotsView recipeSlotsView, double mouseX, double mouseY) {
		if(mouseX >= 48 && mouseX < 64 && mouseY >= 1 && mouseY < 17) {
			RifleMode mode = recipe.mode();
			return List.of(Text.translatable("yttr.rifle_mode." + mode.name().toLowerCase(Locale.ROOT)).formatted(mode.chatColor));
		}
		return List.of();
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
			NbtCompound n = new NbtCompound();
			n.putString("Mode", mode.name());
			n.putInt("Shots", qty);
			is.setNbt(n);
			return is;
		}
	}
}
