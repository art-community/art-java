package ru.art.information.mapping;

import static ru.art.core.checker.CheckerForEmptiness.isNotEmpty;

import java.lang.String;
import ru.art.entity.Entity;
import ru.art.entity.mapper.ValueFromModelMapper;
import ru.art.entity.mapper.ValueToModelMapper;
import ru.art.information.model.GlossList;

public interface GlossListMapper {
	String glossEntry = "glossEntry";

	ValueToModelMapper<GlossList, Entity> toGlossList = entity -> isNotEmpty(entity) ? GlossList.builder()
			.glossEntry(entity.getValue(glossEntry, GlossEntryMapper.toGlossEntry))
			.build() : GlossList.builder().build();

	ValueFromModelMapper<GlossList, Entity> fromGlossList = model -> isNotEmpty(model) ? Entity.entityBuilder()
			.entityField(glossEntry, model.getGlossEntry(), GlossEntryMapper.fromGlossEntry)
			.build() : Entity.entityBuilder().build();
}
