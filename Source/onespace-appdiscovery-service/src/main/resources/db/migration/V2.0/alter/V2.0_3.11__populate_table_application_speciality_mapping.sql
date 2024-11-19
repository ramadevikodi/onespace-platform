BEGIN;
DO $$
BEGIN

UPDATE application_speciality_mapping
SET speciality_id = (SELECT speciality_id FROM speciality WHERE speciality_name = 'Not applicable')
WHERE application_id = (SELECT application_id FROM application WHERE application_name = 'Customer Service Portal');


   INSERT INTO application_speciality_mapping (application_id, speciality_id)
VALUES  ((SELECT application_id FROM application WHERE application_name = 'Philips Learning Center (Totara)'),
        (SELECT speciality_id FROM speciality WHERE speciality_name = 'Not applicable')),
        ((SELECT application_id FROM application WHERE application_name = 'Performance Bridge'),
        (SELECT speciality_id FROM speciality WHERE speciality_name = 'Not applicable')),
        ((SELECT application_id FROM application WHERE application_name = 'InCenter'),
        (SELECT speciality_id FROM speciality WHERE speciality_name = 'Not applicable')),
        ((SELECT application_id FROM application WHERE application_name = 'Early Warning Scoring'),
        (SELECT speciality_id FROM speciality WHERE speciality_name = 'Not applicable'));

     RAISE NOTICE 'Data inserted successfully.';
EXCEPTION
    WHEN OTHERS THEN
        ROLLBACK;
        RAISE EXCEPTION 'An error occurred during the insertion. Transaction has been rolled back: %', SQLERRM;
END $$;
COMMIT;