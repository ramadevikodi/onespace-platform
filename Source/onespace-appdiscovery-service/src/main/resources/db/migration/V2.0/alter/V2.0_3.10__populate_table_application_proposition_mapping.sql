BEGIN;
DO $$
BEGIN

UPDATE application_proposition_mapping
SET proposition_id = (SELECT proposition_id FROM proposition WHERE proposition_name = 'Not applicable')
WHERE application_id = (SELECT application_id FROM application WHERE application_name = 'Customer Service Portal');

UPDATE application_proposition_mapping
SET proposition_id = (SELECT proposition_id FROM proposition WHERE proposition_name = 'Not applicable')
WHERE application_id = (SELECT application_id FROM application WHERE application_name = 'Sentinel');

UPDATE application_proposition_mapping
SET proposition_id = (SELECT proposition_id FROM proposition WHERE proposition_name = 'Not applicable')
WHERE application_id = (SELECT application_id FROM application WHERE application_name = 'HSP IAM Self Service');

INSERT INTO application_proposition_mapping (application_id, proposition_id)
VALUES ((SELECT application_id FROM application WHERE application_name = 'Philips Learning Center (Totara)'),
        (SELECT proposition_id FROM proposition WHERE proposition_name = 'Not applicable')),
        ((SELECT application_id FROM application WHERE application_name = 'Performance Bridge'),
        (SELECT proposition_id FROM proposition WHERE proposition_name = 'Not applicable')),
        ((SELECT application_id FROM application WHERE application_name = 'InCenter'),
        (SELECT proposition_id FROM proposition WHERE proposition_name = 'Not applicable')),
        ((SELECT application_id FROM application WHERE application_name = 'Early Warning Scoring'),
        (SELECT proposition_id FROM proposition WHERE proposition_name = 'Not applicable'));

     RAISE NOTICE 'Data inserted successfully.';
EXCEPTION
    WHEN OTHERS THEN
        ROLLBACK;
        RAISE EXCEPTION 'An error occurred during the insertion. Transaction has been rolled back: %', SQLERRM;
END $$;
COMMIT;