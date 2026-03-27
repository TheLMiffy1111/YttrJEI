package thelm.yttrjei.ingredient.subtype;

import java.util.List;

import mezz.jei.api.ingredients.subtypes.IIngredientSubtypeInterpreter;
import mezz.jei.api.ingredients.subtypes.UidContext;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionUtil;

public class PotionItemSubtypeInterpreter implements IIngredientSubtypeInterpreter<ItemStack> {

	@Override
	public String apply(ItemStack ingredient, UidContext context) {
		if(!ingredient.hasNbt()) {
			return NONE;
		}
		Potion potionType = PotionUtil.getPotion(ingredient);
		String potionTypeString = potionType.finishTranslationKey("");
		StringBuilder stringBuilder = new StringBuilder(potionTypeString);
		List<StatusEffectInstance> effects = PotionUtil.getPotionEffects(ingredient);
		for(StatusEffectInstance effect : effects) {
			stringBuilder.append(";").append(effect);
		}
		return stringBuilder.toString();
	}
}
