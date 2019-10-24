/*
 * ART Java
 *
 * Copyright 2019 ART
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package ru.art.information.mapping;

import static ru.art.core.checker.CheckerForEmptiness.isNotEmpty;

import java.lang.String;
import ru.art.entity.Entity;
import ru.art.entity.mapper.ValueFromModelMapper;
import ru.art.entity.mapper.ValueToModelMapper;
import ru.art.information.model.HttpServiceMethodInformation;

public interface HttpServiceMethodInformationMapper {
	String method = "method";

	String id = "id";

	String url = "url";

	String exampleRequest = "exampleRequest";

	String exampleResponse = "exampleResponse";

	ValueToModelMapper<HttpServiceMethodInformation, Entity> toHttpServiceMethodInformation = entity -> isNotEmpty(entity) ? HttpServiceMethodInformation.builder()
			.method(entity.getString(method))
			.id(entity.getString(id))
			.url(entity.getString(url))
			.exampleRequest(entity.getString(exampleRequest))
			.exampleResponse(entity.getString(exampleResponse))
			.build() : HttpServiceMethodInformation.builder().build();

	ValueFromModelMapper<HttpServiceMethodInformation, Entity> fromHttpServiceMethodInformation = model -> isNotEmpty(model) ? Entity.entityBuilder()
			.stringField(method, model.getMethod())
			.stringField(id, model.getId())
			.stringField(url, model.getUrl())
			.stringField(exampleRequest, model.getExampleRequest())
			.stringField(exampleResponse, model.getExampleResponse())
			.build() : Entity.entityBuilder().build();
}
