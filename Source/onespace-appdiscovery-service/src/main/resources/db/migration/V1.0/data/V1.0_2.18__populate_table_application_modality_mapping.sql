BEGIN;
DO $$
BEGIN
     INSERT INTO application_modality_mapping (application_id, modality_id)
     VALUES ((SELECT application_id FROM application WHERE application_name = 'Customer Service Portal'),
             (SELECT modality_id FROM modality WHERE modality_name = 'Not applicable')),
             ((SELECT application_id FROM application WHERE application_name = 'Cyber Security'),
             (SELECT modality_id FROM modality WHERE modality_name = 'Not applicable')),
             ((SELECT application_id FROM application WHERE application_name = 'Utilization'),
             (SELECT modality_id FROM modality WHERE modality_name = 'Not applicable')),
             ((SELECT application_id FROM application WHERE application_name = 'Inventory'),
             (SELECT modality_id FROM modality WHERE modality_name = 'Not applicable')),
             ((SELECT application_id FROM application WHERE application_name = 'Assessment'),
             (SELECT modality_id FROM modality WHERE modality_name = 'Not applicable')),
             ((SELECT application_id FROM application WHERE application_name = 'Service Performance'),
             (SELECT modality_id FROM modality WHERE modality_name = 'Not applicable')),
             ((SELECT application_id FROM application WHERE application_name = 'User Management'),
             (SELECT modality_id FROM modality WHERE modality_name = 'Not applicable')),
             ((SELECT application_id FROM application WHERE application_name = 'Dose Management'),
             (SELECT modality_id FROM modality WHERE modality_name = 'Not applicable')),
             ((SELECT application_id FROM application WHERE application_name = 'Sentinel'),
             (SELECT modality_id FROM modality WHERE modality_name = 'Not applicable')),
             ((SELECT application_id FROM application WHERE application_name = 'HSP IAM Self Service'),
             (SELECT modality_id FROM modality WHERE modality_name = 'Not applicable'));

     RAISE NOTICE 'Data inserted successfully.';
EXCEPTION
    WHEN OTHERS THEN        
        ROLLBACK;        
        RAISE EXCEPTION 'An error occurred during the insertion. Transaction has been rolled back: %', SQLERRM;
END $$;
COMMIT;