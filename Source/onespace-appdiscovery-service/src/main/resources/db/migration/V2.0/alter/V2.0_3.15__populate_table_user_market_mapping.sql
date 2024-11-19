BEGIN;
DO $$
BEGIN

UPDATE user_market_mapping
SET market_id = (SELECT market_id FROM market WHERE country = 'United States')
WHERE hsp_user_uuid = '004b7049-40f5-48ed-8ae5-77a1b55a8b36';

     RAISE NOTICE 'Data inserted successfully.';
EXCEPTION
    WHEN OTHERS THEN
        ROLLBACK;
        RAISE EXCEPTION 'An error occurred during the insertion. Transaction has been rolled back: %', SQLERRM;
END $$;
COMMIT;