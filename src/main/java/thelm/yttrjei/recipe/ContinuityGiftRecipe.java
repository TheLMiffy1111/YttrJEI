package thelm.yttrjei.recipe;

import java.util.Collection;
import java.util.Comparator;

import diy.y2k.yttr.content.item.resource.DropOfContinuityItem;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;

public record ContinuityGiftRecipe(Collection<Item> results) {

	public ContinuityGiftRecipe() {
		this(DropOfContinuityItem.getPossibilities(true, null).
				stream().
				sorted(Comparator.comparingInt(Registries.ITEM::getRawId)).
				toList());
	}

	public double chance() {
		return 100D / results.size();
	}
}
