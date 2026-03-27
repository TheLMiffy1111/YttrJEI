package thelm.yttrjei.recipe;

import java.util.Optional;
import java.util.Set;

import com.unascribed.yttr.client.resource.RuinedRecipeResourceMetadata;

import it.unimi.dsi.fastutil.ints.IntSet;
import net.minecraft.item.Item;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public record ForgottenCraftingRecipe(Identifier id, Set<Integer> emptySlots, Item result) {

	public ForgottenCraftingRecipe(Identifier id, Optional<RuinedRecipeResourceMetadata> metadata) {
		this(id, metadata.map(m -> m.getEmptySlots()).orElse(IntSet.of()), Registry.ITEM.get(id));
	}
}
