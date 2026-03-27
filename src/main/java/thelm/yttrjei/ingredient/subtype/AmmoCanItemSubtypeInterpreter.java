package thelm.yttrjei.ingredient.subtype;

import java.util.Locale;

import diy.y2k.yttr.init.technical.YOpponents;
import mezz.jei.api.ingredients.subtypes.IIngredientSubtypeInterpreter;
import mezz.jei.api.ingredients.subtypes.UidContext;
import net.minecraft.item.ItemStack;

public class AmmoCanItemSubtypeInterpreter implements IIngredientSubtypeInterpreter<ItemStack> {

	@Override
	public String apply(ItemStack ingredient, UidContext context) {
		if(YOpponents.MODE.present(ingredient)) {
			return YOpponents.MODE.get(ingredient).name().toLowerCase(Locale.ROOT);
		}
		return NONE;
	}
}
