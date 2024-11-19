BEGIN;
DO $$
BEGIN

delete from application_proposition_mapping
where proposition_id = (SELECT proposition_id FROM proposition WHERE proposition_name = 'Customer Support');

     RAISE NOTICE 'Application Proposition Mapping Table Data updated successfully.';
EXCEPTION
    WHEN OTHERS THEN
        ROLLBACK;
        RAISE EXCEPTION 'An error occurred during the population of Application Proposition Mapping Table. Transaction has been rolled back: %', SQLERRM;
END $$;
COMMIT;