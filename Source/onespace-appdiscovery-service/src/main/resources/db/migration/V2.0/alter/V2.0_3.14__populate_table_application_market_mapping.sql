BEGIN;
DO $$
BEGIN


UPDATE application_market_mapping
SET market_id = (SELECT market_id FROM market WHERE country = 'United States')
WHERE application_id = (SELECT application_id FROM application WHERE application_name = 'Cyber Security');

UPDATE application_market_mapping
SET market_id = (SELECT market_id FROM market WHERE country = 'United States')
WHERE application_id = (SELECT application_id FROM application WHERE application_name = 'Utilization');

UPDATE application_market_mapping
SET market_id = (SELECT market_id FROM market WHERE country = 'United States')
WHERE application_id = (SELECT application_id FROM application WHERE application_name = 'Inventory');

UPDATE application_market_mapping
SET market_id = (SELECT market_id FROM market WHERE country = 'United States')
WHERE application_id = (SELECT application_id FROM application WHERE application_name = 'Assessment');

UPDATE application_market_mapping
SET market_id = (SELECT market_id FROM market WHERE country = 'United States')
WHERE application_id = (SELECT application_id FROM application WHERE application_name = 'Service Performance');


INSERT INTO application_market_mapping (application_id, market_id)
VALUES  ((SELECT application_id FROM application WHERE application_name = 'Philips Learning Center (Totara)'),
        (SELECT market_id FROM market WHERE country = 'United States')),
        ((SELECT application_id FROM application WHERE application_name = 'Performance Bridge'),
        (SELECT market_id FROM market WHERE country = 'United States')),
        ((SELECT application_id FROM application WHERE application_name = 'InCenter'),
        (SELECT market_id FROM market WHERE country = 'United States')),
        ((SELECT application_id FROM application WHERE application_name = 'Early Warning Scoring'),
        (SELECT market_id FROM market WHERE country = 'United States'));


     RAISE NOTICE 'Data inserted successfully.';
EXCEPTION
    WHEN OTHERS THEN
        ROLLBACK;
        RAISE EXCEPTION 'An error occurred during the insertion. Transaction has been rolled back: %', SQLERRM;
END $$;
COMMIT;