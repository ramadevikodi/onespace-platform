BEGIN;
DO $$
BEGIN
       INSERT INTO application_speciality_mapping (application_id, speciality_id)
              VALUES ((SELECT application_id FROM application WHERE application_name = 'Customer Service Portal'),
                      (SELECT speciality_id FROM speciality WHERE speciality_name = 'Cardiology')),
                      ((SELECT application_id FROM application WHERE application_name = 'Cyber Security'),
                      (SELECT speciality_id FROM speciality WHERE speciality_name = 'Not applicable')),
                      ((SELECT application_id FROM application WHERE application_name = 'Utilization'),
                      (SELECT speciality_id FROM speciality WHERE speciality_name = 'Not applicable')),
                      ((SELECT application_id FROM application WHERE application_name = 'Inventory'),
                      (SELECT speciality_id FROM speciality WHERE speciality_name = 'Not applicable')),
                      ((SELECT application_id FROM application WHERE application_name = 'Assessment'),
                      (SELECT speciality_id FROM speciality WHERE speciality_name = 'Not applicable')),
                      ((SELECT application_id FROM application WHERE application_name = 'Service Performance'),
                      (SELECT speciality_id FROM speciality WHERE speciality_name = 'Not applicable')),
                      ((SELECT application_id FROM application WHERE application_name = 'User Management'),
                      (SELECT speciality_id FROM speciality WHERE speciality_name = 'Not applicable')),
                      ((SELECT application_id FROM application WHERE application_name = 'Dose Management'),
                      (SELECT speciality_id FROM speciality WHERE speciality_name = 'Not applicable')),
                      ((SELECT application_id FROM application WHERE application_name = 'Sentinel'),
                      (SELECT speciality_id FROM speciality WHERE speciality_name = 'Not applicable')),
                      ((SELECT application_id FROM application WHERE application_name = 'HSP IAM Self Service'),
                      (SELECT speciality_id FROM speciality WHERE speciality_name = 'Not applicable'));
     RAISE NOTICE 'Data inserted successfully.';
EXCEPTION
    WHEN OTHERS THEN
        ROLLBACK;       
        RAISE EXCEPTION 'An error occurred during the insertion. Transaction has been rolled back: %', SQLERRM;
END $$;
COMMIT;