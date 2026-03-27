package thelm.yttrjei.ingredient.subtype;

import com.unascribed.yttr.content.item.AmmoCanItem;

import mezz.jei.api.ingredients.subtypes.IIngredientSubtypeInterpreter;
import mezz.jei.api.ingredients.subtypes.UidContext;
import net.minecraft.item.ItemStack;

public class AmmoCanItemSubtypeInterpreter implements IIngredientSubtypeInterpreter<ItemStack> {

	@Override
	public String apply(ItemStack ingredient, UidContext context) {
		if(ingredient.getItem() instanceof AmmoCanItem) {
			return ingredient.getNbt().getString("Mode");
		}
		return NONE;
	}
}
