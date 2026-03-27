package thelm.yttrjei.ingredient;

import mezz.jei.api.ingredients.IIngredientType;
import net.minecraft.util.Identifier;

public record FakeModelIngredient(Identifier id) {

	public static final IIngredientType<FakeModelIngredient> TYPE = () -> FakeModelIngredient.class;

	public FakeModelIngredient(Identifier id) {
		if(id.getClass() != Identifier.class) {
			id = new Identifier(id.getNamespace(), id.getPath());
		}
		this.id = id;
	}

	public String getTranslationKey() {
		return "yttr.emi_model." + id.getNamespace() + "." + id.getPath();
	}
}
