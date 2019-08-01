package ru.adk.soap.server.model;

import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;
import lombok.Singular;
import ru.adk.entity.mapper.ValueFromModelMapper.XmlEntityFromModelMapper;
import ru.adk.entity.mapper.ValueToModelMapper.XmlEntityToModelMapper;
import ru.adk.soap.content.mapper.SoapMimeToContentTypeMapper;
import static ru.adk.soap.content.mapper.SoapMimeToContentTypeMapper.textXml;
import java.util.Map;

@Getter
@Builder
public class SoapService {
    @Singular
    private Map<@NonNull String, @NonNull SoapOperation> soapOperations;
    private String serviceUrl;
    private String listeningPath;
    private String wsdlResourcePath;
    private Object defaultFaultResponse;
    private XmlEntityFromModelMapper<?> defaultFaultMapper;
    @Builder.Default
    private final SoapMimeToContentTypeMapper consumes = textXml();
    @Builder.Default
    private final SoapMimeToContentTypeMapper produces = textXml();

    @Getter
    @Builder
    public static class SoapOperation {
        private XmlEntityToModelMapper<?> requestMapper;
        private XmlEntityFromModelMapper<?> responseMapper;
        @Singular("faultMapper")
        private Map<Class<? extends Exception>, XmlEntityFromModelMapper<?>> faultMapping;
        private String methodId;
    }

}
