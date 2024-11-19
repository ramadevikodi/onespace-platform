BEGIN;
DO $$
BEGIN

delete from proposition where proposition_name = 'Customer Support';

     RAISE NOTICE 'Proposition Table Data updated successfully.';
EXCEPTION
    WHEN OTHERS THEN
        ROLLBACK;
        RAISE EXCEPTION 'An error occurred during the population of Proposition Table. Transaction has been rolled back: %', SQLERRM;
END $$;
COMMIT;