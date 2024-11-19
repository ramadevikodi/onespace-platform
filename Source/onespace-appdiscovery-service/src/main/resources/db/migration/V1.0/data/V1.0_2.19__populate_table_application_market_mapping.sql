BEGIN;
DO $$
BEGIN
    INSERT INTO application_market_mapping (application_id, market_id)
    VALUES ((SELECT application_id FROM application WHERE application_name = 'Cyber Security'),
            (SELECT market_id FROM market WHERE country = 'SriLanka')),
           ((SELECT application_id FROM application WHERE application_name = 'Utilization'),
            (SELECT market_id FROM market WHERE country = 'SriLanka')),
           ((SELECT application_id FROM application WHERE application_name = 'Inventory'),
            (SELECT market_id FROM market WHERE country = 'SriLanka')),
           ((SELECT application_id FROM application WHERE application_name = 'Assessment'),
            (SELECT market_id FROM market WHERE country = 'SriLanka')),
           ((SELECT application_id FROM application WHERE application_name = 'Service Performance'),
            (SELECT market_id FROM market WHERE country = 'SriLanka'));


     RAISE NOTICE 'Data inserted successfully.';
EXCEPTION
    WHEN OTHERS THEN        
        ROLLBACK;        
        RAISE EXCEPTION 'An error occurred during the insertion. Transaction has been rolled back: %', SQLERRM;
END $$;
COMMIT;