package thelm.yttrjei.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import diy.y2k.yttr.client.YttrClientInit;

@Mixin(YttrClientInit.class)
public class YttrClientInitMixin {

	@Redirect(method = "*", at = @At(value = "INVOKE", target = "Ldiy/y2k/yttr/client/DevModeReminders;test()V"))
	public void cancelDevReminders() {
		// This method loads an EMI class, we don't want that
	}
}
