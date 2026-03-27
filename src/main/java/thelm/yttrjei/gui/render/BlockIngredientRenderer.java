package thelm.yttrjei.gui.render;

import java.util.List;

import com.mojang.blaze3d.systems.RenderSystem;

import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.ingredients.IIngredientRenderer;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.client.render.LightmapTextureManager;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.client.render.model.json.ModelTransformation;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.math.Vec3f;
import thelm.yttrjei.YttrJEI;

public class BlockIngredientRenderer implements IIngredientRenderer<ItemStack> {

	public static final BlockIngredientRenderer INSTANCE = new BlockIngredientRenderer();

	private IIngredientRenderer<ItemStack> delegate;

	@Override
	public void render(MatrixStack poseStack, ItemStack ingredient) {
		if(YttrJEI.jeiRuntime == null) {
			return;
		}
		if(delegate == null) {
			delegate = YttrJEI.jeiRuntime.getIngredientManager().getIngredientRenderer(VanillaTypes.ITEM_STACK);
		}
		if(ingredient != null) {
			MinecraftClient minecraft = MinecraftClient.getInstance();
			TextRenderer font = getFontRenderer(minecraft, ingredient);
			ItemRenderer itemRenderer = minecraft.getItemRenderer();
			BakedModel model = itemRenderer.getModel(ingredient, null, null, 0);

			if(!model.hasDepth() || model.isBuiltin()) {
				delegate.render(poseStack, ingredient);
				return;
			}

			RenderSystem.enableDepthTest();
			VertexConsumerProvider.Immediate bufferSource = VertexConsumerProvider.immediate(Tessellator.getInstance().getBuffer());
			int light = LightmapTextureManager.pack(15, 15);
			int overlay = OverlayTexture.DEFAULT_UV;

			poseStack.push();
			poseStack.translate(8, 8, 50);
			poseStack.scale(16, -16, 16);
			poseStack.multiply(Vec3f.POSITIVE_X.getDegreesQuaternion(-90));
			poseStack.push();
			itemRenderer.renderItem(ingredient, ModelTransformation.Mode.NONE, false, poseStack, bufferSource, light, overlay, model);
			poseStack.pop();
			bufferSource.draw();
			poseStack.pop();

			itemRenderer.renderGuiItemOverlay(font, ingredient, 0, 0);
			RenderSystem.disableBlend();
		}
	}

	@Override
	public List<Text> getTooltip(ItemStack ingredient, TooltipContext tooltipFlag) {
		if(YttrJEI.jeiRuntime == null) {
			return List.of();
		}
		if(delegate == null) {
			delegate = YttrJEI.jeiRuntime.getIngredientManager().getIngredientRenderer(VanillaTypes.ITEM_STACK);
		}
		return delegate.getTooltip(ingredient, tooltipFlag);
	}
}
