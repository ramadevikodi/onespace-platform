CREATE OR REPLACE FUNCTION get_filter_criteria_for_app_integrator()
RETURNS TABLE (
    key TEXT,
    value TEXT[]
) AS $$
BEGIN
    RETURN QUERY
    SELECT DISTINCT
        'deployment' AS key,
        ARRAY_AGG(DISTINCT deployment_mode::TEXT) AS value
    FROM deployment
    GROUP BY key
    UNION ALL
    SELECT DISTINCT
        'market' AS key,
        ARRAY_AGG(DISTINCT market_name::TEXT) AS value
    FROM market m, application_market_mapping amm
	where amm.market_id = m.market_id
    GROUP BY key
    UNION ALL
    SELECT DISTINCT
        'region' AS key,
        ARRAY_AGG(DISTINCT region::TEXT) AS value
    FROM market m, application_market_mapping amm
	where amm.market_id = m.market_id
    GROUP BY key
    UNION ALL
    SELECT DISTINCT
        'country' AS key,
        ARRAY_AGG(DISTINCT country::TEXT) AS value
    FROM market m, application_market_mapping amm
	where amm.market_id = m.market_id
    GROUP BY key
    UNION ALL
    SELECT DISTINCT
        'sub_region'AS key,
        ARRAY_AGG(DISTINCT sub_region::TEXT) AS value
    FROM market m, application_market_mapping amm
	where amm.market_id = m.market_id
    GROUP BY key
    UNION ALL
    SELECT DISTINCT
        'language'AS key,
        ARRAY_AGG(DISTINCT description::TEXT) AS value
    FROM language l, application_language_mapping alm
	where alm.language_id = l.language_id
    GROUP BY key
	UNION ALL
	SELECT DISTINCT
        'sso_enabled' AS key,
        ARRAY_AGG(DISTINCT is_sso_enabled::TEXT) AS value
    FROM application
    GROUP BY key;
END;
$$ LANGUAGE plpgsql;
