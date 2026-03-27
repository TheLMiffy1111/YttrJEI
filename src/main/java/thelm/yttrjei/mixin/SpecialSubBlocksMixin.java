package thelm.yttrjei.mixin;

import org.spongepowered.asm.mixin.Mixin;

import com.unascribed.yttr.content.block.decor.LampBlock;
import com.unascribed.yttr.content.block.lazor.LazorEmitterBlock;
import com.unascribed.yttr.util.SpecialSubItems;

import net.minecraft.block.Block;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.collection.DefaultedList;

@Mixin({LampBlock.class, LazorEmitterBlock.class})
public abstract class SpecialSubBlocksMixin extends Block implements SpecialSubItems {

	public SpecialSubBlocksMixin(Block.Settings settings) {
		super(settings);
	}

	@Override
	public void appendStacks(ItemGroup tab, DefaultedList<ItemStack> items) {
		buildItems(null, items::add);
	}
}
