package thelm.yttrjei.ingredient;

import mezz.jei.api.ingredients.IIngredientHelper;
import mezz.jei.api.ingredients.IIngredientType;
import mezz.jei.api.ingredients.subtypes.UidContext;
import net.minecraft.client.resource.language.I18n;
import net.minecraft.util.Identifier;

public class FakeModelIngredientHelper implements IIngredientHelper<FakeModelIngredient> {

	public static final FakeModelIngredientHelper INSTANCE = new FakeModelIngredientHelper();

	@Override
	public IIngredientType<FakeModelIngredient> getIngredientType() {
		return FakeModelIngredient.TYPE;
	}

	@Override
	public String getDisplayName(FakeModelIngredient ingredient) {
		return I18n.translate(ingredient.getTranslationKey());
	}

	@Override
	public String getUniqueId(FakeModelIngredient ingredient, UidContext context) {
		return "yttr_model:" + ingredient.id();
	}

	@Override
	public Identifier getResourceLocation(FakeModelIngredient ingredient) {
		return ingredient.id();
	}

	@Override
	public FakeModelIngredient copyIngredient(FakeModelIngredient ingredient) {
		return ingredient;
	}

	@Override
	public String getErrorInfo(FakeModelIngredient ingredient) {
		if(ingredient == null) {
			return "null";
		}
		return String.valueOf(ingredient.id());
	}
}
