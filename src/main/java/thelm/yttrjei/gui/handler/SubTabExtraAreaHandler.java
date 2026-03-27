package thelm.yttrjei.gui.handler;

import java.util.List;

import com.unascribed.yttr.mixinsupport.ItemGroupParent;
import com.unascribed.yttr.mixinsupport.SubTabLocation;

import mezz.jei.api.gui.handlers.IGuiContainerHandler;
import net.minecraft.client.gui.screen.ingame.CreativeInventoryScreen;
import net.minecraft.client.util.math.Rect2i;
import net.minecraft.item.ItemGroup;

public class SubTabExtraAreaHandler implements IGuiContainerHandler<CreativeInventoryScreen> {

	@Override
	public List<Rect2i> getGuiExtraAreas(CreativeInventoryScreen containerScreen) {
		ItemGroup selected = ItemGroup.GROUPS[containerScreen.getSelectedTab()];
		if(selected instanceof ItemGroupParent parent &&
				containerScreen instanceof SubTabLocation stl &&
				parent.yttr$getChildren() != null &&
				!parent.yttr$getChildren().isEmpty()) {
			return List.of(new Rect2i(stl.yttr$getX(), stl.yttr$getY(), stl.yttr$getW(), stl.yttr$getH()));
		}
		return List.of();
	}
}
