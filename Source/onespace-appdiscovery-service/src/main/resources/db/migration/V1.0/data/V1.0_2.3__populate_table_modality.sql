BEGIN;
DO $$
BEGIN
    INSERT INTO modality (modality_name)
    VALUES ('CT'),
           ('MR'),
           ('Ultrasound'),
           ('Not applicable');
    RAISE NOTICE 'Data inserted successfully.';
EXCEPTION
    WHEN OTHERS THEN
        ROLLBACK;
        RAISE EXCEPTION 'An error occurred during the insertion. Transaction has been rolled back: %', SQLERRM;
END $$;
COMMIT;