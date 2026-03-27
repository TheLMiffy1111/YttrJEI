package thelm.yttrjei.gui.render;

import java.util.ArrayList;
import java.util.List;

import org.joml.Matrix4f;

import com.mojang.blaze3d.systems.RenderSystem;

import diy.y2k.yttr.mixin.client.AccessorItemRenderer;
import mezz.jei.api.ingredients.IIngredientRenderer;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.client.render.DiffuseLighting;
import net.minecraft.client.render.LightmapTextureManager;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.TexturedRenderLayers;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.client.render.model.json.ModelTransformationMode;
import net.minecraft.client.resource.language.I18n;
import net.minecraft.client.util.ModelIdentifier;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import thelm.yttrjei.ingredient.FakeModelIngredient;

public class FakeModelIngredientRenderer implements IIngredientRenderer<FakeModelIngredient> {

	public static final FakeModelIngredientRenderer INSTANCE = new FakeModelIngredientRenderer();

	@Override
	public void render(DrawContext guiGraphics, FakeModelIngredient ingredient) {
		if(ingredient != null) {
			MinecraftClient minecraft = MinecraftClient.getInstance();
			BakedModel model = minecraft.getBakedModelManager().getModel(new ModelIdentifier(ingredient.id(), "inventory"));
			MatrixStack poseStack = guiGraphics.getMatrices();

			RenderSystem.enableDepthTest();

			poseStack.push();
			poseStack.translate(8, 8, 50);
			poseStack.multiplyPositionMatrix(new Matrix4f().scaling(1, -1, 1));
			poseStack.scale(16, 16, 16);

			if(model.isSideLit()) {
				DiffuseLighting.enableGuiDepthLighting();
			}
			else {
				DiffuseLighting.disableGuiDepthLighting();
			}

			poseStack.push();
			model.getTransformation().getTransformation(ModelTransformationMode.GUI).apply(false, poseStack);
			poseStack.translate(-0.5, -0.5, -0.5);

			AccessorItemRenderer itemRenderer = (AccessorItemRenderer)minecraft.getItemRenderer();
			int light = LightmapTextureManager.pack(15, 15);
			int overlay = OverlayTexture.DEFAULT_UV;
			VertexConsumer vertexConsumer = ItemRenderer.getDirectItemGlintConsumer(guiGraphics.getVertexConsumers(), TexturedRenderLayers.getEntityTranslucentCull(), true, false);
			itemRenderer.yttr$renderBakedItemModel(model, ItemStack.EMPTY, light, overlay, poseStack, vertexConsumer);
			guiGraphics.draw();

			poseStack.pop();

			DiffuseLighting.enableGuiDepthLighting();

			poseStack.pop();

			RenderSystem.disableBlend();
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
