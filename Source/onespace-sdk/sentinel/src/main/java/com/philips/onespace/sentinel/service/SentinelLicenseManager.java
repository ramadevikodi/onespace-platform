package com.philips.onespace.sentinel.service;

import com.philips.onespace.logging.LogExecutionTime;
import com.philips.onespace.sentinel.dto.OneSpaceProduct;
import com.philips.onespace.sentinel.model.Entitlement;
import com.philips.onespace.sentinel.model.ItemProduct;
import com.philips.onespace.sentinel.model.ItemProductFeature;
import com.philips.onespace.sentinel.model.ProductKey;
import com.philips.onespace.sentinel.model.Customers;
import com.philips.onespace.sentinel.model.Entitlements;
import com.philips.onespace.sentinel.model.CommonLicenseAttribute;
import com.philips.onespace.sentinel.model.NamedUser;
import com.philips.onespace.sentinel.model.Attribute;
import com.philips.onespace.sentinel.util.Constants;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.client5.http.impl.io.PoolingHttpClientConnectionManagerBuilder;
import org.apache.hc.client5.http.ssl.NoopHostnameVerifier;
import org.apache.hc.client5.http.ssl.SSLConnectionSocketFactoryBuilder;
import org.apache.hc.client5.http.ssl.TrustAllStrategy;
import org.apache.hc.core5.ssl.SSLContextBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.convert.ConversionService;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.StandardCharsets;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

// TODO:: Move to iTaap APIs, currently using native Sentinel APIs
@Component
public class SentinelLicenseManager implements LicenseManager {
    private final String sentinelBaseUrl;

    private final String username;

    private final String password;

    private RestTemplate restTemplate;

    @Autowired
    private ConversionService conversionService;

    public SentinelLicenseManager(@Value("${sentinel.baseurl}") String sentinelBaseUrl, @Value("${sentinel.username}") String username, @Value("${sentinel.password}") String password) throws NoSuchAlgorithmException, KeyStoreException, KeyManagementException {
        this.sentinelBaseUrl = sentinelBaseUrl;
        this.username = username;
        this.password = password;
        CloseableHttpClient httpClient = HttpClients.custom()
                .setConnectionManager(PoolingHttpClientConnectionManagerBuilder.create()
                        .setSSLSocketFactory(SSLConnectionSocketFactoryBuilder.create()
                                .setSslContext(SSLContextBuilder.create()
                                        .loadTrustMaterial(TrustAllStrategy.INSTANCE)
                                        .build())
                                .setHostnameVerifier(NoopHostnameVerifier.INSTANCE)
                                .build())
                        .build())
                .build();
        HttpComponentsClientHttpRequestFactory requestFactory =
                new HttpComponentsClientHttpRequestFactory();
        requestFactory.setHttpClient(httpClient);// this method is not accepting the CloseableHttpClient object
        requestFactory.setConnectTimeout(30000);
        requestFactory.setConnectionRequestTimeout(30000);
        this.restTemplate = new RestTemplate(requestFactory);
    }

    public List<OneSpaceProduct> getEntitledProductsForCustomer(String customerId) {
        List<Entitlement> entitlements = getEntitlements(customerId);
        if(entitlements != null) {
            return getProducts(entitlements);
        }
        return null;
    }

    public String getEntitlementIdForCustomerProduct(UUID productdId, UUID customerId) {
        List<Entitlement> entitlements = getEntitlements(String.valueOf(customerId));
        for(Entitlement entitlement : entitlements) {
            if (entitlement != null && entitlement.getProductKeys() != null && entitlement.getProductKeys().getProductKey() != null) {
                for (ProductKey productKey : entitlement.getProductKeys().getProductKey()) {
                    List<ItemProduct> productList = getProducts(productKey);
                    if (productList.stream().anyMatch(p -> p.getProduct().getExternalId().equals(productdId.toString()))) {
                        return entitlement.getId();
                    }
                }
            }
        }
        return null;
    }

    public String getFeatureLicenseLimitOfEntitledProduct(String entitlementId, String productId) {
        Entitlement entitlement = getEntitlement(entitlementId);
        if(entitlement != null && entitlement.getProductKeys() != null && entitlement.getProductKeys().getProductKey() != null) {
            for (ProductKey productKey : entitlement.getProductKeys().getProductKey()) {
                List<ItemProduct> productList = getProducts(productKey);
                Optional<ItemProduct> product = productList.stream().filter(p -> p.getProduct().getExternalId().equals(productId)).findFirst();
                if(product.isPresent()) {
                    Optional<ItemProductFeature> itemProductFeature = product.get().getItemProductFeatures().getItemProductFeature().stream().filter(f -> f.getItemFeatureLicenseModel() != null).findFirst();
                    Optional<Attribute> licenseLimit = itemProductFeature.get().getItemFeatureLicenseModel().getAttributes().getAttribute().stream().filter(a -> a.getName().equals("FLOATING_USER_LIMIT")).findFirst();
                    return licenseLimit.map(Attribute::getValue).orElse(null);
                }
            }
        }
        return null;
    }

    @LogExecutionTime
    private String searchCustomerByExternalID(String externalID) {
        final String encodedBasicAuthorization = "Basic " + HttpHeaders.encodeBasicAuth(username, password,
                StandardCharsets.UTF_8);

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", encodedBasicAuthorization);
        HttpEntity<?> request = new HttpEntity<>(headers);

        ResponseEntity<String> sentinelResponse = this.restTemplate.exchange(sentinelBaseUrl + Constants.SENTINEL_CUSTOMERS_BY_EXTERNAL_ID_URL + externalID,
                HttpMethod.GET, request, String.class);
        String sentinelCustomerIDJsonString =  sentinelResponse.getBody();
        Customers customers = conversionService.convert(sentinelCustomerIDJsonString, Customers.class);
        String sentinelCustomerID = null;
        if(customers!= null && customers.getCount() != 0) {
            sentinelCustomerID = customers.getCustomer().get(0).getId();
        }
        return sentinelCustomerID;
    }

    @LogExecutionTime
    private List<String> searchEntitlementsByCustomerID(String customerID) {
        final String encodedBasicAuthorization = "Basic " + HttpHeaders.encodeBasicAuth(username, password,
                StandardCharsets.UTF_8);

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", encodedBasicAuthorization);
        HttpEntity<?> request = new HttpEntity<>(headers);

        ResponseEntity<String> sentinelResponse = this.restTemplate.exchange(sentinelBaseUrl + Constants.SENTINEL_SEARCH_ENTITLEMENTS_BY_CUSTOMER_ID_URL + customerID + "&state=ENABLE",
                HttpMethod.GET, request, String.class);
        String entitlementsJsonString =   sentinelResponse.getBody();
        Entitlements entitlements = conversionService.convert(entitlementsJsonString, Entitlements.class);
        List<String> entitlementIDs = new ArrayList<>();
        if (entitlements != null && entitlements.getCount() != 0) {
            entitlementIDs.addAll(entitlements.getEntitlement().stream().map(Entitlement::getId).toList());
        }
        return entitlementIDs;
    }

    private List<Entitlement> getEntitlements(List<String> entitlementIDs) {
        return entitlementIDs.stream().map(this::getEntitlement).toList();
    }

    @LogExecutionTime
    private Entitlement getEntitlement(String entitlementID) {
        final String encodedBasicAuthorization = "Basic " + HttpHeaders.encodeBasicAuth(username, password,
                StandardCharsets.UTF_8);

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", encodedBasicAuthorization);
        HttpEntity<?> request = new HttpEntity<>(headers);
        ResponseEntity<String> entitlementResponse = this.restTemplate.exchange(sentinelBaseUrl + Constants.SENTINEL_ENTITLEMENTS_URL + entitlementID,
                HttpMethod.GET, request, String.class);
        String entitlementJsonString = entitlementResponse.getBody();
        return conversionService.convert(entitlementJsonString, Entitlement.class);
    }

    private List<OneSpaceProduct> getProducts(List<Entitlement> entitlements) {
        List<OneSpaceProduct> oneSpaceProducts = new ArrayList<>();
        for (Entitlement entitlement : entitlements) {
            if(entitlement != null && entitlement.getProductKeys() != null && entitlement.getProductKeys().getProductKey() != null) {
                for (ProductKey productKey : entitlement.getProductKeys().getProductKey()) {
                    boolean isNamedProductKey = isNamedProductKey(productKey);
                    List<String> namedUsers = null;
                    if (isNamedProductKey) {
                        namedUsers = getNamedUsers(productKey);
                    }
                    List<ItemProduct> productList = getProducts(productKey);
                    oneSpaceProducts.addAll(convertToProductList(productList, isNamedProductKey, namedUsers));
                }
            }
        }
        return oneSpaceProducts;
    }

    private List<Entitlement> getEntitlements(String customerId) {
        String sentinelCustomerID = searchCustomerByExternalID(customerId);
        if(sentinelCustomerID != null) {
            List<String> entitlementIDs = searchEntitlementsByCustomerID(sentinelCustomerID);
            return getEntitlements(entitlementIDs);
        }
        return null;
    }

    private static List<OneSpaceProduct> convertToProductList(List<ItemProduct> entitledItemProducts, boolean isNamedProductKey, List<String> namedUsers) {
        List<OneSpaceProduct> oneSpaceProducts = new ArrayList<>();
        entitledItemProducts.stream().map(ItemProduct::getProduct).forEach(product -> {
            OneSpaceProduct oneSpaceProductObj = OneSpaceProduct.builder()
                    .isNamedProduct(isNamedProductKey)
                    .namedUsers(namedUsers)
                    .externalId(UUID.fromString(product.getExternalId()))
                    .build();
            oneSpaceProducts.add(oneSpaceProductObj);
        });
        return oneSpaceProducts;
    }

    private static List<ItemProduct> getProducts(ProductKey productKey) {
        List<ItemProduct> entitledItemProducts = new ArrayList<>();
        if (productKey.getItem().getItemSuite() != null) {
            entitledItemProducts.addAll(productKey.getItem().getItemSuite().getItemProducts().getItemProduct());
        } else {
            entitledItemProducts.add(productKey.getItem().getItemProduct());
        }
        return entitledItemProducts;
    }

    private static List<String> getNamedUsers(ProductKey productKey) {
        if(productKey.getNamedUsers() != null) {
            return productKey.getNamedUsers().getNamedUser().stream().map(NamedUser::getName).toList();
        }
        return null;
    }

    private static boolean isNamedProductKey(ProductKey productKey) {
        Optional<CommonLicenseAttribute> namedLicenseAttr = productKey.getCommonLicenseAttributes().getCommonLicenseAttribute().stream().filter(c -> c.getName().equals("NAMED_USER_LICENSE")).findFirst();
        return namedLicenseAttr.filter(commonLicenseAttribute -> Boolean.parseBoolean(commonLicenseAttribute.getValue())).isPresent();
    }

}
