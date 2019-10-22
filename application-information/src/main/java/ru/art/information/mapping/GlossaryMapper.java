package ru.art.information.mapping;

import static ru.art.core.checker.CheckerForEmptiness.isNotEmpty;

import java.lang.String;
import ru.art.entity.Entity;
import ru.art.entity.mapper.ValueFromModelMapper;
import ru.art.entity.mapper.ValueToModelMapper;
import ru.art.information.model.Glossary;

public interface GlossaryMapper {
	String glossDiv = "glossDiv";

	String title = "title";

	ValueToModelMapper<Glossary, Entity> toGlossary = entity -> isNotEmpty(entity) ? Glossary.builder()
			.glossDiv(entity.getValue(glossDiv, GlossDivMapper.toGlossDiv))
			.title(entity.getString(title))
			.build() : Glossary.builder().build();

	ValueFromModelMapper<Glossary, Entity> fromGlossary = model -> isNotEmpty(model) ? Entity.entityBuilder()
			.entityField(glossDiv, model.getGlossDiv(), GlossDivMapper.fromGlossDiv)
			.stringField(title, model.getTitle())
			.build() : Entity.entityBuilder().build();
}
