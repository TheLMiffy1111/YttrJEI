package thelm.yttrjei.mixin;

import org.spongepowered.asm.mixin.Mixin;

import com.unascribed.yttr.content.item.block.DyedBlockItem;
import com.unascribed.yttr.content.item.block.SkeletalSorterBlockItem;

import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.collection.DefaultedList;

@Mixin(
		value = {DyedBlockItem.class, SkeletalSorterBlockItem.class},
		targets = {"com.unascribed.yttr.init.content.YItems$1"}
		)
public abstract class SpecialBlockItemMixin extends BlockItem {

	private SpecialBlockItemMixin(Block block, Item.Settings properties) {
		super(block, properties);
	}

	@Override
	public void appendStacks(ItemGroup tab, DefaultedList<ItemStack> items) {
		if(isIn(tab)) {
			items.add(new ItemStack(this));
		}
	}
}
