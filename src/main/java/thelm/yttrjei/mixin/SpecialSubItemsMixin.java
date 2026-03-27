package thelm.yttrjei.mixin;

import org.spongepowered.asm.mixin.Mixin;

import com.unascribed.yttr.content.item.AmmoCanItem;
import com.unascribed.yttr.util.SpecialSubItems;

import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.collection.DefaultedList;

@Mixin({AmmoCanItem.class})
public abstract class SpecialSubItemsMixin extends Item implements SpecialSubItems {

	public SpecialSubItemsMixin(Item.Settings settings) {
		super(settings);
	}

	@Override
	public void appendStacks(ItemGroup tab, DefaultedList<ItemStack> items) {
		if(isIn(tab)) {
			buildItems(null, items::add);
		}
	}
}
