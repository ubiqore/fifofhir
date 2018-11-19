package ca.uhn.fhir.rest.server;

import ca.uhn.fhir.context.FhirVersionEnum;
import org.hl7.fhir.instance.model.api.IBaseMetaType;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;

import java.util.List;

public class FakeResource implements IBaseResource {
    @Override
    public IBaseMetaType getMeta() {
        return null;
    }

    @Override
    public IIdType getIdElement() {
        return null;
    }

    @Override
    public IBaseResource setId(String s) {
        return null;
    }

    @Override
    public IBaseResource setId(IIdType iIdType) {
        return null;
    }

    @Override
    public FhirVersionEnum getStructureFhirVersionEnum() {
        return null;
    }

    @Override
    public boolean isEmpty() {
        return false;
    }

    @Override
    public boolean hasFormatComment() {
        return false;
    }

    @Override
    public List<String> getFormatCommentsPre() {
        return null;
    }

    @Override
    public List<String> getFormatCommentsPost() {
        return null;
    }
}
