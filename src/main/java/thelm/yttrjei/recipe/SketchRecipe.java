package thelm.yttrjei.recipe;

import java.util.List;
import java.util.Optional;

import net.minecraft.util.Identifier;
import thelm.yttrjei.metadata.SketchRecipeMetadataSection;

public record SketchRecipe(Identifier id, List<List<Object>> inputs, List<List<Object>> catalysts, List<List<Object>> outputs) {

	public SketchRecipe(Identifier id, Optional<SketchRecipeMetadataSection> metadata) {
		this(id, metadata.map(m -> m.inputs()).orElse(List.of()), metadata.map(m -> m.catalysts()).orElse(List.of()), metadata.map(m -> m.outputs()).orElse(List.of()));
	}
}
