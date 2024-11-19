package com.philips.onespace.sentinel.converter;

import com.google.common.collect.ImmutableSet;
import com.google.gson.Gson;
import com.philips.onespace.sentinel.model.Customers;
import com.philips.onespace.sentinel.model.Entitlement;
import com.philips.onespace.sentinel.model.Entitlements;
import org.json.JSONObject;
import org.springframework.core.convert.TypeDescriptor;
import org.springframework.core.convert.converter.GenericConverter;

import java.util.Set;

public class SentinelJsonStrToObjectConverter implements GenericConverter {

    @Override
    public Set<ConvertiblePair> getConvertibleTypes() {
        ConvertiblePair[] pairs = new ConvertiblePair[] {
                new ConvertiblePair(String.class, Entitlement.class),
                new ConvertiblePair(String.class, Entitlements.class),
                new ConvertiblePair(String.class, Customers.class)};
        return ImmutableSet.copyOf(pairs);
    }

    @Override
    public Object convert(Object source, TypeDescriptor sourceType, TypeDescriptor targetType) {
        JSONObject obj = new JSONObject((String)source);
        Gson gson = new Gson();
        Class<?> type = targetType.getType();
        if (type.equals(Entitlement.class)) {
            return gson.fromJson(String.valueOf(obj.getJSONObject("entitlement")), Entitlement.class);
        }
        else if (type.equals(Entitlements.class)) {
            return gson.fromJson(String.valueOf(obj.getJSONObject("entitlements")), Entitlements.class);
        }
        else if (type.equals(Customers.class)) {
            return gson.fromJson(String.valueOf(obj.getJSONObject("customers")), Customers.class);
        } else
            return null;
    }
}