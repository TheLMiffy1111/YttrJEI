package thelm.yttrjei.recipe;

import java.util.Set;

import com.unascribed.yttr.client.RuinedRecipeResourceMetadata;

import it.unimi.dsi.fastutil.ints.IntSet;
import net.minecraft.item.Item;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public record ForgottenCraftingRecipe(Identifier id, Set<Integer> emptySlots, Item result) {

	public ForgottenCraftingRecipe(Identifier id, RuinedRecipeResourceMetadata metadata) {
		this(id, metadata == null ? IntSet.of() : metadata.getEmptySlots(), Registry.ITEM.get(id));
	}
}
