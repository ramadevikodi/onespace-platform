BEGIN;
DO $$
BEGIN

UPDATE application_modality_mapping
SET modality_id = (SELECT modality_id FROM modality WHERE modality_name = 'Not applicable')
WHERE application_id = (SELECT application_id FROM application WHERE application_name = 'Customer Service Portal');


INSERT INTO application_modality_mapping (application_id, modality_id)
VALUES  ((SELECT application_id FROM application WHERE application_name = 'Philips Learning Center (Totara)'),
        (SELECT modality_id FROM modality WHERE modality_name = 'Not applicable')),
        ((SELECT application_id FROM application WHERE application_name = 'Performance Bridge'),
        (SELECT modality_id FROM modality WHERE modality_name = 'Not applicable')),
        ((SELECT application_id FROM application WHERE application_name = 'InCenter'),
        (SELECT modality_id FROM modality WHERE modality_name = 'Not applicable')),
        ((SELECT application_id FROM application WHERE application_name = 'Early Warning Scoring'),
        (SELECT modality_id FROM modality WHERE modality_name = 'Not applicable'));

     RAISE NOTICE 'Data inserted successfully.';
EXCEPTION
    WHEN OTHERS THEN
        ROLLBACK;
        RAISE EXCEPTION 'An error occurred during the insertion. Transaction has been rolled back: %', SQLERRM;
END $$;
COMMIT;