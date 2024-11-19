/*
 * (C) Koninklijke Philips Electronics N.V. 2024
 *
 * All rights are reserved. Reproduction or transmission in whole or in part, in any form or by any
 * means, electronic, mechanical or otherwise, is prohibited without the prior written consent of
 * the copyright owner.
 *
 * File name: LicensingService.java
 */

package com.philips.onespace.appdiscoveryframework.service.interfaces;


import java.util.UUID;

import com.philips.onespace.exception.EntitlementNotFoundException;
import com.philips.onespace.exception.InvalidTokenException;
import com.philips.onespace.exception.LicenseConsumptionException;
import com.philips.onespace.exception.ResourceNotFoundException;

public interface LicensingService {
    void consumeLicense(String authorization, UUID appId) throws InvalidTokenException, ResourceNotFoundException, EntitlementNotFoundException, LicenseConsumptionException;
    void releaseLicense(String authorization, UUID appId) throws InvalidTokenException, ResourceNotFoundException, LicenseConsumptionException, EntitlementNotFoundException;
}
