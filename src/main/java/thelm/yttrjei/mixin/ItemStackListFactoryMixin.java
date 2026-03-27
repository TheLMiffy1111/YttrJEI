package thelm.yttrjei.mixin;

import org.apache.logging.log4j.Logger;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.unascribed.yttr.mixinsupport.ItemGroupParent;

import mezz.jei.common.plugins.vanilla.ingredients.item.ItemStackListFactory;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.collection.DefaultedList;

@Mixin(ItemStackListFactory.class)
public class ItemStackListFactoryMixin {

	@Shadow
	private static Logger LOGGER;

	@WrapOperation(method = "create", at = @At(value = "INVOKE", target = "Lnet/minecraft/item/ItemGroup;appendStacks(Lnet/minecraft/util/collection/DefaultedList;)V"))
	private static void yttrjei$appendSubTabContents(ItemGroup tab, DefaultedList<ItemStack> stacks, Operation<Void> original) {
		if(tab instanceof ItemGroupParent parent && !parent.yttr$getChildren().isEmpty()) {
			for(ItemGroup subTab : parent.yttr$getChildren()) {
				try {
					subTab.appendStacks(stacks);
				}
				catch(Throwable e) {
					LOGGER.error("Item subgroup crashed while getting items. Some items from this subgroup will be missing from the ingredient list. {}", subTab.getDisplayName().getString(), e);
					continue;
				}
			}
		}
		else {
			original.call(tab, stacks);
		}
	}
}
