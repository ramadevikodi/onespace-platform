CREATE
OR REPLACE FUNCTION get_application_translations(
	record_id UUID,
    language_code VARCHAR(255)
)
RETURNS TABLE(
	column_name VARCHAR(255),
    description TEXT
) AS $$
BEGIN
RETURN QUERY EXECUTE format(
        'SELECT t.column_name, COALESCE(t.translated_text, a.short_description) AS description
         FROM application a
         LEFT JOIN translations t ON t.record_id = %L
                                  AND LOWER(t.language_code) = LOWER(%L)
		where a.application_id = record_id',
        record_id, language_code
    );
END;
$$
LANGUAGE plpgsql;