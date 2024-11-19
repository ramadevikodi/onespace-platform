package com.philips.onespace.sentinel.service;

import com.philips.onespace.sentinel.dto.OneSpaceProduct;

import java.util.List;
import java.util.UUID;

public interface LicenseManager {
    List<OneSpaceProduct> getEntitledProductsForCustomer(String customerId);
    String getEntitlementIdForCustomerProduct(UUID productdId, UUID customerId);
    String getFeatureLicenseLimitOfEntitledProduct(String entitlementId, String productId);
}
