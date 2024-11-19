BEGIN;
DO $$
BEGIN
    INSERT INTO category (category_name)
    VALUES ('Admin App'),
           ('Business App'),
           ('IT App');

    RAISE NOTICE 'Data inserted successfully.';
EXCEPTION
    WHEN OTHERS THEN
        ROLLBACK;
        RAISE EXCEPTION 'An error occurred during the insertion. Transaction has been rolled back: %', SQLERRM;
END $$;
COMMIT;