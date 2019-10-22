package ru.art.information.mapping;

import static ru.art.core.checker.CheckerForEmptiness.isNotEmpty;

import java.lang.String;
import ru.art.entity.Entity;
import ru.art.entity.mapper.ValueFromModelMapper;
import ru.art.entity.mapper.ValueToModelMapper;
import ru.art.information.model.GlossEntry;

public interface GlossEntryMapper {
	String abbrev = "abbrev";

	String acronym = "acronym";

	String glossDef = "glossDef";

	String glossSee = "glossSee";

	String glossTerm = "glossTerm";

	String iD = "iD";

	String sortAs = "sortAs";

	ValueToModelMapper<GlossEntry, Entity> toGlossEntry = entity -> isNotEmpty(entity) ? GlossEntry.builder()
			.abbrev(entity.getString(abbrev))
			.acronym(entity.getString(acronym))
			.glossDef(entity.getValue(glossDef, GlossDefMapper.toGlossDef))
			.glossSee(entity.getString(glossSee))
			.glossTerm(entity.getString(glossTerm))
			.iD(entity.getString(iD))
			.sortAs(entity.getString(sortAs))
			.build() : GlossEntry.builder().build();

	ValueFromModelMapper<GlossEntry, Entity> fromGlossEntry = model -> isNotEmpty(model) ? Entity.entityBuilder()
			.stringField(abbrev, model.getAbbrev())
			.stringField(acronym, model.getAcronym())
			.entityField(glossDef, model.getGlossDef(), GlossDefMapper.fromGlossDef)
			.stringField(glossSee, model.getGlossSee())
			.stringField(glossTerm, model.getGlossTerm())
			.stringField(iD, model.getID())
			.stringField(sortAs, model.getSortAs())
			.build() : Entity.entityBuilder().build();
}
