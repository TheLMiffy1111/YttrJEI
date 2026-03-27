package thelm.yttrjei.ingredient.subtype;

import diy.y2k.yttr.init.content.YItems;
import mezz.jei.api.ingredients.subtypes.IIngredientSubtypeInterpreter;
import mezz.jei.api.ingredients.subtypes.UidContext;
import net.minecraft.entity.EntityType;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;

public class SnareItemSubtypeInterpreter implements IIngredientSubtypeInterpreter<ItemStack> {

	@Override
	public String apply(ItemStack ingredient, UidContext context) {
		EntityType<?> entityType = YItems.SNARE.getEntityType(ingredient);
		return entityType == null ? NONE : Registries.ENTITY_TYPE.getKey(entityType).toString();
	}
}
