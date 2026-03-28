package thelm.yttrjei.mixin;

import org.spongepowered.asm.mixin.Mixin;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.screen.slot.Slot;

@Mixin(targets = "com.unascribed.yttr.inventory.CanFillerScreenHandler$CFSlot")
public abstract class CanFillerSlotMixin extends Slot {

	private CanFillerSlotMixin(Inventory container, int slot, int x, int y) {
		super(container, slot, x, y);
	}

	@Override
	public boolean canTakePartial(PlayerEntity player) {
		return canTakeItems(player) && (getStack().isEmpty() || canInsert(getStack()));
	}
}
