package ru.art.generator.soap.factory;

import com.predic8.schema.Element;
import com.predic8.schema.TypeDefinition;
import com.predic8.schema.restriction.facet.EnumerationFacet;
import com.predic8.schema.restriction.facet.Facet;
import com.predic8.schema.restriction.facet.FractionDigits;
import com.predic8.schema.restriction.facet.LengthFacet;
import com.predic8.schema.restriction.facet.MaxExclusiveFacet;
import com.predic8.schema.restriction.facet.MaxInclusiveFacet;
import com.predic8.schema.restriction.facet.MaxLengthFacet;
import com.predic8.schema.restriction.facet.MinExclusiveFacet;
import com.predic8.schema.restriction.facet.MinInclusiveFacet;
import com.predic8.schema.restriction.facet.MinLengthFacet;
import com.predic8.schema.restriction.facet.PatternFacet;
import com.predic8.schema.restriction.facet.TotalDigitsFacet;
import com.predic8.schema.restriction.facet.WhiteSpaceFacet;
import groovy.xml.QName;
import java.util.Date;
import lombok.SneakyThrows;
import ru.art.generator.exception.NotFoundPrefixException;
import ru.art.generator.soap.model.Restriction;
import ru.art.generator.soap.model.Restriction.RestrictionBuilder;
import ru.art.generator.soap.model.RestrictionOperation;

public abstract class AbstractTypeFactory {

  public static Class<? extends Object> getTypeByString(String type) {
    switch (type) {
      case "string":
        return String.class;
      case "byte":
        return Byte.class;
      case "boolean":
        return Boolean.class;
      case "float":
        return Float.class;
      case "double":
        return Double.class;
      case "decimal":
        return Double.class;
      case "long":
        return Long.class;
      case "int":
        return Integer.class;
      case "integer":
        return Integer.class;
      case "dateTime":
        return Date.class;
      case "time":
        return Date.class;
      case "date":
        return Date.class;
      default:
        return Object.class;
    }
  }

  public static String getTypeByElement(Element element) {
    if (element.getType() == null) {
      if (element.getEmbeddedType() != null) {
        if (element.getEmbeddedType().getQname() != null) {
          return element.getEmbeddedType().getQname().getLocalPart();
        }
      } else if (element.getRef() != null) {
        return element.getRef().getLocalPart();
      }
    } else {
      return element.getType().getLocalPart();
    }
    return "Object";
  }

  @SneakyThrows
  public static TypeDefinition getTypeDefinition(Element element) {
    if (element.getType() == null) {
      if (element.getEmbeddedType() != null) {
        return element.getEmbeddedType();
      } else if (element.getRef() != null) {
        QName ref = element.getRef();
        if (ref.getNamespaceURI() != null) {
          Element refElement = element.getSchema().getElement(ref);
          element.setName(refElement.getName());
          return getTypeDefinition(refElement);
        }
        if (ref.getPrefix() == null || ref.getPrefix().isEmpty()) {
          throw new NotFoundPrefixException("Not fount prefix for ref about elememt "
              + element.getName());
        }
        String namespace = element.getSchema().getNamespace(ref.getPrefix()).toString();
        QName qName = new QName(namespace, ref.getLocalPart(), ref.getPrefix());
        element.setName(ref.getLocalPart());
        return element.getSchema().getType(qName);
      }
    }
    String localPart = getTypeByElement(element);
    return element.getSchema().getType(localPart);
  }

  public static String getNamespaceByPrefix(Element element, String prefix) {
    return element.getSchema().getNamespace(prefix).toString();
  }

  public static Restriction getRestrictionByFacet(Facet facet) {
    RestrictionBuilder builder = Restriction.builder();
    builder.value(facet.getValue());

    if (facet instanceof MinLengthFacet) {
      builder.operation(RestrictionOperation.MIN_LENGTH);
    } else if (facet instanceof FractionDigits) {
      builder.operation(RestrictionOperation.FRACTION_DIGITS);
    } else if (facet instanceof TotalDigitsFacet) {
      builder.operation(RestrictionOperation.TOTAL_DIGITS);
    } else if (facet instanceof EnumerationFacet) {
      builder.operation(RestrictionOperation.ENUMERATION);
    } else if (facet instanceof LengthFacet) {
      builder.operation(RestrictionOperation.LENGTH);
    } else if (facet instanceof MaxExclusiveFacet) {
      builder.operation(RestrictionOperation.MAX_EXCLUSIVE);
    } else if (facet instanceof PatternFacet) {
      builder.operation(RestrictionOperation.PATTERN);
    } else if (facet instanceof MaxLengthFacet) {
      builder.operation(RestrictionOperation.MAX_LENGTH);
    } else if (facet instanceof MinExclusiveFacet) {
      builder.operation(RestrictionOperation.MIN_EXCLUSIVE);
    } else if (facet instanceof MaxInclusiveFacet) {
      builder.operation(RestrictionOperation.MAX_INCLUSIVE);
    } else if (facet instanceof MinInclusiveFacet) {
      builder.operation(RestrictionOperation.MIN_INCLUSIVE);
    } else if (facet instanceof WhiteSpaceFacet) {
      builder.operation(RestrictionOperation.WHITESPACE);
    }
    return builder.build();
  }


  public static boolean isObject(Class classString) {
    return classString.getTypeName().equals(Object.class.getName());
  }
}
