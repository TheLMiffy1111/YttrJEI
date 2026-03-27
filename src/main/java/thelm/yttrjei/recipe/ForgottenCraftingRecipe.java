package thelm.yttrjei.recipe;

import java.util.Optional;
import java.util.Set;

import diy.y2k.yttr.client.resource.RuinedRecipeResourceMetadata;
import it.unimi.dsi.fastutil.ints.IntSet;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;

public record ForgottenCraftingRecipe(Identifier id, Set<Integer> emptySlots, Item result) {

	public ForgottenCraftingRecipe(Identifier id, Optional<RuinedRecipeResourceMetadata> metadata) {
		this(id, metadata.map(m -> m.getEmptySlots()).orElse(IntSet.of()), Registries.ITEM.get(id));
	}
}
