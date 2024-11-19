BEGIN;
DO $$
BEGIN
    INSERT INTO application_language_mapping (application_id, language_id)
    VALUES ((SELECT application_id FROM application WHERE application_name = 'Customer Service Portal'),
            (SELECT language_id FROM language WHERE language_code = 'en-US')),
            ((SELECT application_id FROM application WHERE application_name = 'Cyber Security'),
            (SELECT language_id FROM language WHERE language_code = 'en-US')),
            ((SELECT application_id FROM application WHERE application_name = 'Utilization'),
            (SELECT language_id FROM language WHERE language_code = 'en-US')),
            ((SELECT application_id FROM application WHERE application_name = 'Inventory'),
            (SELECT language_id FROM language WHERE language_code = 'en-US')),
            ((SELECT application_id FROM application WHERE application_name = 'Assessment'),
            (SELECT language_id FROM language WHERE language_code = 'en-US')),
            ((SELECT application_id FROM application WHERE application_name = 'Service Performance'),
            (SELECT language_id FROM language WHERE language_code = 'en-US')),
            ((SELECT application_id FROM application WHERE application_name = 'User Management'),
            (SELECT language_id FROM language WHERE language_code = 'en-US')),
            ((SELECT application_id FROM application WHERE application_name = 'Dose Management'),
            (SELECT language_id FROM language WHERE language_code = 'en-US')),
            ((SELECT application_id FROM application WHERE application_name = 'Sentinel'),
            (SELECT language_id FROM language WHERE language_code = 'en-US')),
            ((SELECT application_id FROM application WHERE application_name = 'HSP IAM Self Service'),
            (SELECT language_id FROM language WHERE language_code = 'en-US'));

     RAISE NOTICE 'Data inserted successfully.';
EXCEPTION
    WHEN OTHERS THEN        
        ROLLBACK;        
        RAISE EXCEPTION 'An error occurred during the insertion. Transaction has been rolled back: %', SQLERRM;
END $$;
COMMIT;