BEGIN;
DO $$
BEGIN

   INSERT INTO application_language_mapping (application_id, language_id)
VALUES  ((SELECT application_id FROM application WHERE application_name = 'Philips Learning Center (Totara)'),
        (SELECT language_id FROM language WHERE language_code = 'en-US')),
         ((SELECT application_id FROM application WHERE application_name = 'Performance Bridge'),
        (SELECT language_id FROM language WHERE language_code = 'en-US')),
         ((SELECT application_id FROM application WHERE application_name = 'InCenter'),
        (SELECT language_id FROM language WHERE language_code = 'en-US')),
         ((SELECT application_id FROM application WHERE application_name = 'Early Warning Scoring'),
        (SELECT language_id FROM language WHERE language_code = 'en-US'));


     RAISE NOTICE 'Data inserted successfully.';
EXCEPTION
    WHEN OTHERS THEN
        ROLLBACK;
        RAISE EXCEPTION 'An error occurred during the insertion. Transaction has been rolled back: %', SQLERRM;
END $$;
COMMIT;