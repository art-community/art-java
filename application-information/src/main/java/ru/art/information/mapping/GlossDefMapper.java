package ru.art.information.mapping;

import static ru.art.core.checker.CheckerForEmptiness.isNotEmpty;

import java.lang.String;
import ru.art.entity.Entity;
import ru.art.entity.mapper.ValueFromModelMapper;
import ru.art.entity.mapper.ValueToModelMapper;
import ru.art.information.model.GlossDef;

public interface GlossDefMapper {
	String glossSeeAlso = "glossSeeAlso";

	String para = "para";

	ValueToModelMapper<GlossDef, Entity> toGlossDef = entity -> isNotEmpty(entity) ? GlossDef.builder()
			.glossSeeAlso(entity.getStringList(glossSeeAlso))
			.para(entity.getString(para))
			.build() : GlossDef.builder().build();

	ValueFromModelMapper<GlossDef, Entity> fromGlossDef = model -> isNotEmpty(model) ? Entity.entityBuilder()
			.stringCollectionField(glossSeeAlso, model.getGlossSeeAlso())
			.stringField(para, model.getPara())
			.build() : Entity.entityBuilder().build();
}
