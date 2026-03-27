package thelm.yttrjei.recipe.transfer;

import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;

import diy.y2k.yttr.init.YHandledScreens;
import diy.y2k.yttr.inventory.RafterScreenHandler;
import it.unimi.dsi.fastutil.ints.IntList;
import mezz.jei.api.constants.RecipeTypes;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.transfer.IRecipeTransferInfo;
import net.minecraft.recipe.CraftingRecipe;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.screen.slot.Slot;

public class RafterRecipeTransferInfo implements IRecipeTransferInfo<RafterScreenHandler, CraftingRecipe> {

	public static final IntList BASE_RECIPE_SLOTS = IntList.of(15, 16, 17, 21, 22, 23, 27, 28, 29);
	public static final IntList RECIPE_SLOTS = IntList.of(IntStream.concat(
			BASE_RECIPE_SLOTS.intStream(),
			IntStream.range(1, 37).filter(i -> !BASE_RECIPE_SLOTS.contains(i))).
			toArray());

	@Override
	public Class<RafterScreenHandler> getContainerClass() {
		return RafterScreenHandler.class;
	}

	@Override
	public Optional<ScreenHandlerType<RafterScreenHandler>> getMenuType() {
		return Optional.of(YHandledScreens.RAFTING);
	}

	@Override
	public RecipeType<CraftingRecipe> getRecipeType() {
		return RecipeTypes.CRAFTING;
	}

	@Override
	public boolean canHandle(RafterScreenHandler container, CraftingRecipe recipe) {
		return recipe.fits(3, 3);
	}

	@Override
	public List<Slot> getRecipeSlots(RafterScreenHandler container, CraftingRecipe recipe) {
		return RECIPE_SLOTS.intStream().mapToObj(container::getSlot).toList();
	}

	@Override
	public List<Slot> getInventorySlots(RafterScreenHandler container, CraftingRecipe recipe) {
		return IntStream.range(37, 73).mapToObj(container::getSlot).toList();
	}
}
