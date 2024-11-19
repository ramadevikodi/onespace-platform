BEGIN;
DO $$
BEGIN
       INSERT INTO application_proposition_mapping (application_id, proposition_id)
       VALUES ((SELECT application_id FROM application WHERE application_name = 'Customer Service Portal'),
               (SELECT proposition_id FROM proposition WHERE proposition_name = 'Customer Support')),
              ((SELECT application_id FROM application WHERE application_name = 'Utilization'),
               (SELECT proposition_id FROM proposition WHERE proposition_name = 'L0 Service Management pack')),
              ((SELECT application_id FROM application WHERE application_name = 'Service Performance'),
               (SELECT proposition_id FROM proposition WHERE proposition_name = 'L0 Service Management pack')),
              ((SELECT application_id FROM application WHERE application_name = 'Inventory'),
               (SELECT proposition_id FROM proposition WHERE proposition_name = 'L1 Asset Management pack')),
              ((SELECT application_id FROM application WHERE application_name = 'Cyber Security'),
               (SELECT proposition_id FROM proposition WHERE proposition_name = 'L1 Asset Management pack')),
              ((SELECT application_id FROM application WHERE application_name = 'Assessment'),
               (SELECT proposition_id FROM proposition WHERE proposition_name = 'L1 Asset Management pack')),
              ((SELECT application_id FROM application WHERE application_name = 'Sentinel'),
               (SELECT proposition_id FROM proposition WHERE proposition_name = 'Customer Support')),
              ((SELECT application_id FROM application WHERE application_name = 'HSP IAM Self Service'),
               (SELECT proposition_id FROM proposition WHERE proposition_name = 'Customer Support'));


     RAISE NOTICE 'Data inserted successfully.';
EXCEPTION
    WHEN OTHERS THEN        
        ROLLBACK;        
        RAISE EXCEPTION 'An error occurred during the insertion. Transaction has been rolled back: %', SQLERRM;
END $$;
COMMIT;