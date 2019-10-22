package ru.art.information.mapping;

import static ru.art.core.checker.CheckerForEmptiness.isNotEmpty;

import java.lang.String;
import ru.art.entity.Entity;
import ru.art.entity.mapper.ValueFromModelMapper;
import ru.art.entity.mapper.ValueToModelMapper;
import ru.art.information.model.TestModel;

public interface TestModelMapper {
	String glossary = "glossary";

	ValueToModelMapper<TestModel, Entity> toTestModel = entity -> isNotEmpty(entity) ? TestModel.builder()
			.glossary(entity.getValue(glossary, GlossaryMapper.toGlossary))
			.build() : TestModel.builder().build();

	ValueFromModelMapper<TestModel, Entity> fromTestModel = model -> isNotEmpty(model) ? Entity.entityBuilder()
			.entityField(glossary, model.getGlossary(), GlossaryMapper.fromGlossary)
			.build() : Entity.entityBuilder().build();
}
