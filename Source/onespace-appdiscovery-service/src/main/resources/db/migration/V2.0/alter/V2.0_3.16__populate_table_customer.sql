BEGIN;
DO $$
BEGIN

UPDATE customer
SET market_id = (SELECT market_id FROM market WHERE country = 'United States')
WHERE customer_name = 'Apollo Hospitals';

UPDATE customer
SET market_id = (SELECT market_id FROM market WHERE country = 'United States')
WHERE customer_name = 'Manipal Hospitals';


     RAISE NOTICE 'Data inserted successfully.';
EXCEPTION
    WHEN OTHERS THEN
        ROLLBACK;
        RAISE EXCEPTION 'An error occurred during the insertion. Transaction has been rolled back: %', SQLERRM;
END $$;
COMMIT;