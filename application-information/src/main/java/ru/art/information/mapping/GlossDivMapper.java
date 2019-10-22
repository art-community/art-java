package ru.art.information.mapping;

import static ru.art.core.checker.CheckerForEmptiness.isNotEmpty;

import java.lang.String;
import ru.art.entity.Entity;
import ru.art.entity.mapper.ValueFromModelMapper;
import ru.art.entity.mapper.ValueToModelMapper;
import ru.art.information.model.GlossDiv;

public interface GlossDivMapper {
	String glossList = "glossList";

	String title = "title";

	ValueToModelMapper<GlossDiv, Entity> toGlossDiv = entity -> isNotEmpty(entity) ? GlossDiv.builder()
			.glossList(entity.getValue(glossList, GlossListMapper.toGlossList))
			.title(entity.getString(title))
			.build() : GlossDiv.builder().build();

	ValueFromModelMapper<GlossDiv, Entity> fromGlossDiv = model -> isNotEmpty(model) ? Entity.entityBuilder()
			.entityField(glossList, model.getGlossList(), GlossListMapper.fromGlossList)
			.stringField(title, model.getTitle())
			.build() : Entity.entityBuilder().build();
}
