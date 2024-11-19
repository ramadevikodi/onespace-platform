CREATE OR REPLACE FUNCTION get_filter_criteria_for_platform_admin()
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
    FROM market
    GROUP BY key
    UNION ALL
    SELECT DISTINCT
        'region' AS key,
        ARRAY_AGG(DISTINCT region::TEXT) AS value
    FROM market
    GROUP BY key
    UNION ALL
    SELECT DISTINCT
        'country' AS key,
        ARRAY_AGG(DISTINCT country::TEXT) AS value
    FROM market
    GROUP BY key
    UNION ALL
    SELECT DISTINCT
        'sub_region'AS key,
        ARRAY_AGG(DISTINCT sub_region::TEXT) AS value
    FROM market
    GROUP BY key
    UNION ALL
    SELECT DISTINCT
        'language'AS key,
        ARRAY_AGG(DISTINCT description::TEXT) AS value
    FROM language
    GROUP BY key
	UNION ALL
	SELECT DISTINCT
        'sso_enabled' AS key,
        ARRAY_AGG(DISTINCT is_sso_enabled::TEXT) AS value
    FROM application
    GROUP BY key
	UNION ALL
	 SELECT DISTINCT
        'speciality' AS key,
        ARRAY_AGG(DISTINCT speciality_name::TEXT) AS value
    FROM speciality
    GROUP BY key
    UNION ALL
    SELECT DISTINCT
        'modality' AS key,
        ARRAY_AGG(DISTINCT modality_name::TEXT) AS value
    FROM modality
    GROUP BY key
	UNION ALL
	SELECT DISTINCT
        'category' AS key,
        ARRAY_AGG(DISTINCT category_name::TEXT) AS value
    FROM category
    GROUP BY key
	UNION ALL
	SELECT DISTINCT
        'statusName' AS key,
        ARRAY_AGG(DISTINCT status_name::TEXT) AS value
    FROM application_status
    GROUP BY key;
END;
$$ LANGUAGE plpgsql;
