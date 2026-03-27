package thelm.yttrjei.gui.render;

import java.util.ArrayList;
import java.util.List;

import com.mojang.blaze3d.systems.RenderSystem;
import com.unascribed.yttr.mixin.client.AccessorItemRenderer;

import mezz.jei.api.ingredients.IIngredientRenderer;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.client.render.DiffuseLighting;
import net.minecraft.client.render.LightmapTextureManager;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.TexturedRenderLayers;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.client.render.model.json.ModelTransformation;
import net.minecraft.client.resource.language.I18n;
import net.minecraft.client.util.ModelIdentifier;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import thelm.yttrjei.ingredient.FakeModelIngredient;

public class FakeModelIngredientRenderer implements IIngredientRenderer<FakeModelIngredient> {

	public static final FakeModelIngredientRenderer INSTANCE = new FakeModelIngredientRenderer();

	@Override
	public void render(MatrixStack poseStack, FakeModelIngredient ingredient) {
		if(ingredient != null) {
			MinecraftClient minecraft = MinecraftClient.getInstance();
			BakedModel model = minecraft.getBakedModelManager().getModel(new ModelIdentifier(ingredient.id(), "inventory"));

			MatrixStack modelViewStack = RenderSystem.getModelViewStack();
			modelViewStack.push();
			modelViewStack.multiplyPositionMatrix(poseStack.peek().getPositionMatrix());

			RenderSystem.enableDepthTest();
			modelViewStack.push();
			modelViewStack.translate(8, 8, 50);
			modelViewStack.scale(1, -1, 1);
			modelViewStack.scale(16, 16, 16);
			RenderSystem.applyModelViewMatrix();

			if(!model.isSideLit()) {
				DiffuseLighting.disableGuiDepthLighting();
			}

			MatrixStack poseStack2 = new MatrixStack();
			model.getTransformation().getTransformation(ModelTransformation.Mode.GUI).apply(false, poseStack2);
			poseStack2.translate(-0.5, -0.5, -0.5);

			AccessorItemRenderer itemRenderer = (AccessorItemRenderer)minecraft.getItemRenderer();
			int light = LightmapTextureManager.pack(15, 15);
			int overlay = OverlayTexture.DEFAULT_UV;
			VertexConsumerProvider.Immediate bufferSource = MinecraftClient.getInstance().getBufferBuilders().getEntityVertexConsumers();
			VertexConsumer vertexConsumer = ItemRenderer.getDirectItemGlintConsumer(bufferSource, TexturedRenderLayers.getEntityTranslucentCull(), true, false);
			itemRenderer.yttr$renderBakedItemModel(model, ItemStack.EMPTY, light, overlay, poseStack2, vertexConsumer);
			bufferSource.draw();

			if(!model.isSideLit()) {
				DiffuseLighting.enableGuiDepthLighting();
			}

			modelViewStack.pop();
			RenderSystem.disableBlend();
			modelViewStack.pop();
			RenderSystem.applyModelViewMatrix();
		}
	}

	@Override
	public List<Text> getTooltip(FakeModelIngredient ingredient, TooltipContext tooltipFlag) {
		List<Text> tooltip = new ArrayList<>();
		tooltip.add(Text.translatable(ingredient.getTranslationKey()));
		for(int i = 1; I18n.hasTranslation(ingredient.getTranslationKey() + ".tip." + i); ++i) {
			tooltip.add(Text.translatable(ingredient.getTranslationKey() + ".tip." + i));
		}
		return tooltip;
	}
}
