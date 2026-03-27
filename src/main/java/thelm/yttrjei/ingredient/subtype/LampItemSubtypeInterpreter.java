package thelm.yttrjei.ingredient.subtype;

import diy.y2k.yttr.init.technical.YOpponents;
import diy.y2k.yttr.mechanics.LampColor;
import mezz.jei.api.ingredients.subtypes.IIngredientSubtypeInterpreter;
import mezz.jei.api.ingredients.subtypes.UidContext;
import net.minecraft.item.ItemStack;

public class LampItemSubtypeInterpreter implements IIngredientSubtypeInterpreter<ItemStack> {

	@Override
	public String apply(ItemStack ingredient, UidContext context) {
		LampColor color = YOpponents.LAMP_COLOR.get(ingredient);
		boolean inverted = YOpponents.INVERTED.get(ingredient);
		return color + "|" + inverted;
	}
}
