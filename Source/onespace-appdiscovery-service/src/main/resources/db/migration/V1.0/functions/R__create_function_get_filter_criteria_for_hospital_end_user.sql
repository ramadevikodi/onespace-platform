CREATE OR REPLACE FUNCTION get_filter_criteria_for_hospital_end_user()
RETURNS TABLE (
    key TEXT,
    value TEXT[]
) AS $$
BEGIN
    RETURN QUERY
    SELECT DISTINCT
        'speciality' AS key,
        ARRAY_AGG(DISTINCT speciality_name::TEXT) AS value
    FROM speciality s, application_speciality_mapping asm
	where asm.speciality_id = s.speciality_id
    GROUP BY key
    UNION ALL
    SELECT DISTINCT
        'modality' AS key,
        ARRAY_AGG(DISTINCT modality_name::TEXT) AS value
    FROM modality m, application_modality_mapping amm
	where amm.modality_id = m.modality_id
    GROUP BY key
	UNION ALL
	SELECT DISTINCT
        'category' AS key,
        ARRAY_AGG(DISTINCT category_name::TEXT) AS value
    FROM category
    GROUP BY key
    UNION ALL
    SELECT DISTINCT
        'language'AS key,
        ARRAY_AGG(DISTINCT description::TEXT) AS value
    FROM language l, application_language_mapping alm
	where alm.language_id = l.language_id
    GROUP BY key;
END;
$$ LANGUAGE plpgsql;
