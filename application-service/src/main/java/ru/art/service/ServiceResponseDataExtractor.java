package ru.art.service;

import lombok.NoArgsConstructor;
import ru.art.service.model.ServiceResponse;
import static java.util.Objects.nonNull;
import static lombok.AccessLevel.PRIVATE;

@NoArgsConstructor(access = PRIVATE)
class ServiceResponseDataExtractor {
    static <T> T extractResponseDataChecked(ServiceResponse<T> response) {
        if (nonNull(response.getServiceException())) {
            return null;
        }
        return response.getResponseData();
    }
}