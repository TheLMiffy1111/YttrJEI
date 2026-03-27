package thelm.yttrjei.ingredient.subtype;

import com.unascribed.yttr.content.item.block.LampBlockItem;
import com.unascribed.yttr.mechanics.LampColor;

import mezz.jei.api.ingredients.subtypes.IIngredientSubtypeInterpreter;
import mezz.jei.api.ingredients.subtypes.UidContext;
import net.minecraft.item.ItemStack;

public class LampItemSubtypeInterpreter implements IIngredientSubtypeInterpreter<ItemStack> {

	@Override
	public String apply(ItemStack ingredient, UidContext context) {
		LampColor color = LampBlockItem.getColor(ingredient);
		boolean inverted = LampBlockItem.isInverted(ingredient);
		return color + "|" + inverted;
	}
}
